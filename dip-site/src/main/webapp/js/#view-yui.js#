/* record rendering */

YAHOO.namespace("mbi.view");

YAHOO.mbi.view.filter = { 
    onMenuRender: function ( button ){

        //alert('onMenuRender');

        var tab = button.my.tab;
        var ffield = button.my.ffield;
            
        var valSet = function( o ){
 
            //alert('valSet');
           
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
        
        var facetSet = function( o ){

            //alert('facetSet');
            var messages = null;
            try{
                messages = YAHOO.lang.JSON.parse( o.responseText );
            } catch (x) {
                //alert(x);
            }
            
            if( messages == null || messages.facetData == null ){
                
                // back-end does not support faceting; fall back to values_url.

                var valCallback = { cache:false, timeout: 5000, success: valSet,
                                    argument:{ cff:ffield} };
            
            var values_url = tab.my.arg.query +
                '&sl='+ tab.my.tn +
                '&cvl='+ ffield.key +
                '&ret=values';
            
                //alert('VURL:' + values_url);
            
                YAHOO.util.Connect.asyncRequest( 'GET', values_url, valCallback ); 
                
            } else {

                // build filter out of facetData;    
                
                var nvl = messages.facetData[0].partList.part;
                var vl =[];
                for( var v =0; v < nvl.length; v++ ) {
                    var cv = nvl[v];
                    var nvi ={text:cv.node.label,value:cv.node.ac};
                    vl.push( nvi );
                }
                o.argument.cff.button.getMenu().addItems( vl );
            }

        };
        
        try{
            var facet_url = tab.my.arg.query +
                '&sl='+ tab.my.tn +
                '&cvl='+ ffield.key +
                '&ret=facet';
            
            var facetCallback = { cache:false, timeout: 5000, 
                                  success:facetSet, failure:facetSet,
                                  argument:{ cff:ffield} };
            
            YAHOO.util.Connect.asyncRequest( 'GET', facet_url, facetCallback ); 
            
        } catch (x) {
            console.log(x);
        }
    },
    
    onSelectedMenuItemChange: function ( event ) {
        var oMenuItem = event.newValue;
        this.set( "label",('<em class="yui-button-label">'+ 
                           oMenuItem.cfg.getProperty("text") + "</em>"));
    },

    setFilter: function()
    {
        
        var state = YAHOO.mbi.view.stateManager.recTabGetState();
        var tab   = YAHOO.mbi.view.tabView.getTab( state.activeTab );
        var buttonList = tab.my.fpanel.flist;
        var amtOfButtons = buttonList.length;
        var silentListeners = true;
        var i = 0;
        for(i = 0; i < amtOfButtons; i++)
        {
            var filterItems = buttonList[i].button.getMenu().getItems();
             
            if( typeof state[state.activeTab].filter !== 'undefined'  && 
                typeof state[state.activeTab].filter[ buttonList[i].label ] !== 'undefined') 
            {
                //alert('this is one of the filters: ' + buttonList[i].label);
                var amtInFilterList = filterItems.length;
                var j = 0;
                var buttonIsSet = false;
                for(j = 0; j < amtInFilterList; j++)
                {
                    if(state[state.activeTab].filter[ buttonList[i].label].value == filterItems[j].value)
                    {
                        buttonList[i].button.set('selectedMenuItem', filterItems[j], silentListeners);
                        buttonList[i].button.set( "label",('<em class="yui-button-label">'+ 
                           filterItems[j].cfg.getProperty("text") + "</em>"));
                        buttonIsSet = true;
                    }
                }
                if( !buttonIsSet)
                {
                    console.log("no filters were a match to the values.");
                }
            }
            else if(buttonList[i].button.getMenu().getItems().length > 0)
            {
                //this sets the button to the deafult of "ANY"
                buttonList[i].button.set('selectedMenuItem', buttonList[i].button.getMenu().getItems()[0], silentListeners);
                buttonList[i].button.set( "label",('<em class="yui-button-label">'+ 
                   filterItems[0].cfg.getProperty("text") + "</em>"));
                
            }
        }
    }
};           

YAHOO.mbi.view.cyt = {


    buildRex: function( tab, filter ) {

        var url = document.URL;
        var parseUrl = /^(?:([A-Za-z]+):)?(\/{0,3})(([0-9.\-A-Za-z]+)(?::(\d+))?\/[^\/]+)/;
        var res = parseUrl.exec( url );
               
        var parseQuery = /[^\?]+(\?.+)/;
        var resQuery = parseQuery.exec(tab.my.arg.query);
        
        /*var expurl = "http://127.0.0.1:30403" + 
            "/" + res[3] + "/rex" + resQuery[0];
            "?db=" + tab.my.arg.ns + 
            "&ac=" + tab.my.arg.ac +
            "&md=" + tab.my.arg.md + 
            "&sl=" + tab.my.tn;
            console.log(resQuery[0]+"::"+resQuery[1]);
         */        
        
        var expurl = "http://127.0.0.1:30403" +
            "/" + res[3] + "/rex" + resQuery[1] +
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
            YAHOO.mbi.view.cyt.buildRex( tab, false );

        var fexpurl = 
            YAHOO.mbi.view.cyt.buildRex( tab, true );

        var cSimple = new YAHOO.widget.Button({label: "Export", 
                                               value: "0",
                                               type: "link",
                                               target: "cifr",
                                               href: fexpurl,
                                               container: cexp});
        cSimple.on( "click",  YAHOO.mbi.view.cyt.timerSet, 1000 );
        
        var chd2 = document.createElement( 'div' );
        YAHOO.util.Dom.addClass( chd2, 'chd' );
        var scr = "YAHOO.mbi.view.cyt.help( " + 
            "YAHOO.mbi.view.cyt.my.panel, 0 ); return false";
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

        YAHOO.mbi.view.cyt.my = {
            panel: ct,
            depth: depth
        };
         
        tab.my.cpanel = {
            panel: ct,
            depth: depth,
            smpl: cSimple            
        };
        
        depth.on( "valueChange", 
                  YAHOO.mbi.view.cyt.depthSet, tab );
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
            YAHOO.mbi.view.cyt.buildRex( tab, false );
        var fexpurl = 
            YAHOO.mbi.view.cyt.buildRex( tab, true );

        var cFlt = new YAHOO.widget.Button({label: "FLT<br>(0)", 
                                            value: "0",
                                            type: "link",
                                            target: "cifr",
                                            href: fexpurl,
                                            container: cexp});
        cFlt.on( "click",  YAHOO.mbi.view.cyt.timerSet, 1000 );

        var cAll = new YAHOO.widget.Button({label:"ALL<br>(0)", 
                                            value:"0",
                                            type: "link",
                                            target: "cifr",
                                            href: expurl,
                                            container: cexp});
        cAll.on( "click",  YAHOO.mbi.view.cyt.timerSet, 1000 );
                
        var cSel = new YAHOO.widget.Button({label:"SEL<br>(0)", 
                                            value:"0",
                                            type: "link",
                                            target: "cifr",
                                            href: expurl,
                                            container: cexp});
        cSel.on( "click",  
                 YAHOO.mbi.view.cyt.timerSet, 1000 );
        
        var chd2 = document.createElement( 'div' );
        YAHOO.util.Dom.addClass( chd2, 'chd' );
        var scr = "YAHOO.mbi.view.cyt.help( " + 
            "YAHOO.mbi.view.cyt.my.panel, 0 ); return false";
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

        YAHOO.mbi.view.cyt.my = {
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
                  YAHOO.mbi.view.cyt.depthSet, tab );
        return e;
    },

    help: function( e, id ) {
        //YAHOO.mbi.help.show('Cytoscape Export','cytoscape_export');
        YAHOO.mbi.modal.help('Cytoscape Export','cytoscape_export');
    },

    depthSet: function( o, tab ) { 
        if ( tab.my.cpanel.smpl !== undefined) {
            tab.my.cpanel.smpl.set(
                'href', YAHOO.mbi.view.cyt.buildRex( tab, false ) );
        } else {
            tab.my.cpanel.flt.set(
                'href', YAHOO.mbi.view.cyt.buildRex( tab, true ) );
            tab.my.cpanel.all.set(
                'href', YAHOO.mbi.view.cyt.buildRex( tab, false ) );
            tab.my.cpanel.sel.set(
                'href', YAHOO.mbi.view.cyt.buildRex( tab, false ) );
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
        setTimeout( "YAHOO.mbi.view.cyt.timerTest()", delay );
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
                                     'cytoscape_install');
            }
        }
    },

};

YAHOO.mbi.view.stateManager = {
    
      stateData:  { activeTab:  1},

     historyInit: function(){
        //initialize the default state
        var bookmarkedTabViewState = YAHOO.util.History.getBookmarkedState("recTab");
        var initialTabViewState = bookmarkedTabViewState || "tab0";
        //get the instance of the manager
        var stateManager = YAHOO.mbi.view.stateManager;
        //register the module
        YAHOO.util.History.register("recTab", initialTabViewState, stateManager.historyReadyHandler);
        //makes sure the handler is called when the DOM is ready (?)
        YAHOO.util.History.onReady( stateManager.historyReadyHandler );
        // Initialize the browser history management library.
        try {
          YAHOO.util.History.initialize("yui-history-field", "yui-history-iframe");
        } catch (x) {
          // The only exception that gets thrown here is when the browser is
          // not supported (Opera, or not A-grade) Degrade gracefully.
          console.log(x);
        }
    },

    /*-----------------------------------------------
     when a tab is clicked, navigate to the correct tab
     the new tab is also loaded into the yui history.     
    ------------------------------------------------ */
    
    handleTabNavigation: function(state){
        var viewManager = YAHOO.mbi.view;
        var stateManager = YAHOO.mbi.view.stateManager;
        var currentState, newState,newTab;

        //gets the state of the tab from the object passed from the click listener
        var newTab =  viewManager.tabView.getTabIndex(state.newValue);
        if(newTab !== null)
        {
        try {
            currentState = YAHOO.util.History.getCurrentState("recTab");  
//            if(currentState != 'tab0')
              //var state = stateManager.parseStateString(currentState); 
//            else
//                state = { activeTab : false};
            // The following test is crucial. Otherwise, we end up circling forever.
            // Indeed, YAHOO.util.History.navigate will call the module onStateChange
            // callback, which will call this handler and it keeps going from here...
           
            if( typeof stateManager.stateData[newTab] === 'undefined' )
                stateManager.stateData[newTab] = {};

            stateManager.stateData.activeTab = newTab;
            var isANewTab = state.activeTab != newTab;
            var newState = stateManager.generateStateString( stateManager.stateData ) ;
           
            if( isANewTab && newState != "tabnull" && !(currentState === 'tab0' && newTab === 1)) 
            {
                YAHOO.util.History.navigate("recTab", newState);
            }
            //hack to remove focus from the last clicked button staying highlighted on back navigation
            document.getElementsByClassName("selected")[0].firstChild.focus();

                    } catch (e) {
            console.log(e);
          }
        }
     },

    handlePagination: function(clickedState)
    {

        var stateManager = YAHOO.mbi.view.stateManager;
        var activeTab = stateManager.stateData.activeTab;

        stateManager.stateData[activeTab].startIndex = clickedState.recordOffset;
        stateManager.stateData[activeTab].pageSize   = clickedState.rowsPerPage;
        
        YAHOO.util.History
            .navigate( "recTab", 
                     stateManager.generateStateString( stateManager.stateData ) );
    },

    handleFilter: function(state)
    {
        var stateManager = YAHOO.mbi.view.stateManager;
        var clickedMenu = state.newValue.parent;
        var activeTab = stateManager.stateData.activeTab;

        var btnName = stateManager.getFilterBtnName( clickedMenu );
       
        if( typeof stateManager !== 'undefined')
        {
            if( typeof stateManager.stateData[activeTab] === "undefined")
                stateManager.stateData[activeTab] = {filter:{}} ;
            else if( typeof stateManager.stateData[activeTab].filter === 'undefined')
                stateManager.stateData[activeTab].filter = {} ;
      
            stateManager.stateData[activeTab].filter[btnName] = {};
            stateManager.stateData[activeTab].filter[btnName].value = state.newValue.value;
            stateManager.stateData[activeTab].filter[btnName].text  = state.newValue.cfg.config.text.value;
            //var menuItems = state.newValue.parent.getItemGroups();


            YAHOO.util.History
                .navigate( "recTab", 
                         stateManager.generateStateString( stateManager.stateData ) );
        }
    },
    //returns the name of the button that matches the filter that has just been set
    //I dont love this implementation so feel free to clean it up.
    getFilterBtnName: function( clickedMenu )
    {
        var state = YAHOO.mbi.view.stateManager.recTabGetState();
        var tab   = YAHOO.mbi.view.tabView.getTab( state.activeTab );
        var buttonList = tab.my.fpanel.flist;
        var amtOfButtons = buttonList.length;
        var i;
        for(i = 0; i < amtOfButtons; i++)
        {
            var menu = buttonList[i].button.getMenu();
            if(menu == clickedMenu)
                return buttonList[i].label;
        }
        
    },
    /*-----------------------------------------------
    Handles page back and forward as well as refreshes,
    as you can see there is no navigate() so it wont
    load the page, run the js and load the page again
    ------------------------------------------------ */
    historyReadyHandler: function(){
        var viewManager      = YAHOO.mbi.view;
        var stateManager     = YAHOO.mbi.view.stateManager;
        var validPaginator   = false; 
        //gets the curent state in the yui history. 
        var state;
        if( !( state = stateManager.recTabGetState()))
        {
            viewManager.tabView.set("activeIndex", 1);
            return
        }
        var paginatorManager = stateManager.getPaginator(state.activeTab);

        try{
            var paginator    = paginatorManager.getState();
            validPaginator   = true; 
        }catch(e){}

        //sets the active tab by index number
        viewManager.tabView.set("activeIndex", state.activeTab);
        
        if(validPaginator)
        {
            paginator.recordOffset = state[state.activeTab].startIndex || 0;
            paginator.rowsPerPage  = state[state.activeTab].pageSize || 10;
            paginatorManager.setState(paginator);
           
            if( typeof stateManager.stateData[state.activeTab].filter !== 'undefined')
                 YAHOO.mbi.view.filter.setFilter(); 
            viewManager.query.tableReload(); 
                 
        }
    },
    getPaginator: function( tabIndex )
    {
        var tab = YAHOO.mbi.view.tabView.getTab(tabIndex);
        if( typeof tab.my.dataTable === 'undefined' )
            return
        else
            return tab.my.dataTable.get("paginator");
    },
    generateStateString: function( state ){            
        return YAHOO.lang.JSON.stringify( state );
    },
    parseStateString: function( statStr ){
        return YAHOO.lang.JSON.parse(statStr);
    },
    recTabGetState: function(){
        var stateManager = YAHOO.mbi.view.stateManager;
        var state = YAHOO.util.History.getCurrentState("recTab");
        if( state === 'tab0')
            return false;
        return stateManager.parseStateString(state);
    } 
};

YAHOO.mbi.view.load = function( db, ns, ac, md, sl ) {

    var url='record';
    if( db === 'dip' ){
        url='record';
    }
    if( db === 'prl' ){
        url='prl-record';
    }

    var query = url + '?ns=' + ns + '&ac=' + ac + '&md=' + md;
    //alert(query);
    
    return YAHOO.mbi.view.query.init( query, sl );
};
