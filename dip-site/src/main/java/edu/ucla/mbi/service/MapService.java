package edu.ucla.mbi.service;

/*
   #================================================================
   # $Id:: MapService.java 40 2009-05-14 15:06:40Z                 $
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

public interface MapService{

    public String getUrl(String namespace, String accession);

}


