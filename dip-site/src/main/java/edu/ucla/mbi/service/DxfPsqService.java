package edu.ucla.mbi.service;

/* =============================================================================
 * $Id:: DipDbSoapService.java 2189 2012-04-21 17:59:45Z lukasz                $
 * Version: $Rev:: 2189                                                        $
 *==============================================================================
 *                                                                             $
 * DxfPsqService - DXF access to psicquic-based services                       $
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
import org.apache.http.client.utils.*;




import org.apache.http.impl.client.*;

import java.util.*;
import java.io.*;

import org.w3c.dom.*;
import javax.xml.parsers.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.URIResolver;

import javax.xml.bind.*;
import javax.xml.bind.util.JAXBResult;

import edu.ucla.mbi.dxf14.*;

import javax.xml.namespace.QName;
import java.net.URL;

import javax.xml.bind.*;
import javax.xml.ws.BindingProvider;

import edu.ucla.mbi.dxf14.*;
import edu.ucla.mbi.proxy.dip.*;
import edu.ucla.mbi.services.dip.*;
import edu.ucla.mbi.services.dip.direct.*;
import edu.ucla.mbi.legacy.dip.*;

public class DxfPsqService implements DxfService {
        
    private edu.ucla.mbi.dxf14.ObjectFactory
	dxo = new edu.ucla.mbi.dxf14.ObjectFactory();

    private Log log;
    
    String endpoint = null;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint( String endpoint ) {
        this.endpoint = endpoint;
    }

    String xslt = null;
    
    public void setXslt( String xslt ){
        this.xslt = xslt;
    }
    
    Transformer transformer = null;


    DxfService proxyService;
    
    public void setProxyService( DxfService proxyService ){
        this.proxyService = proxyService;
    }

    //--------------------------------------------------------------------------
    // initialize
    //-----------

    public void initialize() {
        
        log = LogFactory.getLog( this.getClass() );
        log.info( "DxfPsqService: initializing" );
        log.info( "DxfPsqService: endpoint=" + endpoint );
        
        // transformation
        //---------------
        
        log.info( "XSLT=" + xslt );

        File xslFile = new File( xslt );
        InputStream xslIStr = null;

        log.info("XSLT=" + xslt);

        try{
            if( !xslFile.canRead() ){
                xslIStr = this.getClass().getClassLoader()
                    .getResourceAsStream( xslt );
            } else {
                xslIStr = new FileInputStream( xslt );
            }
        
            log.info("XSLT Stream=" +  xslIStr );
            
            DocumentBuilderFactory
                dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware( true );
                        
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xslDoc = db.parse( xslIStr );
            
            DOMSource xslDomSource = new DOMSource( xslDoc );
            TransformerFactory tFactory = TransformerFactory.newInstance();
            
            URIResolver defURIRes = tFactory.getURIResolver();
            transformer = tFactory.newTransformer( xslDomSource );
    
        }catch( Exception fx ){
            log.error(fx);
        }
    }


    //--------------------------------------------------------------------------
    // constructor
    //------------
    
    public DxfPsqService(){
        
        Log log = LogFactory.getLog( this.getClass() );
	log.info( "DipSoapDbService: creating service" );
 
    }
   
    //--------------------------------------------------------------------------
    // record retrieval
    //-----------------
    
    public List<NodeType> getRecord( String accession, String detail ) {
        
	return new ArrayList<NodeType>(); // throw exception
    }

    public List<NodeType> getLink(String accession, String detail){
	
	return new ArrayList<NodeType>();
    }

    public List<NodeType> getEvidence(String accession, String detail){
	
	return new ArrayList<NodeType>();
    }

    public List<NodeType> getNode(String accession, String detail){
	
	return new ArrayList<NodeType>();	
    }
    
    public List<NodeType> getSource(String accession, String detail){
		
	return new ArrayList<NodeType>();
    }


    //--------------------------------------------------------------------------
    // record list retrieval
    //----------------------
    
    public List<XrefType> getList( String listKey ) {
        return getList( listKey, "" );
    }
    
    public List<XrefType> getList( String listKey, String match ) {
        
	Log log = LogFactory.getLog( this.getClass() );
        log.debug( "DipSoap: getList key=" + listKey + " match=" + match );

	return new ArrayList<XrefType>();
    }


    //--------------------------------------------------------------------------
    // DxfService interface implementation
    //------------------------------------

    public NodeType getDxfNode( String ns, String accession, 
                                String detail ) {
	//RequestType rq=request("dip",accession,"",detail,"");
	Log log = LogFactory.getLog( this.getClass() );
        log.info("DipSoap: getDxfNode ac="+accession+" tag="+detail);

 	return null;	
    }

    public List<XrefType> getDxfRefList( String ns, String accession, 
                                         String tag ) {
	return getList( accession + "_" + tag );
    }


    public List<XrefType> getDxfRefList( String ns, String accession, 
                                         String tag, String match ) {

	return getList( accession + "_" + tag, match );
    }
    
    public List<NodeType> getDxfNodeList( String ns, String accession, 
                                          String detail ) {
	
        // NOTE: not implemented

	return new ArrayList<NodeType>();
    }

    public List<NodeType> getDxfNodeList( String ns, String accession, 
                                          String detail, String match ) {
	
	// NOTE: not implemented

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "nodelist match= "  +  match );

	
	return new ArrayList<NodeType>();
    }
    
    //--------------------------------------------------------------------------

    public List<NodeType> getDxfQuery( String ns, String query, 
                                       String detail ) {
	
        log.debug( "ns=" + ns + " query=" + query + " detail=" + detail );
        
        // MIQLX
        //------
        
        Map<String,List<String>> miqlx = null;
        String pquery = query;
        if( query != null && query.indexOf( " Miqlx" ) > -1 ){
            MiqlxFilter mf = new MiqlxFilter();
            pquery = mf.process( query );
            miqlx = mf.getMiqlx();
        }
        
        String sresult = restQuery( query ); 
        
        if( sresult == null ) return null;
        
        if( sresult != null){
            //log.debug( "DxfPsqService: sresult:\n" + sresult.substring(0,64) );
            log.debug( "DxfPsqService: sresult:\n" + sresult );
        }
        List<NodeType> result = new ArrayList<NodeType>();
        
        //transform into DOM
        //------------------
        
        StreamSource ssNative = 
            new StreamSource( new StringReader( sresult ) );
        
        try{
            JAXBContext jc = JAXBContext.newInstance( "edu.ucla.mbi.dxf14" );
            JAXBResult jaxbResult = new JAXBResult( jc );
        
            // should synchronize here
        
            transformer.reset();
            transformer.clearParameters();
            
            transformer.setParameter( "text-query-string", pquery );
            
            
            if( miqlx != null && miqlx.get( "MiqlxGroupBy:" ) != null ){
                transformer.setParameter( "text-query-facet", 
                                          miqlx.get( "MiqlxGroupBy:" ).get(0) );
            }

            transformer.transform( ssNative, jaxbResult );

            DatasetType dts = (DatasetType) 
                ((JAXBElement) jaxbResult.getResult()).getValue();
            log.debug( " XsltTransformer: dataset=" + dts + "\n");            
            
            Marshaller m = jc.createMarshaller();
            m.marshal( jaxbResult.getResult(), System.out );
                        
            log.debug("\n\n\n");
            if( miqlx != null){
                result.add( postProc( dts.getNode().get(1) ) );
            } else {
                result.addAll( dts.getNode() );
            }
        }catch( Exception ex) {
            log.error(ex);
            ex.printStackTrace();
        }
   
        return result;
    }

    //--------------------------------------------------------------------------

    public List<NodeType> getDxfMeta( String ns, String accession, 
                                      String tag ) {
            
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "DipDbSoap(getDxfMeta): " 
                  + ns + " :: " + accession + " :: " +tag );
        /*
        try {
            return port.getCounts( ns, accession, "" , "", "" );  
        } catch( Exception ex ) {
            log.info( "DipDbSoap: exception=" + ex.toString() );
        }
        */
        return new ArrayList<NodeType>();
    }   


    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    private String restQuery( String query ){
        
        DefaultHttpClient httpclient = new DefaultHttpClient();            
        String result = null;
        
        HttpGet httpGet = null;

        try {  

            URIBuilder urienc = new URIBuilder();
            urienc.setPath( "/query/" + query );
            String eq = urienc.toString();
            

            log.info("URI(eq):" + eq);


            URIBuilder urib = new URIBuilder( endpoint + eq); // "/query/" + query );

            urib.setParameter("format","xml25");

            httpGet = new HttpGet( urib.build() );

            log.info("URI:" +  urib. toString() );


            HttpResponse response1 = httpclient.execute( httpGet );
            
            log.debug( response1.getStatusLine() );
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
                sb.append( line );
            }
            
            EntityUtils.consume( entity1 );
            
            result = sb.toString()+"\n";
           
            if( result != null) {
                log.debug( "DxfpsqClient: result:\n" + result.substring(0,64) + "\n\n" );
                //log.debug( "DxfpsqClient: result:\n" + result + "\n\n" );

            }
        } catch( Exception ex ) {            
            log.info( "MotifCgiClient: exception=" + ex.toString() );
        } finally {
            httpGet.releaseConnection();
        }
        
        return result;
    
    }

    //--------------------------------------------------------------------------

    private NodeType postProc( NodeType node ){

        List<AttrType>  attr = node.getAttrList().getAttr();         
        List<NodeType.PartList.Part>  part = node.getPartList().getPart();         

        String ffld="";

        log.debug("postProc");
        
        for(Iterator<AttrType> ai = attr.iterator(); ai.hasNext(); ){
            AttrType ca = ai.next();
            if( ca.getName() != null && ca.getName().equals( "facet-field" )){
                ffld = ca.getValue().getValue();
            }
        }
        
        log.debug("postProc: ffld=" + ffld );

        if ( !( ffld.equalsIgnoreCase( "organism" ) ||
                  ffld.equalsIgnoreCase("species") ) ){

            // NOTE: at the moment organism/species processing only
            
            return node;
        }

        for( Iterator<NodeType.PartList.Part> pi = part.iterator(); 
             pi.hasNext(); ){
            
            NodeType.PartList.Part cp = pi.next();
            NodeType pnode = cp.getNode();

            if( pnode.getNs().equals("taxid") ){
                
                // get node from proxy
                //--------------------
                
                NodeType taxon = 
                    proxyService.getDxfNode( "taxid", pnode.getAc(), "stub" );

                // transfer attributes from psq node to pnode
                //-------------------------------------------
                
                if(  pnode.getAttrList() != null ){

                    if( taxon.getAttrList() == null ){
                        taxon.setAttrList(dxo.createNodeTypeAttrList());
                    }
                        
                    taxon.getAttrList().getAttr()
                        .addAll( pnode.getAttrList().getAttr() );
                }
                
                //replace the node
                //----------------   

                cp.setNode( taxon );

            }
        }
/*        



 <node id="0">
  <type ns="dxf" ac="dxf:0063" name="facet-report"/>
  <label>Facet Report</label>
  <attrList>
   <attr name="text-query-string">
    <value/>
   </attr>
   <attr name="facet-field">
    <value/>
   </attr>
  </attrList>
  <partList>
   <part id="0">
    <type ns="dxf" ac="dxf:0027" name="search-result"/>
    <node ns="taxid" ac="562" name="species">
     <type ns="" ac="" name=""/>
     <label>taxid:562</label>
     <attrList>
      <attr name="count">
       <value>3</value>
      </attr>
     </attrList>
    </node>
   </part>
  </partList>
 </node>
*/




        return node;
    }
 
}
