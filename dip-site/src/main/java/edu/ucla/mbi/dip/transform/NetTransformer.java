package edu.ucla.mbi.dip.transform;

/* =============================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-site/s#$
 * $Id:: NetTransformer.java 1804 2011-07-10 02:00:51Z lukasz                  $
 * Version: $Rev:: 1804                                                        $
 *==============================================================================
 *                                                                             $
 * NetTransformer: network transformation                                      $
 *                                                                             $
 *=========================================================================== */

import java.util.Map;
import java.io.InputStream;
import javax.servlet.ServletContext;

public interface NetTransformer{
    
    public Object transform( Object in, Map param );
    
    public Object transform( ServletContext sc, Object in, 
                             Map param, Map<String,InputStream> fisMap);

}