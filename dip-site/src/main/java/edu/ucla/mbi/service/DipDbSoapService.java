package edu.ucla.mbi.service;

/* =============================================================================
 * $Id:: DipDbSoapService.java 3353 2013-07-21 19:10:16Z lukasz                $
 * Version: $Rev:: 3353                                                        $
 *==============================================================================
 *                                                                             $
 * DipDbSoapService - DIP Database access implemented through SOAP             $
 *                                                                             $
 *=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.io.*;

import javax.xml.namespace.QName;
import java.net.URL;

import javax.xml.bind.*;
import javax.xml.ws.BindingProvider;

import edu.ucla.mbi.dxf14.*;
import edu.ucla.mbi.proxy.dip.*;
import edu.ucla.mbi.services.dip.*;
import edu.ucla.mbi.services.dip.direct.*;
import edu.ucla.mbi.legacy.dip.*;

public class DipDbSoapService implements DxfService, DipDbService {
    
    //private DipDirectService service;
    //private DipDirectPort port;

    private DipLegacyService legacyService;
    private DipLegacyPort legacyPort;
    
    private edu.ucla.mbi.dxf14.ObjectFactory
	dxo = new edu.ucla.mbi.dxf14.ObjectFactory();

    /*
    private RequestType request( String ns,String ac, String match, 
                                 String detail, String format ) {
	
        RequestType rq= dxo.createRequestType();
        rq.setNs(ns);
        rq.setAc(ac);
        rq.setDetail(match);
        rq.setMatch(detail);
        rq.setFormat(format);
	return rq;
    }
    */
    
    /*
    String endpoint = null;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint( String endpoint ) {
        this.endpoint = endpoint;
    }
    */
    String legacyEndpoint = null;

    public String getLegacyEndpoint() {
        return legacyEndpoint;
    }

    public void setLegacyEndpoint( String endpoint ) {
        this.legacyEndpoint = endpoint;
    }


    //--------------------------------------------------------------------------
    // initialize
    //-----------

    public void initialize() {
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "DipDbSoapService: initializing" );
        // log.info( "DipDbSoapService: direct endpoint=" + endpoint );
        log.info( "DipDbSoapService: legacy endpoint=" + legacyEndpoint );

        /*

        if ( endpoint == null ||  endpoint.length() == 0 ){
            log.info( "DipDirectService: default endpoint" );
            service = new DipDirectService();
            port = service.getPublic();
        } else {
            try {
                URL url = new URL( endpoint + "?wsdl" );
                QName qn = new QName("http://mbi.ucla.edu/services/dip",
                                     "dipPublic");
                service = new DipDirectService( url, qn );
                port = service.getPublic();

                ( (BindingProvider) port ).getRequestContext()
                    .put( BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                          endpoint );
            } catch ( Exception ex ) {
                log.info( "DipDirectService: cannot initialize");
            }
        }

        */

        if ( legacyEndpoint == null ||  legacyEndpoint.length() == 0 ){
            log.info( "DipDirectService: default endpoint" );
            legacyService = new DipLegacyService();
            legacyPort = legacyService.getLegacyPort();
        } else {
            try {
                URL url = new URL( legacyEndpoint + "?wsdl" );
                QName qn = new QName("http://mbi.ucla.edu/services/legacy/dip",
                                     "DipLegacyService");
                legacyService = new DipLegacyService( url, qn );
                legacyPort = legacyService.getLegacyPort();
                ( (BindingProvider) legacyPort ).getRequestContext()
                    .put( BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                          legacyEndpoint );
            } catch ( Exception ex ) {
                ex.printStackTrace();
                log.info( "DipLegacyService: cannot initialize");
            }
        }
        
        /*
        if( endpoint != null && port != null
            && endpoint.length() > 0 ) {
            Log log = LogFactory.getLog( this.getClass() );
            log.info( "DipDbSoapService: endpoint="+endpoint);
            ((BindingProvider)port).getRequestContext()
                .put( BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                      endpoint);
        }

        if( legacyEndpoint != null && legacyPort != null
            && legacyEndpoint.length() > 0 ) {
            Log log = LogFactory.getLog( this.getClass() );
            log.info( "DipDbSoapService: legacy endpoint=" + legacyEndpoint );
            ((BindingProvider)legacyPort).getRequestContext()
                .put( BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                      legacyEndpoint);
        }
        */
    }

    
    //--------------------------------------------------------------------------
    // constructor
    //------------
    
    public DipDbSoapService(){

        Log log = LogFactory.getLog( this.getClass() );
	log.info( "DipSoapDbService: creating service" );
 
        //DipDirectService service = new DipDirectService();
        //port = service.getPublic();
        
        //DipLegacyService legacyService = new DipLegacyService();
        //legacyPort = legacyService.getLegacyPort();
    }

    
    //--------------------------------------------------------------------------
    // record retrieval
    //-----------------
    
    public List<NodeType> getRecord( String accession, String detail ) {
        
	if( accession.endsWith("E") ) {
	    try {
		return legacyPort.getLink( "dip", accession, "", detail, "" );
	    } catch( Exception ex ) {
		Log log = LogFactory.getLog( this.getClass() );
		log.info( "DipDbSoap: exception=" + ex.toString() );
	    }
	}
	if( accession.endsWith("N") ) {
	    try{
		return legacyPort.getNode( "dip", accession, 
                                           null, "", detail, "" );
	    } catch(Exception ex){
		Log log = LogFactory.getLog( this.getClass() );
		log.info("DipDbSoap: exception="+ex.toString());
	    }
	}
	if(accession.endsWith("X")){
	    try{
		return legacyPort.getEvidence("dip",accession,"",detail,"");
	    } catch(Exception ex){
		Log log = LogFactory.getLog( this.getClass() );
		log.info("DipDbSoap: exception="+ex.toString());
	    }
	}
	if(accession.endsWith("S")){
	    try{
                return legacyPort.getSource("dip",accession,"",detail,"");
	    } catch(Exception ex){
		Log log = LogFactory.getLog( this.getClass() );
		log.info("DipDbSoap: exception="+ex.toString());
	    }
	}
	return new ArrayList<NodeType>(); // throw exception
    }

    public List<NodeType> getLink(String accession, String detail){
	try{
            return legacyPort.getLink("dip",accession,"",detail,"");
	} catch(Exception ex){
	    Log log = LogFactory.getLog( this.getClass() );
	    log.info("DipDbSoap: exception="+ex.toString());
	}
	return new ArrayList<NodeType>();
    }

    public List<NodeType> getEvidence(String accession, String detail){
	try{
            return legacyPort.getEvidence("dip",accession,"",detail,"");
	} catch(Exception ex){
	    Log log = LogFactory.getLog( this.getClass() );
	    log.info("DipDbSoap: exception="+ex.toString());
	}
	return new ArrayList<NodeType>();
    }

    
    public List<NodeType> getImexEvidence(String accession, String detail){
	try{
            return legacyPort.getEvidence("imex",accession,"",detail,"");
	} catch(Exception ex){
	    Log log = LogFactory.getLog( this.getClass() );
	    log.info("DipDbSoap: exception="+ex.toString());
	}
	return new ArrayList<NodeType>();
    }

    public List<NodeType> getNode(String accession, String detail){
	try{
            return legacyPort.getNode( "dip", accession, 
                                       "", "", detail, "" );
	} catch(Exception ex){
	    Log log = LogFactory.getLog( this.getClass() );
	    log.info("DipDbSoap: exception="+ex.toString());
	}
	return new ArrayList<NodeType>();	
    }
    
    public List<NodeType> getSource(String accession, String detail){
	try{
	    return legacyPort.getSource("dip",accession,"",detail,"");
	} catch(Exception ex){
	    Log log = LogFactory.getLog( this.getClass() );
	    log.info("DipDbSoap: exception="+ex.toString());
	}	
	return new ArrayList<NodeType>();
    }


    //--------------------------------------------------------------------------
    // record list retrieval
    //----------------------
    
    public List<XrefType> getList( String listKey ) {
        return getList( listKey, "" );
    }
    
    public List<XrefType> getList( String listKey, String match ) {
        
	Log log = LogFactory.getLog( this.getClass() );
        log.debug( "DipSoap: getList key=" + listKey + " match=" + match );

	if( listKey!=null && listKey.lastIndexOf("_") == listKey.length()-2 ) {
            
	    //RequestType rq = 
            //    request( "dip", 
            //           listKey.substring(0,listKey.length()-3,"",detail,""));

	    if( listKey.endsWith("N") ) {
		try {
		    return legacyPort    
                        .getNodeList( "dip", 
                                      listKey.substring( 0, 
                                                         listKey.length()-2),
                                      "","base","");
		} catch( Exception ex ) {
		    log.info( "DipDbSoap: exception="+ex.toString() );
		}		
	    }
	    if ( listKey.endsWith("E") ) {
		try {
		    return legacyPort
                        .getLinkList( "dip", 
                                      listKey.substring( 0, 
                                                         listKey.length()-2 ), 
                                      match, "base", "" );
		} catch(Exception ex){
		    log.info( "DipDbSoap: exception=" + ex.toString() );
		}
	    }
	    if ( listKey.endsWith("X") ) {
		try {
		    return legacyPort
                        .getEvidenceList( "dip", 
                                          listKey.substring( 0, 
                                                             listKey.length()-2), 
                                          "", "base", "" );
		} catch( Exception ex ) {
		    log.info( "DipDbSoap: exception=" + ex.toString() );
		}
	    }
	    if ( listKey.endsWith("S") ) {
		try {
		    return legacyPort
                        .getSourceList( "dip", 
                                        listKey.substring( 0, 
                                                           listKey.length()-2), 
                                        "", "base", "" );
		} catch( Exception ex ) {
		    log.info( "DipDbSoap: exception=" + ex.toString() );
		}
	    }	    
	}
        
	return new ArrayList<XrefType>();
    }


    //--------------------------------------------------------------------------
    // DxfService interface implementation
    //------------------------------------

    public NodeType getDxfNode( String ns, String accession, 
                                String detail ) {
	//RequestType rq=request("dip",accession,"",detail,"");
	Log log = LogFactory.getLog( this.getClass() );
        log.info("DipSoap: getDxfNode ac="+accession+" tag="+detail);

        List<NodeType> rlist = null;

        if( ns != null && ns.equalsIgnoreCase("dip") ){
            rlist = getRecord( accession, detail );
            if( rlist != null && rlist.size() > 0 ) {
                return rlist.get(0);
            }
        }
        
        if( ns != null && ns.equalsIgnoreCase("imex") ){
            rlist= getImexEvidence( accession, detail );
            if( rlist != null && rlist.size() > 0 ) {
                return rlist.get(0);
            }
        }

 	return null;	
    }

    public List<XrefType> getDxfRefList( String ns, String accession, 
                                         String tag ) {
	return getList( accession + "_" + tag );
    }


    public List<XrefType> getDxfRefList( String ns, String accession, 
                                         String tag, String match ) {

        

	return getList( accession + "_" + tag, match );
    }

    public List<NodeType> getDxfQuery( String ns, String query, 
                                       String detail ) {
	//QueryLC qlc = new QueryLC();
	//qlc.setQuery( query );
	//qlc.setDetail( detail );
	
        // NOTE: dip-legacy uses "organism"; MIQL uses "species"
        //   


        Log log = LogFactory.getLog( this.getClass() );
        

        query = query.replace( "MiqlxGroupBy:species", 
                               "MiqlxFacetBy:organism" );

        log.debug( "DipDbSoap: query(modified)=" + query );

	try {
	    return legacyPort.query( query, detail, "full" );
	} catch( Exception ex ) {
            log.info( "DipDbSoap: exception=" + ex.toString() );
	    return null;
	}
    }
    
    public List<NodeType> getDxfNodeList( String ns, String accession, 
                                          String detail ) {
	
	// NOTE: not implemented
	try{
	    List<NodeType> rlist = getRecord( accession, detail );
	    if( rlist != null && rlist.size() > 0 ) {
		return rlist;
	    } 
	} catch( Exception ex ) {
	    Log log = LogFactory.getLog( this.getClass() );
            log.info( "DipDbSoap: exception=" + ex.toString() );
	}
	return new ArrayList<NodeType>();
    }

    public List<NodeType> getDxfNodeList( String ns, String accession, 
                                          String detail, String match ) {
	
	// NOTE: not implemented


        Log log = LogFactory.getLog( this.getClass() );
        log.info( "nodelist match= "  +  match );

	try{
	    List<NodeType> rlist = getRecord( accession, detail );
	    if( rlist != null && rlist.size() > 0 ) {
		return rlist;
	    } 
	} catch( Exception ex ) {
            log.info( "DipDbSoap: exception=" + ex.toString() );
	}
	return new ArrayList<NodeType>();
    }

    //--------------------------------------------------------------------------

    public List<NodeType> getDxfMeta( String ns, String accession, 
                                      String tag ) {
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "DipDbSoap(getDxfMeta): " 
                  + ns + " :: " + accession + " :: " +tag );
        try {
            return legacyPort.getCounts( ns, accession, "" , "", "" );  
        } catch( Exception ex ) {
            log.info( "DipDbSoap: exception=" + ex.toString() );
        }

        return new ArrayList<NodeType>();
    }    
}
