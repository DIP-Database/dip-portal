package edu.ucla.mbi.dip.struts.action;

/* 
 #=========================================================================
 # $Id:: QueryAware.java 3177 2013-05-24 14:30:06Z lukasz                 $
 # Version: $Rev:: 3177                                                   $
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

public interface QueryAware  {
    
    public String getQt();
    public void setQt( String queryType );

    public String getQuery();
    public void setQuery( String query );
   
}
