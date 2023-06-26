package edu.ucla.mbi.dip.struts.action;

/* =============================================================================
 * $Id:: QueryAction.java 3344 2013-07-17 01:34:22Z lukasz                     $
 * Version: $Rev:: 3344                                                        $
 *==============================================================================
 *                                                                             $
 * QueryAction - returns records matching a query                              $
 *                                                                             $
 *     TO DO:                                                                  $
 *         - query results caching                                             $
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
import edu.ucla.mbi.dip.agent.*;

import edu.ucla.mbi.util.struts.interceptor.*;

public class QueryAction extends QuerySupport{

    private Log log = LogFactory.getLog( this.getClass() );
    
    // service access
    //---------------
    
    private Map getServiceConfig(){
        return (Map) getContext().getConfig().get( "service" );           
    }

    private String getQueryType(){
        if( getQt() != null && !getQt().equals( "" ) ) return getQt();
        
        return (String) ((Map) getServiceConfig()
                        .get( "query" )).get( "default-type" );
    }

    private DxfService getRecordService() {
        return (DxfService) getServiceConfig().get( "record" );
    }

    private DxfService getQueryService( String queryType ){

        if( queryType == null ) return null;

        return (DxfService) ((Map) getServiceConfig()
                             .get( "query" )).get( queryType );             
    }
       
    //---------------------------------------------------------------------
    // agent configuration
    //--------------------

    DipAgentConfig dipAgentConfig;
    
    public void setDipAgentConfig( DipAgentConfig conf ) {
	this.dipAgentConfig = conf;
    }
    
    //---------------------------------------------------------------------    
    // data
    //-----
    
    private NodeType queryNode;   // query result
    
    // record cache
    //-------------

    private String rtp = "";
    private String ckey = "";
    private int cacheSize = 0;
    
    // debugging 
    //----------
    
    private String detail = "";       
    private String resultStr = "";   // result: XML (DXF) representation  
    private String paneStr = "";     // pane: XML (DXF) representation  

    //---------------------------------------------------------------------
    // EXECUTE ACTION
    //---------------

    public String execute() throws Exception {
        
        initialize();

        if( log.isDebugEnabled() ){
            log.debug( "session=" + getSession());
            log.debug( "QueryAction: " + 
                       " ret=" + getRet() + " debug=" + debugOn );
            log.debug( "QueryAction: "
                       + " qt=" + getQt() + " qtype=" + getQueryType() );
            log.debug( "QueryAction: "
                       + " query=" + getQuery() );
        }
        
        setMd( rtp );        
        log.debug( "RTP= " + getRtp()
                   + " MD=" + getMd()
                   + " SL=" + getSl());

        if ( getMd() != null ) {
            
            setModelList( getModel().getModelList( getMd() ) );
            
            String viewType =
                getModel().getModelViewType( getMd(), getSl() );

            String viewName =
                getModel().getModelViewName( getMd(), getSl() );

            Map viewDef 
                = getTableContext().getViewInfo( viewType, viewName );

            setViewType( viewType  );
            setViewDef( viewDef );
        }

        //----------------------------------------------------------------------
        // dispatch
        //---------
        
        return dispatch( getRet() ) ;  // NOTE: calls buildData()
    }
    
    //--------------------------------------------------------------------------
    // dispatcher
    //-----------

    /* 
     * Rrecognized return values:
     *   
     *  <null>          -
     *  model/modellist - tabs/tab content info only
     *  counts          - tab values counts only
     *  data            -            
     *  values          -
     *  facet           -
     *  view            -
    */

    public String dispatch( String ret ) throws Exception {
        
        if( ret == null ) {
            
            // NOTE: possibly trigger agent(s) here
            //-------------------------------------

            buildDataSummary();
            return SUCCESS;   
        } 
        
        if( ret.equals( "view" ) ){
            buildData();
            return SUCCESS;           
        } 
        
        if( ret.equals( "modellist" ) || ret.equals( "model" )){
            return JSON;
        }
         
        if( ret.equals("counts") ){
            return getCounts();
        } 
        
        if( ret.equals( "data" ) ){

            // prepare and return data (page)
            //-------------------------------
            
            return buildData();            
        }
        
        if( ret.equals( "values" ) ){
            buildKnownData();

            log.info( "knowndata size= " + getKnownData() );
            return JSON;
        }
        
        if( ret.equals( "facet" ) ){
            String filter = "";
            return buildColumnValues();
        }
        
        return SUCCESS;
    }
    
    private String dxfQuery = null;

    //--------------------------------------------------------------------------
    // execute queries
    //----------------
    
    private NodeType facetQuery( String queryType, String query, 
                                 String detail, String suffix ){
        
        NodeType result = null;
        
        query = query + " " + suffix;
        log.info( "facet query: " + query );
        
        return query( queryType, query, detail );
    }    

    //--------------------------------------------------------------------------

    private String queryKey( String queryType,  String query, String detail ) {
        return queryType+ "::" + query + "::" + detail;
    }
    
    private NodeType query( String queryType,  String query, String detail ) {
        
        NodeType result = null;

        log.info( " query type: " + queryType );
        log.info( " query string: " + query );
       
        if( query == null ){ query = ""; }
        if( detail == null ){ detail = ""; }
        
        // sanitize query string
        //----------------------

        query = query.replaceAll("^\\s+","").replaceAll("\\s+$","");
        query = query.replaceAll("\\s+"," ");
        
        if( query != null && query.length() > 0 ){
            
            // get cache
            //----------
            
            Map qcache = null;

            synchronized( getSession() ) {
                qcache = (Map) getSession().get( "query-cache" );
                if( qcache == null ) {
                    getSession().put( "query-cache", 
                                      new ConcurrentHashMap() );
                    qcache = (Map) getSession().get( "query-cache" );
                }
            }
            
            // session cache test
            //-------------------
            
            String qkey = queryKey( queryType, query, detail );
            
            NodeType qresult = (NodeType) qcache.get( qkey  );
            if( qresult == null ) {
                log.info( "  qcache: miss" );
                log.debug( "  QueryService(getDxfQuery):" + qkey );
                
                List<NodeType> qnl = null;

                qnl = getQueryService( queryType )
                    .getDxfQuery( queryType, query, detail );
                                
                if( qnl == null || qnl.size() == 0 ) {
                    log.debug( "  DONE: result size=0" );
                } else{
                    log.debug("  DONE: result size=" + qnl.size() );
                    result = qnl.get(0);
                    qcache.put( qkey, result );  // NOTE: turn off cache here
                } 

            } else {
                result = qresult;        
                log.info( "  qcache: hit" );
            }
            
        } else {
            log.debug( "  DONE: result size=0 (no valid query)" );
        }
        setQueryResult( result );
        
        log.debug( "QueryResult:\n" + marshallNode( result ) );
 
       return result;
    }

    public String getCounts() throws Exception {

        log.debug( "QueryAction: count request" );
        
        // prepare and return node counts
        // -------------------------------
        
        NodeType queryResult = this.query( getQueryType(), getQuery(), detail );
        setModelCountList( new ArrayList<Long>() );
        getModelCountList().add( 0L );

        int agc = 0;
        log.debug( "md=" + getMd() ); 
        List<String> modelList = getModel().getModelLabelList( getMd() );
        
        log.debug( "md=" + getMd() + " size=" + modelList.size() ); 

        int ii = 0;

        for ( ListIterator<String> mli = modelList.listIterator();
              mli.hasNext(); ) {
            
            String srvTag = getModel().getServiceTag( getMd(), ii );

            log.debug("ii=" + ii + " tag=" + srvTag );
            ii++;
                        
            String tag = mli.next();
            
            if ( queryResult != null ) {
                int count = 0;
                
                List<NodeType.PartList.Part> p 
                    = queryResult.getPartList().getPart();
                for ( Iterator<NodeType.PartList.Part> pi = p.iterator(); 
                      pi.hasNext(); ) {
                    
                    NodeType.PartList.Part pp = pi.next();
                    TypeDefType tdt =  pp.getNode().getType();

                    if ( (srvTag.equals( "N" ) &&  tdt != null &&
                          tdt.getNs() != null && tdt.getNs().equals("dxf") && 
                          tdt.getAc() != null && 
                          tdt.getAc().equals("dxf:0003") ) ||

                         (srvTag.equals( "E" ) && tdt != null &&
                          tdt.getNs() != null && tdt.getNs().equals("dxf") && 
                          tdt.getAc() != null && 
                          tdt.getAc().equals("dxf:0004") ) ||
                         
                         (srvTag.equals( "X" ) && tdt != null &&
                          tdt.getNs() != null && tdt.getNs().equals("dxf") && 
                          tdt.getAc() != null && 
                          tdt.getAc().equals("dxf:0015") ) ||
                         
                         (srvTag.equals( "S" ) && tdt != null &&
                          tdt.getNs() != null && tdt.getNs().equals("dxf") && 
                          tdt.getAc() != null &&  
                          tdt.getAc().equals("dxf:0016") ) ) {
                        count ++;
                    }                    
                }
                getModelCountList().add( new Long( count ) );
            } else {
                getModelCountList().add( 0L );
            }
        }
        getModelCountList().set( 0, new Long( agc ) );
        log.debug( "QueryAction: count request(DOME)" );
        return JSON;
    }

    //--------------------------------------------------------------------------
    
    public String buildColumnValues() throws Exception {
        
        String viewType = getModel().getModelViewType( getMd(), getSl() );
        String viewName = getModel().getModelViewName( getMd(), getSl() );
        
        log.debug( "QbuildFilterValues: viewType=" + viewType );
        log.debug( "QbuildFilterValues: viewName=" + viewName );
        log.debug( "QbuildFilterValues: cvl=" + getCvl() );

        int cvl = -1;
        try{
            cvl = Integer.parseInt( getCvl() );
        }catch(Exception ex){
            return JSON;
        }
        
        Map colModel = (Map) ((List) getViewDef().get("colModel")).get( cvl );

        String fsrv = (String) colModel.get( "filter-facet" );
        String suffix = (String) colModel.get( "filter-facet-suffix" );

        log.debug( "QbuildFilterValues: " 
                   + "filter-facet-suffix=" + suffix );
        
        if( fsrv != null ){
            
            log.debug( "QbuildFilterValues: server facet=" + fsrv);

            NodeType queryResult = 
                this.facetQuery( getQueryType(), getQuery(), detail, suffix );
            
            List ql = new ArrayList();
            ql.add( queryResult );

            log.debug( "QbuildFilterValues: queryResult=" + queryResult);
            setFacetData( ql );
            return JSON;
            
        } else {
            return buildKnownData();
        }
    }
    
    public String buildKnownData() throws Exception {
        return  buildData( false );
    }

    //--------------------------------------------------------------------------

    public String buildData() throws Exception {
        return  buildData( true );
    }

    //--------------------------------------------------------------------------

    private Map buildMeta(){

        Map metarow = new HashMap();
        metarow.put( "meta", "meta" );
        metarow.put( "dip", "dip" );
        metarow.put( "first", getFirst() );
        metarow.put( "max", getMax() );

        log.debug( "buildMeta: first=" + getFirst() 
                   + " max=" + getMax() );

        //metarow.put( "ac", getAc() );
        //metarow.put( "tp", getSl() );
        //metarow.put( "detail", getDl() );
        return metarow;
    }

    //--------------------------------------------------------------------------
    
    public String buildData( boolean all  ) throws Exception {

        log.debug( "buildData: " 
                   + "qt=" + getQueryType() + " q=" + getQuery() + " det=" + detail);
        
        NodeType queryResult = this.query( getQueryType(), getQuery(), detail );
    
        log.debug( queryResult );
        if( queryResult.getPartList() != null ){
            log.debug( "buildData: result size=" 
                       + queryResult.getPartList().getPart().size() );
        }
        
        // set table name for the interceptor
        //-----------------------------------
        
        String viewType = getModel().getModelViewType( getMd(), getSl() );
        String viewName = getModel().getModelViewName( getMd(), getSl() );
        
        if ( viewType != null && viewType.equals( "table" ) ) {
            setTableName( viewName );
        }
        
        if ( queryResult == null ) {                   // no query result
            setDetail( new ArrayList<NodeType>() );
            setTableMeta( this.buildMeta() );
            return JSON;
        }
            
        setSummary( queryResult );
        
        // build/set tabs
        //---------------

        Map<String,String> vpl = new HashMap<String,String>();

        // reset top pane content
        //-----------------------

        setDetail( new ArrayList<NodeType>() );

        String tag = getModel().getModelViewName( rtp, getSl() );
        String srvTp = getModel().getServiceType( rtp, getSl() );
        String srvTag = getModel().getServiceTag( rtp, getSl() );

        // nothing found
        //--------------
        
        if( queryResult.getPartList() == null ||
            queryResult.getPartList().getPart().size() == 0 ) {
            
            if( all ){
                if( getDetail().size() > 0 ) {
                    setTableData( getDetail() );
                }
                log.debug("All Data done:" +  getTableData().size() );
                
            } else {
                if ( getKnownDetail().size() > 0 ) {
                    setKnownData( getKnownDetail() );
                }
                
                log.debug("KnownData done:" +  getKnownDetail().size() );
            }
            return JSON;
        }
        
        if( all ){
            getDetail().addAll( buildDetail( queryResult, srvTag ) );
        } else {
            getKnownDetail().addAll( buildDetail( queryResult, srvTag) );
        }

        log.info("Detail: size=" + getDetail().size() );
        log.info("KnownDetail: size=" + getKnownDetail().size() );

        /*
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
            log.info("Action: GetRecord starting DipAgent");
            new Thread(new DipAgent(record,session,dipDbService)).start();

        */

        setTableMeta( this.buildMeta() );

        // build item list
        //----------------

        if ( getDetail().size() > 0 ) {
            setTableData( getDetail() );
        }
        
        if ( getKnownDetail().size() > 0 ) {
            setKnownData( getKnownDetail() );
        }

        
        if( debugOn ) {
            /*
            edu.ucla.mbi.dxf14.ObjectFactory
                dof = new edu.ucla.mbi.dxf14.ObjectFactory();

            DatasetType resdoc = dof.createDatasetType();
            DatasetType panedoc = dof.createDatasetType();

            JAXBContext jc = DxfJAXBContext.getDxfContext();
            //JAXBContext.newInstance( "edu.ucla.mbi.dxf14" );
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT,
                                    new Boolean( true ) );
            */
            if ( getSummary() != null ) {
                /*
                resdoc.getNode().add( getSummary() );
                
                java.io.StringWriter sw = new StringWriter();
                marshaller.setProperty( Marshaller.JAXB_ENCODING,
                                        "UTF-8" );
                marshaller.marshal( dof.createDataset( resdoc ),
                                    sw );

                resultStr = sw.toString();
                */
                resultStr = marshallNode( getSummary() );
                log.debug("SUMMARY:\n" + resultStr);
            }

            if ( getDetail() != null ){
                /*
                panedoc.getNode().addAll( getDetail() );

                java.io.StringWriter panesw = new StringWriter();
                marshaller.setProperty( Marshaller.JAXB_ENCODING,
                                        "UTF-8" );
                marshaller.marshal( dof.createDataset( panedoc ),
                                    panesw );

                paneStr = panesw.toString();
                */
                paneStr = marshallNodeList( getDetail() );
                log.debug("DETAIL:\n" + paneStr);
            }
        }

        return JSON;
    }

    
    //--------------------------------------------------------------------------

    private List<NodeType> buildDetail( NodeType queryResult, String srvTag ){

        List<NodeType> detail = new ArrayList<NodeType>();

        log.debug( "buildDetail=" + queryResult );

        edu.ucla.mbi.dxf14.ObjectFactory 
            dof= new edu.ucla.mbi.dxf14.ObjectFactory();	

        // get cache
        //----------

        Map rcache = null;

        synchronized( getSession() ) {
            rcache = (Map) getSession().get( "record-cache" );
            if( rcache == null ) {
                getSession().put( "record-cache",
                                  new ConcurrentHashMap() );
                rcache = (Map) getSession().get( "record-cache" );
            }
        }
        
        // build node list
        //----------------

        int start = 0;
        int stop = -1;

        log.info( "Filter: " + getFlt() );

        if( getFlt() == null || getFlt().length() == 0 ) {
            start = getFr();
            if ( getMr() > 0 ) {
                stop = getFr() + getMr() +1;
            }
        }

        log.debug( "from=" + getFr() + " max=" + getMr() 
                   + " start=" + start + " stop=" + stop );
        
        
        List<edu.ucla.mbi.dxf14.NodeType.PartList.Part>  
            pl = queryResult.getPartList().getPart();
        
        int ii = 0;
        
        for( Iterator pi = pl.iterator(); pi.hasNext(); ) {
            
            edu.ucla.mbi.dxf14.NodeType.PartList.Part part = 
                (edu.ucla.mbi.dxf14.NodeType.PartList.Part) pi.next();
            
            NodeType nd = part.getNode();
            
            log.debug( "nd.ac=" + nd.getAc() + " ? " + srvTag );
            log.debug( "part.attr=" + part.getAttrList() );
            
            if( nd.getAc().endsWith( srvTag ) ) {
		
                ii++;
                //log.debug( "ii=" + ii );
                if ( stop < 0 || ( ii > start && ii < stop ) ) {
                    
                    log.debug( "ii=" + ii );
                    
                    // session cache test
                    //-------------------
                    
                    NodeType nd_base =
                        (NodeType) rcache.get(nd.getAc() + "_base" );
                    if( nd_base == null ) {
                        nd_base = 
                            (NodeType) rcache.get( nd.getAc() + 
                                                   "_full" );
                        if ( nd_base == null ) {
                            nd_base = getRecordService()
                                .getDxfNode( nd.getNs(), nd.getAc(), 
                                             "base" );
                        }
                        if ( nd_base != null ) {
                            // cache response
                            rcache.put( nd.getAc() + "_base", 
                                        nd_base );
                        }
                    }

                    if ( nd_base != null ) {
                        log.debug( "nd_base.attr=" 
                                   + nd_base.getAttrList() );  
                        if( part.getAttrList() != null ){
                            if( nd_base.getAttrList() == null ){
                                nd_base.setAttrList( dof.createNodeTypeAttrList() );
                            }
                            nd_base.getAttrList().getAttr()
                                .addAll( part.getAttrList().getAttr() );
                            log.debug( "nd_base.attr=" 
                                       + nd_base.getAttrList().getAttr() );  
                        }

                        log.debug( "  added nd.ac=" + nd_base.getAc() );
                        log.debug("nd:\n" + marshallNode( nd_base ) + "\n");
                        
                        detail.add( nd_base );
                        log.debug( "added..." );
                        log.debug( "detail size=" + detail.size() );
                    }
                }
            }
        }
        this.setTr( ii );
        log.debug( "detail final size=" + detail.size() );
        return detail;
    }

    //--------------------------------------------------------------------------

    public String buildDataSummary() throws Exception {
        
        NodeType queryResult = this.query( getQueryType(), getQuery(), detail );
        
        // set table name for the interceptor
        //-----------------------------------
        
        String viewType = getModel().getModelViewType( getMd(), getSl() );
        String viewName = getModel().getModelViewName( getMd(), getSl() );
        
        if ( viewType != null && viewType.equals( "table" ) ) {
            setTableName( viewName );
        }
        
	if ( queryResult == null ) {
            setDetail( new ArrayList<NodeType>() );
        } else {
            setSummary( queryResult );

        }        
        return JSON;
    }
    
    //---------------------------------------------------------------------
    
    public static final String MESSAGE = "Dip2DB.message";
    
    private String message;
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    //---------------------------------------------------------------------
    
    public void setRtp(String rtp) {
        this.rtp=rtp;
    }
    
    public String getRtp() {
        return rtp;
    }
    
    //---------------------------------------------------------------------
    
    public NodeType getQueryResult() {
        return queryNode;
    }
    
    public void setQueryResult(NodeType queryNode) {
        this.queryNode=queryNode;
    }
    
    //---------------------------------------------------------------------
    // debugging 
    //----------
    
    public String getResultStr() {
        return resultStr;
    }
    
    public void setResultStr(String resultStr) {
        this.resultStr=resultStr;
    }
    
    //---------------------------------------------------------------------    

    public String getPaneStr() {
        return paneStr;
    }
    
    public void setPaneStr(String paneStr) {
        this.paneStr=paneStr;
    }
    
    //---------------------------------------------------------------------

    boolean debugOn;
    
    public  boolean isDebugOn() {
        return debugOn;
    }
    
    public  boolean getDebug() {
        return debugOn;
    }
    
    public void setDebugOn(boolean debug) {
        this.debugOn=debug;
    }
    
    public void setDebug(boolean debug) {
        this.debugOn=debug;
    }
    
    //---------------------------------------------------------------------

    public void setCacheSize(int size) {
        this.cacheSize=size;
    }
     
    public  int getCacheSize() {
        return cacheSize;
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    private Marshaller marshaller = null;

    private String marshallNode( NodeType node ){
        
        edu.ucla.mbi.dxf14.ObjectFactory
                dof = new edu.ucla.mbi.dxf14.ObjectFactory();
        
        DatasetType resdoc = dof.createDatasetType();
        
        JAXBContext jc = DxfJAXBContext.getDxfContext();
        
        try{

            if( marshaller == null ){ 
                marshaller = jc.createMarshaller();
                marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT,
                                        new Boolean( true ) );
                marshaller.setProperty( Marshaller.JAXB_ENCODING,
                                        "UTF-8" );
            }
         
            if ( node != null ) {
                resdoc.getNode().add( node );

                java.io.StringWriter sw = new StringWriter();
                marshaller.marshal( dof.createDataset( resdoc ), sw );
                return resultStr = sw.toString();
            }
        } catch( Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    private String marshallNodeList( List<NodeType> nodeList ){
        
        edu.ucla.mbi.dxf14.ObjectFactory
                dof = new edu.ucla.mbi.dxf14.ObjectFactory();
        
        DatasetType resdoc = dof.createDatasetType();
        JAXBContext jc = DxfJAXBContext.getDxfContext();
        
        try{
            if( marshaller == null ){ 
                marshaller = jc.createMarshaller();
                marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT,
                                        new Boolean( true ) );
                marshaller.setProperty( Marshaller.JAXB_ENCODING,
                                        "UTF-8" );
            }
            
            if ( nodeList != null ) {
                resdoc.getNode().addAll( nodeList );
                
                java.io.StringWriter sw = new StringWriter();
                marshaller.marshal( dof.createDataset( resdoc ), sw );
                return resultStr = sw.toString();
            }
        } catch( Exception ex){
            ex.printStackTrace();
        }
        
        return null;
    }

}