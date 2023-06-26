package edu.ucla.mbi.service;

/* =============================================================================
 * $Id:: DipDbSoapService.java 2189 2012-04-21 17:59:45Z lukasz                $
 * Version: $Rev:: 2189                                                        $
 *==============================================================================
 *                                                                             $
 * DxfPsqService - DXF access to psicquic-based services                       $
 *                                                                             $
 *=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import org.apache.http.*;
import org.apache.http.util.*;
import org.apache.http.message.*;

import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.entity.*;
import org.apache.http.client.utils.*;

import org.apache.http.impl.client.*;

import java.util.*;
import java.io.*;

import org.w3c.dom.*;
import javax.xml.parsers.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.URIResolver;

import javax.xml.bind.*;
import javax.xml.bind.util.JAXBResult;

import edu.ucla.mbi.dxf14.*;

import javax.xml.namespace.QName;
import java.net.URL;

import javax.xml.bind.*;
import javax.xml.ws.BindingProvider;

import edu.ucla.mbi.dxf14.*;
import edu.ucla.mbi.proxy.dip.*;
import edu.ucla.mbi.services.dip.*;
import edu.ucla.mbi.services.dip.direct.*;
import edu.ucla.mbi.legacy.dip.*;

public class DxfChainService implements DxfService {
        
    private edu.ucla.mbi.dxf14.ObjectFactory
	dxo = new edu.ucla.mbi.dxf14.ObjectFactory();

    private Log log;
    
    private List dxfChain;

    public void setDxfChain( List chain ){
        dxfChain = chain;
    }
    
    //--------------------------------------------------------------------------
    // initialize
    //-----------

    public void initialize() {
        
        log = LogFactory.getLog( this.getClass() );
        log.info( "DxfChainService: initializing" );
    }
    
    //--------------------------------------------------------------------------
    // constructor
    //------------
        
    public DxfChainService(){
        
        Log log = LogFactory.getLog( this.getClass() );
	log.info( "DxfChainService: creating service" );
 
    }
   
    //--------------------------------------------------------------------------
    // record retrieval
    //-----------------
    
    public List<NodeType> getRecord( String accession, String detail ) {
        
	return new ArrayList<NodeType>(); // throw exception
    }

    public List<NodeType> getLink(String accession, String detail){
	
	return new ArrayList<NodeType>();
    }

    public List<NodeType> getEvidence(String accession, String detail){
	
	return new ArrayList<NodeType>();
    }

    public List<NodeType> getNode(String accession, String detail){
	
	return new ArrayList<NodeType>();	
    }
    
    public List<NodeType> getSource(String accession, String detail){
		
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
    
    public List<NodeType> getDxfNodeList( String ns, String accession, 
                                          String detail ) {
	
        // NOTE: not implemented

	return new ArrayList<NodeType>();
    }

    public List<NodeType> getDxfNodeList( String ns, String accession, 
                                          String detail, String match ) {
	
	// NOTE: not implemented

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "nodelist match= "  +  match );

	
	return new ArrayList<NodeType>();
    }
    
    //--------------------------------------------------------------------------

    public List<NodeType> getDxfQuery( String ns, String query, 
                                       String detail ) {
	
        log.info( "ns=" + ns + " query=" + query + " detail=" + detail );
        
        List<NodeType> result = null;

        for( Iterator ci = dxfChain.iterator(); ci.hasNext(); ){

            Map cl = (Map) ci.next();
            
            log.info( "debug=" + cl.get( "debug" ) );

            DxfService ds = (DxfService) cl.get( "service" ); 
            
            if( result == null || result.size() == 0 ){
                
                // pass query
                log.debug( "result: null ?" + result );

                result = ds.getDxfQuery( ns, query, detail );
                
            } else {

                String method = (String) cl.get( "method" ); 
        
                String qtype = cl.get( "query-type" ) == null ? 
                    ns : (String) cl.get( "query-type" );
                
                String qdetail = cl.get( "query-detail" ) == null ?
                    detail : (String) cl.get( "query-detail" ); 

                String pmerge = cl.get( "part-merge-mode" ) == null ?
                    "replace" : (String) cl.get( "part-merge-mode" ); 
        

                log.debug( " meth=" + method + 
                           " qtyp=" + qtype + 
                           " qdet=" +  qdetail + 
                           " pmrg=" +  pmerge);
        
                //--------------------------------------------------------------                

                List<NodeType.PartList.Part> 
                    p = result.get( 0 ).getPartList().getPart();
                
                log.debug( "p=" + p );

                NodeType.PartList npl = dxo.createNodeTypePartList();

                Map npm = new HashMap();

                // go over result parts
                
                for ( Iterator<NodeType.PartList.Part> pi = p.iterator();
                      pi.hasNext(); ) {
                    
                    NodeType.PartList.Part pp = pi.next();
                    
                    log.debug( "pp=" + pp );

                    NodeType partNode =  pp.getNode();
                    String partNs = partNode.getNs();
                    String partAc = partNode.getAc();

                    if( partNs == null || partNs.equals("") ||
                        partAc == null || partAc.equals("") ) continue;

                    if( method != null 
                        && method.equalsIgnoreCase( "nsac-query" ) ){
                        
                        // if method nsac-query pass as ns:ac to getQuery

                        log.info( "nsac-query: " + partNs + ":" + partAc );
                            
                        List<NodeType> partQuery 
                            = ds.getDxfQuery( qtype, partNs + ":" + partAc,
                                              qdetail );


                        // replace part by partQuery parts;
                        
                        if( partQuery == null ){
                            log.debug( "part query: null");
                        } else {
                            log.debug( "part query size= " + partQuery.size() );
                        }
                        if( partQuery == null 
                            || partQuery.size() == 0 ) continue;
                                
                        if( pmerge.equalsIgnoreCase( "replace" ) ){

                            // NOTE: de-duplicating  parts/nodes by ns:ac;
                            log.debug( "replacing...");

                            for( Iterator npi = partQuery.get(0).getPartList()
                                     .getPart().iterator(); npi.hasNext(); ){

                                NodeType.PartList.Part np 
                                    = (NodeType.PartList.Part) npi.next();
                                
                                NodeType pnode = np.getNode();
                                
                                String key = np.getNode().getNs() + ":" +
                                    np.getNode().getAc();
                                
                                if( npm.get( key ) == null ){
                                    npm.put(key, key );
                                    npl.getPart().add( np );
                                }
                            }
                        }    
                        continue;
                    }

                    if( method != null
                        && method.equalsIgnoreCase( "nsac-node" ) ){

                        // get matching node

                        NodeType npn = ds.getDxfNode( partNs, partAc, qdetail );

                        if( npn != null ){

                            // NOTE: de-duplicating  parts/nodes by ns:ac;
                            
                            String key = npn.getNs() + ":" + npn.getAc();
                            
                            if( npm.get( key ) == null ){
                                npm.put(key, key );

                                pp.setNode( npn );
                                log.debug( "replacing...");
                                npl.getPart().add( pp );
                                
                            }
                        }
                    }
                }
                
                if( npl.getPart().size() > 0 ){
                    result.get( 0 ).setPartList( npl );
                }
            }
        }  

        return result;

        /*
        String sresult = restQuery( query ); 
        
        if( sresult == null ) return null;
        
        log.debug( "DxfChainService: sresult:\n" + sresult );
        
        List<NodeType> result = new ArrayList<NodeType>();
        
        //transform into DOM
        //------------------
        
        StreamSource ssNative = 
            new StreamSource( new StringReader( sresult ) );
        
        try{
            JAXBContext jc = JAXBContext.newInstance( "edu.ucla.mbi.dxf14" );
            JAXBResult jaxbResult = new JAXBResult( jc );
        
            // should synchronize here
        
            transformer.reset();
            transformer.clearParameters();
            
            transformer.setParameter( "query-sequence", query );
            transformer.transform( ssNative, jaxbResult );

            DatasetType dts = (DatasetType) 
                ((JAXBElement) jaxbResult.getResult()).getValue();
            log.debug( " XsltTransformer: dataset=" + dts );            
            
            Marshaller m = jc.createMarshaller();
            m.marshal( jaxbResult.getResult(), System.out );
                        
            log.debug("\n\n\n");
            
            result.addAll( dts.getNode() );
            
        }catch( Exception ex) {
            log.error(ex);
            ex.printStackTrace();
        }
   
        return result;
        */
    }

    //--------------------------------------------------------------------------

    public List<NodeType> getDxfMeta( String ns, String accession, 
                                      String tag ) {
            
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "DipDbSoap(getDxfMeta): " 
                  + ns + " :: " + accession + " :: " +tag );
        /*
        try {
            return port.getCounts( ns, accession, "" , "", "" );  
        } catch( Exception ex ) {
            log.info( "DipDbSoap: exception=" + ex.toString() );
        }
        */
        return new ArrayList<NodeType>();
    }   

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
}
