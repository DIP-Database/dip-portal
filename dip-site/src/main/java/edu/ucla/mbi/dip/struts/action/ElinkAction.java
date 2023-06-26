package edu.ucla.mbi.dip.struts.action;

/*
   #================================================================
   # $Id:: ElinkAction.java 2877 2012-12-18 20:42:36Z lukasz       $
   # Version: $Rev:: 2877                                          $
   #================================================================
   #
   # ElinkAction - returns external link corresponding to 
   #               namespace/accession pair
   #
   #================================================================ 
*/

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.CookiesAware;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.util.regex.*;

import java.io.*;

import javax.xml.bind.*;

import edu.ucla.mbi.util.struts.action.*;

import edu.ucla.mbi.dxf14.*;
import edu.ucla.mbi.service.*;
import edu.ucla.mbi.dip.agent.*;

public class ElinkAction extends ElinkSupport {


    //---------------------------------------------------------------------
    // input parameters
    //-----------------
    
    String namespace;
    
    public String getNs() {
        return namespace;
    }

    public void setNs( String ns ) {
        this.namespace = ns;
    }

    String accession;
    
    public String getAc() {
        return accession;
    }

    public void setAc( String ac ) {
        this.accession = ac;
    }

    String rtype = "success";
    
    public String getRt() {
        return rtype;
    }

    public void setRt( String rt ) {
        this.rtype = rt;
    }




    //---------------------------------------------------------------------
    // result
    //-------

    private String  url="";       

    public String getUrl() {
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "getUrl="+this.url);
        return this.url;
    }
    
    public void setUrl(String url) {
        this.url=url;
    }    
    
    //---------------------------------------------------------------------
    

    public String execute() throws Exception {

	Log log = LogFactory.getLog( this.getClass() );
	log.info( " NS=" + namespace + " AC=" + accession );
	
	if( namespace != null && accession != null ) {

	    Map<String,Object> jdd = getElinkContext().getJsonConfig();

	    if ( jdd == null ) {
                //  log.info( " jdd="+jdd);
		String jsonPath =
		    (String) getElinkContext().getConfig().get( "json-config" );
		log.info( "JsonElinkDef=" + jsonPath );
		
		if ( jsonPath != null && jsonPath.length() > 0 ) {

		    String cpath = jsonPath.replaceAll("^\\s+","" );
		    cpath = jsonPath.replaceAll("\\s+$","" );

		    try {
			InputStream is =
			    getServletContext().getResourceAsStream( cpath );
			getElinkContext().readJsonConfigDef( is );

			jdd = getElinkContext().getJsonConfig();

		    } catch ( Exception e ){
			log.info( "JsonConfig reading error" );
		    }
		}
	    }

            //log.info( " jdd=" + jdd );

	    Map urlTemplate = (Map) ((Map)jdd.get("namespace")).get( namespace );

	    if ( urlTemplate != null ) {
                //log.info( " urlTemplate=" +urlTemplate);
		String urlPattern = (String) urlTemplate.get( "url" );
		String accMark = (String) urlTemplate.get( "ac" );
                //log.info( " urlPattern=" +urlPattern);
		try{
		    url = urlPattern.replaceFirst( accMark, accession );
                    //log.info( " url=" + url );
		} catch( PatternSyntaxException pe ) {
                    pe.printStackTrace();
		}
	    } 
	} 


	if( url == null || url.length() == 0 ){
	    url="http://www.doe-mbi.ucla.edu";
	}
	log.info( "success url=" + url );	
	return rtype;
    }
    
}

