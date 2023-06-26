package edu.ucla.mbi.dip.agent;

/* =============================================================================
 # $Id:: DipAgentConfig.java 2877 2012-12-18 20:42:36Z lukasz                  $
 # Version: $Rev:: 2877                                                        $
 #==============================================================================
 #                                                                             $
 # DipAgentConfig - configuration of DipAgent                                  $
 #                                                                             $
 #=========================================================================== */

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucla.mbi.service.*;
import edu.ucla.mbi.dxf14.*;

import edu.ucla.mbi.portal.agent.*;

public class DipAgentConfig implements AgentConfig{

    private Map config;
    
    // inject configuration
    //----------------------

    public void setConfig(Map config){

	Log log = LogFactory.getLog(DipAgentConfig.class);
	log.info("DipAgentConfig: configuration set");
	this.config=config;
    }

    public Map getConfig(){
	return config;
    }
}
