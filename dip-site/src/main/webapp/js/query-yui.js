/* record rendering */

YAHOO.namespace("mbi.view");

YAHOO.mbi.view.query = { 

    TIMEOUT: 60000,
    anim: false,

    /*
    tabupdate was a var inside of init and I was trying to clean up the code but
    I dont know if I just shuffled the code around a little.
     */

    tabUpdate: function( o, query, sl ) {
        
        var dels= [1,1,1,1,1,2,2,2,2,2,5,5,5,5,5,10,10,20,20,20,30,30];
        var i;
        if ( o.argument.start ) {
            i = 0;
        } else {
            i = o.argument.i;
        }

        query = o.argument.query;
        sl = o.argument.sl;

        var tabUpdateCallback = { cache:false, timeout: this.TIMEOUT, 
                                  success: YAHOO.mbi.view.query.tabUpdate,
                                  failure: YAHOO.mbi.view.query.callFail,
                                  argument:{ i: i+1, query:query, sl:sl } };
        
        var call = function() {

            //alert( query + '&sl=' + sl + '&ret=counts');
            YAHOO.util.Connect.asyncRequest( 'GET', query + '&sl=' + sl + 
                                             '&ret=counts', 
                                             tabUpdateCallback ); 
        };
        
        if ( i < dels.length ) {            
            if ( i === 0 ) {
                call();
            } else {

                // parse response
                //---------------

                var messages = YAHOO.lang.JSON.parse( o.responseText );
                var counts = messages.modelCountList;
                var labels = messages.modelList;
                
                var tabs= YAHOO.mbi.view.tabView.get('tabs');
                //alert("tabs="+tabs);
                for( var t=1; t < tabs.length; t++){
                    //alert(tabs.length+ " : "+ t);
                    var ccount = counts[t];
                    var clabel = labels[t-1].label;
                    var ctab = tabs[t];
                    
                    //alert(t+" lbl: "+ctab.get('label'));
                    if ( ccount > 0 ){
                        var ll = clabel +'('+ccount+')';
                        ctab.set('label',ll);
                        ctab.set('disabled', false );
                    } else {
                        if( ctab.my.vtp !== "detail" ){
                            ctab.set('disabled', true );
                        }
                    }
                }
                if(!YAHOO.lang.isUndefined(YAHOO.mbi.view.querySummary) &&
                   messages.modelList[0].label == 'Summary' ){
		       element = YAHOO.util.Dom.getElementsByClassName('rec-tab-body')[0];
		       
                       // NOTE: remove action name dependence ???

		       YAHOO.mbi.view.querySummary.setSummary(
			   query.replace('query?query=',''), 
			   counts.slice(2,counts.length),
			   element);
		}
                
                if ( counts[0] > 0 && i + 1 < dels.length ){
                    if ( ! YAHOO.mbi.view.query.anim ) {
                        YAHOO.mbi.view.query.anim = true;
                        // start animation
                        //----------------
                        var img = YAHOO.util.Dom.get('dlimg');
                        //alert("on:"+img);
                        var ia = new Image();                   
                        ia.src="./images/dla16.gif";
                        img.src = ia.src;
                    }
                    setTimeout( call, dels[i]*1000 );
                } else {
                    //stop animation
                    //--------------
                    
                    var img = YAHOO.util.Dom.get('dlimg');
                    var ia = new Image();                   
                    ia.src="./images/dl16.png";
                    img.src = ia.src;
                    YAHOO.mbi.view.query.anim = false;
                }
            }
        }
    },

    callFail: function( o ) {
        console.log( JSON.stringify( o ));
    },

    tableReload: function( arg ) {
            try {
                var viewManager = YAHOO.mbi.view;     
                var tabIndex =  viewManager.stateManager.recTabGetState().activeTab; 
                var tab = viewManager.tabView.getTab( tabIndex );
                var dt  = tab.my.dataTable;
                
                //var state = dt.get( 'paginator' ).getState();
                // state.page=1;
                //state.recordOffset=0;
                //dt.get('paginator').setState(state);
                
                var reloadCallback = {
                    success: dt.onDataReturnInitializeTable,
                    failure: dt.onDataReturnInitializeTable,
                    scope: dt,
                    argument: dt.getState()
                };
                
                var reloadRequest = 
                    dt.my.requestBuilder( dt.getState(), dt );
                
                dt.getDataSource().sendRequest( reloadRequest, 
                                                reloadCallback );
            } catch (x) {   
                console.log(x);
            }
        },

    /*----------------------
     creates the paginator for the table
     ----------------------*/

    createPaginator: function(pel, template){
        var rowsPerPageList = [5,10,25];
        var rows = 10;
        var state = YAHOO.mbi.view.stateManager.recTabGetState();
        if(typeof state !== "undefined")
        {
            //get rows from url if it matches a value in list
            if(rowsPerPageList.indexOf(state.pageSize) > -1)
                rows = state.pageSize;                
        } 
        YAHOO.mbi.view.query.paginator =  new YAHOO.widget.Paginator(
                { containers: [pel],
                  rowsPerPage: rows,
                  template: template,
                  rowsPerPageOptions: rowsPerPageList,
                  pageLinks: 5
                });

        YAHOO.mbi.view.query.paginator.subscribe( "changeRequest",
                        YAHOO.mbi.view.stateManager.handlePagination );

        return YAHOO.mbi.view.query.paginator;
    },
    

    init:  function( query, sl ) {
        //var anim = false;
      
        //alert( "YAHOO.mbi.view.query: initializing" );
  
        var tabInit = function( o ) {
        
            var messages = YAHOO.lang.JSON.parse( o.responseText ); 
                  
            //alert( "tabInit: arg=" + JSON.stringify(o.argument));

            var arg = o.argument;          

            var modList = messages.modelList;
        
            var query = arg.query;
            var sl = arg.sl;
            
            var summaryPanel=null;
            var summaryBody=null;
        
            // build detail tab (if needed)
            // ----------------------------
        
            if( modList[0]["view-type"] === "detail" 
                && modList[0]["view-as-tile"] === "false" ){
            
                    var rtl = YAHOO.util.Dom.get( "rec-tab-list" );
                    
                    var tli = document.createElement('li');            
                    tli.innerHTML = '<a href="#summary"><em>Summary</em></a>';
                    rtl.appendChild( tli );
                    
                    var rtc = YAHOO.util.Dom.get( "rec-tab-content" );
                    var sdiv = document.createElement( 'div' );
                    
                    var stbl = document.createElement( 'table' );
                    YAHOO.util.Dom.setAttribute( stbl, 'width', '100%' );
                    YAHOO.util.Dom.setAttribute( stbl, 'cellspacing', '0' );
                    
                    var stbr = document.createElement( 'tr' );
                    
                    var stbp = document.createElement( 'td' );
                    YAHOO.util.Dom.addClass( stbp, 'rec-tab-filter-col');           
                    YAHOO.util.Dom.setAttribute( stbp,'valign','top');
                    YAHOO.util.Dom.setAttribute( stbp,'align','left');
                    
                    summaryPanel = YAHOO.util.Dom.generateId( stbp );
                    
                    var stbb = document.createElement('td');
                    YAHOO.util.Dom.addClass( stbb, 'rec-tab-body');            
                    summaryBody = YAHOO.util.Dom.generateId( stbb );
                    
                    sdiv.appendChild(stbl);
                    stbl.appendChild(stbr);
                    stbr.appendChild(stbp);
                    stbr.appendChild(stbb);
                    
                    rtc.appendChild( sdiv );       
                } else {
                    summaryPanel="summary-panel";
                    summaryBody="summary-body";
                }
            
            YAHOO.mbi.view.tabView = new YAHOO.widget.TabView("recTab");
            YAHOO.mbi.view.tabView.my = {};
            
            YAHOO.mbi.view.tabView.my.sumpanid = summaryPanel;
            YAHOO.mbi.view.tabView.my.sumbodid = summaryBody;
            
            // configure refresh tab
            //----------------------
            
            var tab0 = YAHOO.mbi.view.tabView.getTab(0);
            tab0.my = {loaded:true,label:'refresh'};
            tab0.set('disabled',true);
            
            // build tabs
            //-----------
            
            for (var i = 0, len = modList.length; i < len; ++i) { 
                var label = modList[i].label; 
                var pnl   = modList[i].panel;  
                var exprt = modList[i].exprt;  
                var vtype = modList[i]["view-type"];  
                var vdef  = modList[i]["view-def"]; 
                
                var active = (i===0 ? true : false);
                
                if (i === 0 && vtype ==="detail") {
                    
                    var tab = YAHOO.mbi.view.tabView.getTab( 1 );
                    YAHOO.mbi.view.tabView.selectTab( 1 );
                    
                    tab.my = { loaded:true, label:label, vtp:vtype, vdef: vdef, 
                               pstat: messages.modelList[0].panel, estat: exprt };
                    tab.set( 'label', label );
                    
                    var spe = document
                        .getElementById( YAHOO.mbi.view.tabView.my.sumpanid );
                    
                    if (  modList[0].panel !== 'none' ) {
                        
                        tab.my = {
                            loaded:false, label:label, tn:i, tab:tab, vtp:vtype,
                            tv:YAHOO.mbi.view.tabView, arg:arg };
                        
                        YAHOO.mbi.view.panel.build( tab, spe );                        
                        
                        var cytbdy = document.createElement( 'div' );
                        tab.my.cbody = YAHOO.util.Dom.generateId( cytbdy );
                        YAHOO.util.Dom.addClass( cytbdy, 'rec-tab-cyto-body');
                        
                        if ( exprt === 'single' ) {                    
                            cytbdy = YAHOO.mbi.view.cyt.buildSimple( cytbdy, tab );
                            tab.my.pbot.appendChild(cytbdy);
                            
                        } 
                        if ( exprt === 'multi' ) {  
                            cytbdy = YAHOO.mbi.view.cyt.build( cytbdy, tab );
                            tab.my.pbot.appendChild(cytbdy);
                        }
                    }
                    if ( pnl === 'on' ) {
                        YAHOO.util.Dom.replaceClass( spe,
                                                     'rec-tab-filter-col', 
                                                     'rec-tab-filter' );
                    }
                    
                    if ( pnl == 'off' ) {
                        YAHOO.util.Dom.replaceClass( spe,
                                                     'rec-tab-filter',
                                                     'rec-tab-filter-col' );   
                    } 
                    
                } else {               
                    var tab = new YAHOO.widget.Tab(
                        { label: label, content: '' });
                    tab.my = { loaded:false, label:label, tn:i, tab:tab,
                               pstat: pnl, estat: exprt, vtype:vtype, vdef: vdef,
                               tv:YAHOO.mbi.view.tabView, arg:arg };
                    
                    tab.on( "activeChange", tabLoad );
                    YAHOO.mbi.view.tabView.addTab( tab );
                    
                    var cnt = tab.get('contentEl');
                    
                    var ctab= document.createElement("table");
                    YAHOO.util.Dom.setAttribute( ctab, 'cellspacing', '0');
                    YAHOO.util.Dom.setAttribute( ctab, 'width', '100%');
                    cnt.appendChild( ctab );
                }   
                YAHOO.mbi.view.tabView.selectTab( 1 );
            } 
            
            // start tab updates
            //------------------
            

            YAHOO.mbi.view.query.tabUpdate( { argument:{ start:true, query:query, sl:sl } }, query, sl );   
            
            //this listener is for when a tab is clicked. 
            YAHOO.mbi.view.tabView.on("activeTabChange",
                                      YAHOO.mbi.view.stateManager.handleTabNavigation);
            
            YAHOO.mbi.view.stateManager.historyInit(); 
        };
        
        
        //alert( "init: query=" + query + " sl=" + sl );

        var tabCallback = { cache:false, timeout: 
                            YAHOO.mbi.view.query.TIMEOUT, 
                            success:tabInit,
                            argument:{ query:query, sl:sl } };
        
        YAHOO.util.Connect.asyncRequest( 'GET', query +'&sl='+ sl + 
                                         '&ret=modellist', tabCallback ); 
        
        var tabLoad = function( e ) {
            //alert("tab load:" + this.my.tn);
            var vq = YAHOO.mbi.view.query;
            
            if ( !this.my.loaded ) {
                this.my.loaded = true;      
                var mvCallback = { cache:false, 
                                   timeout: YAHOO.mbi.view.query.TIMEOUT, 
                                   success:mvInit, scope:this };
                YAHOO.util.Connect.asyncRequest( 
                    'GET', this.my.arg.query 
                        + '&sl=' + this.my.tn + 
                        '&ret=model', mvCallback );
            } else {
                try{
                    mvDisplay( {tab: this } );
                } catch (x) {
                    console.log(x);
                }           
            }
        };
        
        var mvInit = function( o ) {
            //alert( o.responseText );
            var ctab = this; 
            
            var messages = YAHOO.lang.JSON.parse( o.responseText );
            var columnModel = messages.modelView.colModel;
            var vdef=ctab.my.vdef;
            
            try{
                var cmp = YAHOO.util.Cookie.get( "cm-" + vdef );
                //alert(vdef + " :: " + cmp);
                
                if( cmp !== undefined && cmp !== null ){
                    columnModel = cmPatch( { columnModel:columnModel, cmp:cmp });
                }
            } catch (x) {
                console.log(x);
            }
            
            var cont = YAHOO.util.Dom.getFirstChild( ctab.get('contentEl') );
            
            this.my.id = YAHOO.util.Dom.generateId( cont );
            var tr = document.createElement("tr");
            cont.appendChild( tr );
            
            ctab.my.panel = document.createElement("td");
            YAHOO.util.Dom.setAttribute( ctab.my.panel, 'valign', 'top');
            YAHOO.util.Dom.setAttribute( ctab.my.panel, 'align', 'left');
            YAHOO.util.Dom.addClass( ctab.my.panel, 'rec-tab-filter-col');
            tr.appendChild( ctab.my.panel );
            
            if ( ctab.my.pstat !== 'none' ) {
                
                YAHOO.mbi.view.panel.build( ctab, ctab.my.panel );
                
                var cytbdy = document.createElement( 'div' );
                ctab.my.cbody = YAHOO.util.Dom.generateId( cytbdy );
                YAHOO.util.Dom.addClass( cytbdy, 'rec-tab-cyto-body');
                
                if ( ctab.my.estat === 'single' ) {                    
                    cytbdy = YAHOO.mbi.view.cyt.buildSimple( cytbdy, ctab );
                    ctab.my.pbot.appendChild( cytbdy );
                } 
                if ( ctab.my.estat === 'multi' ) {  
                    cytbdy = YAHOO.mbi.view.cyt.build( cytbdy, ctab );
                    ctab.my.pbot.appendChild( cytbdy );
                }
                
            }
            
            if ( ctab.my.pstat === 'on' ) {
                YAHOO.util.Dom.replaceClass( 
                    ctab.my.panel, 'rec-tab-filter-col','rec-tab-filter' );
            }
            if ( ctab.my.pstat === 'off' ) {
                YAHOO.util.Dom.replaceClass( 
                    ctab.my.panel, 'rec-tab-filter','rec-tab-filter-col' );   
            }                
            
            // filter body
            
            var flist =[];
            for(var c = 0; c < columnModel.length; c++ ){
                if( columnModel[c].filter ) {
                    var ff = {key:columnModel[c].key, label:columnModel[c].label};
                    flist.push( ff );
                    
                }
            }
            
            ctab.my.fpanel = { 
                flist: flist,
                buildFltString: function( flt ) {
                    var fltstr = '';       
                    if ( flt !== null ) {
                        var fltval = {};
                        var nmt = false;
                        for ( var i = 0; i < flt.length; i ++ ) {
                            var item = flt[i].button.get( 'selectedMenuItem' );
                            
                            if ( item !== null && item.value !== undefined ) {
                                fltval[flt[i].key] =  item.value;
                                nmt = true;
                            }
                        }
                        if ( nmt ) {
                            fltstr = "&flt=" + YAHOO.lang.JSON.stringify( fltval ).replace("&","%26");
                                                        alert( fltstr ); // XXXXXXXXXXX
                        }
                    }
                    return fltstr;
                }
            };
            
            
            var fltReset = function( arg ) {
                try{
                    var cp = this.my.cpanel;
                    if ( cp !== undefined && cp.flt !== undefined ) {
                        cp.flt.set( 'href', 
                                    YAHOO.mbi.view.cyt.buildRex( this, 
                                                                 true ) );
                    }                
                } catch (x) {   
                    console.log(x);
                }
            };
            
            if ( flist.length > 0 ) {
                
                var fbdy = document.createElement( 'div' );
                ctab.my.fbody = YAHOO.util.Dom.generateId( fbdy );
                fbdy.innerHTML = '<div class="rec-tab-filter-fhd"><h3>Filter</h3></div>'; 
                YAHOO.util.Dom.addClass( fbdy, 'rec-tab-filter-body' );
                
                ctab.my.ptop.appendChild( fbdy );
                
                for( var f = 0; f < flist.length; f++ ) {
                    
                    var cff = flist[f];
                    
                    var fitm = document.createElement('div');
                    YAHOO.util.Dom.addClass( fitm, 'rec-tab-filter-ihd' );
                    fitm.innerHTML = cff.label;
                    fbdy.appendChild( fitm );
                    
                    cff.fel = document.createElement( 'div' );
                    fitm.appendChild( cff.fel );
                    
                    YAHOO.mbi.view.query.filterButton =
                        cff.button = new YAHOO.widget.Button(
                            { label: "<em class=\"yui-button-label\">ANY</em>",
                              type: "menu",  
                              menu: ['ANY'], 
                              container:  cff.fel } );
                    cff.button.my ={tab:ctab,ffield:cff};
                    
                    cff.button.on( "selectedMenuItemChange", 
                                   YAHOO.mbi.view.filter.onSelectedMenuItemChange );
                    
                    
                    cff.button.getMenu().subscribe( 
                        "render",
                        function ( type, args ) {
                            YAHOO.mbi.view.filter.onMenuRender( this );
                        }, cff.button, true);                
                    
                    cff.button.on( "selectedMenuItemChange",
                                   YAHOO.mbi.view.stateManager.handleFilter , ctab, ctab );
                    
                    cff.button.on( "selectedMenuItemChange",
                                   fltReset , ctab, ctab );
                    
                }
            }
            
            this.my.tdBody = document.createElement( "td" );  
            YAHOO.util.Dom.setAttribute( this.my.tdBody, 'valign', 'top' );
            YAHOO.util.Dom.addClass( this.my.tdBody, 'rec-tab-body' );
            tr.appendChild( this.my.tdBody );
            
            var pel = document.createElement( "div" );
            this.my.tdBody.appendChild( pel );
            var el = document.createElement( "div" );
            this.my.tdBody.appendChild( el );
            
            var fields = [];
            var twd = 0; 
            for (var i = 0, len = columnModel.length; i < len; ++i) {
                fields[i] = columnModel[i].key;
                twd = twd + columnModel[i].width; 
            }
            
            var datasource = 
                new YAHOO.util.DataSource( this.my.arg.query + 
                                           '&sl=' + this.my.tn + '&ret=data&' );
            
            datasource.responseType = YAHOO.util.DataSource.TYPE_JSON; 
            datasource.doBeforeCallback = YAHOO.mbi.view.query.getFullRecord;
            
            //myDS.connXhrMode = 'queueRequests'; 
            
            datasource.responseSchema = { 
                resultsList:'modelData.data', 
                fields:fields,
                metaFields:{total: 'modelData.total',
                            noflt: 'modelData.noflt', 
                            max: 'modelData.max',
                            first: 'modelData.first'}  
            }; 
            
            var myRequestBuilder = function( oState, oSelf ) {
                
                // Get states (or use defaults)
                //-----------------------------
                
                oState = oState || {pagination:null, sortedBy:null};
                var sort = (oState.sortedBy) ? oState.sortedBy.key : "id";
                var dir = ( oState.sortedBy && 
                            oState.sortedBy.dir === YAHOO.widget.DataTable.CLASS_DESC) 
                    ? "desc" : "asc";
                var startIndex = (oState.pagination) 
                    ? oState.pagination.recordOffset : 0;
                var results = (oState.pagination) 
                    ? oState.pagination.rowsPerPage : 10;
                
                // Get filter status
                //------------------
                
                var fltstr = oSelf.my.filter.buildFltString(
                    oSelf.my.filter.flist );
                
                // Build request
                //--------------
                
                var request = 'srt={"' + sort + '":"' + dir + '"}' +
                    fltstr + "&fr=" + startIndex + "&mr=" + results;
                return request;
            };
            
            var template = '{FirstPageLink} {PreviousPageLink} '+ 
                '{PageLinks} '+  
                '{NextPageLink} {LastPageLink}' +
                ' Show{RowsPerPageDropdown}rows per page';
            
            var paginator = YAHOO.mbi.view.query.createPaginator(pel, template); 
            
            var myConfig = {
                initialRequest: 'mr=10', //number of returned results to the datasource?
                paginator: paginator,
                dynamicData : true,
                generateRequest : myRequestBuilder,
                draggableColumns: true,
                selectionMode:"standard",
                MSG_LOADING: '<center><img src="./images/dla16.gif"/></center>',
                width:"95%" 
            };
            
            this.my.dataTable =
                new YAHOO.widget.DataTable( el, columnModel, datasource, myConfig );       
            
            this.my.dataTable.handleDataReturnPayload = 
                function( oRequest, oResponse, oPayload ) {
                    oPayload.totalRecords = oResponse.meta.total;
                    //oPayload.pagination.rowsPerPage =  oResponse.meta.max;
                    //oPayload.pagination.recordOffset = oResponse.meta.first;
                    
                    var cpanel = this.my.ctab.my.cpanel;
                    
                    if ( cpanel !== undefined ) {
                        cpanel.flt.set( "label","FLT<br>(" + 
                                        oResponse.meta.total + ")" );
                        cpanel.all.set( "label","ALL<br>(" + 
                                        oResponse.meta.noflt + ")" );
                    }
                    return oPayload;
                };
            
            this.my.dataTable.handleMouseDown =
                function( oRequest, oResponse, oPayload ) {
                    console.log( oRequest );
                };
            
            this.my.dataTable.my = { filter: ctab.my.fpanel, ctab: ctab, 
                                     requestBuilder: myRequestBuilder };
            
            this.my.dataTable.subscribe( "rowMouseoverEvent",
                                         this.my.dataTable.onEventHighlightRow );
            this.my.dataTable.subscribe( "rowMouseoutEvent",
                                         this.my.dataTable.onEventUnhighlightRow );

            this.my.dataTable.subscribe( "rowClickEvent",
                                         this.my.dataTable.onEventSelectRow );

  
            this.my.dataTable.my.selectListener = function( o ) {
            
                var state = this.my.dataTable.getState();
                var rset = this.my.dataTable.getRecordSet();
                var cpanel = this.my.cpanel;
                cpanel.sel.set( "label","SEL<br>("+ 
                                state.selectedRows.length + ")" );
            };
            
            this.my.dataTable.subscribe( "rowSelectEvent", 
                                         this.my.dataTable.my.selectListener, 
                                         this.my.dataTable, this );
            this.my.dataTable.subscribe( "rowUnselectEvent", 
                                         this.my.dataTable.my.selectListener, 
                                         this.my.dataTable, this );    
            this.my.dataTable.subscribe( "renderEvent", 
                                         this.my.dataTable.my.selectListener, 
                                         this.my.dataTable, this );
            // table context menu
            //-------------------
            
            var dtcId = 'table-context-menu-' + ctab.my.tn;
            buildDtcMenu( {id: dtcId, tab: this} );
            
            // column reorder/resize
            //----------------------
            
            this.my.dataTable.subscribe( "columnReorderEvent",onCmChange );
            this.my.dataTable.subscribe( "columnResizeEvent", onCmChange );
            
        };   
        
        var mvDisplay = function( o ) {       
            var ctab = o.tab; 
            var dtcId = 'table-context-menu-' + ctab.my.tn;
        };
        
        var buildDtcMenu = function(o){ 
            
            var id = o.id;        
            var idVc = id + "-vcol";
            var tab = o.tab;
            var dt = o.tab.my.dataTable;
            
            var menu = new YAHOO.widget
                .ContextMenu( id,
                              {trigger: dt.getTbodyEl(),
                               lazyload: true});        
            
            menu.my = { dt: dt, idVc: idVc };
            
            tab.my.dtcMenu  = menu;
            menu.subscribe( "triggerContextMenu", onTriggerDsContextMenu );
            menu.subscribe( "hide", onTriggerDsContextMenu );
            menu.subscribe( "render", function(){ menu.my.rendered = true; } );
        };
        
        var cmPatch = function( o ){
            
            var cm = o.cm;
            var scmp = o.cmp;
            
            //alert( "cm:"+ YAHOO.lang.JSON.stringify(cm) );
            var cmp = null;
            
            try{
                cmp = YAHOO.lang.JSON.parse( scmp ); 
            } catch (x) {
                console.log("EX: " + x);
            }
            
            //alert( "cmPatch: " + cmp.length + " :: " + cm.length );
            
            if( cmp.length !== cm.length){
                return cm;  // return original cm (wrong cookie) ?
            }
            
            var kcm = []; // key hash
            for( var i=0;i<cm.length; i++ ){
                kcm[ cm[i].key ] = cm[i];
            }
            
            var pcm=[];
            for( var j=0; j< cmp.length; j++ ){
                var cc = kcm[ cmp[j]["key"] ];
                //alert("J=" + j + " :: " + cmp[j]["key"] + " :: " + cc );
                if( cc === undefined || cc === null ){
                    return cm;
                }
                
                
                var prop;
                for( prop in cmp[j] ){
                    if( prop !== "key" ){
                        cc[prop]=cmp[j][prop];
                    }
                }
                pcm.push( cc );
            }
            //alert("pcm:"+ YAHOO.lang.JSON.stringify(pcm));
            return pcm;
        };

        //note* I dont know what this does but it is not the main
        //function to add to the filter buttons.
        var onTriggerDsContextMenu = function( p_sType, p_aArgs){
            
            if( this.getRoot() == this ){       
                try{
                    var items=[[{ text:"View Columns",
                                  submenu:{id: this.my.idVc,
                                           itemdata:[]}
                                }
                               ],
                               [{ text:"Store Preferences", disabled: true}
                               ]];
                    
                    var dt = this.my.dt;
                    var cm = dt.getColumnSet().getDefinitions();
                    
                    var vdef = dt.my.ctab.my.vdef;
                    
                    for( var ci=0; ci<cm.length; ci++){
                        var cl = cm[ci].label;
                        var key = cm[ci].key;
                        var width = cm[ci].width;
                        var hidden = cm[ci].hidden;
                        
                        var mi = {text: cl, checked: !hidden, 
                                  onclick: { fn: colToggle, 
                                             obj: [ dt, key]} };
                        items[0][0].submenu.itemdata.push( mi );
                    }
                    
                    this.clearContent();
                    this.addItems( items );        
                    this.setItemGroupTitle( "Table Preferences", 0 );
                    
                    if( ! this.my.rendered ){
                        this.render( this.cfg.getProperty("container") );
                    }else{
                        this.render();
                    }
                    setCmCookie( {dt:dt} );
                    
                } catch (x) {
                    console.log(x);
                }
            }
        };
        
        var onCmChange = function( p_sType, p_aArgs){
            setCmCookie( { dt:this } );        
        };
        
        
        var colToggle = function( p_sType, p_aArgs, par ){
                    
            var dt = par[0];
            var key = par[1];
            
            if( dt.getColumn(key).hidden ){
                dt.showColumn(key);
            } else {
                dt.hideColumn(key);
            }
            setCmCookie( {dt:dt} );
        };
        
        var setCmCookie = function( o ){
            
            var cm = o.dt.getColumnSet().getDefinitions();
            var vdef = o.dt.my.ctab.my.vdef;
            
            var cookie = [];
            for( var ci=0; ci<cm.length; ci++){
                var cl = cm[ci].label;
                var key = cm[ci].key;
                var width = cm[ci].width;
                var hidden = cm[ci].hidden;
                cookie[ cookie.length ] 
                    = { key:key, width:width, hidden:hidden };
            }
            
            var scookie = YAHOO.lang.JSON.stringify( cookie );
            //alert( "SetCookie: vdef=" + vdef+ " :: " + scookie );
            YAHOO.util.Cookie.set( "cm-" + vdef, scookie );      
        };
        YAHOO.mbi.view.formatter.formatterInit();
    },
    getFullRecord: function(oRequest,oFullResponse,oParsedResponse,oCallback)
    {
        var numRecords = oFullResponse.modelData.total;
        for(i = 0; i < numRecords; i++)
        {
            fullRecord = oFullResponse.modelData.data[i];
            oParsedResponse.results[i] = fullRecord;
        }
        return oParsedResponse;
    }
};
