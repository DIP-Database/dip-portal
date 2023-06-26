package edu.ucla.mbi.orm;

/*===========================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/trunk/orm-hibernate-comp#$
 * $Id:: AbstractDAO.java 40 2009-05-14 15:06:40Z                           $
 * Version: $Rev:: 40                                                       $
 *===========================================================================
 *
 * AbstractDAO:
 *
 *========================================================================= */

import org.hibernate.*;
import java.util.*;
//import org.compass.core.*;
import java.io.Serializable;

/**
 * A layer supertype that handles the common operations for all
 * Data Access Objects.
 */

public abstract class AbstractDAO {
    
    protected Session session;
    protected Transaction tx;

    public AbstractDAO() {
        // HibernateFactory.buildIfNeeded();
    }
    
    protected void saveOrUpdate( Object obj ) {
        
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = session.beginTransaction();

        try {
            session.saveOrUpdate( obj );
            tx.commit();
        } catch ( HibernateException e ) {
            handleException( e );
        }
    }

    protected void delete( Object obj ) {
    
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = session.beginTransaction();
         
        try {
            session.delete( obj );
            tx.commit();
        } catch ( HibernateException e ) {
            handleException( e );
        } 
    }
    
    protected Object find( Class clazz, Serializable id ) {

        Object obj = null;
        
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = session.beginTransaction();
        
        try {
            obj = session.load( clazz, id );
            tx.commit();
        } catch ( HibernateException e ) {
            handleException( e );
        } 
        return obj;
    }
    
    protected Object find(Class clazz, int id) {

        Object obj = null;
        
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = session.beginTransaction();
        
        try {
            obj = session.load( clazz, id );
            tx.commit();
        } catch ( HibernateException e ) {
            handleException( e );
        }
        return obj;
    }
    
    protected Object find( Class clazz, long id ) {
        
        Object obj = null;
        
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = session.beginTransaction();
        
        try {
            obj = session.load( clazz, id );
            tx.commit();
        } catch ( HibernateException e ) {
            handleException(e);
        }
        return obj;
    }
    
    protected Object find( Class clazz, String id ) {

        Object obj = null;
        
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = session.beginTransaction();
        
	try {
	    obj = session.load(clazz, id);
	    tx.commit();
        } catch (HibernateException e) {
            handleException(e);
        }
        return obj;
    }
    
    protected List findAll( Class clazz ) {
        
        List objects = null;
        
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = session.beginTransaction();
        
        try {
            Query query = session.createQuery( "from " + clazz.getName());
            objects = query.list();
            tx.commit();
        } catch ( HibernateException e ) {
            handleException( e );
        }
        
        return objects;
    }

    protected List findAll( Class clazz, String column ) {
        
        List objects = null;
        
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = session.beginTransaction();
        
        try {
            Query query = session.createQuery( "from " + clazz.getName() +
                                               " order by " + column );
            objects = query.list();
            tx.commit();
        } catch ( HibernateException e ) {
            handleException( e );
        }
        return objects;
    }
    
    protected void handleException( HibernateException e ) {
        HibernateUtil.rollbackTransaction();
        e.printStackTrace();
    }
    
    
    protected void startOperation() throws HibernateException {
        
        session = HibernateUtil.getCurrentSession();
        tx = session.beginTransaction();
    }

    protected void startOperation( Session session, Transaction tx) 
        throws HibernateException {
        
        session = HibernateUtil.getCurrentSession();
        tx = session.beginTransaction();
    }
    /*    
    protected Object[] compassQuery( String query ) throws CompassException {
        CompassSession session = HibernateUtil.getCompassSession();
        CompassTransaction tr = session.beginTransaction();
        CompassHits hits = session.find(query);
        
        CompassDetachedHits dhits = hits.detach();
        Object[] obj = dhits.getDatas();
	
        tr.commit();
        session.close();
        return obj;			
    } 
    */

}
