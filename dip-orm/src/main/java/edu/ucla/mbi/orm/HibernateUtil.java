package edu.ucla.mbi.orm;

/*=============================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/trunk/orm-hibernate-comp#  $
 * $Id:: HibernateUtil.java 40 2009-05-14 15:06:40Z                           $
 * Version: $Rev:: 40                                                         $
 *=============================================================================
 *
 * HibernateUtil:
 *  - based on Hibernate in Action exampl
 *  - compass integration of non-jndi based sessionfactories  
 *=========================================================================== */

import org.apache.commons.logging.*;

import org.hibernate.*;
import org.hibernate.cfg.*;

import javax.naming.*;

import org.compass.core.*;
import org.compass.core.config.CompassConfiguration;
import org.compass.core.config.CompassConfigurationFactory;

import org.compass.gps.*;
import org.compass.gps.impl.*;
import org.compass.gps.device.*;
import org.compass.gps.device.hibernate.*;
import org.compass.gps.device.hibernate.dep.*;

public class HibernateUtil {
    
    private static final String 
        INTERCEPTOR_CLASS = "hibernate.util.interceptor_class";
    private static Configuration configuration;
    private static SessionFactory sessionFactory;

    private static Compass compass; 

    static {
        try {
            configuration = new Configuration();            
            configuration.configure();

            // Assign a global, user-defined interceptor with no-arg constructor
            String 
                interceptorName = configuration.getProperty(INTERCEPTOR_CLASS);

            if( interceptorName != null ){
                Class interceptorClass = HibernateUtil.class.getClassLoader()
                    .loadClass(interceptorName);
                Interceptor 
                    interceptor = (Interceptor) interceptorClass.newInstance();
                configuration.setInterceptor(interceptor);
            }
            
            if( configuration
                .getProperty( Environment.SESSION_FACTORY_NAME) != null ){
                // Let Hibernate bind it to JNDI
                configuration.buildSessionFactory();
            } else {
                // or use static variable handling
                sessionFactory = configuration.buildSessionFactory();
		compassInit();
            }
        } catch (Throwable ex) {
            // We have to catch Throwable, otherwise we will miss
            // NoClassDefFoundError and other subclasses of Error
	    Log log = LogFactory.getLog(HibernateUtil.class);
            log.error("Building SessionFactory failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Returns the original Hibernate configuration.
     *
     * @return Configuration
     */

    public static Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Returns the global SessionFactory.
     *
     * @return SessionFactory
     */

    public static SessionFactory getSessionFactory() {
        SessionFactory sf = null;
        String sfName = 
            configuration.getProperty( Environment.SESSION_FACTORY_NAME);

        if (sfName != null) {
	    Log log = LogFactory.getLog(HibernateUtil.class);
            log.debug("Looking up SessionFactory in JNDI.");

            try {
                sf = (SessionFactory) new InitialContext().lookup(sfName);
            } catch (NamingException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            sf = sessionFactory;
        }

        if (sf == null) {
            throw new IllegalStateException("SessionFactory not available.");
        }

        return sf;
    }
    
    /**
     * Closes the current SessionFactory and releases all resources.
     * <p>
     * The only other method that can be called on HibernateUtil
     * after this one is rebuildSessionFactory(Configuration).
     */

    public static void shutdown() {
	Log log = LogFactory.getLog(HibernateUtil.class);
        log.debug("Shutting down Hibernate.");
        // Close caches and connection pools
        getSessionFactory().close();
        
        // Clear static variables
        configuration = null;
        sessionFactory = null;

    }

    /**
     * Rebuild the SessionFactory with the static Configuration.
     * <p>
     * This method also closes the old SessionFactory before, if still open.
     * Note that this method should only be used with static SessionFactory
     * management, not with JNDI or any other external registry.
     */
    public static void rebuildSessionFactory() {
	Log log = LogFactory.getLog(HibernateUtil.class);
        log.debug("Using current Configuration for rebuild.");
        rebuildSessionFactory(configuration);
    }

    /**
     * Rebuild the SessionFactory with the given Hibernate Configuration.
     * <p>
     * HibernateUtil does not configure() the given Configuration object,
     * it directly calls buildSessionFactory(). This method also closes
     * the old SessionFactory before, if still open.
     *
     * @param cfg
     */
    public static void rebuildSessionFactory(Configuration cfg) {
	Log log = LogFactory.getLog(HibernateUtil.class);
        log.debug("Rebuilding the SessionFactory from given Configuration.");

        synchronized (sessionFactory) {
            if ((sessionFactory != null) && !sessionFactory.isClosed()) {
                sessionFactory.close();
            }

            if (cfg.getProperty(Environment.SESSION_FACTORY_NAME) != null) {
                cfg.buildSessionFactory();
            } else {
                sessionFactory = cfg.buildSessionFactory();
		compassInit();
            }

            configuration = cfg;
        }
    }

    /**
     * Retrieves the current Session local to the thread.
     * <p/>
     * If no Session is open, opens a new Session for the running thread.
     * If CMT is used, returns the Session bound to the current JTA
     * container transaction. Most other operations on this class will
     * then be no-ops or not supported, the container handles Session
     * and Transaction boundaries, ThreadLocals are not used.
     *
     * @return Session
     */
    public static Session getCurrentSession() {

        Log log = LogFactory.getLog(HibernateUtil.class);

        Session s = getSessionFactory().getCurrentSession();
        //log.info("session(factory)=" + s );
        return s;
    }

    /**
     * Closes the Session local to the thread.
     * <p>
     * Is a no-op (with warning) if called in a CMT environment. Should be
     * used in non-managed environments with resource local transactions, or
     * with EJBs and bean-managed transactions.
     */
    
    public static void closeSession() {
    }

    /**
     * Start a new database transaction.
     * <p>
     * Is a no-op (with warning) if called in a CMT environment. Should be
     * used in non-managed environments with resource local transactions, or
     * with EJBs and bean-managed transactions. In both cases, it will either
     * start a new transaction or join the existing ThreadLocal or JTA
     * transaction.
     */
    
    public static void beginTransaction() {
    }

    /**
     * Commit the database transaction.
     * <p>
     * Is a no-op (with warning) if called in a CMT environment. Should be
     * used in non-managed environments with resource local transactions, or
     * with EJBs and bean-managed transactions. It will commit the
     * ThreadLocal or BMT/JTA transaction.
     */

    public static void commitTransaction() {
    }

    /**
     * Rollback the database transaction.
     * <p>
     * Is a no-op (with warning) if called in a CMT environment. Should be
     * used in non-managed environments with resource local transactions, or
     * with EJBs and bean-managed transactions. It will rollback the
     * resource local or BMT/JTA transaction.
     */
    public static void rollbackTransaction() {
        getSessionFactory().getCurrentSession().getTransaction().rollback();
    }

    /**
     * Reconnects a Hibernate Session to the current Thread.
     * <p>
     * Unsupported in a CMT environment.
     *
     * @param session The Hibernate Session to be reconnected.
     */
    public static void reconnect(Session session) {
    }

    /**
     * Disconnect and return Session from current Thread.
     *
     * @return Session the disconnected Session
     */
    public static Session disconnectSession() {
        return null;
    }
    
    /**
     * Register a Hibernate interceptor with the current SessionFactory.
     * <p>
     * Every Session opened is opened with this interceptor after
     * registration. Has no effect if the current Session of the
     * thread is already open, effective on next close()/getCurrentSession().
     * <p>
     * Attention: This method effectively restarts Hibernate. If you
     * need an interceptor active on static startup of HibernateUtil, set
     * the <tt>hibernateutil.interceptor</tt> system property to its
     * fully qualified class name.
     */
    
    public static void registerInterceptorAndRebuild( Interceptor interceptor ){
	Log log = LogFactory.getLog(HibernateUtil.class);
        log.debug("Setting new global Hibernate interceptor and restarting.");
        configuration.setInterceptor(interceptor);
        rebuildSessionFactory();
    }

    public static Interceptor getInterceptor() {
        return configuration.getInterceptor();
    }
    
    private static void compassInit(){
	
	Log log = LogFactory.getLog(HibernateUtil.class);
	log.info("HibernateUtil: initializing Compass GPS");
	
	if(sessionFactory==null){
	    rebuildSessionFactory();
	}
	
	if(sessionFactory!=null && 1==0){
	    // initialize compass
	    //-------------------
	    
	    CompassConfiguration cconf =
		CompassConfigurationFactory.newConfiguration();
	    
	    //cconf.setClassLoader(loader);
	    cconf.configure(); 
            //config.getProperty("compass-cfg","configure/compass.cfg.xml"));
	    compass = cconf.buildCompass();
            
	    // initialize compassGps
	    //----------------------

	    SingleCompassGps gps = new SingleCompassGps(compass);
	    CompassGpsDevice hibernateDevice
		= new Hibernate3GpsDevice("hibernate", sessionFactory);
	    
	    gps.addGpsDevice(hibernateDevice);
	    gps.start();
	    log.info("HibernateUtil: ..indexing");   
	    gps.index();
	}
	log.info("HibernateUtil: Compass GPS ready");
    }
    
    public static CompassSession getCompassSession(){
	if( compass == null ){
	    compassInit();
	} 
	return compass.openSession();
    }
}
