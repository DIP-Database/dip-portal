package edu.ucla.mbi.dip.struts.action;

/* ========================================================================
 # $Id:: QuerySupport.java 3221 2013-06-10 21:31:20Z lukasz               $
 # Version: $Rev:: 3221                                                   $
 #=========================================================================
 #                                                                        $
 # GetJqRecord action - returns DIP records                               $
 #                                                                        $
 #     TO DO:                                                             $
 #         - agent setup                                                  $
 #======================================================================= */

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.ServletContext;

import edu.ucla.mbi.portal.struts.action.*;

public abstract class QuerySupport extends TableViewSupport 
    implements QueryAware  {
    
    public abstract String execute() throws Exception;
    
    //---------------------------------------------------------------------
    // QueryAware interface implementation
    //------------------------------------
    
    private String query;
    
    public void setQuery( String query ) {
        
        if( query != null ){
            query = query.replaceAll("\\s+"," ");
            query = query.replaceAll("^\\s+","");
            query = query.replaceAll("\\s+$","");
        }
        this.query = query;
        
    }
    
    public String getQuery() {
        return query;
    }
    
    String queryType;
    
    public void setQt( String queryType ) {
        this.queryType = queryType;
    }
    
    public String getQt() {
        return queryType;
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
