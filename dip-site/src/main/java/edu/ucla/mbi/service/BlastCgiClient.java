package edu.ucla.mbi.service;

/* =============================================================================
 * $Id:: BlastCgiClient.java 3221 2013-06-10 21:31:20Z lukasz                  $
 * Version: $Rev:: 3221                                                        $
 *==============================================================================
 *                                                                             $
 * BlastCgiClient - connects to blast.cgi service                              $
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

import org.apache.http.impl.client.*;

import java.util.*;
import java.io.*;

import javax.xml.namespace.QName;
import java.net.URL;

import javax.xml.bind.*;
import javax.xml.ws.BindingProvider;

import edu.ucla.mbi.dxf14.*;
import edu.ucla.mbi.proxy.dip.*;
import edu.ucla.mbi.services.dip.*;
import edu.ucla.mbi.services.dip.direct.*;
import edu.ucla.mbi.legacy.dip.*;


import org.json.*;

public class BlastCgiClient  {
        
    private edu.ucla.mbi.dxf14.ObjectFactory
	dxo = new edu.ucla.mbi.dxf14.ObjectFactory();
    
    String endpoint = "http://dip.doe-mbi.ucla.edu/dip/blast.cgi";

    public void setEndpoint( String endpoint ) {
        this.endpoint = endpoint;
    }
    
    String database = "nr";
    
    public void setDatabase( String database ) {
        this.database = database;
    }

    int maxhits = 100;

    public void setMaxhits( int maxhits ) {
        this.maxhits = maxhits;
    }

    double evalue = 1.0e-10;

    public void setEvalue( double evalue ) {
        this.evalue = evalue;
    }
    
    public String query( String seq ){
        
        Log log = LogFactory.getLog( this.getClass() );
        
        String result = null;
        
        String result_id = null;
        String result_url = null;
        String result_status = null;
        
        DefaultHttpClient httpclient = new DefaultHttpClient();            
        HttpPost httpPost = new HttpPost( endpoint );
        
        try {
            
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add( new BasicNameValuePair( "database", database ));
            nvps.add( new BasicNameValuePair( "sequence", seq ));
            nvps.add( new BasicNameValuePair( "maxhits", 
                                              Integer.toString( maxhits )));
            nvps.add( new BasicNameValuePair( "evalue", 
                                              Double.toString( evalue )));

            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            
            HttpResponse response1 = httpclient.execute( httpPost );
            
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            
            InputStreamReader isr =
                new InputStreamReader( entity1.getContent() );
            BufferedReader br = new BufferedReader( isr );
            
            StringBuffer sb = new StringBuffer();
            String line = null;
            
            boolean xskip = true;
            boolean dskip = true;
            
            while ( (line = br.readLine()) != null ) {

                if( xskip ){
                    if( line.indexOf( "<?xml version=\"1.0\"?>" ) >= 0 ){
                        line = line.replace( "<?xml version=\"1.0\"?>","");
                        xskip = false;
                    }
                }
                if( dskip ){
                    if( line.indexOf( "<!DOCTYPE" ) >= 0 ){
                        line = line.replace( "<!DOCTYPE BlastOutput PUBLIC \"-//NCBI//NCBI BlastOutput/EN\" \"NCBI_BlastOutput.dtd\">","");  
                        dskip = false;
                    }
                }
                sb.append(line);
            }
            
            EntityUtils.consume( entity1 );
            
            result = sb.toString();
            log.info( "BlastCgiClient: exception=" + result );
        } catch( Exception ex ) {
            log.info( "BlastCgiClient: exception=" + ex.toString() );
        } finally {
            httpPost.releaseConnection();
        }
        
        return result;
    }
}
