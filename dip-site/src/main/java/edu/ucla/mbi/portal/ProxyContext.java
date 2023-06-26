package edu.ucla.mbi.portal;

/* =============================================================================
 # $Id:: ProxyContext.java 2877 2012-12-18 20:42:36Z lukasz                    $
 # Version: $Rev:: 2877                                                        $
 #==============================================================================
 #                                                                             $
 # ProxyContext - configuration of proxy                                       $
 #                                                                             $
 #=========================================================================== */

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucla.mbi.service.*;
import edu.ucla.mbi.dxf14.*;

import edu.ucla.mbi.portal.*;

public class ProxyContext {

    private Map config;
    
    // inject configuration
    //----------------------

    public void setConfig( Map config ){
        
	Log log = LogFactory.getLog( ProxyContext.class );
	log.info( "ProxyConfig: configuration set" );
	this.config = config;
    }

    public Map getConfig(){
	return config;
    }
}
