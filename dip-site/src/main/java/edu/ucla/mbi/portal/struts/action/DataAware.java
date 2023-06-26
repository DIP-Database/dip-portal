package edu.ucla.mbi.portal.struts.action;

/* =============================================================================
 # $Id:: DataAware.java 1400 2011-01-07 21:32:50Z lukasz                       $
 # Version: $Rev:: 1400                                                        $
 #==============================================================================
 #                                                                             $
 # DataAware interface                                                         $
 #                                                                             $
 #  ns/ac - namespace(db)/accession pair                                       $
 #  md    - model name (as defined in RecordModel)                             $
 #  sl    - slot (as defined in RecordModel)                                   $
 #                                                                             $
 #=========================================================================== */
                                                                           
import java.util.List;
import edu.ucla.mbi.dxf14.NodeType;

public interface DataAware  {
        
    public NodeType getSummary();
    public void setSummary( NodeType summary );

    //--------------------------------------------------
    
    public List<NodeType> getDetail();
    public void setDetail( List<NodeType> detail );

}
