package edu.ucla.mbi.dip.struts.action;

/* =========================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-si#$
 * $Id:: DownloadAction.java 2877 2012-12-18 20:42:36Z lukasz              $
 * Version: $Rev:: 2877                                                    $
 *==========================================================================
 *
 * UserAction action
 *                
 *
 ======================================================================== */

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

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.context.*;
import edu.ucla.mbi.util.struts.action.*;
import edu.ucla.mbi.util.struts.interceptor.*;

import edu.ucla.mbi.dip.*;

import java.util.*;
import java.io.*;


public class DownloadAction extends DownloadSupport {
    
    private DownloadContext downloadContext;

    public void setDownloadContext(  DownloadContext context ) {
	downloadContext = context;
    }

    private List tabs;
    
    public List getTabs() {
	return tabs;
    }

    public void setTabs( List tabs ) {
	this.tabs = tabs;	
    }

    private Map tabData;

    public Map getTabData() {
        return tabData;
    }

    public void setTabData( Map tabData ) {
        this.tabData = tabData;
    }


    //---------------------------------------------------------------------
    // tab selection
    //--------------

    private List<Integer> tabSelect;

    public List<Integer> getTabSelect() {

	if ( tabSelect == null ) {
	    tabSelect = new ArrayList();
	    tabSelect.add( 1 );
	    tabSelect.add( 1 );
	}
	return tabSelect;
    }
    
    public void setTabSelect( List<Integer> tabSelect ) {
	this.tabSelect = tabSelect;
    }

    private String tbs;

    public String getTbs() {
	return tbs;
    }

    public void setTbs( String select ) {

	this.tbs = select;

	if ( select != null && select.length() > 0 ) {
	   
	    tabSelect = new ArrayList();
            if( select != null ) {
                String split[] = select.split( ":" );
		
                for( int i = 0; i < split.length; i++ ) {
                    try {
                        int val = Integer.parseInt( split[ i ] );
                        tabSelect.add( val );
                    } catch ( NumberFormatException e ){
                        tabSelect.add( 0 );
                    }
                }
            }
	}
    }

    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    
    public String execute() throws Exception{

	Log log = LogFactory.getLog( DownloadAction.class );
	log.info( " initilizing download defs..." );

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

 	//tabs = (List) jdd.get("tabs");   // remove tabs ???
	log.info( " menusel=" + getMenuSel());
	if( getMenuSel()!=null ){
	    tabData = downloadContext.getData( getMenuSel().get(2)-1, tabSelect );
	} else {
	    tabData = downloadContext.getData( -1, tabSelect );
	}


        if(getRet() != null && getRet().equals("JSON") ) {
            return "json";
        }
        
	return SUCCESS;
    }
    
    //---------------------------------------------------------------------

    public void validate() { 

	Log log = LogFactory.getLog( DownloadAction.class );
	
    }
}