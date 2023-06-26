package edu.ucla.mbi.dip.orm;

import org.hibernate.*;

import edu.ucla.mbi.orm.*;
import edu.ucla.mbi.dip.*;

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.data.dao.*;

import java.net.*;
import java.util.*;

public class DipGroupDAO extends AbstractDAO implements GroupDao {
    
    public Group getGroup( int id ) { 
        
        Group group = null;

        try {
            group =  (Group) super.find( Group.class, id );
        } catch ( DAOException dex ) {
            // log exception ?
        }
        return group; 
    }


    //---------------------------------------------------------------------

    public Group getGroup( String label ) {

        Group group = null;
        try {
            startOperation();
            Query query =
                session.createQuery( "from Group g where " +
                                     " g.label = :label ");
            query.setParameter( "label", label );
            query.setFirstResult( 0 );
            group = (Group) query.uniqueResult();
            tx.commit();

        } catch( DAOException dex ) {
            // log error ?
        }
        return group;
    }


    
    //---------------------------------------------------------------------

    public List<Group> getGroupList() {
     
        List<Group> glst = null;
        
        try {
            glst = (List<Group>) super.findAll( Group.class );
        } catch ( DAOException dex ) {
            // log exception ?
        }
        return glst;
    }

    //---------------------------------------------------------------------

    public List<Group> getGroupList( int firstRecord, int blockSize ){

        List<Group> rlst = null;

        try {
            startOperation();
            Query query =
                session.createQuery( "from Group g order by id ");
            query.setFirstResult( firstRecord );
            query.setMaxResults( blockSize );
            
            rlst = (List<Group>) query.list();
            tx.commit();
            
        } catch ( DAOException dex ) {
            // log exception ?
        }
        return rlst;
    }
   
    //---------------------------------------------------------------------

    public long getGroupCount(){

        long cnt = 0;
        
        try {
            startOperation();
            Query query =
                session.createQuery( "select count(r) from DipGroup r");
            
            query.setFirstResult( 0 );
            cnt = (Long) query.uniqueResult();
            tx.commit();
            
        } catch( DAOException dex ) {
            // log error ?
        }
        
        return cnt;        
    }

    //---------------------------------------------------------------------

    public void saveGroup( Group group ) { 
        try {
            super.saveOrUpdate( group );
        } catch ( DAOException dex ) {
            // log exception ?
        }
    }
    

    //---------------------------------------------------------------------
    
    public void updateGroup( Group group ) { 
        try {
            super.saveOrUpdate( group );
        } catch ( DAOException dex ) {
            // log exception ?
        }
    }
    
    
    //---------------------------------------------------------------------

    public void deleteGroup( Group group ) { 
        try {
            super.delete( group );
        } catch ( DAOException dex ) {
            // log exception ?
        }
    }

    
    //---------------------------------------------------------------------
    // usage/count: Users
    //-------------------

    public long getUserCount( Group group ) {

        long cnt = 0;
        
        try {
            startOperation();
            Query query =
                session.createQuery( "select count(u) from DipUser u" +
                                     " join u.groups as grp" +
                                     " where grp.id = :gid");
            query.setParameter( "gid", group.getId() );
            query.setFirstResult( 0 );
            cnt = (Long) query.uniqueResult();
            tx.commit();

        } catch( DAOException dex ) {
            // log error ?
        }
        
        return cnt;
    }
    
    //---------------------------------------------------------------------

    public List<User> getUserList( Group group ) {

        // NOTE: use sparingly - potentially can load all users
        
        List<User> ulst = null;

        try {
            startOperation();
            Query query =
                session.createQuery( "select u from DipUser u" +
                                     " join u.groups as grp" +
                                     " where grp.id = :gid");
            query.setParameter( "gid", group.getId() );
            query.setFirstResult( 0 );
            ulst = (List<User>) query.list();
            tx.commit();

        } catch( DAOException dex ) {
            // log error ?
        }
        
        return  ulst;
    }
}
