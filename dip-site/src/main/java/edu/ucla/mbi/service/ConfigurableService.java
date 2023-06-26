package edu.ucla.mbi.service;

/* =============================================================================
 * $Id:: ConfigurableService.java 1217 2010-09-12 17:16:32Z lukasz             $
 * Version: $Rev:: 1217                                                        $
 *==============================================================================
 *                                                                             $
 * ConfigurableService - on-line configuration hooks                           $
 *                                                                             $
 *=========================================================================== */

import java.util.List;

public interface ConfigurableService {
    
    public void setJsonConfig( String config );
    public void readJsonConfig();
    public void saveJsonConfig();
    public List<String> getEndpointList();
    public void addEndpoint( String endpoint );
    public void dropEndpoint( int index );
    public String setDefaultEndpoint( int index );
    
}


