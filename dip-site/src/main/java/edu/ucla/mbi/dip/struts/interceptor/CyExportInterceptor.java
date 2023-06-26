package edu.ucla.mbi.dip.struts.interceptor;

/* ========================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-s#$
 * $Id:: CyExportInterceptor.java 2877 2012-12-18 20:42:36Z lukasz        $
 * Version: $Rev:: 2877                                                   $
 *=========================================================================
 *                                                                        $
 * CyExportInterceptor: generates returnes DXF-formatted data preceded    $
 *  by parameters requred by Cytoscape/MiSink                             $
 *                                                                        $
 *====================================================================== */

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.regex.PatternSyntaxException;

import java.util.*;
import java.io.*;
import javax.xml.bind.*;

import org.json.*;
import edu.ucla.mbi.dxf14.*;

import edu.ucla.mbi.util.struts.interceptor.*;

public class CyExportInterceptor extends ExportInterceptor {
    
    protected String buildExport( ValueStack stack, ExportAware action) {
        
        TableViewContext tblContext = action.getTableContext();
        String tblName = action.getTableName();
        String filter = action.getFlt();
        List tblData = action.getTableData();
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "\n\nCyExportInterceptor: tableName=" + tblName );

        String dxfString = "ADD DXF\n" + 
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
            "<entrySet/>\n";
        
        List tblLayout = tblContext.getLayout( tblName );
        TableFilter tflt = null; 
        if ( filter != null ) {
            tflt = new TableFilter( tblLayout, filter );
        }

        Map modelData = new HashMap();
        List data = new ArrayList();
        
        if ( tblData != null ) {
            
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
                    
                    dxfString = "ADD DXF\n" + sw.toString();

                    // remove default namespace info
                    String ns =
                        "xmlns=\"http://dip.doe-mbi.ucla.edu/services/dxf14\"";
                    dxfString =  dxfString.replaceAll( ns, "" );

                }           
            } catch ( JAXBException jbe ) {
                // ignore (for now)
            }
        }
        return dxfString;
    }
}
