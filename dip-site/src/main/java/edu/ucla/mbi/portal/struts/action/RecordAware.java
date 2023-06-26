package edu.ucla.mbi.portal.struts.action;

/* =============================================================================
 # $Id:: RecordAware.java 1400 2011-01-07 21:32:50Z lukasz                     $
 # Version: $Rev:: 1400                                                        $
 #==============================================================================
 #                                                                             $
 # RecordAware interface - record related parmeters                            $
 #                                                                             $
 #  db    - source database                                                    $
 #  ns/ac - namespace(db)/accession pair                                       $
 #  dl    - detail level                                                       $
 #                                                                             $
 #=========================================================================== */

public interface RecordAware  {
    
    public String getDb();
    public void setDb( String database );
    
    //---------------------------------------------------------------------   

    public String getNs();
    public void setNs( String namespace );
    
    //---------------------------------------------------------------------   
    
    public String getAc();
    public void setAc( String accession );

    //---------------------------------------------------------------------
    
    public String getDl();
    public void setDl( String detail );
    
}
