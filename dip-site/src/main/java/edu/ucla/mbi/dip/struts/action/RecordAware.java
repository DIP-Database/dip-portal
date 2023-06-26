package edu.ucla.mbi.dip.struts.action;

/* 
 #=========================================================================
 # $Id:: RecordAware.java 2877 2012-12-18 20:42:36Z lukasz                $
 # Version: $Rev:: 2877                                                   $
 #=========================================================================
 #                                                                        $
 # RecordAware interface - record related parmeters                       $
 #                                                                        $
 #  ns/ac - namespace(db)/accession pair                                  $
 #  md    - model name (as defined in RecordModel)                        $
 #  sl    - slot (as defined in RecordModel)                              $
 #                                                                        $
 #========================================================================
*/
                                                                           
import java.util.List;
import edu.ucla.mbi.dxf14.NodeType;

public interface RecordAware  {
    
    //---------------------------------------------------------------------   

    public String getNs();
    public void setNs( String namespace );
    
    //---------------------------------------------------------------------   
    
    public String getAc();
    public void setAc( String accession );

    //---------------------------------------------------------------------
    
    public String getDl();
    public void setDl( String dl );


}
