package edu.ucla.mbi.service;

/* ========================================================================
 * $Id:: EbiDbSoapService.java 3435 2013-09-11 15:31:48Z lukasz           $
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

import javax.xml.namespace.QName;
import java.net.URL;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;

import edu.ucla.mbi.dxf14.*;
import edu.ucla.mbi.proxy.*;
//import edu.ucla.mbi.proxy.ebi.*;
import javax.xml.datatype.XMLGregorianCalendar;

public class EbiDbSoapService 
    implements DxfService {

    
    private EbiProxyPort port;
    private EbiProxyService service;
    
    //private EbiCachingImplService service;  
    //private EbiProxyPortType port;

    private edu.ucla.mbi.dxf14.ObjectFactory
	dxo = new edu.ucla.mbi.dxf14.ObjectFactory();

    public EbiDbSoapService() {
	Log log = LogFactory.getLog( EbiDbSoapService.class );
	log.info( "EbiSoapDbService: creating service" );
        
        //service = new EbiProxyService();
        //port = service.getProxyPort();

	//port = new EbiPublic().getPublic();
    }

    String endpoint=null;

    public String getEndpoint() {
	return endpoint;
    }

    public void setEndpoint( String endpoint ) {
	this.endpoint = endpoint;
    }
    
    //---------------------------------------------------------------------
    // initialize
    //-----------
    
    public void initialize() {

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "EbiSoapDbService: endpoint=" + endpoint);
        
        if ( endpoint == null ||  endpoint.length() == 0 ){
            log.info( "EbiSoapDbService: default endpoint" );
            service = new EbiProxyService();
            port = service.getProxyPort();

            //service = new EbiCachingImplService();
            //port = service.getEbiCachingImplPort();
            
        } else {
            try {
                URL url = new URL( endpoint + "?wsdl" );
                QName qn = new QName("http://mbi.ucla.edu/proxy/ebi",
                                     "EbiProxyService");

                //QName qn = new QName("http://ebi.proxy.mbi.ucla.edu/",
                //                     "EbiCachingImplService");
                
                log.info( "EbiSoapDbService: url=" + url);

                service = new EbiProxyService( url, qn );
                port = service.getProxyPort();
                
                //service = new EbiCachingImplService( url, qn );
                //port = service.getEbiCachingImplPort();                

                ( (BindingProvider) port ).getRequestContext()
                    .put( BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                          endpoint );
            } catch ( Exception ex ) {
                log.info( "EbiSoapDbService: cannot initialize");
            }
        }
    
        /*
	if( endpoint != null && port != null 
	    && endpoint.length() > 0 ) {
	    Log log = LogFactory.getLog(EbiDbSoapService.class);
	    log.info("EbiSoapDbService: endpoint="+endpoint);
	    ((BindingProvider)port).getRequestContext()
		.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                endpoint);	    
	}
        */
    }
 
    // DxfService interface implementation
    //------------------------------------
    
    public NodeType getDxfNode(String ns, String accession, String detail){
	//RequestType rq=request("dip",accession,"",detail,"");
	Log log = LogFactory.getLog(EbiDbSoapService.class);
        log.info(" getDxfNode ac=" + accession + " tag=" + detail);
        
	List<NodeType> rlist = getDxfNodeList( ns, accession, detail );
	if( rlist != null && rlist.size() > 0 ){
	    return rlist.get(0);
	} else {
	    return null;
	}
    }



    public List<XrefType> getDxfRefList( String ns,String accession,
                                         String tag, String match ){
        return getDxfRefList( ns, accession, tag);
    }

    
    public List<XrefType> getDxfRefList(String ns,String accession, String tag){
	
        String ac = accession;
        String match = "";
        String detail = tag;
        String format = "";

	try{
            
            Holder<DatasetType> resDataset = new  Holder<DatasetType>();
            Holder<String> resNative = new  Holder<String>();
            Holder<XMLGregorianCalendar> timestamp =
                new Holder<XMLGregorianCalendar>();
         
	    port.getPicrList( ns, ac, "", detail, "",
                              "", 0, timestamp, resDataset,resNative);

            edu.ucla.mbi.dxf14.ObjectFactory
                dof = new edu.ucla.mbi.dxf14.ObjectFactory();

            //DatasetType dataset = dof.createDatasetType();
            //dataset.getNode().addAll(resdoc);
            
            DatasetType dataset = resDataset.value;
            NodeType nodeT = dataset.getNode().get( 0 );
            
	    //List<NodeType> rlist = port.getPicrList(ns,ac,match,detail,format);
	    //NodeType nodeT = rlist.get(0);

	    List<XrefType> xrefList = (List<XrefType>)nodeT.getXrefList();
	    return xrefList;
	} catch(Exception ex){
	    Log log = LogFactory.getLog(EbiDbSoapService.class);
            log.info("EbiSoap: exception="+ex.toString());
	}
	return new ArrayList<XrefType>();

    }


    
    public List<NodeType> getDxfNodeList( String ns, String accession, 
                                          String tag, String match ) {
     
        return getDxfNodeList( ns, accession, tag );

    }
    
    public List<NodeType> getDxfNodeList( String ns, String accession, 
                                          String tag ) {
        
        String ac = accession;
        String match = "";
        String detail = tag;
        String format = "";

	Log log = LogFactory.getLog( EbiDbSoapService.class );
	log.info( " getDxfNodeList ac=" + accession + " tag=" + detail );
	log.info( "  port: " + port);

	try{
	    //List<NodeType> nl=port.getUniprot(ns,ac, match,detail, format);
	    
            Holder<DatasetType> resDataset = new  Holder<DatasetType>();
            Holder<String> resNative = new  Holder<String>();
            Holder<XMLGregorianCalendar> timestamp =
                new Holder<XMLGregorianCalendar>();
            port.getUniprot( "uniprot" , ac, "", detail, format,
                             "", 0, timestamp, resDataset, resNative );
            
            edu.ucla.mbi.dxf14.ObjectFactory
                dof = new edu.ucla.mbi.dxf14.ObjectFactory();

            //DatasetType dataset = dof.createDatasetType();
            //dataset.getNode().addAll(resdoc);

            DatasetType dataset = resDataset.value;
            NodeType nodeT = dataset.getNode().get( 0 );

	    log.info("EbiSoap: getDxfNodeList:" + dataset);
	    if( dataset!=null && dataset.getNode() != null ) {
		log.info("EbiSoap: getDxfNodeList: " + 
                         dataset.getNode().size() );
	    }
	    return dataset.getNode();
	} catch(Exception ex){

            StringWriter sw = new StringWriter();
            ex.printStackTrace( new PrintWriter(sw) );
            log.info( " StackTrace: " + sw.toString() );
	}
	return new ArrayList<NodeType>();
    }

    public List<NodeType> getDxfQuery(String ns,String accession, String tag){
	return new ArrayList<NodeType>();
    }

    public List<NodeType> getDxfMeta(String ns,String accession, String tag) {
        return new ArrayList<NodeType>();
    }
    
}



