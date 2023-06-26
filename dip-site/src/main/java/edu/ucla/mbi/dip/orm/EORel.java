package edu.ucla.mbi.dip.orm;

/*==============================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-site/s#$
 * $Id:: EORel.java 3115 2013-04-12 22:25:15Z lukasz                           $
 * Version: $Rev:: 3115                                                        $
 *==============================================================================
 *
 * EORel:  event-observer relationship
 *
 *=========================================================================== */

import edu.ucla.mbi.util.data.*;

public class EORel {

    int id;

    String event = null;
    User observer = null;

    public EORel(){};
    
    public EORel( String event, User observer){
        this.event = event;
        this.observer = observer;
    }

   
    //--------------------------------------------------------------------------
    
    public void setId( int id ){
        this.id = id;
    }

    public int getId(){
        return id;
    }
    //--------------------------------------------------------------------------
    
    public void setObserver( User observer ){
        this.observer = observer;
    }

    public User getObserver(){
        return observer;
    }

    //--------------------------------------------------------------------------
    
    public void setEvent( String event ){
        this.event = event;
    }
    
    public String getEvent(){
        return event;
    }

}