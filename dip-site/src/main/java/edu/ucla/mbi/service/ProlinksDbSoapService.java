package edu.ucla.mbi.service;

/* ========================================================================
 * $Id:: ProlinksDbSoapService.java 3435 2013-09-11 15:31:48Z lukasz      $
 * Version: $Rev:: 3435                                                   $
 *=========================================================================
 *                                                                        $
 * EbiDbSoapService - EBI Database access implemented through SOAP        $
 *                                                                        $
 *====================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.io.*;
import java.net.URL;

import javax.xml.bind.*;
import javax.xml.ws.BindingProvider;

import javax.xml.namespace.QName;

import edu.ucla.mbi.dxf14.*;
import edu.ucla.mbi.proxy.*;
//import edu.ucla.mbi.proxy.prolinks.*;



public class ProlinksDbSoapService 
    implements DxfService {
    
    private ProlinksProxyService service;
    private ProlinksProxyPort port;

    //private ProlinksCachingImplService service;
    //private ProlinksProxyPortType port;

    private edu.ucla.mbi.dxf14.ObjectFactory
	dxo = new edu.ucla.mbi.dxf14.ObjectFactory();

    String endpoint=null;

    public String getEndpoint(){
        return endpoint;
    }

    public void setEndpoint(String endpoint){
        this.endpoint=endpoint;
    }


    //---------------------------------------------------------------------
    // initialize
    //-----------

    public void initialize() {

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "ProlinksSoapDbService: endpoint=" + endpoint);

        if ( endpoint == null ||  endpoint.length() == 0 ){
            log.info( "ProlinksSoapDbService: default endpoint" );
            service = new ProlinksProxyService();
            port = service.getProxyPort();

            //service = new ProlinksCachingImplService();
            //port = service.getProlinksCachingImplPort();


        } else {
            try {
                URL url = new URL( endpoint + "?wsdl" );
                QName qn = new QName("http://mbi.ucla.edu/proxy/prolinks",
                                     "ProlinksProxyService");

                //QName qn = new QName("http://prolinks.proxy.mbi.ucla.edu/",
                //                     "ProlinksCachingImplService");


                //service = new ProlinksCachingImplService( url, qn);
                //port = service.getProlinksCachingImplPort();
                
                service = new ProlinksProxyService( url, qn );
                port = service.getProxyPort();

                ( (BindingProvider) port ).getRequestContext()
                    .put( BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                          endpoint );
            } catch ( Exception ex ) {
                log.info( "ProlinksSoapDbService: cannot initialize");
            }
        }

        /*
        if(endpoint!=null && port!=null
           && endpoint.length()>0){
            Log log = LogFactory.getLog(ProlinksDbSoapService.class);
            log.info("ProlinksDbSoapService: endpoint="+endpoint);
            ((BindingProvider)port).getRequestContext()
                .put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                     endpoint);
        }
        */
    }

    public ProlinksDbSoapService(){

        Log log = LogFactory.getLog(ProlinksDbSoapService.class);
	log.info("ProlinksSoapDbService: creating service");

        //ProlinksProxyService service = new ProlinksProxyService();
        //port = service.getProxyPort();

	//port = new ProlinksPublic().getPublic();
    }
    
    // DxfService interface implementation
    //------------------------------------
    
    public NodeType getDxfNode(String ns, String accession, String detail){
	//RequestType rq=request("dip",accession,"",detail,"");
	Log log = LogFactory.getLog(ProlinksDbSoapService.class);
        log.info("ProlinksSoap: getDxfNode ac="+accession+" tag="+detail);

	List<NodeType> rlist= getDxfNodeList(ns,accession,detail);
	if(rlist!=null && rlist.size()>0){
	    return rlist.get(0);
	} else {
	    return null;
	}
    }

    public List<XrefType> getDxfRefList(String ns,String accession, String tag){
	return new ArrayList<XrefType>(); // getList(accession+"_"+tag);
    }

    public List<XrefType> getDxfRefList( String ns,String accession, 
                                         String tag, String match ){
	return new ArrayList<XrefType>(); // getList(accession+"_"+tag);
    }
    
    public List<NodeType> getDxfNodeList(String ns,String accession, String tag){
	
	// NOTE: not implemented
	return new ArrayList<NodeType>();
    }

    public List<NodeType> getDxfNodeList( String ns, String accession,
                                          String tag, String match ) {

        return getDxfNodeList( ns, accession, tag );

    }

    public List<NodeType> getDxfQuery(String ns,String accession, String tag){
        return new ArrayList<NodeType>();
    }

    public List<NodeType> getDxfMeta( String ns, String accession,
                                      String tag ) {

        return new ArrayList<NodeType>();

    }    

}



