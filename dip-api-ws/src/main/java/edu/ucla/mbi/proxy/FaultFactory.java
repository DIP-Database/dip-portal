package edu.ucla.mbi.proxy;

/*===========================================================================
 * $HeadURL:: https://lukasz@imex.mbi.ucla.edu/svn/dip-ws/branches/yui-test#$
 * $Id:: FaultFactory.java 508 2009-09-02 17:35:27Z lukasz                  $
 * Version: $Rev:: 508                                                      $
 *===========================================================================
 *
 * FaultFactory:
 *
 *========================================================================= */

import org.w3c.dom.*;

import edu.ucla.mbi.services.Fault;
import edu.ucla.mbi.services.ServiceFault;

//import edu.ucla.mbi.fault.Fault;
//import edu.ucla.mbi.fault.ServiceFault;

public class FaultFactory {

    private static final String MESSAGE = "ProxyFault";
    
    public static ProxyFault newInstance( int code ) {
        return new ProxyFault( MESSAGE, Fault.getServiceFault( code ) );
    }

    public static ProxyFault newInstance( ServiceFault fault ) {
        return new ProxyFault( MESSAGE, fault );
    }

    public static String getCode( Element detail ) { 
        return detail.getElementsByTagName("faultCode").item( 0 ).toString();  
    }

    public static String getMessage( Element detail ) { 
        return detail.getElementsByTagName("message").item( 0 ).toString();  
    }

}
