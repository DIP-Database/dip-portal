package edu.ucla.mbi.service;

/* ========================================================================
 * $Id:: NcbiDbSoapService.java 3435 2013-09-11 15:31:48Z lukasz          $
 * Version: $Rev:: 3435                                                   $
 *=========================================================================
 *                                                                        $
 * NcbiDbSoapService - NCBI Database access implemented through SOAP      $
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
import javax.xml.datatype.XMLGregorianCalendar;

import edu.ucla.mbi.dxf14.*;
import edu.ucla.mbi.proxy.*;
//import edu.ucla.mbi.proxy.ncbi.*;

public class NcbiDbSoapService 
    implements DxfService {


    private NcbiProxyService service;
    private NcbiProxyPort port;

    //private NcbiCachingImplService service;
    //private NcbiProxyPortType port;

    private edu.ucla.mbi.dxf14.ObjectFactory
	dxo = new edu.ucla.mbi.dxf14.ObjectFactory();
    
    public NcbiDbSoapService() {
	Log log = LogFactory.getLog( NcbiDbSoapService.class );
	log.info( "NcbiSoapDbService: creating service" );
        
        //NcbiProxyService service = new NcbiProxyService();
        //port = service.getProxyPort();
	
	//port = new NcbiPublic().getPublic();
    }
    
    String endpoint = null;

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
        log.info( "NcbiSoapDbService: endpoint=" + endpoint);

        if ( endpoint == null ||  endpoint.length() == 0 ){
            log.info( "NcbiSoapDbService: default endpoint" );
            service = new NcbiProxyService();
            port = service.getProxyPort();
            //service = new NcbiCachingImplService();
            //port = service.getNcbiCachingImplPort();
        } else {
            try {
                URL url = new URL( endpoint + "?wsdl" );
                QName qn = new QName("http://mbi.ucla.edu/proxy/ncbi",
                                     "NcbiProxyService");

                //QName qn = new QName("http://ncbi.proxy.mbi.ucla.edu/",
                //                     "NcbiCachingImplService");

                service = new NcbiProxyService( url, qn );
                port = service.getProxyPort();

                //service = new NcbiCachingImplService( url, qn );
                //port = service.getNcbiCachingImplPort();

                ( (BindingProvider) port ).getRequestContext()
                    .put( BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                          endpoint );
            } catch ( Exception ex ) {
                log.info( "NcbiSoapDbService: cannot initialize");
            }
        }

        /*
        if( endpoint != null && port != null
            && endpoint.length() > 0 ) {
	    Log log = LogFactory.getLog( NcbiDbSoapService.class );
	    log.info( "NcbiSoapDbService: endpoint=" + endpoint);
            ( (BindingProvider) port ).getRequestContext()
                .put( BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                      endpoint );
        }
        */
    }

    //--------------------------------------------------------------------------
    // DxfService interface implementation
    //------------------------------------

    public NodeType getDxfNode( String ns, String accession, 
                                String detail ) {
	Log log = LogFactory.getLog( NcbiDbSoapService.class );
        log.info( "NcbiSoap: getDxfNode ac=" + accession + 
                  " tag=" + detail);
        
	List<NodeType> rlist= getDxfNodeList( ns, accession, 
                                              detail );
	if( rlist != null && rlist.size() > 0 ) {
	    return rlist.get( 0 );
	} else {
	    return null;
	}
    }
    

    //--------------------------------------------------------------------------

    public List<XrefType> getDxfRefList( String ns, String accession, 
                                         String tag, String match ) {
        return getDxfRefList( ns, accession, tag );
    }
    
    public List<XrefType> getDxfRefList( String ns, String accession, 
                                         String tag ) {
	String ac = accession;
	String match = "";
	String detail = tag;
	String format = "";
	
	List<NodeType> rlist = null;
        
        Log log = LogFactory.getLog( NcbiDbSoapService.class );
        
        //port = service.getProxyPort();
        
        Holder<DatasetType> resDataset = new  Holder<DatasetType>();
        Holder<String> resNative = new  Holder<String>();
        Holder<XMLGregorianCalendar> timestamp =
            new Holder<XMLGregorianCalendar>();

        edu.ucla.mbi.dxf14.ObjectFactory
            dof = new edu.ucla.mbi.dxf14.ObjectFactory();

        try {

            //DatasetType dataset = dof.createDatasetType();
            //dataset.getNode().addAll(resdoc);

            if( ns.equalsIgnoreCase( "pmid" ) ) {
                //rlist = port.getPubmedArticle( ns, accession, 
                //                               match, detail, format);
                port.getPubmedArticle( ns, ac, "", detail, "",
                                       "", 0, timestamp, resDataset,resNative);
                
            } else if ( ns.equalsIgnoreCase( "entrezgene" ) ) {
                //rlist = port.getGene( ns, ac, match, detail, format);
                
                port.getGene( ns, ac, "", detail, "",
                              "", 0,timestamp, resDataset,resNative);
            } else if ( ns.equalsIgnoreCase( "taxid" ) ) {
                //rlist = port.getGene( ns, ac, match, detail, format);
                
                port.getTaxon( ns, ac, "", detail, "",
                              "", 0,timestamp, resDataset,resNative);
            } else {
                //rlist = port.getRefseq( ns, ac, match,detail, format);
                port.getRefseq( ns, ac, "", detail, "",
                                "", 0, timestamp, resDataset,resNative);
            }
            
        } catch ( ProxyFault f ){
            log.info( "NcbiSoap: getDxfRefList: " + f );
        }
        DatasetType dataset = resDataset.value;
        
	if( dataset != null && dataset.getNode() != null &&
            dataset.getNode().size() > 0 ) {
            NodeType nodeT = dataset.getNode().get( 0 );
            
            List<XrefType> xrefList = 
                (List<XrefType>)nodeT.getXrefList();
          
            return xrefList;
	} else {
            return new ArrayList<XrefType>();
	}
    }

    //--------------------------------------------------------------------------

    public List<NodeType> getDxfNodeList( String ns, String accession, 
                                          String tag, String match ) {

        return getDxfNodeList( ns, accession, tag );
    }
    
    public List<NodeType> getDxfNodeList( String ns, 
                                          String accession, 
                                          String tag ) {
	String ac = accession;
	String match = "";
	String detail = tag;
	String format = "";
        
        Log log = LogFactory.getLog( NcbiDbSoapService.class );

        log.debug(" getDxfNodeList: ns=" + ns + " ac=" + ac);

        //port = service.getProxyPort();
	
        Holder<DatasetType> resDataset = new  Holder<DatasetType>();
        Holder<String> resNative = new  Holder<String>();
        Holder<XMLGregorianCalendar> timestamp =
            new Holder<XMLGregorianCalendar>();
        edu.ucla.mbi.dxf14.ObjectFactory
            dof = new edu.ucla.mbi.dxf14.ObjectFactory();
        
        try {
            if ( ns.equalsIgnoreCase( "pmid" ) ) {
                
                log.info( "NcbiSoapDbService: calling ProxyWS(pmid)" +
                          " NS=" + ns + " AC=" + ac);
                port.getPubmedArticle( ns, ac, "", detail, "",
                                       "", 0, timestamp, 
                                       resDataset, resNative);

                //return port.getPubmedArticle(ns, ac, match, detail, format);
            } else if ( ns.equalsIgnoreCase( "entrezgene" ) ) {

                log.info( "NcbiSoapDbService: calling ProxyWS(entrezgene)" +
                          " NS=" + ns + " AC=" + ac );
                port.getGene( ns, ac, "", detail, "",
                              "", 0, timestamp, resDataset,resNative);
        
                //return port.getGene(ns, ac, match, detail, format);
            } else if ( ns.equalsIgnoreCase( "taxid" ) ) {

                log.info( "NcbiSoapDbService: calling ProxyWS(taxid)" +
                          " NS=" + ns + " AC=" + ac );
                port.getTaxon( ns, ac, "", detail, "",
                              "", 0, timestamp, resDataset,resNative);
        
                //return port.getGene(ns, ac, match, detail, format);
            } else {
                
                log.info( "NcbiSoapDbService: calling ProxyWS(refseq)" +
                          " NS=" + ns + " AC=" + ac );
                
                port.getRefseq( ns, ac, "", detail, "",
                                "", 0, timestamp, resDataset,resNative);
                log.info( "NcbiSoapDbService: DONE calling ProxyWS(refseq)");
                //return port.getRefseq(ns, ac, match,detail, format);
            }
            
        } catch ( ProxyFault f ) {
            log.info( "NcbiSoap: getDxfRefList: " + f );
        }
        
        DatasetType dataset = resDataset.value;
        
        if( dataset != null && dataset.getNode() != null &&
            dataset.getNode().size() > 0 ) {
	    
            return dataset.getNode();
            
        } else {
            return new ArrayList<NodeType>();
        }
    }

    //--------------------------------------------------------------------------

    public List<NodeType> getDxfQuery( String ns, String accession, 
                                       String tag ){
        // NOTE: not implemented
        return null;
    }

    public List<NodeType> getDxfMeta( String ns, String accession, 
                                      String tag ){
        return new ArrayList<NodeType>();
    }    
}



