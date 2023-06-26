package edu.ucla.mbi.client;

/*==============================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-api-ws#$           
 * $Id:: DipTrimURL.java 2117 2012-02-02 01:42:31Z wyu                         $
 * Version: $Rev:: 2117                                                        $
 *==============================================================================
 *
 * DipTrimURL:  
 *
 *============================================================================*/

public class DipTrimURL {

    public static String trim ( String serviceURL ) {

        serviceURL = serviceURL.replaceAll( "^\\s*", "" );
        serviceURL = serviceURL.replaceAll( "\\s*$", "" );

        return serviceURL;

    }

}
