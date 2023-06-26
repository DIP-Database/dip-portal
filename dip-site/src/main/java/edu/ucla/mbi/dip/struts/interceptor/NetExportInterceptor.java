package edu.ucla.mbi.dip.struts.interceptor;

/* =============================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-site/s#$
 * $Id:: NetExportInterceptor.java 2877 2012-12-18 20:42:36Z lukasz            $
 * Version: $Rev:: 2877                                                        $
 *==============================================================================
 *                                                                             $
 * NetExportInterceptor: generates/returns interaction networks in a variety   $
 *  of formats, including:                                                     $
 *       - DXF (preceeded by parameters requred by Cytoscape/MiSink)           $
 *       - graphviz (http://www.graphviz.org) dot format                       $
 *       - ...                                                                 $
 *                                                                             $
 *=========================================================================== */

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

import org.apache.struts2.util.ServletContextAware;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.regex.PatternSyntaxException;

import java.util.*;
import java.io.*;

import javax.xml.bind.*;
import javax.servlet.ServletContext;

import org.json.*;
import edu.ucla.mbi.dxf14.*;
import edu.ucla.mbi.dip.transform.*;

import edu.ucla.mbi.util.struts.interceptor.*;
import edu.ucla.mbi.util.struts.action.PortalSupport;

public class NetExportInterceptor extends ExportInterceptor {

    public NetExportInterceptor(){}
    
    Map<String,NetTransformer> ntMap= null;

    public void setExportTransformer( Map<String,NetTransformer> map ){
        ntMap = map;
    }

    protected String buildExport( ValueStack stack, ExportAware action) {
        
        TableViewContext tblContext = action.getTableContext();
        String tblName = action.getTableName();
        String filter = action.getFlt();
        List tblData = action.getTableData();
        
        Map xformat = action.getXf();
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "NetExportInterceptor: xformat=" + xformat );

        Map<String,Object> jpd = getExportContext().getJsonConfig();
        
        String prefix ="";
        String contentType = "text/plain; charset=ISO-8859-1";
 
        String ck = null;
        Map xfm = null;
        
        try {
            ck = (String) xformat.keySet().iterator().next();
            Object vs = xformat.get( ck );

            log.info( "ck=" + ck + " vs=" + vs );

            int v = Integer.parseInt( ((String[])vs)[0] ); 

            log.info( "v=" + v);

            xfm = (Map) ((List)((Map)jpd.get("format")).get(ck)).get( v );

            log.info( "xfm map: " + xfm);

            if( xfm.get("prefix") != null ){
                prefix = (String) xfm.get("prefix") +"\n";
            }

            if( xfm.get("content-type") != null ){
                contentType = (String) xfm.get("content-type");
            }
            
        } catch( Exception x ){

            x.printStackTrace();
        };

        log.info( "content-type=" + contentType);
        log.info( "prefix=" + prefix );

        action.setContentType( contentType );        
        
        String dxfString = prefix + 
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
            "<entrySet/>\n";
        
        List tblLayout = tblContext.getLayout( tblName );
        TableFilter tflt = null; 
        if ( filter != null ) {
            tflt = new TableFilter( tblLayout, filter );
        }

        Map modelData = new HashMap();
        List data = new ArrayList();
        
        if ( tblData == null ) {
            if( ck != null && (ck.equals("dxf") || ck.equals("cydxf") ) ){
                return dxfString;
            } else {
                return "";
            }
        }

        // go over items/rows
        // -------------------
            
        for ( int r = 0; r < tblData.size(); r++ ) {
            if ( tflt == null ) {  // no filter 
                data.add( tblData.get(r) );
            } else {               // apply filter
                if ( tflt.match( stack, r ) ) {
                    data.add( tblData.get(r) );
                }
            }
        }            
            
        edu.ucla.mbi.dxf14.ObjectFactory
            dof = new edu.ucla.mbi.dxf14.ObjectFactory();
        
        DatasetType expDoc = dof.createDatasetType();
        
        JAXBContext jc = DxfJAXBContext.getDxfContext();
        
        try {
            
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT,
                                    new Boolean( true ) );
            
            if ( data != null ) {
                expDoc.getNode().addAll( data );
                java.io.StringWriter sw = new StringWriter();
                marshaller.setProperty( Marshaller.JAXB_ENCODING,
                                        "UTF-8" );
                marshaller.marshal( dof.createDataset( expDoc ),
                                    sw );
                
                dxfString = prefix + sw.toString();
                
                // remove default namespace info
                String ns =
                    "xmlns=\"http://dip.doe-mbi.ucla.edu/services/dxf14\"";
                dxfString =  dxfString.replaceAll( ns, "" );
                
            }           
        } catch ( JAXBException jbe ) {
            // ignore (for now)
        }
        
        
        if( ck != null && !ck.equals( "dxf" ) && !ck.equals( "cydxf" ) ){

            log.info( "Transforming: ck = " + ck );
            
            String outString = (String) transform( dxfString, ck, jpd, xfm, 
                                                   action );
            return outString;
        }
        return dxfString;
    }
    
    //--------------------------------------------------------------------------
    
    private Object transform( Object in, String nt, 
                              Map jpd, Map param, ExportAware action ){

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "nt=" + nt);
        
        if( param.get( "pretrans" ) != null ){ 

            String sTr = (String) param.get( "pretrans" );
            String sVr = param.get( "pretrver" ).toString();
            
            int vr = 0;
            try{
                vr = Integer.parseInt( sVr );

                Map pxfm = (Map) ((List)((Map) jpd.get( "format" ))
                                  .get( sTr )).get( vr );
                
                in = transform( in, sTr, jpd, pxfm, action );
                log.info( "pretr out=" + in );
            } catch( Exception ex){
                ex.printStackTrace();
            }
        }
        
        log.info( "nt=" + nt + " ntm=" + ntMap.get( nt ) );

        if( action instanceof PortalSupport ){
            ServletContext sc 
                = ((PortalSupport) action).getServletContext();
                
            if( param.get( "file" ) != null ){
                try{
                
                    log.info( "processing file params");


                    Map fmap = (Map) param.get( "file" );
                    Map<String,InputStream> 
                        fisParam = new HashMap<String,InputStream>();
                    
                    for( Iterator fi = fmap.keySet().iterator(); 
                         fi.hasNext(); ){
                        
                        String cfk = (String) fi.next();
                        String cfp = (String) fmap.get( cfk );
                        
                        log.info( "file:" +  cfk + " = " + cfp);
                        
                        InputStream cfis = sc.getResourceAsStream( cfp );
                        
                        fisParam.put(cfk,cfis);
                    }
                
                    Object out 
                        = ntMap.get( nt ).transform( sc, in, param, fisParam );
                    return out;

                } catch( Exception ex ){
                    ex.printStackTrace();
                }
                return null;
            }
        
            Object out = ntMap.get( nt ).transform( sc, in, param, null );
            return out;
        }
        Object out = ntMap.get( nt ).transform( in, param );
        return out;
    }
}
