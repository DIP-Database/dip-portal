package edu.ucla.mbi.portal.struts.action;

/* =============================================================================
 * $Id:: RecordAction.java 2877 2012-12-18 20:42:36Z lukasz                    $
 * Version: $Rev:: 2877                                                        $
 *-----------------------------------------------------------------------------$
 *                                                                             $
 * RecordAction action - returns database records                              $
 *                                                                             $
 *     TO DO:                                                                  $
 *         - agent setup                                                       $
 *                                                                             $
 *=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

import javax.xml.bind.*;

import edu.ucla.mbi.dxf14.*;
import edu.ucla.mbi.service.*;
import edu.ucla.mbi.portal.*;
import edu.ucla.mbi.portal.agent.*;

import edu.ucla.mbi.util.struts.interceptor.*;

public class RecordAction extends RecordSupport 
    implements AccessionAware   {

    //--------------------------------------------------------------------------
    // background agent configuration 
    //--------------------------------

    AgentConfig agentConf;

    public void setAgentConfig( AgentConfig conf ) {
        this.agentConf = conf;
    }
    
    //--------------------------------------------------------------------------
    // EXECUTE ACTION
    //---------------
    
    /* valid parameter combinations:
         - view: 
               record?ns=dip&ac=DIP-310N&md=N&sl=0&ret=view 

         - JSON record model:
               record?ns=dip&ac=DIP-310N&md=N&sl=0&ret=jqmodel         

         - JSON record data: 
               record?ns=dip&ac=DIP-310N&md=N&sl=0&ret=jqdata         
    */
    
    public String execute() throws Exception {

        Log log = LogFactory.getLog( this.getClass() );
        log.debug( " MenuContext: " + super.getMenuContext() );
        
        initialize();

        String dxfPrefix = "";

        //----------------------------------------------------------------------
        // preprocess ns/ac/md/sl
        // -----------------------
        
        this.buildDxfQuery();
        
        log.debug( "  ns:ac=" + getNs() + ":" + getAc() );
        log.debug( "   model=" + getMd() + " slot=" + getSl() );
        log.debug( "   ret=" + getRet() + " big=" + isBigOn() + 
                  " debug=" + isDebugOn() );
        
        if( getDl() == null || getDl().length() == 0 ) {
            setDl( "full" );
        }

        //----------------------------------------------------------------------
        // dispatch 
        //---------

        return dispatch();
    }
    
    //--------------------------------------------------------------------------
    // dispatcher
    //-----------

    public String dispatch() throws Exception {

        if ( getMd() != null ) {
            // DEPRECIATED
            
            setModelList( getModel().getModelList( getMd() ) );
            
            String viewType = getModel().getModelViewType( getMd(), getSl() );
            String viewName = getModel().getModelViewName( getMd(), getSl() );

            setViewType( getModel().getModelViewType( getMd(), getSl() ) );
            setViewDef( getTableContext().getViewInfo( viewType, viewName ) );
        }

        if ( getRet() == null || getRet().equals( "view" ) ) {

            // NOTE: possibly trigger agent(s) here
            //-------------------------------------

            buildData();
            return SUCCESS;

        } else if ( getRet().equals( "modellist" ) ) {
            return "json";
        } else if ( getRet().equals( "model" ) ) {

            // prepare and return grid layout info
            //------------------------------------

            String viewType = getModel().getModelViewType( getMd(), getSl() );
            String viewName = getModel().getModelViewName( getMd(), getSl() );
            return "json";
            
        } else if ( getRet().equals("counts") ) {
            return getCounts();
        } else if( getRet().equals( "data" ) ) {

            // prepare and return data (page)
            //-------------------------------

            return buildData();

        } else if( getRet().equals( "values" ) ) {
            return buildKnownData();
        }
        return SUCCESS;
    }

    //---------------------------------------------------------------------------
    
    public String getCounts() throws Exception {

        // prepare and return cache counts
        // --------------------------------
        
        Log log = LogFactory.getLog( this.getClass() );
        log.debug( "RecordAction: count request");

        setModelCountList( new ArrayList<Long>() );
        
        if ( getAc() != null && getAc().length() > 0 ) {
            
            Map rcache = (Map) getSession().get("record-cache");
            Map lcache = (Map) getSession().get("list-cache");
            
            Integer agentCount = (Integer) getSession().get("agc");
            int agc = 0;
            
            if ( agentCount != null) {
                agc = agentCount.intValue();
            } else {
                agc = 0;
            }
            
            // set agent count 
            //----------------

            getModelCountList().add( new Long( agc ) );

            log.debug( "RecordAction: agent count=" + agc);

            // generate pane counts
            // ---------------------
            
            List<String> modelList = getModel().getModelLabelList(getMd());
            
            int ii = 0;
            
            for ( ListIterator<String> mli = modelList.listIterator();
                  mli.hasNext(); ) {
                
                String srvTag = getModel().getServiceTag(getMd(), ii);
                ii++;
                
                String tag = mli.next();
                String listKey = dxfQuery + "_" + srvTag;
                
                if ( lcache != null ) {

                    List<edu.ucla.mbi.dxf14.XrefType> dataList =
                        (List) lcache.get( listKey );
                
                    if (dataList != null) {                    
                        getModelCountList().add( new Long( dataList.size() ) );
                    } else {
                        getModelCountList().add( 0L );
                    } 
                } else {
                    getModelCountList().add( 0L );
                }

                log.debug( "count processed: tag=" + tag 
                           + "  count=" + getModelCountList().get( ii ) );
            }            
            getModelCountList().set( 0, new Long( agc ) );
            log.debug("count processed: agent count=" + agc );
        }
        return "json";
    }


    //--------------------------------------------------------------------------
    // get known data
    //---------------
    
    public String buildKnownData() throws Exception {

        Log log = LogFactory.getLog( this.getClass() );
        log.debug("KnownData requested");
        
        String dxq = buildDxfQuery();
        String srvType = getModel().getServiceType( getMd(), getSl() );
        String srvTag = getModel().getServiceTag( getMd(), getSl() );
        String listKey = dxq + "_" + srvTag;
        
        String viewType = getModel().getModelViewType( getMd(), getSl() );
        String viewName = getModel().getModelViewName( getMd(), getSl() );

        if ( viewType != null && viewType.equals( "table" ) ) {
            setTableName( viewName );
        }

        Map lcache =  (Map) getSession().get("list-cache");
        Map rcache =  (Map) getSession().get("record-cache");
        
        if ( lcache == null || rcache == null ) return "json";
        
        List<edu.ucla.mbi.dxf14.XrefType>
            paneList = (List) lcache.get( listKey );
        
        if ( ! srvType.equals("reflist") ) return "json";
        
        List<edu.ucla.mbi.dxf14.XrefType>
            cref = (List) lcache.get( listKey );
        
        if ( cref == null ) return "json";
             
        for( Iterator<edu.ucla.mbi.dxf14.XrefType> pli = cref.iterator(); 
             pli.hasNext(); ) {
            
            edu.ucla.mbi.dxf14.XrefType cxr = pli.next();
            
            NodeType cn = null;
            if ( getContext().isCacheOn() ){
                cn = (NodeType) rcache.get( cxr.getAc() + 
                                            "_base" );
                if( cn == null ) {
                    cn = (NodeType) rcache.get( cxr.getAc() + 
                                                "_full" );
                }
            }

            if ( cn != null ) {
                getKnownDetail().add( cn );
            }
        }

        if ( getKnownDetail().size() > 0 ) {
            setKnownData( getKnownDetail() );
        }
        
        log.debug( "KnownData done:" +  getKnownDetail().size() );
        return "json";
    }

   
    String dxfQuery = null;

    private String buildDxfQuery() {
        
        if( dxfQuery != null ) return dxfQuery;        
        
        String dxfPrefix = "";
        
        //----------------------------------------------------------------------
        // preprocess ns/ac/md/sl
        // -----------------------
        
        if( getAc() != null && getAc().length() > 0 ){
            
            dxfQuery = getAc();
            dxfPrefix = getNs();

            if( dxfPrefix != null && !dxfPrefix.equals("dip") ){
                if( dxfPrefix.equals("pmid") ){
                    dxfQuery = dxfQuery + "_S";
                } else if( dxfPrefix.equals("uniprot")
                           || dxfPrefix.equals("refseq") ){
                    dxfQuery = dxfQuery + "_N";
                }
            }
            
            if( getMd() == null || getMd().length() == 0 ){
                if( dxfQuery.endsWith("N") ){
                    setMd("N");
                }
                if( dxfQuery.endsWith("E") ){
                    setMd("E");
                }
                if( dxfQuery.endsWith("X") ){
                    setMd("X");
                }
                if( dxfQuery.endsWith("S") ){
                    setMd("S");
                }
            }
        }
        return dxfQuery;
    }
    
    //--------------------------------------------------------------------------
    // build data structures
    //----------------------
    
    public String buildData() throws Exception {
        
        Log log = LogFactory.getLog( this.getClass() );
        log.debug( "RecordAction: building data" );
        
        // set table name for the interceptor
        //-----------------------------------
     
        String viewType = getModel().getModelViewType( getMd(), getSl() );
        String viewName = getModel().getModelViewName( getMd(), getSl() );
 
        if ( viewType != null && viewType.equals( "table" ) ) {
            setTableName( viewName );
        }
     
        String dxfQuery = null;
        String dxfPrefix = "";
 
        // preprocess ns/ac/md/sl 
        //-----------------------

        if( getAc() != null && getAc().length() > 0 ) {
            dxfQuery = getAc();
            dxfPrefix = getNs();
        }

        log.debug( " cookies: " + getCookiesMap() );
        log.debug( " GetRecord: dxfQuery=" + dxfQuery );
        log.debug( " GetRecord: dxfPrefix=" + dxfPrefix );
 
        if( dxfQuery != null ) {
     
            Map rcache = null; // record cache
            Map lcache = null; // list cache
     
            if( getContext().isCacheOn() ){
                synchronized( getSession() ){
                    rcache = (Map) getSession().get("record-cache");
                    if( rcache == null ) {
                        getSession().put( "record-cache", 
                                          new ConcurrentHashMap() );
                        rcache = (Map) getSession().get( "record-cache" );
                    }
                }
                
                synchronized( getSession() ) { 
                    lcache = (Map) getSession().get("list-cache");
                    if( lcache == null ) { 
                        getSession().put( "list-cache", 
                                          new ConcurrentHashMap() );
                        lcache = (Map) getSession().get( "list-cache" );
                    }
                }
  
                log.debug( "cache query for: " + getAc() + "_" + getDl() );
                setSummary( (NodeType) rcache.get( getAc() + "_" + 
                                                   getDl() ) );
                log.debug( " cache hit: " + getDetail() );
            }
     
            if( getSummary() == null ){
                try{
                    
                    DxfService service = getModel().getDxfService();

                    log.debug( " dxf service: " + service );

                    setSummary( service.getDxfNode( dxfPrefix,
                                                    getAc(), getDl() ) );
                    
                    if( getSummary() != null ){
                        dxfPrefix = getSummary().getNs();
                    }
                } catch( Exception ex ){
                    ex.printStackTrace();
                    return ERROR;
                }
            }else{
                dxfPrefix = getSummary().getNs();
            }
            
            if( getSummary() != null && getContext().isCacheOn() ) {
                rcache.put( getAc() + "_" + getDl(), getSummary() );
                cacheSize = rcache.size();
            }
     
            // build/set view
            //---------------
     
            Map<String,String> vpl = new HashMap<String,String>();
     
            // reset top pane content
            //-----------------------
     
            setDetail( new ArrayList<NodeType>() );
     
            // generate pane content
            //----------------------
     
            log.debug( " GetRecord: pane content rtp: " + getMd() + 
                       " tp: " + getSl() );
            
            String tag = getModel().getViewName( getMd(), getSl() );    
            String srvType = getModel().getServiceType( getMd(), getSl() );     
            String srvTag = getModel().getServiceTag( getMd(), getSl() );     
            
            String listKey = dxfQuery + "_" + srvTag;

            List<edu.ucla.mbi.dxf14.XrefType> 
                paneList = (List) lcache.get( listKey );

            log.debug( "RecordAction:  tag: " + srvTag + 
                      " srvType: " + srvType );
            
            if( srvType.equals( "node" ) ){
  
                // service returns node
                
                edu.ucla.mbi.dxf14.NodeType
                    cnode = (NodeType) rcache.get( listKey );
                if( cnode == null ){
                    cnode = getModel()
                        .getService( getMd(), getSl() )
                        .getDxfNode( dxfPrefix, dxfQuery, srvTag );
                }
                if( cnode != null ){
      
                    if( getContext().isCacheOn() ){
                        // cache response
                        rcache.put( listKey, cnode );
                    }
                    // add node to pane data
                    getDetail().add( cnode );
                }
            }

            if( srvType.equals( "nodelist" ) ){
                // service returns node list
                // NOTE: node lists are not cached to save space
  
                List<edu.ucla.mbi.dxf14.NodeType> nlist =
                    getModel().getService( getMd(), getSl() )
                    .getDxfNodeList( dxfPrefix, dxfQuery, srvTag );
                if( nlist.size() > 0 ){
                    for( Iterator<edu.ucla.mbi.dxf14.NodeType>
                             nli=nlist.iterator(); nli.hasNext(); ){
                        edu.ucla.mbi.dxf14.NodeType cn = nli.next();
                        
                        log.debug( "pane: adding node: " + cn.getAc() );
                        getDetail().add( cn );
                    }
                }
            }
     
            if( srvType.equals( "reflist" ) ){
                
                // service returns reference list

                String matchTag = getNb();
                if( matchTag == null ){
                    matchTag = "EXACT";
                }
                

                log.debug( "reflist prefix:query:tag: " + 
                           dxfPrefix  + ":" + dxfQuery + ":" + srvTag +"\n" +
                           " listKey=" +listKey + " match=" + matchTag + "\n\n");

                log.debug( "matchTag=" + matchTag);
                
                List<edu.ucla.mbi.dxf14.XrefType>
                    cref = (List) lcache.get( listKey );
  
                if ( cref == null ) {
                    cref = getModel().getService( getMd(), getSl() )
                        .getDxfRefList( dxfPrefix, dxfQuery, srvTag, matchTag);
                    log.debug( " direct: " + cref );
                }
  
                log.debug( "cref=" + cref );
                
                if ( cref != null ) {
                    log.debug( " cache hit: pane size=" + cref.size() );
                    if ( getContext().isCacheOn() ) {
                        // cache reference list
                        lcache.put( listKey, cref );
                    }

                    //----------------------------------------------------------
                    // build node list
                    //----------------
                    
                    int start = 0; 
                    int stop = cref.size();
                    
                    log.debug( "Filter: " + getFlt() );

                    if( getFlt() == null || getFlt().length() == 0 ) {
                        start = getFr(); 
                        if ( getMr() > 0 ) {
                            stop = getFr() + getMr() < cref.size() ? 
                                getFr() + getMr() : cref.size();
                        }
                    }
                    
                    this.setTr( cref.size() );
                    
                    if( cref.size() > 0 ){      
                        for( Iterator<edu.ucla.mbi.dxf14.XrefType> 
                                 pli = cref.subList( start, stop ).iterator(); 
                             pli.hasNext(); ) {
                            
                            edu.ucla.mbi.dxf14.XrefType cxr = pli.next();
       
                            NodeType cn = null;
                            if ( getContext().isCacheOn() ){
                                cn = (NodeType) rcache.get( cxr.getAc() + 
                                                            "_base" );
                                if( cn == null ) {
                                    cn = (NodeType) rcache.get( cxr.getAc() + 
                                                                "_full" );
                                }
                            }
       
                            if( cn == null ) {
                                cn = getModel()
                                    .getService( getMd(), getSl())
                                    .getDxfNode( dxfPrefix, cxr.getAc(),
                                                 "base");
                            }
       
                            if( cn != null ) {
                                if ( getContext().isCacheOn() ) {
                                    // cache response
                                    rcache.put(cxr.getAc()+"_base",cn);
                                }
                                // add to pane
                                log.debug( "pane: adding node: "+cn.getAc());
                                getDetail().add( cn );
                            }
                        }
                    }
                }
            }
     
            /*
            // NOTE: temporary solution. Ultimately should be 
            // called according to configration file of some sort...
     
            if( getSummary() != null && getSl() == 0 ) {
                List<XrefType>
                    xrefs = getSummary().getXrefList().getXref();
                for ( Iterator<XrefType>
                          xi = xrefs.iterator(); xi.hasNext(); ) {
                    XrefType cxref = xi.next();
      
                    if ( cxref.getNs().toLowerCase().equals( "uniprot" ) && 
                         rcache != null && getContext().isCacheOn() ) {
                        log.debug( " Xref: NS=\"uniprot\" AC=" + 
                                   cxref.getAc() );
                        
                        if ( rcache.get( cxref.getAc() + 
                                         "_base" ) == null ) {
                            NodeType result = ebiDbService
                                .getDxfNode( cxref.getNs().toLowerCase(),
                                             cxref.getAc(), "base" );
                            if ( result != null ) {
                                rcache.put( cxref.getAc() + "_base", 
                                            result);
                                getDetail().add( result );
                                log.debug( " Xref: hit added: " + 
                                          cxref.getAc() + "_base");
                            } else {
                                log.debug( "Xref: xref no record found." );
                            }
                        } else {
                            getDetail().add( (NodeType) 
                                             rcache.get( cxref.getAc() + 
                                                         "_base" ) );
                            log.debug( " Xref: record already cached." );
                        }
                    }
                    
                    if ( cxref.getNs().toLowerCase().equals( "refseq" ) && 
                    rcache != null && getContext().isCacheOn() ) {
                        log.debug( " Xref: NS=\"refseq\" AC=" + 
                                   cxref.getAc() );
                        
                        if ( rcache.get( cxref.getAc() + 
                                         "_base") == null ) {
                            NodeType result = ncbiDbService
                                .getDxfNode( cxref.getNs().toLowerCase(),
                                             cxref.getAc(), "base" );
                            if ( result != null ) {
                                rcache.put( cxref.getAc() + "_base", 
                                            result );
                                getDetail().add( result );
                                log.debug( " Xref: hit added: " + 
                                           cxref.getAc() + "_base" );
                                           } else {
                            log.debug( " Xref: no record found." );
                                }
                            } else {
                        getDetail().add( (NodeType) 
                            rcache.get( cxref.getAc() +
                                                         "_base" ) );
                            log.debug( " Xref: record already cached." );
                        }
                    }
                }
            }
            */
            // call async agent
            //-----------------
            
            // collect from DIP:
            //   Node      - interactions, experiments, sources
            //   Link      - interactors,  experiments, sources
            //   Evidence  - interactors,  interactions, source
            //   Source    - interactions, nodes, experiments -
            //               limit number retrieved here
  
            // full record only
            //-----------------
            
            if ( getContext().isAgentOn() ) {
      
                log.debug( "Action: GetRecord starting Agent" );
                
                new Thread( new RecordAgent( agentConf, getSummary(), 
                                             getSession() ) ).start();
            }

            log.debug( "Action: GetRecord done" );
  
            if( debugOn ) {
                edu.ucla.mbi.dxf14.ObjectFactory 
                    dof = new edu.ucla.mbi.dxf14.ObjectFactory(); 
      
                DatasetType resdoc = dof.createDatasetType();
                DatasetType panedoc = dof.createDatasetType();
      
      
                JAXBContext jc = DxfJAXBContext.getDxfContext();
                //JAXBContext.newInstance( "edu.ucla.mbi.dxf14" );
                Marshaller marshaller = jc.createMarshaller();
                marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT,
                                        new Boolean( true ) );  

                if ( getSummary() != null ) { 
                    resdoc.getNode().add( getSummary() );
                    
                    java.io.StringWriter sw = new StringWriter();
                    marshaller.setProperty( Marshaller.JAXB_ENCODING, 
                                            "UTF-8" );
                    marshaller.marshal( dof.createDataset( resdoc ), 
                                        sw );
   
                    resultStr = sw.toString();
                }
      
                if ( getDetail() != null ){
                    panedoc.getNode().addAll( getDetail() );
   
                    java.io.StringWriter panesw = new StringWriter();
                    marshaller.setProperty( Marshaller.JAXB_ENCODING, 
                                            "UTF-8" );
                    marshaller.marshal( dof.createDataset( panedoc ), 
                                        panesw );
                    
                    paneStr = panesw.toString();
                }
            }
     
            log.debug(" building table");
  
            Map metarow = new HashMap();
            metarow.put( "meta", "meta" );
            metarow.put( "dip", "dip" );
            metarow.put( "first", getFirst() );
            metarow.put( "max", getMax() );
            metarow.put( "ac", getAc() );
            metarow.put( "tp", getSl() );
            metarow.put( "detail", getDl() );
  
            setTableMeta( metarow );
            log.debug(" table meta data created");
  
            // build item list
            //----------------
  
            if ( getDetail().size() > 0 ) {
                setTableData( getDetail() );
            }

            if ( getRet() != null && getRet().equals( "data" ) ){  
                //setModelData();
            }
        }
        log.debug( "items.size=" + getTableData().size() );
        return "json";
    }
    
    
    //--------------------------------------------------------------------------
    // configuration
    //--------------
    
    boolean configFlag;
    
    public String getConf() {
        return String.valueOf( configFlag );
    }

    public void setConf( String conf ) {
        this.configFlag = conf.equalsIgnoreCase( "true" );
    }
    
    //--------------------------------------------------------------------------
    
    //public List<RecordTab> getPaneTab() {
    //    return paneTab;
    //}
    
    //public void setPaneTab( List<RecordTab> tabList ) {
    //    this.paneTab = tabList;
    //}
    
    //--------------------------------------------------------------------------
    //debugging properties
    //-------------------- 

    boolean isJsonOn;

    public void setJsonOn( boolean json ) {
        if ( json ) {
            setRetType( "json" );
            this.isJsonOn = true;
        }
    }
    
    //--------------------------------------------------------------------------
    
    String resultStr;
    
    public String getResultStr() {
        return resultStr;
    }
    public void setResultStr(String resultStr) {
        this.resultStr=resultStr;
    }
    
    //--------------------------------------------------------------------------

    String paneStr;

    public String getPaneStr() {
        return paneStr;
    }
    
    public void setPaneStr(String paneStr) {
        this.paneStr=paneStr;
    }
    
    //--------------------------------------------------------------------------
    
    boolean debugOn;

    public  boolean isDebugOn() {
        return debugOn;
    }

    public  boolean getDebug() {
        return debugOn;
    }
    
    public void setDebug( boolean debug ) {
        this.debugOn=debug;
    }

    //--------------------------------------------------------------------------

    int cacheSize;
    
    public void setCacheSize(int size) {
        this.cacheSize=size;
    }

    public  int getCacheSize() {
        return cacheSize;
    }
}
