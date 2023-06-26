package edu.ucla.mbi.dip.struts.action;

/* ========================================================================
 # $Id:: DataAware.java 2877 2012-12-18 20:42:36Z lukasz                  $
 # Version: $Rev:: 2877                                                   $
 #=========================================================================
 #                                                                        $
 # DataAware interface                                                    $
 #                                                                        $
 #  ns/ac - namespace(db)/accession pair                                  $
 #  md    - model name (as defined in RecordModel)                        $
 #  sl    - slot (as defined in RecordModel)                              $
 #                                                                        $
 #====================================================================== */
                                                                           
import java.util.List;
import edu.ucla.mbi.dxf14.NodeType;

public interface DataAware  {
        
    public NodeType getSummary();
    public void setSummary( NodeType summary );

    //--------------------------------------------------
    
    public List<NodeType> getDetail();
    public void setDetail( List<NodeType> detail );

}
