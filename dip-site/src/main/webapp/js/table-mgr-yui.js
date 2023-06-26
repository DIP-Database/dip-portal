/* record table editor */

YAHOO.namespace("mbi.tablemgr");

YAHOO.mbi.tablemgr.typeMenu = [
    {text: 'text', value: 'text' },
    {text: 'text-list', value: 'text-list' },
    {text: 'url', value: 'url' },
    {text: 'url-list', value: 'url-list' }
];

YAHOO.mbi.tablemgr.formMenu=[
    {text: 'NONE', value: 'NONE' },
    {text: 'center', value: 'center' }
];

YAHOO.mbi.tablemgr.onMenuItemClick = function ( p_sType, p_aArgs, p_oItem ) {
    var sText = p_aArgs[1].cfg.getProperty("text");
    var btn = p_oItem.btn;
    btn.set("label", sText);
};

YAHOO.mbi.tablemgr.onSaveClick = function ( p_sType, p_aArgs, p_oItem ) {
    alert("Save");
    
    // update json fields of the current column, send to server (async), 
    // reinitialize from a freshly-read json data

};

YAHOO.mbi.tablemgr.onResetClick = function ( p_sType, p_aArgs, p_oItem ) {
    alert("Reset");
    // reinitialize from a freshly-read json data 
};

YAHOO.mbi.tablemgr.onDropClick = function ( p_sType, p_aArgs, p_oItem ) {
    alert("Drop");
    // pop up warning dialog, if ok remove column from json, send to server (async),
    // reinitialize from a freshly-read json data

};


YAHOO.mbi.tablemgr.onMoveClick = function ( p_sType, p_aArgs, p_oItem ) {
    alert( "Move" );

    // move columns around within json, send to server (async),
    // reinitialize from a freshly-read json data

};

YAHOO.mbi.tablemgr.onAddClick = function ( p_sType, p_aArgs, p_oItem ) {
    alert( "Add" );
    // append empty column to json data, send to server (async),
    // reinitialize from a freshly-read json data

};

YAHOO.mbi.tablemgr.load = function() {

    var tabErr = function( o) {
       alert("Table definition loading error");  
    };
    
    var tabInit = function( o ) {
        var arg = o.argument;  // if passed
        
        var messages = YAHOO.lang.JSON.parse( o.responseText ); 
        YAHOO.mbi.tablemgr.tabView = new YAHOO.widget.TabView("tmgr-col-list");

        //YAHOO.mbi.tablemgr.tabView.selectTab(0);  // initially select 0th tab 
        YAHOO.mbi.tablemgr.tabView.getTab(0).set( 'disabled', true);
               
        var field =  messages.tables[arg["table"]].fields;
        
        for (var fi = 0; fi < field.length; fi++) { 
            
            var label = field[fi].field;
            var name = field[fi].name;             

            if( field[fi].field === undefined) {
                label = fi;
            }
            
            if( field[fi].name === undefined) {
                name = label;
            }
            
            var value = field[fi].value; 
            var type =  field[fi].type; 
            
            var cid = arg["table"] + '-' + fi;
            
            var tab = new YAHOO.widget.Tab(
                { label: name,
                  content: '<table id="' + cid + '" width="100%" cellspacing="5"/>' });
            
            YAHOO.mbi.tablemgr.tabView.addTab( tab );

            var ctel = YAHOO.util.Dom.get( cid );
            var tnav =  document.createElement("tr");
            ctel.appendChild( tnav );

            // column move/add buttons
            //------------------------

            var lltd =  document.createElement("td");
            var llBT = new YAHOO.widget.Button(
                { label:"<<",id:"tmgr-cmvll"+fi,
                  container:lltd });
            llBT.on("click", YAHOO.mbi.tablemgr.onMoveClick );

            var ltd =  document.createElement("td");
            var lBT = new YAHOO.widget.Button(
                { label:"<",id:"rmgr-cmvl"+fi,
                  container:ltd });
            lBT.on("click", YAHOO.mbi.tablemgr.onMoveClick );           

            var cctd =  document.createElement("td");
            YAHOO.util.Dom.addClass(cctd,"tmgr-wbt");
            var ccBT = new YAHOO.widget.Button(
                { label:"Add",id:"tmgr-cadd"+fi,
                  container:cctd });
            ccBT.on("click", YAHOO.mbi.tablemgr.onAddClick );

            var rtd =  document.createElement("td");
            var rBT = new YAHOO.widget.Button(
                { label:">",id:"tmgr-cmvr"+fi,
                  container:rtd });
            rBT.on("click", YAHOO.mbi.tablemgr.onMoveClick );

            var rrtd =  document.createElement("td");
            var rrBT = new YAHOO.widget.Button(
                { label:">>",id:"tmgr-cmvrr"+fi,
                  container:rrtd });
            rrBT.on("click", YAHOO.mbi.tablemgr.onMoveClick );
            
            tnav.appendChild( lltd );
            tnav.appendChild( ltd );
            tnav.appendChild( cctd );
            tnav.appendChild( rtd );
            tnav.appendChild( rrtd );

            // column editor
            //--------------
            
            var cedr =  document.createElement( "tr" );
            var cedd =  document.createElement( "td" );
            YAHOO.util.Dom.setAttribute( cedd, "colspan", "5" );
            YAHOO.util.Dom.addClass(cedd,"tmgr-urow");            
            cedr.appendChild( cedd );
            ctel.appendChild( cedr );
            
            cedd.innerHTML='<table width="100%"><tr class="tmgr-urow">'
                + '<td width="25%" nowrap> ID <input type="text" id="tmgr-cid-'+fi+'" size="24"/></td>' 
                + '<td width="25%" nowrap>Name <input type="text" id="tmgr-cname-'+fi+'" size="24"/> </td>'
                + '<td width="50%">&nbsp;</td>'
                + '<td width="5%" nowrap>Type<td width="20%"><div id="tmgr-ctype-'+fi+'"/></td>'
                + '<td width="5%" nowrap>Formatter</td><td width="20%"><div id="tmgr-cfrmt-'+fi+'"></td>'
                + '</tr>'
                + '<tr>'
                + '<td colspan="7" align="right" nowrap>Value(OGML)'
                + ' <input type="text" id="tmgr-cval-'+fi+'"size="96"/></td>'
                + '</tr>'
                + '<tr>'
                + '<td colspan="7" nowrap>'
                + 'Show <input type="checkbox"/> | '
                + 'Hidden <input type="checkbox"/> |'
                + 'Sort <input type="checkbox"/> | '
                + 'Resize <input type="checkbox"/> |' 
                + 'Size <input type="text" size="8" id="tmgr-csize-'+fi+'" />'
                + '</td>'
                + '</tr>'
                + '<tr>'
                + '<td colspan="7" class="tmgr-trow" nowrap>URL<br/><br/><br/><br/></td>'
                + '</tr>'
                + '<tr>'
                + '<tr>'
                + '<td colspan="7" class="tmgr-trow" nowrap>List<br/><br/><br/><br/></td>'
                + '</tr>'
                + '<tr>'
                + '<tr>'
                + '<td colspan="7" class="tmgr-trow" nowrap>Filter<br/><br/><br/><br/></td>'
                + '</tr>'
                + '<tr>'
                + '</table>';
            
            // type menu
            //----------
            
            var ctpeBT = new YAHOO.widget.Button(
                { type: "menu", label:"--type--" ,
                  menu: YAHOO.mbi.tablemgr.typeMenu,
                  container:'tmgr-ctype-'+fi,  
                  name:'tmgr-ctype-menu-'+fi
                });
            
            ctpeBT.getMenu().subscribe( "click",
                                        YAHOO.mbi.tablemgr.onMenuItemClick, 
                                        { "btn":ctpeBT });            
            
            // formatter menu
            //---------------
            
            var cfrmBT = new YAHOO.widget.Button(
                { type: "menu", label:"--formatter--" ,
                  menu: YAHOO.mbi.tablemgr.formMenu,
                  container:'tmgr-cfrmt-'+fi,  
                  name:'tmgr-cfrmt-menu-'+fi
                });
            
            cfrmBT.getMenu().subscribe( "click", 
                                        YAHOO.mbi.tablemgr.onMenuItemClick, 
                                        { "btn":cfrmBT } );
            
            

            // URL fields
            //-----------



            // List Fields
            //------------



            // Filter fileds
            //--------------




            // save/reset/drop buttons
            //------------------------

            var csbr =  document.createElement( "tr" );
            var csbd =  document.createElement( "td" );
            YAHOO.util.Dom.setAttribute( csbd, "colspan", "5" );
            
            csbr.appendChild( csbd );
            ctel.appendChild( csbr );
            
            csbd.innerHTML='<table width="100%"><tr>'
                + '<td width="33%" align="left" nowrap><div id="tmgr-csave'+fi+'"/></td>'
                + '<td width="33%" align="center" nowrap><div id="tmgr-creset'+fi+'"/></td>'
                + '<td width="33%" align="right" nowrap><div id="tmgr-cdrop'+fi+'"/></td>'
                +'</tr></table>';
            
            var svBT = new YAHOO.widget.Button(
                { label:"Save",id:"tmgr-csave-bt"+fi,
                  container:"tmgr-csave"+fi });
            svBT.on("click", YAHOO.mbi.tablemgr.onSaveClick );
                    

            var rsBT = new YAHOO.widget.Button(
                { label:"Reset",id:"tmgr-creset-bt"+fi,
                  container:"tmgr-creset"+fi });
            rsBT.on("click", YAHOO.mbi.tablemgr.onResetClick );

            var drBT = new YAHOO.widget.Button(
                { label:"Drop",id:"tmgr-cdrop-bt"+fi,
                  container:"tmgr-cdrop"+fi });      
            drBT.on("click", YAHOO.mbi.tablemgr.onDropClick );

            // fill in values 
            //---------------
            
            
      
        };
        
        // select initial tab once ready
        //------------------------------

        YAHOO.mbi.tablemgr.tabView.deselectTab(0);
        YAHOO.mbi.tablemgr.tabView.selectTab(1);
    };

    var tabCallback = { cache:false, timeout: 5000, success:tabInit,
                        failure:tabErr, argument:{"table":"source-list"} };
    
    try{   
        YAHOO.util.Connect.asyncRequest( 'GET','tblmgr?op.none=none',
                                         tabCallback ); 
    } catch (x) {
        alert(x);
    }
};

/* sample call

YAHOO.util.Event.addListener( window, "load", 
   YAHOO.mbi.tablemgr.load() );
*/