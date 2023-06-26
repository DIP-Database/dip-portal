package edu.ucla.mbi.util;

/* =============================================================================
 # $Id:: UserPrefManager.java 3115 2013-04-12 22:25:15Z lukasz                 $
 # Version: $Rev:: 3115                                                        $
 #==============================================================================
 #
 # UserPrefManager - businness logic of user preferences manager 
 #                 
 #=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.io.*;
import java.util.regex.PatternSyntaxException;

import java.util.GregorianCalendar;
import java.util.Calendar;
       
import edu.ucla.mbi.util.context.*;
import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.data.dao.*;

public class UserPrefManager {
    
    public UserPrefManager() {
    Log log = LogFactory.getLog( this.getClass() );
    log.info( "UserPrefManagerManager: creating manager" );
    }
    
    private UserContext userContext;

    public void setUserContext( UserContext context ) {
        this.userContext = context;
    }

    public UserContext getUserContext() {
        return this.userContext;
    }

    private String defUserPrefs="";

    public void setDefUserPrefs( String prefs ) {
        this.defUserPrefs = prefs;
    }

    public String getDefUserPrefs() {
        return this.defUserPrefs;
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
        log.info( "UserPrefManager: initializing" );
    }

    //---------------------------------------------------------------------

    public void cleanup(){
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "UserPrefManager: cleanup" );
    }


    //---------------------------------------------------------------------
    // Operations
    //---------------------------------------------------------------------
    


    //---------------------------------------------------------------------
    //---------------------------------------------------------------------
    // private methods 


}
