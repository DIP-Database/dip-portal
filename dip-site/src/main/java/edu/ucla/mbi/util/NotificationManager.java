package edu.ucla.mbi.util;

/* =============================================================================
 # $Id:: NotificationManager.java 3174 2013-05-23 16:54:24Z zplat              $
 # Version: $Rev:: 3174                                                        $
 #==============================================================================
 #
 # NotificationManager -  
 #                 
 #=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.io.*;
import java.util.regex.PatternSyntaxException;

import java.util.GregorianCalendar;
import java.util.Calendar;

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.data.dao.*;


public class NotificationManager {
    
    public NotificationManager() {
	Log log = LogFactory.getLog( this.getClass() );
	log.info( "NotificationManager: creating notification manager" );
    }
     
    String queueDir = "/var/dip-site/queue";

    public void setQueueDir( String dirName ){
        queueDir = dirName;
    }

    public void initialize(){
        Log log = LogFactory.getLog( this.getClass() );
	log.info( "NotificationManager: initialized" );
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    public void newsNotify( String newsItem, List<User> rcpLst ){
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "newsNotify called" );

        if( newsItem == null || rcpLst == null || rcpLst.size() == 0 ){
            log.info( "newsNotify: DONE" );
            return;
        }       
        
        String recipients = "";
        boolean send = false;
        
        Iterator<User> rcpi = rcpLst.iterator();
        
        while( rcpi.hasNext() ){
            User recipient = rcpi.next();
            
            String globalMailFlag = 
                PrefUtil.getPrefOption( recipient.getPrefs(), "message-mail" );
            
            String mailFlag = PrefUtil.getPrefOption( recipient.getPrefs(), 
                                                      "mail-news" );
            if( globalMailFlag != null && mailFlag != null
                && globalMailFlag.equalsIgnoreCase( "true" )
                && mailFlag.equalsIgnoreCase( "true" ) ){
                
                String email = recipient.getEmail();
                recipients += " " + email  + ",";
                send = true;
            } 
        }
        
        if( !send ) return;  // no mail notifications requested
        
        recipients  = recipients.substring(0, recipients.length() - 1);
                                           
        String message = "EMAIL=\"" + recipients + "\"\n" 
            + "MODE=\"NEWS_ITEM\"\n" 
            + "NEWS_ITEM=\"" + newsItem.replace("\"","\\\"") +   "\"\n" ;
        
        //Write message information to a queue file
        
        try {
            String fileName = queueDir + File.separator 
                + String.valueOf( System.currentTimeMillis() ) 
                + ".queue";
            
            File file = new File( fileName );
            file.createNewFile();
            FileOutputStream fout = new FileOutputStream( file );
            fout.write( ( message ).getBytes());
            fout.close();
            
        }catch ( IOException ex ) { 
            System.out.println( ex );
        }
    }
    
}
