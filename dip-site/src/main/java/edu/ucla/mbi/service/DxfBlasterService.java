package edu.ucla.mbi.service;

/* =============================================================================
 * $Id:: DxfBlasterService.java 3177 2013-05-24 14:30:06Z lukasz               $
 * Version: $Rev:: 3177                                                        $
 *==============================================================================
 *                                                                             $
 * DxfBlasterService - blaster service access through standard Dxf interface   $
 *                                                                             $
 *=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;

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

public class DxfBlasterService implements  DxfService{

    Map<String,BlasterClient> bcMap = null;
    
    public void setBcMap( Map<String,BlasterClient> map ){
        this.bcMap = map;
    }

    String xslt = null;

    public void setXslt( String xslt ){
        this.xslt = xslt;
    }
    
    Transformer transformer = null;
    
    public void initialize(){

        Log log = LogFactory.getLog( this.getClass() );

        // transformation
        //---------------

        log.info("XSLT=" + xslt);

        File xslFile = new File( xslt );
        InputStream xslIStr = null;

        log.info("XSLT=" + xslt);

        try{
            if( !xslFile.canRead() ){
                xslIStr = this.getClass().getClassLoader()
                    .getResourceAsStream( xslt );
            } else {
                xslIStr = new FileInputStream( xslt );
            }
        
            log.info("XSLT Stream=" +  xslIStr );
            
            DocumentBuilderFactory
                dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware( true );
            
            

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xslDoc = db.parse( xslIStr );
            
            DOMSource xslDomSource = new DOMSource( xslDoc );
            TransformerFactory tFactory = TransformerFactory.newInstance();
            
            URIResolver defURIRes = tFactory.getURIResolver();
            //URIResolver cpURIRes = new ClassPathURIResolver( defURIRes );
            //tFactory.setURIResolver( cpURIResolver );
            transformer = tFactory.newTransformer( xslDomSource );
    
        }catch( Exception fx ){
            log.error(fx);
        }
    }
    
    // DxfService interfce
    //--------------------

    public NodeType getDxfNode( String ns, String accession, 
                                String tag ){
        return null;
    }

    public List<XrefType> getDxfRefList( String ns, String accession, 
                                         String tag ){

        return null;
    }

    public List<XrefType> getDxfRefList( String ns, String accession, 
                                         String tag, String match ){
        return null;
    }

    public List<NodeType> getDxfNodeList( String ns,String accession, 
                                          String tag){
        return null;
    }
    
    public List<NodeType> getDxfNodeList( String ns, String accession, 
                                          String tag, String match ){
        return null;
    }

    public List<NodeType> getDxfQuery( String db, String sequence, 
                                       String tag ){
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "DB="+ db + " BlasterClient=" + bcMap.get( db ) );
        
        if( bcMap.get(db)  == null ) return null;
        
        BlasterClient bclient = bcMap.get( db );
        String bcresult = bclient.query( sequence ); 
        if( bcresult == null ) return null;

        List<NodeType> result = new ArrayList<NodeType>();
        

        //transform into DOM
        //------------------
        
        StreamSource ssNative = 
            new StreamSource( new StringReader( bcresult ) );


        try{
            JAXBContext jc = JAXBContext.newInstance( "edu.ucla.mbi.dxf14" );
            JAXBResult jaxbResult = new JAXBResult( jc );
        
            // should synchronize here
        
            transformer.reset();
            transformer.clearParameters();
            
            transformer.setParameter( "sequence", sequence );
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
        }

        return result;
    }

    public List<NodeType> getDxfMeta( String ns, String accession, 
                                      String tag ){
        return null;
    }
}


