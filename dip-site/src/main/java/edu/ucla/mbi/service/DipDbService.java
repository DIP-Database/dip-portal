package edu.ucla.mbi.service;

/*
   #================================================================
   # $Id:: DipDbService.java 40 2009-05-14 15:06:40Z               $
   # Version: $Rev:: 40                                            $
   #================================================================
   #
   # DipDbService - DIP Database access
   #
   #================================================================ 
*/

import edu.ucla.mbi.dxf14.NodeType;
import edu.ucla.mbi.dxf14.XrefType;
import java.util.List;

public interface DipDbService{

    public List<NodeType> getRecord(String accession, String detail);

    public List<NodeType> getNode(String accession, String detail);
    public List<NodeType> getLink(String accession, String detail);
    public List<NodeType> getEvidence(String accession, String detail);
    public List<NodeType> getSource(String accession, String detail);

    public List<NodeType> getDxfQuery(String ns,String query, String detail);

    public List<XrefType> getList(String listKey);
}


