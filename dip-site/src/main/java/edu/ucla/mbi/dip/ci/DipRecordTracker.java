package edu.ucla.mbi.dip.ci;

/*
   #================================================================
   # $Id:: DipRecordTracker.java 2887 2013-01-02 16:53:44Z lukasz  $
   # Version: $Rev:: 2887                                          $
   #================================================================
   #
   # ExtLinktracker - logs visits to external links
   #                
   #
   #================================================================
*/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.io.*;

import edu.ucla.mbi.portal.struts.action.RecordSupport;

public class DipRecordTracker{
    
    public DipRecordTracker(){}
    
    public void before( RecordSupport el ) {
		
	Log log = LogFactory.getLog( this.getClass() );
	log.info("tracker: dip:"+el.getAc());
	
    }
}

