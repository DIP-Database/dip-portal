package edu.ucla.mbi.dip.transform;

/* =============================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-site/s#$
 * $Id:: DotTransformer.java 2877 2012-12-18 20:42:36Z lukasz                  $
 * Version: $Rev:: 2877                                                        $
 *==============================================================================
 *                                                                             $
 * DotTransformer: generate dot (graphviz) file                                $
 *                                                                             $
 *=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.dom.*;

import java.util.Map;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.servlet.ServletContext;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;

import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.bind.util.JAXBResult;
import javax.xml.bind.JAXBContext;

public class DotTransformer implements NetTransformer{

    private Transformer transformer;

    public void initialize( InputStream isXslt ){

        Log log = LogFactory.getLog( this.getClass() );
        
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware( true );

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xslDoc = db.parse( isXslt );

            DOMSource xslDomSource = new DOMSource( xslDoc );
            TransformerFactory tFactory = TransformerFactory.newInstance();
            
            ErrorListener logErrorListener = new TransformLogErrorListener();
            tFactory.setErrorListener( logErrorListener );

            transformer = tFactory.newTransformer( xslDomSource );
            transformer.setErrorListener( logErrorListener );

        } catch( Exception e ) {
            log.info( e.toString() );
            transformer = null;
        }
    }
    
    public void setParameters( String detail, String ns, String ac) {
	
        Log log = LogFactory.getLog( this.getClass() );
	
        transformer.clearParameters();
        transformer.setParameter( "edu.ucla.mbi.services.detail", detail );
        transformer.setParameter( "edu.ucla.mbi.services.ns", ns );
        transformer.setParameter( "edu.ucla.mbi.services.ac", ac );
    }  

    public void transform( StreamSource xmlStreamSource, 
                           JAXBResult jaxbResult) {
	    
	Log log = LogFactory.getLog( this.getClass() );
	
        try{
	        transformer.transform( xmlStreamSource, jaxbResult );
        }catch( Exception e ){
            log.info( "Transformation error=" + e.toString() );
            // NOTE: should throw exception/fault
        }
    }

    public Object transform( Object in, Map parMap ){
        
        Log log = LogFactory.getLog( this.getClass() );
        log.debug( "Dot Transformation performed" );
        
        return in;
    }

    //--------------------------------------------------------------------------

    public Object transform( ServletContext sc, Object in, 
                             Map parMap, Map<String,InputStream> fisMap ){
        
        Log log = LogFactory.getLog( this.getClass() );

        initialize( fisMap.get( "xslt" ));
        
        try{
            
            ByteArrayInputStream bisIn =
                new ByteArrayInputStream( ((String) in).getBytes( "UTF-8" ) );
            StreamSource ssIn = new StreamSource( bisIn );

            ByteArrayOutputStream bosOut = new ByteArrayOutputStream();
            StreamResult srOut = new StreamResult( bosOut );
            
            //pTrans.setParameters( detail, ns, ac );
            transformer.transform( ssIn, srOut );
            
            return bosOut.toString();
            
        }catch( Exception e ){
            log.info( "Transformation error=" + e.toString() );
            // NOTE: should throw exception/fault
        }
   
        return in;
    }
}
