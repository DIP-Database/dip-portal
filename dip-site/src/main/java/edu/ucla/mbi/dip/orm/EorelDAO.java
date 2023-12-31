package edu.ucla.mbi.dip.orm;

/*==============================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-site/s#$
 * $Id:: EorelDAO.java 3115 2013-04-12 22:25:15Z lukasz                        $
 * Version: $Rev:: 3115                                                        $
 *==============================================================================
 *
 * EorelDao: Access to event-observer relationship data
 *
 *=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.*;
import java.util.*;

import org.hibernate.*;
import org.hibernate.criterion.*;

import edu.ucla.mbi.orm.*;

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.data.dao.*;

public class EorelDAO extends AbstractDAO{
    
    public void addEORel( String event, User observer ){

        Log log = LogFactory.getLog( this.getClass() );
        log.debug( '\n' + "got to addEORel with event = " + event 
                   + " and  observer = " + observer );

        // NOTE: MUST check if old record present as neither observer nor
        //       subject are primary record ID 
        
        EORel oldRelationship = getEORel( event, observer );
        log.debug( "addEORel: oldRelationship=" + oldRelationship );
        
        if( oldRelationship == null ){
            
            
            EORel newRelationship = new EORel( event, observer );
            log.debug( newRelationship );
            super.saveOrUpdate( newRelationship );
        }
        log.debug( "addEORel: DONE" );
    }

    public void dropEORel( String event, User observer ){
        Log log = LogFactory.getLog( this.getClass() );
        log.debug( '\n' + "got to dropSOrel with event = " 
                   + event + " and  observer = " + observer );
        
        // NOTE: MUST check if old record present as neither observer nor
        //       subject are primary record ID
        
        EORel oldRelationship = getEORel( event, observer );
        if( oldRelationship != null ){
            log.debug( oldRelationship );
            super.delete( oldRelationship );
        }
    }
    
    //--------------------------------------------------------------------------

    public EORel getEORel( String event, User observer  ){

        Log log = LogFactory.getLog( this.getClass() );
        log.debug( "getEORel(HQL) event=" + event
                   + " sid=" + observer.getId() );
        
        Session session =
            HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        EORel watchStatus = null;
        
        try {
            Query query =
                session.createQuery( "select eor from EORel as eor where "
                                     + " eor.event = :event " 
                                     + " and eor.observer = :observer" );

            query.setParameter( "event", event );
            query.setParameter( "observer", observer );
            
            watchStatus = (EORel) query.uniqueResult();
            
        } catch ( HibernateException e ) {
            log.info( e );
            handleException( e );
            // log exception ?
            e.printStackTrace();
        } catch ( Exception ex ){
            ex.printStackTrace();

        } finally {
            
            session.close();
        }

        log.debug( "(getEORel)watchStatus=" + watchStatus );
        return watchStatus;
    }

    //--------------------------------------------------------------------------
    
    public List<User> getEORel( String event ){
        
        Log log = LogFactory.getLog( this.getClass() );
        log.debug( "getEORel(HQL) event=" + event );
        
        Session session =
            HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        List<User> olst = null;
        
        try {
            Query query =
                session.createQuery( "select eor.observer " 
                                     + " from EORel as eor where "
                                     + " eor.event = :event " );
            
            query.setParameter( "event", event );
            
            olst = (List<User>) query.list();
            
        } catch ( HibernateException e ) {
            log.info( e );
            handleException( e );
            // log exception ?
            e.printStackTrace();
            
        } finally {
            session.close();
        }
        
        return olst;
    }

}
