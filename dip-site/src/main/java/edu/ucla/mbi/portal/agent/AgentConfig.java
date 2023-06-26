package edu.ucla.mbi.portal.agent;

/* =============================================================================
 # $Id:: AgentConfig.java 1400 2011-01-07 21:32:50Z lukasz                     $
 # Version: $Rev:: 1400                                                        $
 #==============================================================================
 #                                                                             $
 # DipAgentConfig - configuration of DipAgent                                  $
 #                                                                             $
 #=========================================================================== */

import java.util.Map;

public interface AgentConfig{
    
    public void setConfig(Map config);
    public Map getConfig();

}
