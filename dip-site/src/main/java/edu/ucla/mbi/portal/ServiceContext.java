package edu.ucla.mbi.portal;

/* =============================================================================
 # $Id:: ServiceContext.java 1400 2011-01-07 21:32:50Z lukasz                  $
 # Version: $Rev:: 1400                                                        $
 #==============================================================================
 #                                                                             $
 # ServiceContext - configuration of services                                  $
 #                                                                             $
 #=========================================================================== */

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucla.mbi.service.*;
import edu.ucla.mbi.dxf14.*;

import edu.ucla.mbi.portal.*;

public class ServiceContext {

    private Map config;
    
    // inject configuration
    //----------------------

    public void setConfig( Map config ){
        
	Log log = LogFactory.getLog( ServiceContext.class );
	log.info( "ServiceConfig: configuration set" );
	this.config = config;
    }

    public Map getConfig(){
	return config;
    }
}
