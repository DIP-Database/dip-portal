package edu.ucla.mbi.dip.struts.action;

/* =============================================================================
 # $Id:: StatAction.java 2877 2012-12-18 20:42:36Z lukasz                      $
 # Version: $Rev:: 2877                                                        $
 #==============================================================================
 #                                                                             $
 # StatAction - returns DIP statistic/status information                       $
 #                                                                             $
 #     TO DO:                                                                  $
 #         - query results caching                                             $
 #         - agent setup                                                       $
 #                                                                             $ 
 #=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

import javax.xml.bind.*;

import com.opensymphony.xwork2.ActionSupport;

import edu.ucla.mbi.dxf14.*;
import edu.ucla.mbi.service.*;
import edu.ucla.mbi.dip.agent.*;

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.context.*;
import edu.ucla.mbi.util.struts.action.*;
import edu.ucla.mbi.util.struts.interceptor.*;

public class StatAction extends PortalSupport {

    //---------------------------------------------------------------------
    // configuration
    //---------------
    
    JsonContext statsContext;
    
    public JsonContext getStatsContext() {
        return statsContext;
    }
    
    public void setStatsContext( JsonContext context ) {
        this.statsContext = context;
    }

    //--------------------------------------------------------------------------
    // DIP database access
    //--------------------
    
    DxfService dipDbService;
    
    public void setDipDbService( DxfService service ) {
	this.dipDbService = service;
    }
    
    //--------------------------------------------------------------------------
    // arguments
    //----------
    
    String ns;
    
    public String getNs() {
        return ns;
    }

    public void setNs( String ns ) {
        this.ns = ns;
    }

    //--------------------------------------------------------------------------

    String ac;

    public String getAc() {
        return ac;
    }

    public void setAc( String ac ) {
        this.ac = ac;
    }

    //--------------------------------------------------------------------------

    int list = 0;

    public int getList() {
        return list;
    }

    public void setList( int list ) {
        this.list = list;
    }

    //--------------------------------------------------------------------------
    // result
    //-------
    
    Map<String,Object> meta;
    
    public Map<String,Object> getMeta() {
        return meta;
    }


    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------

    private void initialize() {

        Map<String,Object> jpd = statsContext.getJsonConfig();
        
        if ( jpd == null ) {
            Log log = LogFactory.getLog( this.getClass() );
            log.info( " initilizing stats config..." );
            String jsonPath =
                (String) statsContext.getConfig().get( "json-config" );
            log.info( "JsonStatsDef=" + jsonPath );

            if ( jsonPath != null && jsonPath.length() > 0 ) {

                String cpath = jsonPath.replaceAll("^\\s+","" );
                cpath = jsonPath.replaceAll("\\s+$","" );

                try {
                    InputStream is = getServletContext()
                        .getResourceAsStream( cpath );
                    statsContext.readJsonConfigDef( is );

                    jpd = statsContext.getJsonConfig();

                } catch ( Exception e ){
                    log.info( "JsonConfig reading error" );
                }
            }
        }
    }

    //--------------------------------------------------------------------------
    // EXECUTE ACTION
    //---------------

    public String execute() throws Exception {

        Log log = LogFactory.getLog( this.getClass() );
        log.info("list=" + list + " SKN=" + this.getSkn() );
        meta = new HashMap<String,Object>();

        if( list > 0 ) {
            
            initialize();
            
            Map<String,Object> jpd = statsContext.getJsonConfig();
            
            log.info("Stats: list# " + getList() +
                     " jpd.keySet()=" + jpd.keySet() );

            List spc = (List) ((Map) jpd.get( "statConfig" )).get( "species" );

            if( spc != null ) {
                log.info("spc count=" + spc.size() );
                meta.put("spc",spc.subList( 0, list) );
            }
            return "success";            
        } 


        List<NodeType> dxf = null;

        if ( ns != null && ns.equals( "taxid" ) ) {
            dxf = dipDbService.getDxfMeta( "taxid", ac,"full" );  
        } else {
            dxf = dipDbService.getDxfMeta( "psi-mi", "MI:0465","full" );
            setNs("psi-mi");
            setAc("MI:0465");
        }
        
        if( dxf == null ) { return "success"; }
        
        NodeType top = dxf.get(0);
        
        meta.put("label", top.getLabel() );
        meta.put("name", top.getName() );
        
        for( Iterator<NodeType.PartList.Part>  
                 pi = top.getPartList().getPart().iterator(); 
             pi.hasNext(); ) {
                
            NodeType nd = pi.next().getNode();

            String srcAc = "";
            String recSt = "";
            Map<String,Long>  curCounts = new HashMap<String,Long>();

            // get record producer
            //--------------------
            
            List<XrefType> xl = nd.getXrefList().getXref(); 
            for( Iterator<XrefType> xi = xl.iterator(); xi.hasNext(); ) {
                XrefType xref =  xi.next();
                if( xref.getTypeAc() != null && 
                    xref.getTypeAc().equals("dxf:0040") ){
                    srcAc = xref.getAc();
                }
            }
            
            // get record status and counts
            //-----------------------------
            
            List<AttrType> al = nd.getAttrList().getAttr(); 
            
            for( Iterator<AttrType> ai = al.iterator(); ai.hasNext(); ) {
                AttrType at =  ai.next();
                
                if( at.getAc() != null && at.getAc().equals("dip:0048") ) {
                    recSt = at.getValue().getValue();
                } else {
                    String name = at.getName();
                    String sval = at.getValue().getValue();
                    log.info( name + " :: " + sval);      
                    try{
                        Long val = Long.valueOf( sval );
                        curCounts.put( name, val );
                    } catch( Exception ex ) {
                        System.out.println( "Format error:" + sval );
                    }
                }
            }
            
            // get old counts
            //---------------
            
            Map<String,Object> dprv = null;
            
            if( srcAc!=null && srcAc.equals( "MI:0465" ) ){
                dprv = (Map<String,Object>) meta.get("DIP");
                if( dprv == null ) {
                    dprv = new HashMap<String,Object>();
                    meta.put( "DIP", dprv );
                }
                
            } else {
                dprv = (Map<String,Object>) meta.get( "other" );
                if( dprv == null ) {
                    dprv = new HashMap<String,Object>();
                    meta.put( "other", dprv );
                }
            }
            
            Map<String,Long> oldCounts = null;
            
            if( recSt != null ) {
                oldCounts = (Map<String,Long>)dprv.get( recSt );
                if( oldCounts == null ) {
                    oldCounts = new HashMap<String,Long>();
                    dprv.put( recSt, oldCounts );
                }
            }
            
            // add curCounts to oldCounts
            //---------------------------
            
            if( curCounts != null && oldCounts != null ) {
                
                for( Iterator<String> ci = curCounts.keySet().iterator(); 
                     ci.hasNext(); ) {
                    
                    String key = ci.next();
                    Long cval = curCounts.get( key );
                    Long oval = oldCounts.get( key );
                    
                    if( oval != null ) {
                        oval = new Long( oval.longValue() 
                                         + cval.longValue());
                    } else {
                        oval = cval;
                    }
                    oldCounts.put( key, oval );
                }
            }                   
        }
        return "success";
    }
}
