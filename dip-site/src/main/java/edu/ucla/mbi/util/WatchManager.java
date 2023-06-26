package edu.ucla.mbi.util;

/* =============================================================================
 # $Id:: WatchManager.java 3115 2013-04-12 22:25:15Z lukasz                    $
 # Version: $Rev:: 3115                                                        $
 #==============================================================================
 #
 # WatchManager - businness logic of watch lists (subject/event relationships) 
 #                 
 #=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.io.*;
import java.util.regex.PatternSyntaxException;

import java.util.GregorianCalendar;
import java.util.Calendar;

import org.json.*;

import edu.ucla.mbi.dip.*;       
import edu.ucla.mbi.util.context.*;
import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.data.dao.*;

public class WatchManager {
    
    public WatchManager() {
    Log log = LogFactory.getLog( this.getClass() );
    log.info( "WatchManager: creating manager" );
    }

    //---------------------------------------------------------------------
    //  UserContext
    //-------------

    private UserContext userContext;
    
    public void setUserContext( UserContext context ) {
        this.userContext = context;
    }
    
    public UserContext getUserContext() {
        return this.userContext;
    }
        
    //---------------------------------------------------------------------
    
    boolean debug = false;

    public boolean getDebug() {
        return debug;
    }
    
    public void setDebug( boolean debug ) {
        this.debug = debug;
    }
    
    //---------------------------------------------------------------------

    public void initialize(){
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "WatchManager: initializing" );
    }

    //---------------------------------------------------------------------

    public void cleanup(){
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "WatchManager: cleanup" );
    }


    //---------------------------------------------------------------------
    // Operations
    //---------------------------------------------------------------------
    
    
    //---------------------------------------------------------------------
    // Event observers
    //----------------
    
    public void addNewsObserver( User usr ){

        Log log = LogFactory.getLog( this.getClass() );
        log.debug( "addNewsObserver; user =" +  usr);

        log.debug( "ddNewsObserver; getUserContext=" + getUserContext() );
        log.debug( "ddNewsObserver; eorel =" + 
                   ((DipUserContext) getUserContext()).getEorelDao() );
        
        ((DipUserContext) getUserContext())
            .getEorelDao().addEORel( "news", usr );
    }

    public void dropNewsObserver( User usr ){
        Log log = LogFactory.getLog( this.getClass() );
        log.debug( "dropNewsObserver; user =" +  usr);

        log.debug( "dropNewsObserver; getUserContext=" + getUserContext() );
        log.debug( "dropNewsObserver; eorel =" + 
                   ((DipUserContext) getUserContext()).getEorelDao() );
        
        ((DipUserContext) getUserContext())
            .getEorelDao().dropEORel( "news", usr );
    }

                                                                        
    //---------------------------------------------------------------------
    // List queries
    //-------------
    
    public List<User> getNewsObserverList(){
        return ((DipUserContext) getUserContext())
            .getEorelDao().getEORel( "news" );
    }
    
    
    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    // private methods 

   
}
