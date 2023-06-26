package edu.ucla.mbi.dip.transform;

/*==============================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-site/s#$
 * $Id:: TransformLogErrorListener.java 2877 2012-12-18 20:42:36Z lukasz       $
 * Version: $Rev:: 2877                                                        $
 *==============================================================================
 *                                                                             $
 * TransformLogErrorListener:                                                  $
 *     NOTE: should be moved to dip-util-data ?                                $
 *=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.transform.*;

public class TransformLogErrorListener implements ErrorListener{

    public void warning( TransformerException exception ) {

	Log log = LogFactory.getLog( TransformLogErrorListener.class );
	log.info( "TransformLogErrorListener: warning: " + 
		  exception.getMessage() );
    }
    
    public void error( TransformerException exception )
	throws TransformerException {

	Log log = LogFactory.getLog( TransformLogErrorListener.class );
	log.info( "TransformLogErrorListener: error: " + 
		  exception.getMessage() );
	throw exception;
    }

    public void fatalError( TransformerException exception )
	throws TransformerException {

	Log log = LogFactory.getLog( TransformLogErrorListener.class );
	log.info( "TransformLogErrorListener: fatalError: " + 
		  exception.getMessage() );
	throw exception;
    }
}

