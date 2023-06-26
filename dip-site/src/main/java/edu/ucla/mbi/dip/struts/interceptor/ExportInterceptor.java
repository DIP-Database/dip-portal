package edu.ucla.mbi.dip.struts.interceptor;

/* =============================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-site/s#$
 * $Id:: ExportInterceptor.java 2877 2012-12-18 20:42:36Z lukasz               $
 * Version: $Rev:: 2877                                                        $
 *==============================================================================
 *                                                                             $
 * ExportInterceptor: data export                                              $
 *                                                                             $
 *=========================================================================== */

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

import javax.servlet.ServletContext;

import org.apache.struts2.util.ServletContextAware;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.context.*;

import edu.ucla.mbi.util.struts.action.*;
import edu.ucla.mbi.util.struts.interceptor.*;

public abstract class ExportInterceptor implements Interceptor {

    protected abstract String buildExport( ValueStack stack,
                                           ExportAware action );        
   
    public void destroy() { }

    private JsonContext exportContext = null;

    public void setExportContext( JsonContext context ){
        this.exportContext = context;
    }

    public JsonContext getExportContext(){
        return this.exportContext;
    }

    //--------------------------------------------------------------------------

    public void init() { 
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "ExportInterceptor: initializing" );
        
        //initialize( true );
    }

    //--------------------------------------------------------------------------

    public void initialize( ActionInvocation invocation, boolean force ){
        if( exportContext == null ) return;  // no context
        
        Map<String,Object> jpd = getExportContext().getJsonConfig();

        if ( jpd == null || force ) {

            Log log = LogFactory.getLog( this.getClass() );
            log.info( " (Re)initilizing ExportInterceptor..." );
            String jsonPath =
                (String) getExportContext().getConfig().get( "json-config" );
            log.debug( "JsonExportDef=" + jsonPath );

            if ( jsonPath != null && jsonPath.length() > 0 ) {

                String cpath = jsonPath.replaceAll("^\\s+","" );
                cpath = jsonPath.replaceAll("\\s+$","" );

                ServletContext sc = 
                    ((PortalSupport) invocation.getAction())
                    .getServletContext();

                log.debug( "ServletContext =" + sc );

                try {
                    InputStream is =
                        sc.getResourceAsStream( cpath );
                    getExportContext().readJsonConfigDef( is );
                    
                    jpd = getExportContext().getJsonConfig();

                } catch ( Exception e ){
                    log.info( "JsonConfig reading error" );
                }
            }
        }
    }

    public void initialize( ActionInvocation invocation ){
        initialize( invocation, true );
    }

    //--------------------------------------------------------------------------
    
    public String intercept( ActionInvocation invocation ) 
        throws Exception {
        
        invocation.addPreResultListener( new PreResultListener() {
                public void beforeResult( ActionInvocation invocation, 
                                          String resultCode ) {
                    
                    Log log = LogFactory.getLog( this.getClass() );
                    boolean b = invocation.getAction() instanceof ExportAware;
                    log.debug("beforeResult: exportaware=" + b );
                    
                    if ( invocation.getAction() instanceof 
                         ExportAware ) {

                        initialize( invocation );
                        build( invocation );
                    } else {
                        return; // abort if not ExportAware
                    }
                }
            } );
        
        return invocation.invoke();
    }
    
    protected void build( ActionInvocation invocation ) {
     
        Log log = LogFactory.getLog( this.getClass() );
        log.debug( "building..." );
        ValueStack stack = invocation.getStack();
        
        ExportAware action = (ExportAware) invocation.getAction();
        
        TableViewContext tableContext = action.getTableContext();
        String table = action.getTableName();
        List tableData = action.getTableData();

        String expString =  buildExport( stack, action);
        
        try { 
            byte[] bytes = expString.getBytes("UTF-8");
            action.setExportStream( new ByteArrayInputStream( bytes ) );
        } catch( UnsupportedEncodingException uee ) {
            // NOTE: should not happen
        }
        //action.setContentType( "text/xml; charset=ISO-8859-1" );
    }
}

