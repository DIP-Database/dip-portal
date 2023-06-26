package edu.ucla.mbi.portal.struts.action;

/* =============================================================================
 * $Id:: ProxyAction.java 2877 2012-12-18 20:42:36Z lukasz                     $
 * Version: $Rev:: 2877                                                        $
 *==============================================================================
 *                                                                             $
 * RecordAction action - returns database records                              $
 *                                                                             $
 *     TO DO:                                                                  $
 *         - agent setup                                                       $
 *=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

import javax.xml.bind.*;

import edu.ucla.mbi.dxf14.*;
import edu.ucla.mbi.service.*;
import edu.ucla.mbi.portal.*;
import edu.ucla.mbi.portal.agent.*;

import edu.ucla.mbi.util.struts.interceptor.*;

public class ProxyAction extends RecordSupport 
    implements AccessionAware   {
    
    public String execute() throws Exception {

        Log log = LogFactory.getLog( this.getClass() );
        log.debug( " MenuContext: " + super.getMenuContext() );
        
        initialize();

        //----------------------------------------------------------------------
        // dispatch 
        //---------

        return dispatch();
    }
    
    //--------------------------------------------------------------------------
    // dispatcher
    //-----------

    public String dispatch() throws Exception {

        if ( getRet() == null || getRet().equals( "view" ) ) {
            
            buildData();
            return SUCCESS;

        } else if( getRet().equals( "data" ) ) {
            
            // prepare and return data (page)
            //-------------------------------

            return buildData();

        }
        return SUCCESS;
    }

    //---------------------------------------------------------------------------
    
    String dxfQuery = null;
    
    //--------------------------------------------------------------------------
    // build data structures
    //----------------------
    
    public String getCounts() throws Exception {
        return "json";
    }

    public String buildKnownData() throws Exception {
        return "json";
    }
    
    public String buildData() throws Exception {
        
        Log log = LogFactory.getLog( this.getClass() );
        log.debug( "ProxyAction: building data" );
        
        // preprocess ns/ac/md/sl 
        //-----------------------

        if( getAc() == null || getAc().length() == 0 ) {
            return "json";
        }
        
        String dxfQuery = getAc();
        String dxfPrefix = getNs();
        

        log.debug( " cookies: " + getCookiesMap() );

        log.debug( " GetRecord: database=" + getDb() );

        log.debug( " GetRecord: namespace=" + getNs() );
        log.debug( " GetRecord: accession=" + getAc() );

        log.debug( " GetRecord: format=" + frm );
        log.debug( " GetRecord: detail=" + dtl );


        log.debug( " GetRecord: actionContext=" + getContext().getConfig() );
 
        if( dxfQuery != null ) {
     
            Map rcache = null; // record cache
            
            if ( getContext().isCacheOn() ) {
                synchronized( getSession() ) {
                    rcache = (Map) getSession().get("record-cache");
                    if( rcache == null ) {
                        getSession().put( "record-cache", 
                                          new ConcurrentHashMap() );
                        rcache = (Map) getSession().get( "record-cache" );
                    }
                }
                
                log.debug( "cache query for: " + getDb() + "_" + getNs() + "_" + getAc() + "_" + getDl() );
                setSummary( (NodeType) rcache.get( getDb() + "_" + getNs() 
                                                   + "_" + getAc() + "_" + getDl() ) ); 
             
                log.debug( " cache hit: " + getSummary() );
            }
     
            if ( getSummary() == null ) {
                try {
      
                    Map prxmap = getContext().getConfig();

                    if( prxmap.get( getDb() ) != null ){
                        DxfService service =  (DxfService) prxmap.get( getDb() );
                        setSummary( service.getDxfNode( getNs(), getAc(), getDl() ) );
                    }
                    
                } catch ( Exception ex ) {
                    ex.printStackTrace();
                    return ERROR;
                }
            } else {
                //dxfPrefix = getSummary().getNs();
            }
            
            if( getSummary() != null && getContext().isCacheOn() ) {
                rcache.put( getDb() + "_" + getNs() + "_" + getAc() + "_" + getDl(), 
                            getSummary() );
                
                cacheSize = rcache.size();
            }
            
            Map metarow = new HashMap();
            metarow.put( "meta", "meta" );
            metarow.put( "dip", "dip" );
            metarow.put( "first", getFirst() );
            metarow.put( "max", getMax() );
            metarow.put( "ac", getAc() );
            metarow.put( "tp", getSl() );
            metarow.put( "detail", getDl() );
  
            setTableMeta( metarow );
            log.debug(" table meta data created");
  
            // build item list
            //----------------
  
            if ( getDetail() != null && getDetail().size() > 0 ) {
                setTableData( getDetail() );
            }

            if ( getRet() != null && getRet().equals( "data" ) ){  
                //setModelData();
            }
        }
        log.debug( "items.size=" + getTableData().size() );
        return "json";
    }
    
    
    //--------------------------------------------------------------------------
    // configuration
    //--------------
    
    boolean configFlag;
    
    public String getConf() {
        return String.valueOf( configFlag );
    }

    public void setConf( String conf ) {
        this.configFlag = conf.equalsIgnoreCase( "true" );
    }

    //--------------------------------------------------------------------------
    // parameters
    //-----------

    String db;
    
    public String getDb() {
        return db;
    }
    public void setDb( String db ) {
        this.db = db;
    }
    
    //--------------------------------------------------------------------------

    String dtl;
    
    public String getDtl() {
        return dtl;
    }

    public void setDtl( String dtl ) {
        this.dtl = dtl;
    }
    
    //--------------------------------------------------------------------------

    String frm;
    
    public String getFrm() {
        return frm;
    }

    public void setFrm( String frm ) {
        this.frm = frm;
    }
    
    //--------------------------------------------------------------------------
    //debugging properties
    //-------------------- 

    boolean isJsonOn;

    public void setJsonOn( boolean json ) {
        if ( json ) {
            setRetType( "json" );
            this.isJsonOn = true;
        }
    }
    
    //--------------------------------------------------------------------------
    
    String resultStr;
    
    public String getResultStr() {
        return resultStr;
    }
    public void setResultStr(String resultStr) {
        this.resultStr=resultStr;
    }
    
    //--------------------------------------------------------------------------

    String paneStr;

    public String getPaneStr() {
        return paneStr;
    }
    
    public void setPaneStr(String paneStr) {
        this.paneStr=paneStr;
    }
    
    //--------------------------------------------------------------------------
    
    boolean debugOn;

    public  boolean isDebugOn() {
        return debugOn;
    }

    public  boolean getDebug() {
        return debugOn;
    }
    
    public void setDebug( boolean debug ) {
        this.debugOn=debug;
    }

    //--------------------------------------------------------------------------

    int cacheSize;
    
    public void setCacheSize(int size) {
        this.cacheSize=size;
    }

    public  int getCacheSize() {
        return cacheSize;
    }
}
