package edu.ucla.mbi.dxf14;

/*===========================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-api#$
 * $Id:: DxfJAXBContext.java 85 2009-06-09 23:40:40Z lukasz                 $
 * Version: $Rev:: 85                                                       $
 *===========================================================================
 *
 * DxfJAXBContex:
 *
 *    JAXBContex singleton - use as:
 *
 *      JAXBContext jc = DxfJAXBContext.getDxfContext();
 *
 *========================================================================= */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import edu.ucla.mbi.dxf14.DatasetType;

public class DxfJAXBContext  {
    
    // no instances/subclassing
    
    private DxfJAXBContext() {}

    /* 
     *  generate static instance of JAXBContext
     *  see https://jaxb.dev.java.net/faq/index.html#threadSafety 
     *  for details.
     */
    
    static final JAXBContext dxfContext = initDxfContext();
    
    private static JAXBContext initDxfContext() {
	try {
	    JAXBContext jbx = JAXBContext
		.newInstance( "edu.ucla.mbi.dxf14",
			      DxfJAXBContext.class.getClassLoader() );
	    return jbx;
	    
	} catch( JAXBException jbe ){
	    Log log = LogFactory.getLog( DxfJAXBContext.class );
	    log.info( "Exception="+jbe.toString() );
	}
	return null;	
    }
    
    public static JAXBContext getDxfContext() {
	return dxfContext;
    }

}
