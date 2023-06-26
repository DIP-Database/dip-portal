package edu.ucla.mbi.service;

/*
  #========================================================================
   # $Id:: RecordView.java 122 2009-06-15 17:37:28Z lukasz                $
   # Version: $Rev:: 122                                                  $
   #=======================================================================
   #
   #  RecordView - configuration of the record 
   #
   #      
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

public class RecordView{
    
    // DIP database access
    //---------------------

    DxfService dxfService;

    public void setDxfService(DxfService service){
	this.dxfService=service;
    }

    //---------------------------------------

    // tabs configuration
    //-------------------

    Map services;

    public void setServiceConfig(Map services){
        this.services=services;
    }

    public Map getServiceConfig(){
	return services;
    }

    //--------------------------------------


    public RecordView(){
	Log log = LogFactory.getLog( RecordView.class );
        log.info("RecordView: initialized");
    }

    //--------------------------------------

    public List<String> buildTagList(String record){
	Log log = LogFactory.getLog(RecordServices.class);
	log.info("RecordTab: building taglist for: "+record);
	
	List<String> res = new ArrayList<String>();

	List recordCfg=(List) services.get(record);
	if(recordCfg!=null){
	    for(ListIterator rci=recordCfg.listIterator();rci.hasNext();){
		Map tabCfg = (Map)rci.next();
		String tt= (String) tabCfg.get("tag");
		res.add(tt);
	    }
	}
	return res;
    }

    
    public List<RecordTab> buildTabState(String record,Map<String,String> vlabel){

	Log log = LogFactory.getLog(RecordServices.class);
	log.info("RecordTab: building tabs for: "+record);

	List recordCfg=(List) services.get(record);
	if(recordCfg==null || recordCfg.size()==0){
	    return null;
	} else {

	    List tabs= new ArrayList();
	    for(ListIterator rci=recordCfg.listIterator();rci.hasNext();){
		int index=rci.nextIndex();
		Map tabCfg = (Map)rci.next();

		log.info("tab: "+tabCfg.get("label")+":"+tabCfg.get("vlabel")+":"+vlabel.get((String)tabCfg.get("vlabel"))+":");

		if(tabCfg.get("vlabel")!=null && 
		   vlabel.get((String)tabCfg.get("vlabel"))!=null){
		    log.info(" vtab: set");
		    RecordTab tab = 
			new RecordTab((String)tabCfg.get("label"),   // tab label
				      vlabel.get((String)tabCfg.get("vlabel")),
				      index==0 ? true : false,    // on/off toggle
				      Boolean.parseBoolean((String)tabCfg.get("active")) // active flag 
				      );
		    tabs.add(tab);
		} else {
		    RecordTab tab = 
			new RecordTab((String)tabCfg.get("label"),   // tab label
				      index==0 ? true : false,    // on/off toggle
				      Boolean.parseBoolean((String)tabCfg.get("active")) // active flag 
				      );
		    tabs.add(tab);
		}
	    }
	    return tabs;
	}
    }
                                                                           
    public List<RecordTab> buildTabState(NodeType instance,
					 Map<String,String> vlabel){
	// extract record type from record iinstance,
	// pass to string version
	// NOTE: pre-wired as node for now
	
	return buildTabState("N",vlabel);
    }
    
    public DxfService getService(String record, int slot){

	Log log = LogFactory.getLog(RecordServices.class);
        log.info("RecordTab: Service for: "+record+" : "+slot);
	
        List recordCfg=(List) services.get(record);
        if(recordCfg!=null && recordCfg.size()>0){
	    Map tab= (Map) recordCfg.get(slot);
	    if(tab!=null){
		return (DxfService) tab.get("service");
	    }
	}
	return null;
    }

    public String getServiceType(String record, int slot){
	Log log = LogFactory.getLog(RecordServices.class);
        log.info("RecordTab: Active state for: "+record+" : "+slot);

        List recordCfg=(List) services.get(record);
        if(recordCfg!=null && recordCfg.size()>0){
            Map tab= (Map) recordCfg.get(slot);
            if(tab!=null){
                return (String) tab.get("service-type");
            }
        }
        return "node";
    }

    public String getTableDefName( String record, int slot ) {
	Log log = LogFactory.getLog(RecordServices.class);
        log.info("RecordTab: Active state for: "+record+" : "+slot);
        
        List recordCfg=(List) services.get(record);
        if(recordCfg!=null && recordCfg.size()>0){
            Map tab= (Map) recordCfg.get(slot);
            if(tab!=null){
                return (String) tab.get("table-def");
            }
        }
        return "NodeList";
    }

    public boolean isActive(String record, int slot){
	Log log = LogFactory.getLog(RecordServices.class);
        log.info("RecordTab: Active state for: "+record+" : "+slot);

        List recordCfg=(List) services.get(record);
        if(recordCfg!=null && recordCfg.size()>0){
            Map tab= (Map) recordCfg.get(slot);
            if(tab!=null){
                return Boolean.parseBoolean((String)tab.get("active"));
            }
        }
        return true;
    }
    
    public boolean isCacheOn(String record, int slot){
	Log log = LogFactory.getLog(RecordServices.class);
        log.info("RecordTab: Active state for: "+record+" : "+slot);
	
        List recordCfg=(List) services.get(record);
        if(recordCfg!=null && recordCfg.size()>0){
            Map tab= (Map) recordCfg.get(slot);
            if(tab!=null){
                return Boolean.parseBoolean((String)tab.get("cacheOn"));
            }
        }
        return false;
    }
    
    public String getTag(String record, int slot){

	Log log = LogFactory.getLog(RecordServices.class);
        log.info("RecordTab: Tag for: "+record+" : "+slot);

        List recordCfg=(List) services.get(record);
        if(recordCfg!=null && recordCfg.size()>0){
            Map tab= (Map) recordCfg.get(slot);
            if(tab!=null){
                return (String) tab.get("tag");
            }
        }
        return null;
    }
} 
