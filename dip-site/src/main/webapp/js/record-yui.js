/* record rendering */

YAHOO.namespace("mbi.record");

YAHOO.mbi.record.panel = {
    
    build : function ( ctab, self ) {
                      
        /* side panel: td  */

        ctab.my.panel = self; // document.createElement("td");
        
        YAHOO.util.Dom.setAttribute( ctab.my.panel,'valign','top');
        YAHOO.util.Dom.addClass( ctab.my.panel, 'rec-tab-filter');

        /* side panel header/control */
        
        var fhd =  document.createElement("div");
        YAHOO.util.Dom.addClass( fhd, 'rec-tab-filter-hd');  
        YAHOO.util.Dom.addClass( fhd, 'yui-layout-hd');
        ctab.my.panel.appendChild( fhd );
         
        var fhdc = document.createElement("div");
        ctab.my.collapse = YAHOO.util.Dom.generateId( fhdc );
        ctab.my.fop = true; 
        YAHOO.util.Dom.setAttribute( fhdc, 'title', 
                                     'Click to collapse this pane.' );
        YAHOO.util.Dom.addClass( fhdc, 'collapse' );  
        fhd.appendChild(fhdc);

        YAHOO.util.Event.addListener ( 
            ctab.my.collapse, "click", 
            function() {
                this.my.fop = ! this.my.fop;            
                if ( this.my.fop ){
                    YAHOO.util.Dom.replaceClass( this.my.panel, 
                                                 'rec-tab-filter-col',
                                                 'rec-tab-filter' );
                    YAHOO.util.Dom.setAttribute( 
                        this.my.collapse, 'title',
                        'Click to collapse this pane.');
                } else {
                    YAHOO.util.Dom.replaceClass( this.my.panel, 
                                                 'rec-tab-filter',
                                                 'rec-tab-filter-col');
                    YAHOO.util.Dom.setAttribute( 
                        this.my.collapse, 'title',
                        'Click to expand this pane.');
                }
                
            }, ctab, true ); 
        
        /* left panel content */

        var lpanel = document.createElement( 'table' );
        ctab.my.lpanel = YAHOO.util.Dom.generateId( lpanel );
        YAHOO.util.Dom.setAttribute( lpanel, 'cellpadding', '0');
        YAHOO.util.Dom.addClass( lpanel, 'rec-tab-left-panel');
        ctab.my.panel.appendChild( lpanel );
        
        var lpr0 = document.createElement('tr');
        lpanel.appendChild( lpr0 );

        ctab.my.ptop = document.createElement('td');
        YAHOO.util.Dom.setAttribute( ctab.my.ptop, 'valign', 'top');
        lpr0.appendChild( ctab.my.ptop );
  
        var lpr1 = document.createElement('tr');
        lpanel.appendChild( lpr1 );  

        ctab.my.pbot = document.createElement('td');
        YAHOO.util.Dom.setAttribute( ctab.my.pbot, 'valign', 'bottom');
        lpr1.appendChild( ctab.my.pbot );  

        return self;
    }    
};

YAHOO.mbi.record.filter = { 
    onMenuRender: function ( button ) {
        
        var tab = button.my.tab;
        var ffield = button.my.ffield;
        
        var valSet = function( o ){

            var messages = YAHOO.lang.JSON.parse( o.responseText );
            var nvl = messages.modelData.colValue[o.argument.cff.key];
            var vl =[];
            for( var v =0; v < nvl.length; v++ ) {
                var cv = nvl[v];
                var nvi ={text:cv.label,value:cv.value};
                vl.push( nvi );
            }
            o.argument.cff.button.getMenu().addItems( vl );
        };

        var valCallback = { cache:false, timeout: 5000, success:valSet,
                            argument:{ cff:ffield} };
        
        var url = tab.my.arg.action + 
            '?md=' + tab.my.arg.md +
            '&sl=' + tab.my.tn +
            '&ns=' + tab.my.arg.ns +
            '&ac=' + tab.my.arg.ac +
            '&cvl=' + ffield.key +
            '&ret=fquery';
        
        alert('Filter URL:'+ url);

        YAHOO.util.Connect.asyncRequest( 'GET', url, valCallback ); 
    },
    
    onSelectedMenuItemChange: function ( event ) {
        var oMenuItem = event.newValue;
        this.set( "label",('<em class="yui-button-label">'+ 
                           oMenuItem.cfg.getProperty("text") + "</em>"));
    }
};           

YAHOO.mbi.record.cyt = {

    buildRex: function( tab, filter ) {

        var url = document.URL;
        var parse = /^(?:([A-Za-z]+):)?(\/{0,3})(([0-9.\-A-Za-z]+)(?::(\d+))?\/[^\/]+)/;
        var res = parse.exec( url );
               
        var expurl = "http://127.0.0.1:30403" + 
            "/" + res[3] + "/rex" +
            "?db=" + tab.my.arg.ns + 
            "&ac=" + tab.my.arg.ac +
            "&md=" + tab.my.arg.md + 
            "&sl=" + tab.my.tn;
        
        if ( tab.my.cpanel !== undefined && 
             tab.my.cpanel.depth !== undefined ) {
                 var dp = tab.my.cpanel.depth.get('value');
                 if ( dp !== undefined ) {
                     expurl = expurl + "&nbh=" + dp;
                 }
             }
        if ( filter ) {
            if ( tab.my.fpanel !== undefined ) {
                var fltstr = tab.my.fpanel.buildFltString(
                    tab.my.fpanel.flist );
                expurl = expurl + fltstr;
            }
        }
        return expurl;
    },

    buildSimple: function( e, tab ) {
        
        var ct = document.createElement('table');
        YAHOO.util.Dom.addClass( ct, 'ctbl' );
        var ctr = document.createElement('tr');
        var cc1 = document.createElement('td');
        YAHOO.util.Dom.addClass( cc1, 'clogo' );
        YAHOO.util.Dom.setAttribute( cc1,'valign','top');
        var cc2 = document.createElement('td');
        YAHOO.util.Dom.addClass( cc2, 'cbdy' );
        YAHOO.util.Dom.setAttribute( cc2,'valign','top');
        ctr.appendChild(cc1);
        ctr.appendChild(cc2);
        ct.appendChild(ctr);
        e.appendChild(ct);
        
        var cexp = document.createElement('div');
        YAHOO.util.Dom.addClass( cdepth, 'cbt' );
        cc2.appendChild(cexp);
        
        var expurl = 
            YAHOO.mbi.record.cyt.buildRex( tab, false );

        var fexpurl = 
            YAHOO.mbi.record.cyt.buildRex( tab, true );

        var cSimple = new YAHOO.widget.Button({label: "Export", 
                                               value: "0",
                                               type: "link",
                                               target: "cifr",
                                               href: fexpurl,
                                               container: cexp});
        cSimple.on( "click",  YAHOO.mbi.record.cyt.timerSet, 1000 );
        
        var chd2 = document.createElement( 'div' );
        YAHOO.util.Dom.addClass( chd2, 'chd' );
        var scr = "YAHOO.mbi.record.cyt.help( " + 
            "YAHOO.mbi.record.cyt.my.panel, 0 ); return false";
        chd2.innerHTML = 'Neighbors (<a href="" onClick="' + scr + 
            '">help</a>)';
        cc2.appendChild( chd2 );
        
        var cdepth = document.createElement('div');
        YAHOO.util.Dom.addClass( cdepth, 'cbt' );
        cc2.appendChild(cdepth);
        var depth = 
            new YAHOO.widget.ButtonGroup({id:  "cdepth",  
                                          name:  "rf",  
                                          container: cdepth });
        depth.addButtons([{label:"None", value:"0", checked: true},
                          {label:"1", value:"1"},
                          {label:"1+", value:"1+"}]);
        
        var clogo = new Image();
        clogo.src = './images/cytoscape4A_90.png';
        cc1.appendChild(clogo);

        YAHOO.mbi.record.cyt.my = {
            panel: ct,
            depth: depth
        };
         
        tab.my.cpanel = {
            panel: ct,
            depth: depth,
            smpl: cSimple            
        };
        
        depth.on( "valueChange", 
                  YAHOO.mbi.record.cyt.depthSet, tab );
        return e;
    },

    build: function( e, tab ) {
        
        var ct = document.createElement('table');
        YAHOO.util.Dom.addClass( ct, 'ctbl' );
        var ctr = document.createElement('tr');
        var cc1 = document.createElement('td');
        YAHOO.util.Dom.addClass( cc1, 'clogo' );
        YAHOO.util.Dom.setAttribute( cc1,'valign','top');
        var cc2 = document.createElement('td');
        YAHOO.util.Dom.addClass( cc2, 'cbdy' );
        YAHOO.util.Dom.setAttribute( cc2,'valign','top');
        ctr.appendChild(cc1);
        ctr.appendChild(cc2);
        ct.appendChild(ctr);
        e.appendChild(ct);
        
        var cexp = document.createElement('div');
        YAHOO.util.Dom.addClass( cdepth, 'cbt' );
        cc2.appendChild(cexp);
        
        var expurl = 
            YAHOO.mbi.record.cyt.buildRex( tab, false );
        var fexpurl = 
            YAHOO.mbi.record.cyt.buildRex( tab, true );

        var cFlt = new YAHOO.widget.Button({label: "FLT<br>(0)", 
                                            value: "0",
                                            type: "link",
                                            target: "cifr",
                                            href: fexpurl,
                                            container: cexp});
        cFlt.on( "click",  YAHOO.mbi.record.cyt.timerSet, 1000 );

        var cAll = new YAHOO.widget.Button({label:"ALL<br>(0)", 
                                            value:"0",
                                            type: "link",
                                            target: "cifr",
                                            href: expurl,
                                            container: cexp});
        cAll.on( "click",  YAHOO.mbi.record.cyt.timerSet, 1000 );
                
        var cSel = new YAHOO.widget.Button({label:"SEL<br>(0)", 
                                            value:"0",
                                            type: "link",
                                            target: "cifr",
                                            href: expurl,
                                            container: cexp});
        cSel.on( "click",  
                 YAHOO.mbi.record.cyt.timerSet, 1000 );
        
        var chd2 = document.createElement( 'div' );
        YAHOO.util.Dom.addClass( chd2, 'chd' );
        var scr = "YAHOO.mbi.record.cyt.help( " + 
            "YAHOO.mbi.record.cyt.my.panel, 0 ); return false";
        chd2.innerHTML = 'Neighbors (<a href="" onClick="' + scr + 
            '">help</a>)';
        cc2.appendChild( chd2 );
        
        var cdepth = document.createElement('div');
        YAHOO.util.Dom.addClass( cdepth, 'cbt' );
        cc2.appendChild(cdepth);
        var depth = 
            new YAHOO.widget.ButtonGroup({id:  "cdepth",  
                                          name:  "rf",  
                                          container: cdepth });
        depth.addButtons([{label:"None", value:"0", checked: true},
                          {label:"1", value:"1"},
                          {label:"1+", value:"1+"}]);
        
        var clogo = new Image();
        clogo.src = './images/cytoscape4A_90.png';
        cc1.appendChild(clogo);

        YAHOO.mbi.record.cyt.my = {
            panel: ct,
            depth: depth
        };
         
        tab.my.cpanel = {
            panel: ct,
            depth: depth,
            flt: cFlt,
            all: cAll,
            sel: cSel
        };

        depth.on( "valueChange", 
                  YAHOO.mbi.record.cyt.depthSet, tab );
        return e;
    },

    help: function( e, id ) {
        YAHOO.mbi.help.show('Cytoscape Export','cytoscape_export');
    },

    depthSet: function( o, tab ) { 
        if ( tab.my.cpanel.smpl !== undefined) {
            tab.my.cpanel.smpl.set(
                'href', YAHOO.mbi.record.cyt.buildRex( tab, false ) );
        } else {
            tab.my.cpanel.flt.set(
                'href', YAHOO.mbi.record.cyt.buildRex( tab, true ) );
            tab.my.cpanel.all.set(
                'href', YAHOO.mbi.record.cyt.buildRex( tab, false ) );
            tab.my.cpanel.sel.set(
                'href', YAHOO.mbi.record.cyt.buildRex( tab, false ) );
        }
    },

    timerSet: function( o, delay ) { 
        var timer = document.getElementById( "timer" );
        if ( timer == null ) {
            timer = document.createElement( "timer" );
        }
        timer.id = "timer"; timer.name = "timer";
        timer.setAttribute( "status", "waiting..." );
        document.getElementById( "cifr" ).appendChild( timer );
        setTimeout( "YAHOO.mbi.record.cyt.timerTest()", delay );
    },

    timerReset: function( o ) { 
        var timer = document.getElementById( "timer" );
        if ( timer == null ) {
            timer = document.createElement( "timer" );
        }
        timer.id = "timer"; timer.name = "timer";
        document.getElementById( "cifr" ).appendChild( timer );
        timer.setAttribute( "status", "done" );
    },

    timerTest: function ( o ) {
        var timer = document.getElementById( "timer" );
        if ( timer !== null ) {
            if ( timer.getAttribute( "status" ) == "waiting..." ) {
                YAHOO.mbi.help.show( 'Cytoscape Installation',
                                     'guide-cytoscape-install');
            }
        }
    }
};
          
YAHOO.mbi.record.load = function( db, ns, ac, md, sl ) {

    var dels= [1,1,1,1,1,2,2,2,2,2,5,5,5,5,5,10,10,20,20,20,30,30];
    var anim = false;

    var recordAction = 'record';
    if( db === 'dip' ){
        recordAction = 'record';
    }
    if( db === 'prl' ){
        recordAction = 'prl-record';
    }
    //alert(recordAction);

    var tabUpdate = function( o ) {
        var i;
        if ( o.argument.start ) {
            i = 0;
        } else {
            i = o.argument.i;
        }
        


        var tabUpdateCallback = { cache:false, timeout: 5000, success:tabUpdate,
                                  argument:{ i: i+1, 
                                             action:recordAction, 
                                             ns:ns, ac:ac, md:md, sl:sl } };
        
        var call = function() {
            YAHOO.util.Connect.asyncRequest( 'GET',recordAction +
                                             '?ns=' + ns + '&ac=' + ac + 
                                             '&md=' + md + '&sl=' + sl + 
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
                
                var tabs= YAHOO.mbi.record.tabView.get('tabs');

                for( var t=2; t < tabs.length; t++){
                    var ccount = counts[t-1];
                    var clabel = labels[t-1].label;
                    var ctab = tabs[t];
                    
                    if ( ccount > 0 ){
                        var ll = clabel +'('+ccount+')';
                        ctab.set('label',ll);
                        ctab.set('disabled', false );
                    } else {
                        ctab.set('disabled', true );
                    }
                }
                
                if ( counts[0] > 0 && i +1 < dels.length ){
                    if ( !anim ) {
                        anim = true;
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
                    anim = false;
                }
            }
        }
    };

    var tabInit = function( o ) {
        
        var messages = YAHOO.lang.JSON.parse( o.responseText ); 
        YAHOO.mbi.record.tabView = new YAHOO.widget.TabView("recTab");
        
        var arg = o.argument;          
        
        /* build tabs */
        
        // refresh tab
        //------------
        
        var tab0 = YAHOO.mbi.record.tabView.getTab(0);
        tab0.my = {loaded:true,label:'refresh'};
        tab0.set('disabled',true);
        
        // sumary tab
        //-----------
        
        for (var i = 0, len = messages.modelList.length; i < len; ++i) { 
            var label = messages.modelList[i].label; 
            var panel = messages.modelList[i].panel;  
            var exprt = messages.modelList[i].exprt;  
            var active = (i===0 ? true : false);
            
            if ( i === 0 ) {
                var tab = YAHOO.mbi.record.tabView.getTab( 1 );
                tab.my = { loaded:true, label:label, pstat: panel, estat: exprt };
                tab.set( 'label', label );
                var spe = document.getElementById('summary-panel');
                if ( panel == 'none' ) {
                    
                }
                if ( panel == 'on' ) {
                    YAHOO.util.Dom.replaceClass( 
                        spe, 'rec-tab-filter-col', 'rec-tab-filter' );
                    tab.my = {
                        loaded:false, label:label, tn:i, tab:tab,
                        tv:YAHOO.mbi.record.tabView, arg:arg };
                   
                    YAHOO.mbi.record.panel.build( tab, spe );
                    
                    var cytbdy = document.createElement( 'div' );
                    tab.my.cbody = YAHOO.util.Dom.generateId( cytbdy );
                    YAHOO.util.Dom.addClass( cytbdy, 'rec-tab-cyto-body');

                    if ( exprt === 'single' ) {                    
                        cytbdy = YAHOO.mbi.record.cyt.buildSimple( cytbdy, tab );
                        tab.my.pbot.appendChild( cytbdy );
                    } 
                    if ( exprt === 'multi' ) {  
                        cytbdy = YAHOO.mbi.record.cyt.build( cytbdy, tab );
                        tab.my.pbot.appendChild( cytbdy );
                    }
                }
                if ( panel == 'off' ) {
                    YAHOO.util.Dom.replaceClass( 
                        spe, 'rec-tab-filter','rec-tab-filter-col' );   
                }                
                YAHOO.mbi.record.tabView.selectTab( 1 );
            } 
            if( i > 0 ) {
                var tab = new YAHOO.widget.Tab(
                    { label: label,
                      content: '<table width="100%" cellspacing="0"/>' });
                tab.my = { loaded:false, label:label, tn:i, tab:tab,
                           pstat: panel, estat: exprt,
                           tv:YAHOO.mbi.record.tabView, arg:arg };
                
                tab.on( "activeChange", tabLoad );
                YAHOO.mbi.record.tabView.addTab( tab );
            }    
        } 
        
        // start tab updates
        //------------------
        
        tabUpdate( { argument:{ start:true } } );    
    };
    
    var tabCallback = { cache:false, timeout: 5000, success:tabInit,
                        argument:{ action:recordAction, 
                                   ns:ns, ac:ac, md:md, sl:sl } };

    //alert( recordAction + '?md='+ md +'&sl='+ sl + 
    //       '&ret=modellist');
    
    YAHOO.util.Connect.asyncRequest( 'GET', recordAction + '?md='+ md +'&sl='+ sl + 
                                     '&ret=modellist', tabCallback ); 
    
    var tabLoad = function( e ) {
        if ( !this.my.loaded ) {
            this.my.loaded = true;
       
            var mvCallback = { cache:false, timeout: 5000, 
                               success:mvInit, scope:this };
            YAHOO.util.Connect.asyncRequest( 
                'GET',this.my.arg.action + '?md=' + this.my.arg.md 
                    + '&sl=' + this.my.tn + 
                    '&ret=model', mvCallback );
        }
    };

    var mvInit = function( o ) {
        
        var messages = YAHOO.lang.JSON.parse( o.responseText );
        var cm = messages.modelView.colModel;

        var ctab = this; 
        var cont = YAHOO.util.Dom.getFirstChild( ctab.get('contentEl') );
        
        this.my.id = YAHOO.util.Dom.generateId( cont );
        var tr = document.createElement("tr");
        cont.appendChild( tr );

        ctab.my.panel = document.createElement("td");
        YAHOO.util.Dom.setAttribute( ctab.my.panel,'valign','top');
        YAHOO.util.Dom.addClass( ctab.my.panel, 'rec-tab-filter');
        tr.appendChild( ctab.my.panel );

        if ( ctab.my.pstat === 'on' ) {
            YAHOO.mbi.record.panel.build( ctab, ctab.my.panel );
            //YAHOO.util.Dom.replaceClass( 
            //    ctab.my.panel, 'rec-tab-filter-col', 'rec-tab-filter' );
            
            //alert(ctab.my.ptop);        
            var cytbdy = document.createElement( 'div' );
            ctab.my.cbody = YAHOO.util.Dom.generateId( cytbdy );
            YAHOO.util.Dom.addClass( cytbdy, 'rec-tab-cyto-body');

            if ( ctab.my.estat === 'single' ) {                    
                cytbdy = YAHOO.mbi.record.cyt.buildSimple( cytbdy, ctab );
                ctab.my.pbot.appendChild( cytbdy );
            } 
            if ( ctab.my.estat === 'multi' ) {  
                cytbdy = YAHOO.mbi.record.cyt.build( cytbdy, ctab );
                ctab.my.pbot.appendChild( cytbdy );
            }
        }
        if ( ctab.my.pstat === 'off' ) {
            YAHOO.util.Dom.replaceClass( 
                        ctab.my.panel, 'rec-tab-filter','rec-tab-filter-col' );   
        }                
        
        /* filter body */

        var flist =[];
        for(var c = 0; c < cm.length; c++ ){
            if( cm[c].filter ) {
                var ff = {key:cm[c].key, label:cm[c].label};
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
                        var item = flt[i].button.get('selectedMenuItem');
                    
                        if ( item !== null && item.value !== undefined ) {
                            fltval[flt[i].key] =  item.value;
                            nmt = true;
                        }
                    }
                    if ( nmt ) {
                        fltstr = "&flt=" + YAHOO.lang.JSON.stringify(fltval);
                    }
                }
                return fltstr;
            }
        };

        var tableReload = function( arg ) {
            try {    
                var dt = this.my.dataTable;
                
                var state = dt.get('paginator').getState();
                state.page=1;
                state.recordOffset=0;
                dt.get('paginator').setState(state);
                
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
                alert(x);
            }
        };

        var fltReset = function( arg ) {
            try{
                var cp = this.my.cpanel;
                if ( cp !== undefined && cp.flt !== undefined ) {
                    cp.flt.set( 'href', 
                                YAHOO.mbi.record.cyt.buildRex( this, 
                                                               true ) );
                }                
            } catch (x) {   
                alert(x);
            }
        };
        
        if ( flist.length > 0 ) {

            var fbdy = document.createElement( 'div' );
            ctab.my.fbody = YAHOO.util.Dom.generateId( fbdy );
            fbdy.innerHTML = '<div class="rec-tab-filter-fhd">Filter</div>'; 
            YAHOO.util.Dom.addClass( fbdy, 'rec-tab-filter-body');
            
            ctab.my.ptop.appendChild( fbdy );
            
            for( var f = 0; f < flist.length; f++ ) {

                var cff = flist[f];

                var fitm = document.createElement('div');
                YAHOO.util.Dom.addClass( fitm, 'rec-tab-filter-ihd');
                fitm.innerHTML = cff.label;
                fbdy.appendChild( fitm );

                cff.fel = document.createElement( 'div' );
                fitm.appendChild( cff.fel );
                
                cff.button = new YAHOO.widget.Button(
                    { label: "<em class=\"yui-button-label\">ANY</em>",
                      type: "menu",  
                      menu: ['ANY'], 
                      container:  cff.fel } );
                cff.button.my ={tab:ctab,ffield:cff};
                
                cff.button.on( "selectedMenuItemChange", 
                               YAHOO.mbi.record.filter.onSelectedMenuItemChange );
               

                cff.button.getMenu().subscribe( 
                    "render",
                    function ( type, args ) {
                        YAHOO.mbi.record.filter.onMenuRender( this );
                    }, cff.button, true);                

                cff.button.on( "selectedMenuItemChange",
                               tableReload , ctab, ctab );

                cff.button.on( "selectedMenuItemChange",
                               fltReset , ctab, ctab );

            }
        }

        this.my.tdBody = document.createElement("td");  
        YAHOO.util.Dom.setAttribute( this.my.tdBody, 'valign', 'top');
        YAHOO.util.Dom.addClass( this.my.tdBody, 'rec-tab-body');
        tr.appendChild( this.my.tdBody );
        
        var pel = document.createElement("div");
        this.my.tdBody.appendChild(pel);
        var el = document.createElement("div");
        this.my.tdBody.appendChild(el);
          
        var fields = [];
        var twd=0; 
        for (var i = 0, len = cm.length; i < len; ++i) {
            fields[i] = cm[i].key;
            twd = twd + cm[i].width; 
        }
                      
        var myDS = 
            new YAHOO.util.DataSource( 'record?db=' + this.my.arg.ns + 
                                       '&ac=' + this.my.arg.ac + 
                                       '&md=' + this.my.arg.md +
                                       '&sl=' + this.my.tn + '&ret=data&' );
        
        myDS.responseType = YAHOO.util.DataSource.TYPE_JSON; 

        //myDS.connXhrMode = 'queueRequests'; 
        
        myDS.responseSchema = { 
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
            return encodeURI(request);
        };

        var template = '{FirstPageLink} {PreviousPageLink} '+ 
            '{PageLinks} '+  
            '{NextPageLink} {LastPageLink}' +
            ' Show{RowsPerPageDropdown}rows per page';

        var paginator = new YAHOO.widget.Paginator(
                { containers: [pel],
                  rowsPerPage: 10,
                  template: template,
                  rowsPerPageOptions: [5,10,25],
                  pageLinks: 5
                });

        var myConfig = {
            initialRequest: 'mr=10',
            paginator: paginator,
            dynamicData : true,
            generateRequest : myRequestBuilder,
            draggableColumns: true,
            selectionMode:"standard",
            MSG_LOADING: '<center><img src="./images/dla16.gif"/></center>',
            width:"95%" 
        };
       
        this.my.dataTable =
            new YAHOO.widget.DataTable( el, cm, myDS, myConfig );       

        this.my.dataTable.handleDataReturnPayload = 
            function( oRequest, oResponse, oPayload ) {
                oPayload.totalRecords = oResponse.meta.total;
                oPayload.pagination.rowsPerPage =  oResponse.meta.max;
                oPayload.pagination.recordOffset = oResponse.meta.first;
                
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
              alert( oRequest );
            };

        this.my.dataTable.my = { filter: ctab.my.fpanel, ctab: ctab, 
                                 requestBuilder: myRequestBuilder };
        
        this.my.dataTable.subscribe( "rowMouseoverEvent",
                                     this.my.dataTable.onEventHighlightRow );
        this.my.dataTable.subscribe( "rowMouseoutEvent",
                                     this.my.dataTable.onEventUnhighlightRow );
        this.my.dataTable.subscribe( "rowClickEvent",
                                     this.my.dataTable.onEventSelectRow );

        //this.my.dataTable.subscribe( "theadLabelMousedownEvent",
        //                             this.my.dataTable.handleMouseDown );
        
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
                                     
                                     
		YAHOO.mbi.view.formatter.formatterInit();
    };   
};

/* sample call

YAHOO.util.Event.addListener( window, "load", 
   YAHOO.mbi.record.load( "prl", "dip","DIP-378N","N",1) );
*/
