package edu.ucla.mbi.service;

/* =============================================================================
 * $Id:: DxfService.java 1815 2011-07-14 19:32:37Z lukasz                      $
 * Version: $Rev:: 1815                                                        $
 *==============================================================================
 *                                                                             $
 * DxfService - service access through standard Dxf interface                  $
 *                                                                             $
 *=========================================================================== */

import edu.ucla.mbi.dxf14.NodeType;
import edu.ucla.mbi.dxf14.XrefType;
import java.util.List;

public interface DxfService{
    public NodeType       getDxfNode( String ns, String accession, 
                                      String tag );

    public List<XrefType> getDxfRefList( String ns, String accession, 
                                         String tag );
    public List<XrefType> getDxfRefList( String ns, String accession, 
                                         String tag, String match );

    public List<NodeType> getDxfNodeList( String ns,String accession, 
                                          String tag);
    public List<NodeType> getDxfNodeList( String ns, String accession, 
                                          String tag, String match );

    public List<NodeType> getDxfQuery( String ns, String accession, 
                                       String tag);

    public List<NodeType> getDxfMeta( String ns, String accession, 
                                      String tag );
}


