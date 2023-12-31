package edu.ucla.mbi.util;

/* =============================================================================
 # $Id:: NewsManager.java 3115 2013-04-12 22:25:15Z lukasz                     $
 # Version: $Rev:: 3115                                                        $
 #==============================================================================
 #
 # NewsManager - 
 #                 
 #=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.io.*;
import java.util.regex.PatternSyntaxException;

import java.util.GregorianCalendar;
import java.util.Calendar;
       
import edu.ucla.mbi.util.*;
import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.data.dao.*;

public class NewsManager {
    
    public NewsManager() {
	Log log = LogFactory.getLog( this.getClass() );
	log.info( "NewsManager: creating news manager" );
    }
    
    public String buildMailMessage( String date, String time,
                                    String header, String body,
                                    String email ){
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "NewsManager: buildMailMessage: " );

        StringBuffer anno = new StringBuffer();
        
        anno.append( "Subject: " + header + "\n");
        anno.append( "--------\n\n");
        anno.append( body + "\n");
        anno.append( "\n");
        anno.append( "Contact: " + email + "\n");
        
        return anno.toString();
   
    }
}
