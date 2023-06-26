package edu.ucla.mbi.dip.transform;

/* =============================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-site/s#$
 * $Id:: GraphvizTransformer.java 2877 2012-12-18 20:42:36Z lukasz             $
 * Version: $Rev:: 2877                                                        $
 *==============================================================================
 *                                                                             $
 * GrvTransformer: graphviz-based layout/transformation                        $
 *                                                                             $
 *=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.io.*;
import edu.ucla.mbi.util.context.*;
import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.graphviz.*;

import javax.servlet.ServletContext;

public class GraphvizTransformer implements NetTransformer{

    JsonContext grvContext;

    public void setGraphvizContext( JsonContext context ){
        grvContext = context;       
    }
    
    JsonContext getGraphvizContext(){
        Log log = LogFactory.getLog( this.getClass() );
        return grvContext;
    }
    
    //--------------------------------------------------------------------------

    public void initialize( ServletContext sc ){
        initialize( sc, false );
    }

    public void initialize( ServletContext sc, boolean force ){
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "initializing: GraphvizContext=" + getGraphvizContext() );
        
        JsonContext gc = getGraphvizContext();

        // initialize GraphvizContext
        //---------------------------
        
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
                    InputStream is = sc.getResourceAsStream( cpath );
                    gc.readJsonConfigDef( is );
                } catch ( Exception e ){
                    e.printStackTrace();
                    log.info( "JsonConfig parsing error" );
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

    public Object transform( Object in, Map param ){

        Log log = LogFactory.getLog( this.getClass() );
        log.debug( "param=" + param );
        
        String layout = (String) param.get( "layout" );
        String format = (String) param.get( "format" );
        Boolean binary = (Boolean) param.get( "binary" );
        if( binary == null ){
            binary = true;
        }
        
        log.debug( "layout=" + layout + " format=" + format + " binary=" + binary );
        
        GraphViz gv = new GraphViz();

        byte[] g = gv.getGraph( (String) in, layout, format );
        log.debug( "Grv Transformation (fis) performed" );
        
        if( !binary ){
            return new String( g );
        } else {
            return g;
        }
    }

    public Object transform( ServletContext sc, Object in, Map param,
                             Map<String,InputStream> fisMap ){
        
        Log log = LogFactory.getLog( this.getClass() );

        log.debug( "sc=" + sc);
        initialize( sc );
        
        return transform( in, param );        
    }
}
