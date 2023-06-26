package edu.ucla.mbi.service;

/*
  #========================================================================
   # $Id:: RecordModel.java 2877 2012-12-18 20:42:36Z lukasz              $
   # Version: $Rev:: 2877                                                 $
   #=======================================================================
   #
   #  RecordModel - record model configuration
   #
   #     TO DO:     
   #
   #
   #=======================================================================
*/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.io.*;

import edu.ucla.mbi.dxf14.*;
import edu.ucla.mbi.service.*;

public class RecordModel {

    //---------------------------------------------------------------------
    // database access
    //----------------

    DxfService dxfService;

    public void setDxfService( DxfService service ) {
	this.dxfService=service;
    }
    
    public DxfService getDxfService(){
        return dxfService;
    }
    
    //---------------------------------------------------------------------
    // model configuration
    //--------------------

    Map model;

    public void setModelConfig( Map model ) {
        this.model = model;
    }

    public Map getModelConfig(){
	return model;
    }

    //---------------------------------------------------------------------
    // constructor/initialization
    //----------------------------

    public RecordModel(){
	Log log = LogFactory.getLog( RecordModel.class );
        log.info( "RecordModel: constructed" );
    }

    public void initialize(){
	Log log = LogFactory.getLog( RecordModel.class );
        log.info( "RecordModel: initialized" );
    }


    //---------------------------------------------------------------------
    // model access
    //-------------

    public List<String> buildTagList( String record ) {

	Log log = LogFactory.getLog(RecordServices.class);
	//log.info("RecordTab: building taglist for: " + record );
	
	List<String> res = new ArrayList<String>();

	List recordCfg = (List) model.get( record );
	if( recordCfg != null ) {
	    for( ListIterator rci = recordCfg.listIterator(); rci.hasNext(); ) {
		Map tabCfg = (Map)rci.next();
		String tt= (String) tabCfg.get("service-tag");
		res.add(tt);
	    }
	}
	return res;
    }

    public List<String> getModelLabelList( String record ) {

	Log log = LogFactory.getLog( RecordModel.class);
	//log.info("RecordModel: building model list: " + record );
	
	List<String> res = new ArrayList<String>();

	List recordCfg = (List) model.get( record );
	if( recordCfg != null ) {
	    for( ListIterator rci = recordCfg.listIterator(); rci.hasNext(); ) {
		Map tabCfg = (Map)rci.next();
		String tt= (String) tabCfg.get( "label" );
		res.add(tt);
	    }
	}
	return res;
    }

    public List<String> getModelTypeList( String record ) {

	Log log = LogFactory.getLog( RecordModel.class);
	//log.info("RecordModel: building model list: " + record );
	
	List<String> res = new ArrayList<String>();

	List recordCfg = (List) model.get( record );
	if( recordCfg != null ) {
	    for( ListIterator rci = recordCfg.listIterator(); rci.hasNext(); ) {
		Map tabCfg = (Map)rci.next();
		String tt= (String) tabCfg.get( "view-type" );
		res.add(tt);
	    }
	}
	return res;
    }

    public List<Map<String,String>> getModelList( String record ) {

	Log log = LogFactory.getLog( RecordModel.class);
	//log.info("RecordModel: building model list: " + record );
	
	List<Map<String,String>> res = new ArrayList<Map<String,String>>();

	List recordCfg = (List) model.get( record );
	if( recordCfg != null ) {
	    for( ListIterator rci = recordCfg.listIterator(); rci.hasNext(); ) {
		Map tabCfg = (Map)rci.next();
		String ll= (String) tabCfg.get( "label" );
		String pp= (String) tabCfg.get( "panel" );
		String ee= (String) tabCfg.get( "export" );


                Map<String,String> tt= new HashMap<String,String>();
                tt.put("label",ll);
                tt.put("panel",pp);
                tt.put("exprt",ee);
                tt.put( "active", (String) tabCfg.get( "active" ) );
                tt.put( "view-type", (String) tabCfg.get( "view-type" ) );
                tt.put( "view-def", (String) tabCfg.get( "view-def" ) );
                tt.put( "view-as-tile", (String) tabCfg.get( "view-as-tile" ) );
		res.add(tt);
	    }
	}
	return res;
    }

    public String getModelViewType( String record, int slot ) {

        if ( record == null ) return null;
        
        List recordCfg = (List) model.get( record );
        if ( recordCfg == null ) return null;
        
        Map slotCfg = (Map) recordCfg.get( slot );
        if ( slotCfg == null ) return null;
        
        return (String) slotCfg.get( "view-type" );
    }

    public String getModelViewName( String record, int slot ) {

        if ( record == null ) return null;
	List recordCfg = (List) model.get( record );
        if ( recordCfg == null ) return null;
        
        Map slotCfg = (Map) recordCfg.get( slot );
	if ( slotCfg == null ) return null;
        
        return (String) slotCfg.get( "view-def" );
    }

    //----------------------------

    public List<RecordTab> buildTabState( String record,
					  Map<String,String> vlabel ) {
	
	Log log = LogFactory.getLog( RecordModel.class);
	//log.info("RecordTab: building tabs for: "+record);

	List recordCfg = (List) model.get( record );
	if( recordCfg == null || recordCfg.size() == 0 ) {
	    return null;
	} else {

	    List tabs = new ArrayList();
	    for( ListIterator rci = recordCfg.listIterator(); 
		 rci.hasNext(); ) {
		int index = rci.nextIndex();
		Map tabCfg = (Map) rci.next();
		
		//log.info("tab: " + tabCfg.get("label") + ":" + 
		//	 tabCfg.get("vlabel") + ":" + 
		//	 vlabel.get( (String) tabCfg.get( "vlabel" ) ) + ":" );
		
		if( tabCfg.get( "vlabel" ) != null && 
		    vlabel.get( (String) tabCfg.get( "vlabel" ) ) != null ) {
		    //log.info( " vtab: set" );
		    RecordTab tab = 
			new RecordTab( (String) tabCfg.get( "label" ),   // tab label
				       vlabel.get( (String) tabCfg.get( "vlabel" ) ),
				       index==0 ? true : false,    // on/off toggle
				       Boolean.parseBoolean( (String) tabCfg.get( "active" ) ) // active flag 
				       );
		    tabs.add( tab );
		} else {
		    RecordTab tab = 
			new RecordTab( (String) tabCfg.get( "label" ),   // tab label
				       index == 0 ? true : false,    // on/off toggle
				      Boolean.parseBoolean( (String) tabCfg.get( "active" ) ) // active flag 
				       );
		    tabs.add( tab );
		}
	    }
	    return tabs;
	}
    }
    
    public List<RecordTab> buildTabState( NodeType instance,
					  Map<String,String> vlabel ) {
	// extract record type from record iinstance,
	// pass to string version
	// NOTE: pre-wired as node for now
	
	return buildTabState( "N", vlabel );
    }
    
    public DxfService getService( String record, int slot ) {
	
	Log log = LogFactory.getLog( RecordModel.class );
        //log.info( "RecordModel: Service for: " + record + "#" + slot );
	
        List recordCfg = (List) model.get( record );
        if( recordCfg !=null && recordCfg.size() > 0 ) {
	    Map tab= (Map) recordCfg.get( slot );
	    if(tab!=null){
		return (DxfService) tab.get( "service" );
	    }
	}
	return null;
    }
    
    public String getServiceType( String record, int slot ){
	Log log = LogFactory.getLog( RecordModel.class);
        //log.info( "RecordModel: Service type: " + record + "#" + slot );

        List recordCfg = (List) model.get( record );
        if( recordCfg != null && recordCfg.size() > 0 ) {
            Map tab = (Map) recordCfg.get(slot);
            if( tab !=null ) { 
                return (String) tab.get( "service-type" );
            }
        }
        return "node";
    }

    public String getServiceTag( String record, int slot ) {
	
	Log log = LogFactory.getLog( RecordModel.class);
        //log.info("RecordModel: Tag for: " + record + "#" + slot );
	
        List recordCfg = (List) model.get( record );
        if( recordCfg != null && recordCfg.size() > 0 ) {
            Map tab = (Map) recordCfg.get( slot );
            if( tab != null ) {
                return (String) tab.get( "service-tag" );
            }
        }
        return null;
    }



    public String getViewName( String record, int slot ) {
	Log log = LogFactory.getLog( RecordModel.class);
        //log.info( "RecordModel: View name: " + record + "#" + slot );
        
        List recordCfg = (List) model.get( record );
        if( recordCfg != null && recordCfg.size() > 0 ) {
            Map tab = (Map) recordCfg.get( slot );
            if( tab != null ) {
                return (String) tab.get( "view-def" );
            }
        }
        return "node-list";
    }

    public boolean isActive( String record, int slot ) {
	Log log = LogFactory.getLog( RecordModel.class );
        //log.info( "RecordModel: Active flag for: " + record+"#" + slot );

        List recordCfg = (List) model.get( record );
        if( recordCfg != null && recordCfg.size() > 0 ) {
            Map tab= (Map) recordCfg.get( slot );
            if( tab != null ) {
                return Boolean.parseBoolean( (String) tab.get( "active" ) );
            }
        }
        return true;
    }
    
    // not sure what this one's for ?

    public boolean isCacheOn( String record, int slot ) {
	Log log = LogFactory.getLog( RecordModel.class );
        //log.info( "RecordModel: Cache flag for: " + record + " : " + slot );
	
        List recordCfg = (List) model.get( record );
        if( recordCfg != null && recordCfg.size() > 0 ) {
            Map tab= (Map) recordCfg.get( slot );
            if( tab != null ) {
                return Boolean.parseBoolean( (String) tab.get( "cacheOn" ) );
            }
        }
        return false;
    }
    
} 
