package edu.ucla.mbi.service;

/*
   #================================================================
   # $Id:: MapRecordService.java 40 2009-05-14 15:06:40Z           $
   # Version: $Rev:: 40                                            $
   #================================================================
   #
   # MapRecordService - maps namespace/accession to original record
   #                    
   #================================================================ 
*/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.io.*;

import javax.xml.bind.*;

import edu.ucla.mbi.dxf14.*;
import edu.ucla.mbi.services.dip.*;

public class MapRecordService 
    implements MapService{
    
    public String getUrl(String namespace, String accession){
	
	// NOTE: not implemented
	return null;
    }

}



