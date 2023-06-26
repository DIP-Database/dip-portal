package edu.ucla.mbi.dip.struts.action;

/* =============================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-site/s#$
 * $Id:: FileAction.java 3609 2014-03-15 18:51:56Z lukasz                      $
 * Version: $Rev:: 3609                                                        $
 *==============================================================================
 *
 * FileAction action
 *                
 *
 ============================================================================ */

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.CookiesAware;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;

import javax.servlet.http.Cookie;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import org.json.*;

import edu.ucla.mbi.util.context.*;
import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.struts.action.*;
import edu.ucla.mbi.util.struts.interceptor.*;

import edu.ucla.mbi.dip.*;

import java.util.*;
import java.util.regex.*;
import java.io.*;


public class FileAction extends DownloadSupport {

    public static final String NOACCESS = "noaccess";
    public static final String NOFILE = "nofile";
    
    private DownloadContext downloadContext;
    
    public void setDownloadContext(  DownloadContext context ) {
	downloadContext = context;
    }

    //--------------------------------------------------------------------------
    // file
    //-----

    String file;

    public void setFn( String file ){
        this.file = file;
    }

    //--------------------------------------------------------------------------
    
    String format ="";

    public void setFf( String format ){
        this.format = format;
    }

    String compress ="";

    public void setFc( String compress ){
        this.compress = compress;
    }

    //--------------------------------------------------------------------------
    
    String dtsId ="";

    public void setDs( String dataset ){
        this.dtsId = dataset;
    }

    //--------------------------------------------------------------------------
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
    //--------------------------------------------------------------------------
    
    public String execute() throws Exception{

	Log log = LogFactory.getLog( this.getClass() );
	log.info( "DS=" + dtsId + " FN=" + file + " FF=" + format );
		  	
        
        if( dtsId== null || dtsId.length() == 0 ||
            file == null || file.length() == 0 ){
            return NOACCESS;            
        }
        
        String role = "default";
	    
        log.info( "session=" + getSession() );

        if ( getSession() != null && 
             getSession().get( "USER_ID" ) != null ) {
            int uid = (Integer) getSession().get( "USER_ID" );
            log.info( "uid=" + getSession().get( "USER_ID" ) );
            if (uid > 0 ) {
                role = "user";
            }
        }
	
        Map<String,Object> jdd = downloadContext.getJsonConfig();
        if ( jdd == null ) {
            String jsonPath =
                (String) downloadContext.getConfig().get( "json-config" );
            log.info( "JsonPageDef=" + jsonPath );
            
            if ( jsonPath != null && jsonPath.length() > 0 ) {
                String cpath = jsonPath.replaceAll("^\\s+","" );
                cpath = jsonPath.replaceAll("\\s+$","" );
		
                try {
                    InputStream is =
                        getServletContext().getResourceAsStream( cpath );
                    downloadContext.readJsonConfigDef( is );
                    
                    jdd = downloadContext.getJsonConfig();
                    
                } catch ( Exception e ){
                    log.info( "JsonConfig reading error" );
                }
            }
        }
        
        Map dmap = downloadContext.getData( dtsId );
        
        String qfile = null;
        
        if( dmap != null && file != null && format != null ){
            
            List dlist = (List) dmap.get( "data" );
            for( Iterator ii = dlist.iterator(); ii.hasNext(); ){
                Map fi = (Map) ii.next(); 
                
                if( file.equals( (String) fi.get("file") ) &&
                    format.equals( (String) fi.get("ext") ) ){
                    
                    qfile = (String) fi.get( "path" ) + 
                        (String) fi.get("file") +
                        "." + (String) fi.get("ext");
                    format="." + format;
                    if( compress != "" ){
                        compress  = "." + compress;
                        qfile += compress;
                    }
                    break;
                }
            }
            log.info( "qfile(context): " + qfile );
        }
        
        if( qfile == null ){ 
            
            // NOTE:eliminate - log format sold ne known only to data context

            String  dtsLog = null;
            List<Map> dtsList = (List<Map>) jdd.get("dataset");

            for( Iterator<Map> di = dtsList.iterator(); di.hasNext(); ){
                Map cm = di.next();
                
                if( cm.get( "id" ) != null &&  
                    ((String) cm.get( "id" )).equals( dtsId ) ){
                    dtsLog = (String) cm.get("log");
                    break;
                }            
            }
 
            log.debug( "dtsLog=" + dtsLog );
            
            if( dtsLog == null ){
                return NOACCESS;
            }
            
            try{
                Pattern fp = Pattern.compile( "^File:\\s+(.+" + file +")\\t.+" );  
                BufferedReader in
                    = new BufferedReader(new FileReader( dtsLog ));
                
                String strLn = null;
                while( (strLn = in.readLine()) != null ){
                    Matcher m = fp.matcher( strLn );
                    if( m.matches() ){
                        qfile = m.group(1);
                        log.info( "qfile(dtslog): " + qfile );
                        break;
                }
            }

            in.close();

            } catch( Exception ex ){
                ex.printStackTrace();
                return NOACCESS;
            }
        }
        
        if( qfile == null ){
            return NOACCESS;
        }

        List accPattern = (List) ((Map) jdd.get( "access" )).get( role );
        
        boolean access = true; // false;
        for ( Iterator ii = accPattern.iterator(); ii.hasNext() && !access; ) {	 
            String pattern = (String) ii.next();
            try {
                if ( qfile.matches( pattern ) ){
                    access = true;
                }
            } catch ( PatternSyntaxException psx ) {
                log.info( "Accession pattern invalid: " + pattern );
            }
        }
        
        if( access ){
             
            //if(format != null && format.length() > 0 ){
            //    qfile = qfile + "." + format;
            //    format = "." + format;
            //} else {
            //    format = "";
            //}
            try {
                log.info( "Opening file: -->" + qfile + "<--" );
                is = new FileInputStream( qfile ) ;
            } catch ( FileNotFoundException fx ) {
                log.info( "File not found: " + qfile );
                return NOFILE;
            }

            contentType ="text/plain";
            contentDisposition = 
                "attachment;filename=\"" + file + format + compress + "\"";
            return SUCCESS;
        } else {
            return NOACCESS;
        }
    }
}