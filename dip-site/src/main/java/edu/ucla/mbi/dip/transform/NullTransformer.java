package edu.ucla.mbi.dip.transform;

/* =============================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-site/s#$
 * $Id:: NullTransformer.java 1810 2011-07-13 18:58:20Z lukasz                 $
 * Version: $Rev:: 1810                                                        $
 *==============================================================================
 *                                                                             $
 * NullTransformer: null transformation                                        $
 *                                                                             $
 *=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.io.InputStream;
import javax.servlet.ServletContext;

public class NullTransformer implements NetTransformer{
    
    public Object transform( Object in, Map param ){

        Log log = LogFactory.getLog( this.getClass() );
        log.debug( "Null Transformation (no fis) performed" );
        return in;
    }

    public Object transform( ServletContext sc, Object in, 
                             Map param, Map<String,InputStream> fisMap){

        Log log = LogFactory.getLog( this.getClass() );
        log.debug( "Null Transformation (fis) performed" );
        return in;
    }
}