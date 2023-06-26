package edu.ucla.mbi.dip.ci;

/*
   #================================================================
   # $Id:: ExtLinkTracker.java 2877 2012-12-18 20:42:36Z lukasz    $
   # Version: $Rev:: 2877                                          $
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

import edu.ucla.mbi.dip.struts.action.ElinkAction;

public class ExtLinkTracker {

    public ExtLinkTracker() {
    }
    
    public void before( ElinkAction el ) {
		
	Log log = LogFactory.getLog( this.getClass() );
	log.info( "ExtLinkTracker: " + el.getNs() + ":" + el.getAc() );
	log.info( "ExtLinkTracker session: " + el.getSession().keySet() );

	/* NOTE: items to track:
	          - namespace/accession
                  - timestamp
                  - IP
                  - session id
                  - login
	*/
    }
}

