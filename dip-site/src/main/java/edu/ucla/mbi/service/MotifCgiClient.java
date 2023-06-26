package edu.ucla.mbi.service;

/* =============================================================================
 * $Id:: BlastCgiClient.java 3218 2013-06-07 20:48:22Z lukasz                  $
 * Version: $Rev:: 3218                                                        $
 *==============================================================================
 *                                                                             $
 * MotifCgiClient - connects to motif.cgi service                              $
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

public class MotifCgiClient  {
        
    private edu.ucla.mbi.dxf14.ObjectFactory
	dxo = new edu.ucla.mbi.dxf14.ObjectFactory();
    
    String endpoint = "http://dip.doe-mbi.ucla.edu/dip/motif.cgi";

    public void setEndpoint( String endpoint ) {
        this.endpoint = endpoint;
    }
    
    String database = "dip";
    
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
    
    String queryType ="regex";

    public void setQueryType( String qtype ){
        this.queryType = qtype;
    }
    
    public String query( String query ){
        
        Log log = LogFactory.getLog( this.getClass() );
        
        String result = null;
        
        String result_id = null;
        String result_url = null;
        String result_status = null;

        String motif = null;
        String ns = null;
        String ac = null;

        if( query != null && queryType.equals("regex")){
            motif = query;
        }
        if( query != null && queryType.equals("ns:ac")){
            if( query.indexOf(":") > 0 ){
                ns = query.substring( 0, query.indexOf(":") );
                ac = query.substring( query.indexOf(":") + 1 );
            }
        }

        log.debug( "motif=" + motif + " ns=" + ns + " ac=" + ac );

        DefaultHttpClient httpclient = new DefaultHttpClient();            
        HttpPost httpPost = new HttpPost( endpoint );
        
        try {
            
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add( new BasicNameValuePair( "database", database ));

            if(motif != null){
                nvps.add( new BasicNameValuePair( "motif", motif ) );
            }
            
            if( ns != null && ac != null){
                nvps.add( new BasicNameValuePair( "ns", ns ) );
                nvps.add( new BasicNameValuePair( "ac", ac ) );
            }
            
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
                /*
                if( xskip ){
                    if( line.indexOf( "<?xml version=\"1.0\"?>" ) >= 0 ){
                        line = line.replace( "<?xml version=\"1.0\"?>","");
                        xskip = false;
                    }
                }
                */
                sb.append(line);
            }
            
            EntityUtils.consume( entity1 );
            
            result = sb.toString()+"\n";
            log.info( "MotifCgiClient: result:\n" + result + "\n" );
        } catch( Exception ex ) {            
            log.info( "MotifCgiClient: exception=" + ex.toString() );
        } finally {
            httpPost.releaseConnection();
        }
        
        return result;
    }
}
