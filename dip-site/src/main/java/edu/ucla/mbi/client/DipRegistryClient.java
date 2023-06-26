package edu.ucla.mbi.client;

/*==============================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-api-ws#$           
 * $Id:: DipRegistryClient.java 2113 2012-02-02 01:38:47Z wyu                  $
 * Version: $Rev:: 2113                                                        $
 *==============================================================================
 *
 * DipRegistryClient: function used to get Dip AC from DipRegistryClient 
 *
 *============================================================================*/

import edu.ucla.mbi.dxf14.*;
import edu.ucla.mbi.services.dip.registry.*;

import javax.xml.ws.BindingProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DipRegistryClient {

    private static String dipRegistryLocation;

    public DipRegistryClient( String dipRegistryLocation ) {
        initialize ( dipRegistryLocation );
    }

    public static void initialize ( String dipRegistryLocationIn ) {
        dipRegistryLocation = DipTrimURL.trim( dipRegistryLocationIn );
    }

    public static String getDipAC( String keyspace ) {

        Log log = LogFactory.getLog(DipRegistryClient.class);

        log.info( "DipRegistryClient: registry = " + dipRegistryLocation );
        log.info( "DipRegistryClient: keyspace = " + keyspace );


        DipKeyassignerService service = new DipKeyassignerService();


        DipKeyassignerPort port = service.getPublic();

        ( ( BindingProvider )port ).getRequestContext()
                    .put( BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                          dipRegistryLocation);

        log.info( "DipRegistryClient: keyassignerService=" + service 
                  + " port=" + port);

        edu.ucla.mbi.services.dip.registry.ObjectFactory regOF =
                        new edu.ucla.mbi.services.dip.registry.ObjectFactory();

        GetNewKey newKeyKeyspace = regOF.createGetNewKey();
        newKeyKeyspace.setKeyspace( keyspace );

        String accession = port.getNewKey( newKeyKeyspace );

        return accession;
    }
}

