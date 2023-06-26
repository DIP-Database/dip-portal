package edu.ucla.mbi.portal.struts.action;

/* =============================================================================
 * $Id:: TableViewSupport.java 2887 2013-01-02 16:53:44Z lukasz                $
 * Version: $Rev:: 2887                                                        $
 *==============================================================================
 *                                                                             $
 * TableViewSupport                                                            $
 *                                                                             $
 *     TO DO:                                                                  $
 *                                                                             $
 *=========================================================================== */
                                                                           
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucla.mbi.dxf14.*;
import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.context.*;
import edu.ucla.mbi.util.struts.action.*;
import edu.ucla.mbi.util.struts.interceptor.*;

import edu.ucla.mbi.service.RecordModel;

import javax.servlet.ServletContext;

public abstract class TableViewSupport extends PortalSupport 
    implements DataAware, TableViewAware, ExportAware {
    
    public abstract String execute() throws Exception;
    public abstract String buildData() throws Exception;
    public abstract String buildKnownData() throws Exception;
    public abstract String getCounts() throws Exception;


    //--------------------------------------------------------------------------
    // initialize
    //-----------

    public void initialize(){
        initialize( false );
    }

    public void initialize( boolean force ){
        
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "initializing: ActionContext=" + getContext() );

        // initialize tableContext
        //------------------------
        
        if( getTableContext() != null 
            && ( getTableContext().getTables() == null || force ) ){   
            
            // process json configuration
            //---------------------------

            if( getTableContext() instanceof TableViewJsonContext ){
                
                TableViewJsonContext tvgc 
                    = (TableViewJsonContext) this.getTableContext();
                
                String jsonPath = (String) tvgc.getJsonContext()
                    .getConfig().get( "json-config" );
                
                log.info( " JsonTableDef=" + jsonPath );

                if ( jsonPath != null && jsonPath.length() > 0 ) {

                    String cpath = jsonPath.replaceAll("^\\s+","" );
                    cpath = jsonPath.replaceAll("\\s+$","" );

                    try {

                        ServletContext sc = getServletContext();
                        log.info( " ServletContext sc=" + sc );
                        InputStream is =
                            getServletContext().getResourceAsStream( cpath );

                        log.info( " JsonTableDef stream=" + is );

                        tvgc.jsonInitialize( is );
                        
                    } catch ( Exception e ){
                        e.printStackTrace();
                        log.info( "JsonConfig reading error" );
                    }
                    
                    tvgc.initialize();
                }                
            }
        }
    }

    //--------------------------------------------------------------------------
    // dispatcher 
    //-----------
    
    public String dispatch() throws Exception {
        
        if ( getMd() != null ) {
            // DEPRECIATED
            setJqModelList( getModel().getModelLabelList( getMd() ) );
            //setModelList( getModel().getModelLabelList( getMd() ) );
            setModelList( getModel().getModelList( getMd() ) );
            
            String viewType =
                getModel().getModelViewType( getMd(), getSl() );
            String viewName =
                getModel().getModelViewName( getMd(), getSl() );
            
            setViewType( getModel().getModelViewType( getMd(), 
                                                      getSl() ) );
            setViewDef( getTableContext().getViewInfo( viewType, 
                                                       viewName ) );
        }
        
        if ( getRet() == null || getRet().equals( "view" ) ) {
            
            // NOTE: possibly trigger agent(s) here
            //-------------------------------------
            
            buildData();
            return SUCCESS;
            
        } else if ( getRet().equals( "modellist" ) ) {
            return "json";
        } else if ( getRet().equals( "model" ) ) {

            // prepare and return grid layout info
            //------------------------------------

            String viewType = 
                getModel().getModelViewType( getMd(), getSl() );
            String viewName = 
                getModel().getModelViewName( getMd(), getSl() );
            
            setJqModelView( getTableContext().getViewInfo( viewType, 
                                                           viewName ) );
            
            return "json";
        } else if ( getRet().equals("counts") ) {    
            return getCounts();
        } else if( getRet().equals( "data" ) ) { 
            
            // prepare and return data (page)
            //-------------------------------
            
            return buildData();

        } else if( getRet().equals( "values" ) ) {
            return buildKnownData();
        }
        return SUCCESS;
    }


    //--------------------------------------------------------------------------
    // DataAware interface implementation
    //-----------------------------------
    
    private NodeType summary;
    
    public NodeType getSummary(){
        return summary;
    }
    
    public void setSummary( NodeType summary ){
        this.summary = summary; 
    }
    
    //--------------------------------------------------------------------------
    
    private List<NodeType> detail;
    
    public List<NodeType> getDetail(){
        return this.detail;
    }
    
    public void setDetail( List<NodeType> detail ){
        this.detail = detail;
    }

    //--------------------------------------------------------------------------
    
    private List<NodeType> knownDetail;
    
    public List<NodeType> getKnownDetail(){
        if ( knownDetail == null ) {
             this.knownDetail = new ArrayList<NodeType>();
        }
        return this.knownDetail;
    }
    
    public void setKnownDetail( List<NodeType> knownDetail ){
        this.knownDetail = knownDetail;
    }
    

    //---------------------------------------------------------------------
    // TableViewAware interface implementation
    //----------------------------------------

    RecordModel model;

    public void setModel( RecordModel model ) {
        this.model = model;
    }
    
    public RecordModel getModel() {
        return model;
    }
    
    private TableViewContext tableContext;
    
    public void setTableContext( TableViewContext context ){
        this.tableContext = context;
    }
    
    public TableViewContext getTableContext() {
        return tableContext;
    }
    
    //---------------------------------------------------------------------
    
    private String md;
    
    public String getMd(){
        return md;
    }
    
    public void setMd( String model ){
        this.md = model;
    }
    
    //---------------------------------------------------------------------
    
    private int slot;     // alternative to tab 
    
    public int getSl(){
        return slot;
    }
    
    public void setSl( int slot ){
        this.slot = slot;
    }

    public int getTab(){
        return slot;
    }
    
    public void setTab( int tab ){
        this.slot = tab;
    }

    //---------------------------------------------------------------------
    
    private String cvl;
    
    public String getCvl() {
        return cvl;
    }
    
    public void setCvl( String cvl ) {
        this.cvl = cvl;
    }
    
    //---------------------------------------------------------------------
    
    private String rettype; // return type (view/jqmodel/jqdata)
    
    public void setRet( String ret ) {
        this.rettype = ret;
    }
    
    public String getRet() {
        return rettype;
    }
    
    //---------------------------------------------------------------------
    
    private String tableName;
    
    public String getTableName(){
        return tableName;
    }
    
    public void setTableName( String table ) {
        this.tableName = table;
    }
    
    //---------------------------------------------------------------------
    
    private Map tableMeta   = new HashMap();
    
    public Map getTableMeta() {
        return tableMeta;
    }
    
    public void setTableMeta( Map map) {
        this.tableMeta = map;
    }
    
    //---------------------------------------------------------------------
    
    private List tableItems = new ArrayList();
    
    public List getItems() {
        return tableItems;
    }
    
    public void setItems( List items ) {
        this.tableItems = items;
    }
    
    //---------------------------------------------------------------------
    
    private List tableData  = new ArrayList();
    
    public List getTableData() {
        return tableData;
    }
    
    public void setTableData( List items ) {
        this.tableData = items;
    }

    //---------------------------------------------------------------------
    
    private List knownData  = new ArrayList();
    
    public List getKnownData() {
        return knownData;
    }
    
    public void setKnownData( List items ) {
        this.knownData = items;
    }
    
    //---------------------------------------------------------------------
    
    private String itemId = "";
    
    public String getIdentifier(){
        return itemId;
    }
    
    public void setIdentifier( String identifier ){
        itemId=identifier;
    }
        
    //---------------------------------------------------------------------
    
    private String itemLabel = "";
    
    public String getLabel(){
        return itemLabel;
    }
    
    public void setLabel( String label ){
        itemLabel=label;
    }
    
    //---------------------------------------------------------------------
    
    private Map jqModelView;

    public Map getJqModelView() {
        return jqModelView;
    }

    public void setJqModelView( Map model ) {       
        jqModelView = model;
    }

    //--------------------------------------------------------------------

    private List<String> jqModelList;

    public List<String> getJqModelList() {
        return jqModelList;
    }

    public void setJqModelList( List<String> list ) {
        jqModelList = list;
    }

    //--------------------------------------------------------------------

    private List<String> jqModelType;

    public List<String> getJqModelType() {
        return jqModelType;
    }

    public void setJqModelType( List<String> list ) {
        jqModelType = list;
    }

    //--------------------------------------------------------------------------
    
    private List jqCountList;
    public List getJqCountList(){
        return jqCountList;
    }
    
    public void setJqCountList( List items ) {
        jqCountList = items;
    }

    
    //--------------------------------------------------------------------------
    // TableViewAware: library neutral version
    //----------------------------------------

    private int firstRecord;

    public void setFr( int firstRecord ){
        this.firstRecord = firstRecord;
    }
    public int getFr(){
        return firstRecord;
    }

    //--------------------------------------------------------------------------

    int maxRecord;

    public void setMr( int maxRecord ){
        this.maxRecord = maxRecord;
    }
    public int getMr(){
        return maxRecord;
    }

    //--------------------------------------------------------------------------

    int totalRecord;

    public void setTr( int totalRecord ){
        this.totalRecord = totalRecord;
    }
    public int getTr(){
        return totalRecord;
    }

    //--------------------------------------------------------------------------

    private String recordFilter;

    public void setFlt( String filter ) {
        recordFilter = filter;
    }

    public String getFlt() {
        return recordFilter;
    }

    //--------------------------------------------------------------------------

    private String recordSort;

    public void setSrt( String sort ) {
        recordSort = sort;
    }

    public String getSrt() {
        return recordSort;
    }

    //--------------------------------------------------------------------------

    private List<Map<String,String>> modelList;
    
    public List<Map<String,String>> getModelList() {
        if (modelList == null ) {
            modelList = new ArrayList<Map<String,String>>();
        }
        return modelList;
    }

    public void setModelList( List<Map<String,String>> models ) {
        modelList = models;
    }

    //--------------------------------------------------------------------------

    private List<Long> modelCountList;

    public List<Long> getModelCountList(){
        if (modelCountList == null ) {
            modelCountList = new ArrayList<Long>();
        }
        return modelCountList;
    }

    public void setModelCountList( List<Long> counts ) {
        modelCountList = counts;
    }
    
    //--------------------------------------------------------------------------

    String viewType;

    public String getViewType() {
        return viewType;
    }

    public void setViewType( String type ) {
        viewType = type;
    }

    //--------------------------------------------------------------------------

    Map viewDef;
    
    public Map getViewDef() {
        return viewDef;
    }

    public void setViewDef( Map def ) {
        viewDef = def;
    }

    //--------------------------------------------------------------------------

    private Map modelView;

    public Map getModelView() {;
        if ( modelView == null ) {
            modelView = new HashMap();
        }
        return modelView;
    }
    
    public void setModelView( Map model ) {
        modelView = model;
    }

    //--------------------------------------------------------------------------

    private Map modelData;
    
    public Map getModelData() {
        if ( modelData == null ) {
            modelData = new HashMap();
        }
        return modelData;
    }
    
    public void setModelData( Map data ) {
        modelData = data;
    }

    //--------------------------------------------------------------------------

    private Map colValue;
    
    public Map getColValue() {
        if ( colValue == null ) {
            colValue = new HashMap();
        }
        return colValue;
    }
    
    public void setColValue( Map val ) {
        colValue = val;
    }

    
    //--------------------------------------------------------------------------
    // local data:
    //----------------------------------------
    
    // global action configuration - ActionContext

    private ActionContext context;

    public void setContext( ActionContext context ) {
        this.context = context;
    }

    public ActionContext getContext() {
        return this.context;
    }

    //---------------------------------------------------------------------

    private String message;

    public void setMessage( String message ) { //  ???
        this.message = message;
    }
    
    //---------------------------------------------------------------------
    
    // configurable services/tabs

    //private RecordServices recordServices;
    //
    //public void setRecordServices( RecordServices services ) {
    //    this.recordServices = services;
    //}
    
    // pane first/size 
    

    private int firstRec;
    
    public String getFirst() {
        return Integer.toString( firstRec );
    }
    
    public void setFirst( String first ) {
        try{
            int ifirst = Integer.parseInt( first );
            this.firstRec = ifirst;
        } catch( NumberFormatException ne) {
            this.firstRec = 0;
        }
    }
    
    //---------------------------------------------------------------------
    
    private int maxRecs;

    public String getMax() {
        return Integer.toString( maxRecs );
    }
    
    public void setMax( String max ) {
        try{
            int imax = Integer.parseInt( max );
            this.maxRecs = imax;
        } catch( NumberFormatException ne ) {
            this.maxRecs = 10;
        }
    }
    
    //---------------------------------------------------------------------
    
    private String retType;

    public String getRetType() {
        return retType;
    }
    
    public void setRetType( String retType ) {
        if ( retType != null && retType.equals( "json" ) ) {
            this.retType = retType;
        }
    }   
        
    //---------------------------------------------------------------------
    
    private List jqData;
    
    public List getJqData() {
        return this.jqData;
    }
    
    public void setJqData( List items ) {
        this.jqData = items;
    }
    
    //---------------------------------------------------------------------

    private Map jqGridData;
    
    public Map getJqGridData() {
        return jqGridData;
    }

    public void setJqGridData( Map items ) {
        jqGridData = items;
    }

    //---------------------------------------------------------------------

    private List rows;
    
    public List getRows() {
        return rows;
    }

    public void setRows( List items ) {
        this.rows = items;
    }

    //---------------------------------------------------------------------
    
    private int total;

    public void setTotal( int total ) { 
        this.total = total;
    }

    public int getTotal() { 
        return this.total;
    }

    private int filtered;

    public void setFiltered( int filtered ) {
        this.filtered = filtered;
    }

    public int getFiltered(){
        return this.filtered;
    }

    //---------------------------------------------------------------------
    
    private int page;
    
    public void setPage( int page ) { 
        this.page = page;
    }
    
    public int getPage() { 
        return this.page;
    }

    //---------------------------------------------------------------------

    private int records;

    public void setRecords( int records ) { 
        this.records = records;
    }

    public int getRecords() {
        return this.records;
    }

    //---------------------------------------------------------------------
    // ExportAware
    //----------------------------------------

    private String neighbours;

    public void setNb( String neighbours ) {
        this.neighbours = neighbours;
    }

    public String getNb(){
        return neighbours;
    }

    Map exportFormat=null;

    public void setXf( Map format ){
        this.exportFormat = format;
    }
    
    public Map getXf(){
        return this.exportFormat;
    }

    //--------------------------------------------------------------------------
    
    private String export;

    public void setExport( String export ) {
        this.export = export;
    }
    
    public String getExport() {
        return export;
    }
    
    //--------------------------------------------------------------------------

    private String contentType;

    public String getContentType() {
        return contentType;
    }

    public void setContentType( String contentType ) {
        this.contentType = contentType;
    }
    
    //--------------------------------------------------------------------------

    private String contentDisposition;

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition( String contentDisposition ) {
        this.contentDisposition = contentDisposition;
    }
    
    //--------------------------------------------------------------------------

    private InputStream  is;

    public InputStream getExportStream() {
        return is;
    }
    
    public void setExportStream( InputStream is ) {
        this.is = is;
    }    
}
