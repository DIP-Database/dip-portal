package edu.ucla.mbi.dip.agent;

/*
   #================================================================
   # $Id:: DipAgent.java 2877 2012-12-18 20:42:36Z lukasz          $
   # Version: $Rev:: 2877                                          $
   #================================================================
   #
   # DipAgent - retrieves asynchroneously data from DIP database
   #
   #================================================================
*/

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import edu.ucla.mbi.service.*;
import edu.ucla.mbi.dxf14.*;

public class DipAgent 
    implements Runnable{

    private static final int AGCMAX=2;

    private DxfService dbservice;  // same as dsm.get("dip")
    private Map<String,DxfService> dsm;
    private DipAgentConfig dac;
    
    private Map  session;
    private NodeType record;
    private Log log;
    

    // inject dip database service
    //----------------------------

    public void setDxfService(DxfService service){
	this.dbservice=service;
    }

    // inject session map
    //-------------------

    public void setSession(Map session){
	this.session=session;
    }

    public DipAgent(NodeType record,
		    Map session,
		    DxfService dbservice){

	this.record=record;
	this.session=session;
	this.dbservice=dbservice;

	log = LogFactory.getLog(DipAgent.class);

    }

    public DipAgent(NodeType record,
		    Map session,
		    Map<String,DxfService> dsm){
	
	this.record=record;
	this.session=session;
	this.dsm=dsm;
	
	if(dsm.get("dipDbService")!=null){
	    this.dbservice=dsm.get("dipDbService");
	}
	
	log = LogFactory.getLog(DipAgent.class);

    }

    public DipAgent(DipAgentConfig config,
		    NodeType record,
		    Map session,
		    Map<String,DxfService> dsm){
	
	this.dac=config;
	this.record=record;
	this.session=session;
	this.dsm=dsm;
	
	if(dsm.get("dipDbService")!=null){
	    this.dbservice=dsm.get("dipDbService");
	}
	
	log = LogFactory.getLog(DipAgent.class);

    }

    public void run(){

	log.info("DipAgent: run starting...");

	// test agent count
	//-----------------

	if(session!=null && record!=null){
	    Integer agc= null;
	    
	    synchronized(session){
		agc = (Integer) session.get("agc");
		if(agc==null){
		    session.put("agc",new Integer(1));
		    agc=new Integer(1);
		} else {
		    session.put("agc",new Integer(agc.intValue()+1));
		}
	    }
	    
	    if(agc.intValue() > AGCMAX){
		log.info("DipAgent: skipping jobs: too many agents per session");
	    } else {
		log.info("DipAgent: processing "+record.getAc());
		this.process(dac);
		log.info("DipAgent: processing "+record.getAc()+"  DONE");
	    }
	    
	    synchronized(session){
		agc=(Integer) session.get("agc");
		if(agc.intValue()>0){
		    session.put("agc",new Integer(agc.intValue()-1));
		}
	    }
	    log.info("DipAgent: done...");
	}
    }

    public void process(DipAgentConfig conf){
	
	String recType="";
	
	if(record.getAc().matches(".+\\d+N")){
	    recType="N";
	}
	if(record.getAc().matches(".+\\d+E")){
	    recType="E";
	}
	if(record.getAc().matches(".+\\d+X")){
	    recType="X";
	}
	if(record.getAc().matches(".+\\d+S")){
	    recType="S";
	}
	
	log.info("DipAgent: recType="+recType);

	// services to execute for record of recType
	//------------------------------------------

	List slist = (List) conf.getConfig().get(recType);
	
	if(slist!=null && slist.size()>0){
	    log.info("DipAgent: servicelist="+slist.size());
	    
	    for(Iterator sli=slist.iterator();sli.hasNext();){
		
		Map cs= (Map) sli.next();

		String ctp=(String) cs.get("component-type");
		String cns=(String)cs.get("component-ns");
		String cca=(String)cs.get("component-ac");
		DxfService svr=(DxfService)cs.get("server");
		String cvc=(String)cs.get("service");
		String stg=(String)cs.get("service-tag");
		String rtg=(String)cs.get("result-tag");

		log.info("DipAgent: component-type="+ctp+" component-ns="+cns+" stg="+stg+" rtg="+rtg);
		
		if(ctp.equals("self")){
		    if(record.getNs().toLowerCase().equals(cns.toLowerCase())){
			log.info("DipAgent: xref: NS="+cns+" AC="+record.getAc());

			ConcurrentHashMap lcache = null;
			synchronized(session){
			    lcache = (ConcurrentHashMap) session.get("list-cache");
			    if(lcache==null){
				session.put("list-cache",new ConcurrentHashMap());
				lcache = (ConcurrentHashMap) session.get("list-cache");
			    }
			}
			ConcurrentHashMap rcache = null;
			synchronized(session){
			    rcache = (ConcurrentHashMap) session.get("record-cache");
			}
			    
			if(rcache!=null && lcache!=null){
			    if(cvc.equals("getDxfNode") && rcache.get(record.getAc()+"_"+rtg)==null){
				NodeType result = svr.getDxfNode(record.getNs().toLowerCase(),record.getAc(),stg);
				if(result!=null){
				    rcache.put(record.getAc()+"_"+rtg,result);
				    log.info("DipAgent: part: hit added: "+record.getAc()+"_"+rtg);
				} else {
				    log.info("DipAgent: part no record found...");
				}
			    } else{
                                log.info("DipAgent: record already cached.");
                            }
			    
			    if(cvc.equals("getDxfRefList") && lcache.get(record.getAc()+"_"+rtg)==null){
				List<XrefType> result = svr.getDxfRefList(record.getNs().toLowerCase(),record.getAc(),stg);
				if(result!=null){
				    lcache.put(record.getAc()+"_"+rtg,result);
				    log.info("DipAgent: part: hit added: "+record.getAc()+"_"+rtg);
				} else {
				    log.info("DipAgent: part no record found...");
				}
			    } else{
				log.info("DipAgent: record already cached.");
			    }
			}
		    }
		}
		
		if(ctp.equals("xref")){
		    List<XrefType> 
			xrefs= record.getXrefList().getXref();
		    
		    for(Iterator<XrefType> 
			    xi=xrefs.iterator();xi.hasNext();){
			XrefType cxref= xi.next();
			
			if(cxref.getNs().toLowerCase().equals(cns.toLowerCase())){
			    log.info("DipAgent: xref: NS="+cns+" AC="+cxref.getAc());
			    
			    ConcurrentHashMap lcache = null;
			    synchronized(session){
				lcache = (ConcurrentHashMap) session.get("list-cache");
				if(lcache==null){
				    session.put("list-cache",new ConcurrentHashMap());
				    lcache = (ConcurrentHashMap) session.get("list-cache");
				}
			    }
			    ConcurrentHashMap rcache = null;
			    synchronized(session){
				rcache = (ConcurrentHashMap) session.get("record-cache");
			    }
			    			    
			    if(rcache!=null && lcache!=null){
				if(cvc.equals("getDxfNode") && rcache.get(cxref.getAc()+"_"+rtg)==null){
				    NodeType result = svr.getDxfNode(cxref.getNs().toLowerCase(),cxref.getAc(),stg);
				    if(result!=null){
					rcache.put(cxref.getAc()+"_"+rtg,result);
					log.info("DipAgent: part: hit added: "+cxref.getAc()+"_"+rtg);
				    } else {
					    log.info("DipAgent: part no record found...");
				    }
				} else{
				    log.info("DipAgent: record already cached.");   
				}
				if(cvc.equals("getDxfRefList") && lcache.get(cxref.getAc()+"_"+rtg)==null){
				    List<XrefType> result = svr.getDxfRefList(cxref.getNs().toLowerCase(),cxref.getAc(),stg);
				    if(result!=null){
					lcache.put(cxref.getAc()+"_"+rtg,result);
					log.info("DipAgent: part: hit added: "+cxref.getAc()+"_"+rtg);
				    } else {
					log.info("DipAgent: part no record found...");
				    }
				}else{
				    log.info("DipAgent: record already cached.");
				}
			    }
			}
		    }
		}
	    
		if(ctp.equals("part")){
		    List<NodeType.PartList.Part> 
			parts= record.getPartList().getPart();
		    
		    for(Iterator<NodeType.PartList.Part> 
			    pi=parts.iterator();pi.hasNext();){
			PartType cpart= pi.next();
			NodeType cnd=cpart.getNode();
			if(cnd.getNs().equals(cns)){
			    log.info("DipAgent: xref: NS="+cns+" AC="+cnd.getAc());
			    
			    ConcurrentHashMap lcache = null;
			    synchronized(session){
				lcache = (ConcurrentHashMap) session.get("list-cache");
				if(lcache==null){
				    session.put("list-cache",new ConcurrentHashMap());
				    lcache = (ConcurrentHashMap) session.get("list-cache");
				}
			    }
			    ConcurrentHashMap rcache = null;
			    synchronized(session){
				rcache = (ConcurrentHashMap) session.get("record-cache");
			    }
			    
			    if(rcache!=null && lcache!=null){
				if(cvc.equals("getDxfNode") && rcache.get(cnd.getAc()+"_"+rtg)==null){
				    NodeType 
					result = svr.getDxfNode(cnd.getNs().toLowerCase(),cnd.getAc(),stg);
				    if(result!=null){
					rcache.put(cnd.getAc()+"_"+rtg,result);
					log.info("DipAgent: part: hit added: "+cnd.getAc()+"_"+rtg);
				    } else {
					log.info("DipAgent: part no record found...");
				    }
				} else{
				    log.info("DipAgent: record already cached.");
				}
			    }
			}
		    }
		}
	    }
	}
    }

    public void process(){
	
	if(record.getAc().matches(".+\\d+E")){
	    
	    List<NodeType.PartList.Part> 
		parts= record.getPartList().getPart();
	    
	    for(Iterator<NodeType.PartList.Part> 
		    pi=parts.iterator();pi.hasNext();){
		NodeType cnode= pi.next().getNode();
		
		log.info("DipAgent: part: "+cnode.getAc());
		
		ConcurrentHashMap rcache = null;
		synchronized(session){
		    rcache = (ConcurrentHashMap) session.get("record-cache");
		}
		
		if(rcache!=null){
		    if(rcache.get(cnode.getAc())==null){
			NodeType 
			    result = dbservice.getDxfNode(cnode.getNs().toLowerCase(),cnode.getAc(),"base");
			
			if(result!=null){
			    rcache.put(cnode.getAc()+"_base",result);
			    log.info("DipAgent: part: hit added: "+cnode.getAc()+"_base");
			} else {
			    log.info("DipAgent: part no record found...");
			}
		    } else{
			log.info("DipAgent: record already cached.");
		    }
		}
	    }
	}
	if(record.getAc().matches(".+\\d+N")){
	    
	    log.info("DipAgent: list: "+record.getAc()+"_E");
	    
	    ConcurrentHashMap lcache = null;
	    synchronized(session){
		lcache = (ConcurrentHashMap) session.get("list-cache");
		if(lcache==null){
		    session.put("list-cache",new ConcurrentHashMap());
		    lcache = (ConcurrentHashMap) session.get("list-cache");
		}
	    }
	    
	    if(lcache!=null){
		if(lcache.get(record.getAc()+"_E")==null){
		    List<XrefType> 
			result = dbservice.getDxfRefList(record.getNs().toLowerCase(),record.getAc(),"E");
		    
		    if(result!=null && result.size()>0){
			lcache.put(record.getAc()+"_E",result);
			log.info("DipAgent: list: hit added: "+record.getAc()+"_E");
		    } else {
			log.info("DipAgent: list no record found...");
		    }
		} else{
		    log.info("DipAgent: list already cached.");
		}
	    }
	}
    }
}
