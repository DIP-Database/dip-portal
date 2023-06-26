package edu.ucla.mbi.portal.struts.action;

/* =============================================================================
 * $Id:: GraphAction.java 2877 2012-12-18 20:42:36Z lukasz                     $
 * Version: $Rev:: 2877                                                        $
 *==============================================================================
 *                                                                             $
 * GraphAction action - graph rendering                                        $
 *                                                                             $
 *     TO DO:                                                                  $
 *                                                                             $
 *=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.io.*;

import javax.servlet.ServletContext;

import edu.ucla.mbi.portal.*;
import org.json.*;

import edu.ucla.mbi.util.context.JsonContext;
import edu.ucla.mbi.util.struts.action.PortalSupport;
import edu.ucla.mbi.util.struts.interceptor.*;

import edu.ucla.mbi.util.graphviz.*;

public class GraphAction extends PortalSupport {

    private static final String FILE = "file";
    private static final String JSON = "json";
    
    //--------------------------------------------------------------------------
    // configuration
    //--------------
    
    private JsonContext graphContext;

    public void setGraphContext( JsonContext context ) {
        this.graphContext = context;
    }

    public JsonContext getGraphContext() {
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "GraphAction: graphContext=" + graphContext );

        return this.graphContext;
    }

    //--------------------------------------------------------------------------
    // parameters
    //-----------
    
    String dot;

    public void setDot( String dot ){
        this.dot=dot;
    }

    public String getDot(){
        return dot;
    }
    
    String gf = "svg";

    public void setGf( String format ){
        gf = format;
    }

    String gl = "fdp";

    public void setGl( String layout ){
        gl = layout;
    }

    String ret="data";

    public String getRet(){
        return ret;
    }

    public void setRet(String ret){
        this.ret = ret;
    }


    //---------------------------------------------------------------------
    // svg 
    //----

    private String svg;

    public String getSvg(){
        return svg;
    }
    
    
    //---------------------------------------------------------------------
    // file download
    //--------------

    String contentType;

    public String getContentType() {
        return contentType;
    }
    
    String contentDisposition;

    public String getContentDisposition() {
        return contentDisposition;
    }

    InputStream  is;
    public InputStream getFileStream() {
        return is;
    }
    
    //--------------------------------------------------------------------------
    // EXECUTE ACTION
    //---------------
    
    public String execute() throws Exception {
        
        Log log = LogFactory.getLog( this.getClass() );
        log.debug( " MenuContext: " + super.getMenuContext() );
        log.debug( " GraphContext: " + getGraphContext() );
        
        initialize();
        
        //----------------------------------------------------------------------
        // dispatch 
        //---------
        
        return dispatch();
    }
    
    //--------------------------------------------------------------------------
    // initialize
    //-----------
    
    public void initialize(){
        initialize( false );
    }

    public void initialize( boolean force ){
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "initializing: GraphContext=" + getGraphContext() );
        
        JsonContext gc = getGraphContext();

        // initialize GraphContext
        //------------------------
        
       if( gc != null && ( gc.getJsonConfig() == null || force ) ){
            
            // process json configuration
            //---------------------------
            
            String jsonPath = 
                (String) gc.getConfig().get( "json-config" );
                
            log.info( " config file=" + jsonPath );
            
            if ( jsonPath != null && jsonPath.length() > 0 ) {
                    
                String cpath = jsonPath.replaceAll("^\\s+","" );
                cpath = jsonPath.replaceAll("\\s+$","" );
                    
                try {
                    
                    ServletContext sc = getServletContext();
                    log.info( " ServletContext sc=" + sc );
                    InputStream is =
                        getServletContext().getResourceAsStream( cpath );
                        
                    log.info( " GraphContext stream=" + is );
                        
                    gc.readJsonConfigDef( is );
                    
                } catch ( Exception e ){
                    e.printStackTrace();
                    log.info( "JsonConfig reading error" );
                }       

                Map<String,Object> gcm = gc.getJsonConfig();
                if(gcm != null ) {
                    log.info( " dot=" + gcm.get("dot-bin"));
                    log.info( " tmp=" + gcm.get("dot-tmp"));

                    if( gcm.get("dot-bin") != null ){
                        GraphViz.setDotBin( (String) gcm.get("dot-bin") );
                        GraphViz.setTempDir( (String) gcm.get("dot-tmp") );
                    }
                }
            }
        }
    }
    
    //--------------------------------------------------------------------------
    // dispatcher
    //-----------

    public String dispatch() throws Exception {

        if( dot != null ){
            Log log = LogFactory.getLog( this.getClass() );
            
            GraphViz gv = new GraphViz();
            byte[] g = gv.getGraph( dot, gl, gf );
            
            if(g != null ){
                if( gf !=null ){
                    
                    if( ret != null && ret.equals( "file" ) ){
                        if( gf.equals( "svg" ) ){
                            contentType ="image/svg+xml";
                        }
                        
                        if( gf.equals( "png" ) ){
                            contentType ="image/png";
                        }
                        
                        is = new ByteArrayInputStream( g );   
                        
                        return FILE;
                    }
                    
                    if( gf.equals( "svg" ) ){
                     
                        this.svg = new String( g );
                        
                        log.info("SVG=\n" + this.svg +"\n");

                        if( ret != null && ret.equals( "json" )){
                            return JSON;
                        }
                    }
                }
            }
        }
        return SUCCESS;
    }

}
