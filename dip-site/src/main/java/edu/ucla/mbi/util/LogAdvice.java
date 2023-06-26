package edu.ucla.mbi.util;

/* =============================================================================
 # $Id:: LogAdvice.java 3174 2013-05-23 16:54:24Z zplat                        $
 # Version: $Rev:: 3174                                                        $
 #==============================================================================
 #
 # LogAdvice - AOP logger
 #                 
 #=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.io.*;
import java.util.regex.PatternSyntaxException;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.lang.reflect.*;
       
import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.data.dao.*;

public class LogAdvice {


    ////------------------------------------------------------------------------
    /// Watch Manager
    //---------------

    private WatchManager watchManager;

    public void setWatchManager( WatchManager manager ) {
        this.watchManager = manager;
    }

    public WatchManager getWatchManager() {
        return this.watchManager;
    }
    
    ////------------------------------------------------------------------------
    /// Notification Manager
    //----------------------

    private NotificationManager notificationManager;

    public void setNotificationManager( NotificationManager manager ) {
        this.notificationManager = manager;
    }

    public NotificationManager getNotificationManager() {
        return this.notificationManager;
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------

    public LogAdvice() {
	Log log = LogFactory.getLog( this.getClass() );
	log.info( "LogManager: creating log manager" );
    }
    
    //--------------------------------------------------------------------------

    public void simpleMonitor(){
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "LogManager: monitor called");
    }
    
    //--------------------------------------------------------------------------

    public void newsMonitor( Object rnewsItem ){
        Log log = LogFactory.getLog( this.getClass() );
        log.debug( "LogManager: news monitor called:" );
        log.debug( rnewsItem );
        
        List<User> usrNewsObsLst = watchManager.getNewsObserverList();
        notificationManager.newsNotify( (String) rnewsItem, usrNewsObsLst );        
    }
    
}
