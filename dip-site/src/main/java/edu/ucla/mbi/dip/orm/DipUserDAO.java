package edu.ucla.mbi.dip.orm;

/*===========================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-sit#$
 * $Id:: DipUserDAO.java 2877 2012-12-18 20:42:36Z lukasz                   $
 * Version: $Rev:: 2877                                                     $
 *===========================================================================
 *
 * DipUserDAO:
 *
 *========================================================================= */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.*;
import edu.ucla.mbi.orm.*;
import edu.ucla.mbi.dip.*;

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.data.dao.*;

import java.net.*;
import java.util.*;

public class DipUserDAO extends AbstractDAO implements UserDao {

    public User getUser( int id ) { 
        
        User user = null;
        
        try {
            user =  (DipUser) super.find( DipUser.class, id );
        } catch ( DAOException dex ) {
            // log exception ?
        }
        return user;
    }

    
    //---------------------------------------------------------------------
    
    public User getUser( String login ) {
        
        User user = null;
        try {
            startOperation();
            Query query =
                session.createQuery( "from DipUser u where " +
                                     " u.login = :login ");
            query.setParameter( "login", login );
            query.setFirstResult( 0 );
            user = (DipUser) query.uniqueResult();
            tx.commit();
            
        } catch( DAOException dex ) {
            // log error ?
        }
        return user;
    }


    //---------------------------------------------------------------------
    
    public List<User> getUserList() {   
        return null;
    }
    
    public List<User> getUserList( int firstRecord, int blockSize ) { 
       
        List<User> userl = null;

        try {
            startOperation();
            
            Query query = 
                session.createQuery( "from DipUser u order by id ");
            query.setFirstResult( firstRecord );
            query.setMaxResults( blockSize );
            
            userl = (List<User>) query.list();
            tx.commit();
            
        } catch( DAOException dex ) {
            // log error ?
        }
            
        return userl;
    }

    
    //---------------------------------------------------------------------

    public long getUserCount(){ 

        long count = 0;

        Log log = LogFactory.getLog( this.getClass() );
        
        try {
            startOperation();

            Query query =
                session.createQuery( "select count(*) from DipUser");
            
            count = (Long) query.uniqueResult();
            
            log.info("Total users=" + count);

        } catch( DAOException dex ) {
            // log error ?
            log.info(dex);
        }

        return count;
    }

    public void saveUser( User user ) { 
        super.saveOrUpdate( new DipUser ( user ) );
    }
    
    public void updateUser( User user ) { 
        super.saveOrUpdate( user );
    }


    public void deleteUser( User user ) { 
        super.delete( user );
    }
}
