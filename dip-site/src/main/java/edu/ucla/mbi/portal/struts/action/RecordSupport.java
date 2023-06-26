package edu.ucla.mbi.portal.struts.action;

/* =============================================================================
 # $Id:: RecordSupport.java 2887 2013-01-02 16:53:44Z lukasz                   $
 # Version: $Rev:: 2887                                                        $
 #==============================================================================
 #                                                                             $
 # RecordSupport action - returns database records                             $
 #                                                                             $
 #     TO DO:                                                                  $
 #         - agent setup                                                       $
 #=========================================================================== */

import java.util.List;
                                                                           
import edu.ucla.mbi.util.struts.action.*;
import edu.ucla.mbi.util.struts.interceptor.*;

public abstract class RecordSupport extends TableViewSupport 
    implements RecordAware {

    //--------------------------------------------------------------------------
    // RecordAware interface implementation
    //--------------------------------------
    
    private String database;
    
    public String getDb() {
        return database;
    }
    
    public void setDb( String database ) {
        this.database = database;
    }
    
    //--------------------------------------------------------------------------
    
    private String namespace;
    
    public String getNs() {
        return namespace;
    }
    
    public void setNs( String namespace ) {
        this.namespace = namespace;
    }
    
    //--------------------------------------------------------------------------

    private String accession;
    
    public String getAc(){
        return accession;
    }
    
    public void setAc( String accession ){
        this.accession = accession;
    }
    
    //--------------------------------------------------------------------------
    
    private String detailLevel;
    
    public String getDl(){
        return detailLevel;
    }
    
    public void setDl( String level ){
        this.detailLevel = level;
    }  

    //---------------------------------------------------------------------
    // FacetAware interface implementation
    //------------------------------------

    List facetData = null;

    public List getFacetData(){
        return facetData;
    }

    public void setFacetData( List data ){
        facetData = data;
    }

}
