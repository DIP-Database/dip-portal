package edu.ucla.mbi.service;

/* =============================================================================
 * $Id:: BlasterClient.java 3177 2013-05-24 14:30:06Z lukasz                   $
 * Version: $Rev:: 3177                                                        $
 *==============================================================================
 *                                                                             $
 * BlasterClient - connects to MBI cluster blast service                       $
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

public class BlasterClient  {
        
    private edu.ucla.mbi.dxf14.ObjectFactory
	dxo = new edu.ucla.mbi.dxf14.ObjectFactory();
    
    String endpoint = "http://services.mbi.ucla.edu/blaster";
   

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

                
    int maxretry = 10;
    int sleep = 5;

    public String query( String seq ){

        Log log = LogFactory.getLog( this.getClass() );
        
        String result = null;
        
        String result_id = null;
        String result_url = null;
        String result_status = null;
        
        DefaultHttpClient httpclient = new DefaultHttpClient();
        
        try {
            
            HttpPost httpPost = 
                new HttpPost( "http://services.mbi.ucla.edu/blaster/submit" );
            
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("database", database));
            nvps.add(new BasicNameValuePair("sequence", seq));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            HttpResponse response2 = httpclient.execute( httpPost );
            
            try {
                log.info( response2.getStatusLine() );
                HttpEntity entity2 = response2.getEntity();
                
                InputStreamReader isr = 
                    new InputStreamReader( entity2.getContent() );
                BufferedReader br = new BufferedReader( isr );
                
                StringBuffer sb = new StringBuffer();
                String line = null;
                
                while ( (line = br.readLine()) != null ) {
                    sb.append(line);
                }
                //log.info( sb.toString() );
                  
                //{"time":"2013-05-21 12:42:37",
                // "status":"queued",
                // "id":"5e15dbce1b9c0a237fae3f3cbdab0915",
                // "result_url":"http:\/\/services.mbi.ucla.edu\/blaster\/result\/5e15dbce1b9c0a237fae3f3cbdab0915"
                //}

                try{ 
                    JSONObject jo = new JSONObject( sb.toString() );
                    result_id = jo.getString( "id" );
                    result_url = jo.getString( "result_url" );
                    result_status = jo.getString( "status" );
                    
                } catch( Exception ex ){
                    ex.printStackTrace();
                }
                
                EntityUtils.consume( entity2 );
               
            } finally {
                httpPost.releaseConnection();
            }
            
            int retry = maxretry;
            
            while( result_url != null && result == null && retry > 0 ){
                
                HttpGet httpGet = new HttpGet( result_url );
                
                try {
                    HttpResponse response1 = httpclient.execute( httpGet );
                    
                    System.out.println(response1.getStatusLine());
                    HttpEntity entity1 = response1.getEntity();
                
                    InputStreamReader isr =
                        new InputStreamReader( entity1.getContent() );
                    BufferedReader br = new BufferedReader( isr );
                    
                    StringBuffer sb = new StringBuffer();
                    String line = null;
                    
                    while ( (line = br.readLine()) != null ) {
                        sb.append(line);
                    }
                    
                    EntityUtils.consume( entity1 );
                    
                    JSONObject jo = new JSONObject( sb.toString() );
                    String status = jo.getString( "status" );
                
                    if( status != null && status.equalsIgnoreCase("done") ){;
                        retry = 0;
                        result = jo.getString( "results" );
                        //log.info( result );
                        
                    } else {
                        Thread.sleep( sleep*1000 );
                        retry--;
                    }
                } finally {
                    httpGet.releaseConnection();
                }
            } 
            
        } catch( Exception ex ) {
            log.info( "DipDbSoap: exception=" + ex.toString() );
        }
        
        //{"time":"2013-05-21 15:24:29",
        // "status":"done",
        // "format":"xml",
        // "submitted":"2013-05-21 15:24:08",
        // "processing_time":16,
        // "params":{"database":"nr","sequence":"DAEFRHDSGYEVHHQKLVFFAEDVGSNKGAIIGLMVGGVVIA",
        //           "async":true,"format":"xml","maxhits":100,"evalue":1.0e-10},
        // "results":"....."
        //}
        
        return result;
    }
    
}
