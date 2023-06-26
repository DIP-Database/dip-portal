YAHOO.namespace( "mbi.d3" );

YAHOO.mbi.d3.gui = {

    vis: null,
    force: null,
    keys: null,
    anchor: null,
    
    graphContextMenu: null,
    nodeContextMenu: null,
    edgeContextMenu: null,

    graphContextMenuRendered: null,
    nodeContextMenuRendered: null,
    edgeContextMenuRendered: null,

    //--------------------------------------------------------------------------

    init: function( arg ){
        //alert("d3 gui.init");

        var gui = YAHOO.mbi.d3.gui;
        gui.anchor=arg.anchor;
        gui.vis = arg.vis;
        gui.force = arg.force;
        gui.keys = [             
            { cd:67, chr:"C", down:false, 
              fn: gui.center },  // center            
            { cd:73, chr:"I", down:false, 
              fn: gui.info  },  // show info 
            { cd:78, chr:"N", down:false, 
              fn: YAHOO.mbi.d3.pane.append }, // neighbor
            { cd:80, chr:"P", down:false, 
              fn: gui.tpin  },                // pin down
            { cd:82, chr:"R", down:false, 
              fn: YAHOO.mbi.d3.pane.reduce }, // reduce
            { cd:86, chr:"V", down:false, 
              fn: YAHOO.mbi.d3.pane.tvisible } // visible

        ];
             
        var kl = [];
        for( var i=0; i < gui.keys.length; i++ ){
            kl.push( gui.keys[i].cd );
        }
        
        var kd = new YAHOO.util.KeyListener( document, { keys: kl },
                                             { fn: gui.kdListener,
                                               scope: gui,
                                               correctScope: true },
                                             'keydown' );
        kd.enable();
 
        var ku = new YAHOO.util.KeyListener( document, { keys: kl },
                                             { fn: gui.kuListener,
                                               scope: gui,
                                               correctScope: true },
                                             'keyup' );
        ku.enable();

        

        d3.select(gui.anchor).append( "xhtml:div" )
            .attr("id","d3-info");
        
        gui.buildNodeInfo({anchor:"#d3-info"});
        gui.buildEdgeInfo({anchor:"#d3-info"});

        gui.graphContextMenu = new YAHOO.widget.ContextMenu( 
            YAHOO.util.Dom.generateId(),
            { trigger: YAHOO.util.Dom.getElementsByClassName("d3c"),
              itemData: [],
              lazyLoad:true,
              zindex: 50
            });
        
        gui.nodeContextMenu = new YAHOO.widget.ContextMenu( 
            YAHOO.util.Dom.generateId(),
            { trigger: [],
              itemData: [],
              lazyLoad:true,
              zindex: 50
            });
        
        gui.edgeContextMenu = new YAHOO.widget.ContextMenu( 
            YAHOO.util.Dom.generateId(),
            { trigger: [],
              itemData: [],
              lazyLoad:true,
              zindex: 50
            });
/* 
        gui.edgeContextMenu.subscribe( 
            "click", 
            function( type, arg ){
                var ev = arg[0],
                menuItem = arg[1],
                target = this.contextEventTarget;
                
                var pane = YAHOO.mbi.d3.pane;                            
                var ac= target.getAttribute("ac");
                var edge = YAHOO.mbi.d3.pane.eed[ac];
                try{
                    
                    switch( menuItem.value ){
                    case "info": 
                        //alert("Y "+ pane.ipanelOn);
                        if( pane.ipanelOn === true ){
                            pane.ipanelOn = false;
                        } else {
                            pane.ipanelOn = true;
                        }
                        pane.tinfo( pane.ipanelOn, target, edge );
                        //alert("YY");
                        
                        gui.tinfo( edge, null, target);    
                        break;
                    default:   
                        alert("item=" + menuItem.value);    
                    }
                    
                } catch (x) {
                    alert(x);
                }
            });
*/       
        gui.graphContextMenu
            .subscribe( "triggerContextMenu", gui.onTriggerGraphContextMenu);
        gui.graphContextMenu
            .subscribe( "hide", gui.onGraphContextMenuHide);
        gui.graphContextMenu
            .subscribe( "render", gui.onGraphContextMenuRender);
        
        gui.nodeContextMenu.subscribe( "triggerContextMenu",
                                       gui.onTriggerNodeContextMenu);
        gui.nodeContextMenu.subscribe( "hide",
                                       gui.onNodeContextMenuHide);
        gui.nodeContextMenu.subscribe( "render",
                                       gui.onNodeContextMenuRender);

        gui.edgeContextMenu.subscribe( "triggerContextMenu",
                                       gui.onTriggerEdgeContextMenu);
        gui.edgeContextMenu.subscribe( "hide",
                                       gui.onEdgeContextMenuHide);
        gui.edgeContextMenu.subscribe( "render",
                                       gui.onEdgeContextMenuRender);
        
        if(gui.anchor !== null ){
            //alert("ANC...");
            gui.graphContextMenu.render( gui.anchor );
        } else {
            //alert("BDY...");
            //gui.svgContextMenu.render( document.body );
        }

        YAHOO.util.Event.on( "d3-canvas", "click", gui.onCanvasClick );
    },
/*    
    onNodeContextMenuClick: function( type, arg ){
        var ev = arg[0],
        menuItem = arg[1],
        target = this.contextEventTarget;

        var gui = YAHOO.mbi.d3.gui;
        alert("NodeContextMenuClick");        
        try{
            
            var pane = YAHOO.mbi.d3.pane;
            var ac= target.getAttribute("ac");
            var node = pane.nnd[ac];

            switch( menuItem.value ){
            case "info": 
                //alert("X");
                if( pane.ipanelOn === true ){
                    pane.ipanelOn = false;
                } else {
                    pane.ipanelOn = true;
                }
                pane.tinfo( pane.ipanelOn, target, node );
                //alert("XX");
                
                gui.tinfo( node, null, target);    
                break;
            case "tpin": gui.tpin( node, null, target );   
                break;
            case "avis": pane.tvisible( node, null, target );   
                break;
            case "treduced": pane.reduce( node, null, target );   
                break;
            case "tcenter": gui.tcenter( node, null, target );   
                break;
            case "append-n1": gui.append( node, "1", target );   
                break;
            case "append-n1+": gui.append( node, "1+", target );   
                break;
            default:   
                alert("item=" + menuItem.value);    
            }
        } catch (x) {
            alert(x);
        }   
    },
*/   
    unsetNodeDetail: function(){
        var dom = YAHOO.util.Dom;
        try{
            var d3inl =dom.getElementsByClassName("d3-info-node");
            if(d3inl !== null && d3inl.length>0){
                dom.replaceClass( d3inl[0],"d3-info-on","d3-info-off");
            }
            dom.get( "d3-info-node-ac" ).innerHTML="[-----]";
        } catch (x) {
            alert(x);
        }
    },

    setNodeDetail: function( d, ds ){
        //alert( "setNodeDetail: d=" + d + " ds=" + ds 
        //       + "infoOn="+YAHOO.mbi.d3.pane.ipanelOn); 
        var dom = YAHOO.util.Dom;
        if( d === null && ds === null) { return; }
        if( d === null) { d = ds; }
        if( YAHOO.mbi.d3.pane.ipanelOn ){
            try{
                var ac = d.ac;
                ac = ac.replace("DIP-","DIP<br/>");
                dom.get( "d3-info-node-ac" ).innerHTML=ac;
               
                var d3iel =dom.getElementsByClassName( "d3-info-edge" );
                var d3inl =dom.getElementsByClassName( "d3-info-node" );

                if( d3iel !== null && d3iel.length>0 
                    && d3inl !== null && d3inl.length>0){
                    
                    dom.replaceClass( d3iel[0],"d3-info-on","d3-info-off");
                    dom.replaceClass( d3inl[0],"d3-info-off","d3-info-on");
                }
                //alert('getNodeInfo');
                YAHOO.mbi.d3.pane
                    .getNodeInfo( { ns:d.ns, ac: d.ac,
                                    callback: YAHOO.mbi.d3.gui.setNodeDetailCallback });
            } catch (x) {
                alert("XX:"+x);
            }
        }
    },

    setNodeDetailCallback: function( arg ){

        //alert('setNodeDetailCallback: called');
        //alert(YAHOO.lang.JSON.stringify(arg.json));

        var dom = YAHOO.util.Dom;

        var name = arg.json["dataset"]["node"]["name"]["#text"];
        dom.get( "d3-info-node-name" ).innerHTML=name;
        var label="&nbsp;";
        var elLabel =arg.json["dataset"]["node"]["label"];
        if( elLabel !== undefined && elLabel["#text"] !== undefined){
            label = arg.json["dataset"]["node"]["label"]["#text"]; 
        }  
        
        dom.get( "d3-info-node-shn" ).innerHTML=label;

        var xref =arg.json["dataset"]["node"]["xrefList"]["xref"];
        

        dom.get( "d3-info-node-swp" ).innerHTML="&nbsp;";
        dom.get( "d3-info-node-rsq" ).innerHTML="&nbsp;";
        dom.get( "d3-info-node-ezg" ).innerHTML="&nbsp;";

        for( var i=0; i<xref.length; i++){ 
            if( xref[i]["@attributes"]["typeAc"] === "dxf:0006" &&
                xref[i]["@attributes"]["ns"] === "uniprot"){
                dom.get( "d3-info-node-swp" ).innerHTML=xref[i]["@attributes"]["ac"];
            }
            if( xref[i]["@attributes"]["typeAc"] === "dxf:0006" &&
                xref[i]["@attributes"]["ns"] === "refseq"){
                dom.get( "d3-info-node-rsq" ).innerHTML=xref[i]["@attributes"]["ac"];
            }
            if( xref[i]["@attributes"]["typeAc"] === "dxf:0022" &&
                xref[i]["@attributes"]["ns"] === "entrezgene"){
                dom.get( "d3-info-node-ezg" ).innerHTML=xref[i]["@attributes"]["ac"];
            }
            try{
                
            if( xref[i]["@attributes"]["typeAc"] === "dxf:0007"){
                var taxid = xref[i]["@attributes"]["ac"];               
                var txNode =xref[i]["node"];
                var org = txNode["label"]["#text"];
                var txVar = org + " TaxID[" + taxid + "]";
                dom.get( "d3-info-node-taxon" ).innerHTML = txVar; 
            }

            } catch (x) {
                alert(x);
            }
        }
        
    },

    fillVertexInfo: function( arg ){
        var d = arg.ndd;
        var acEl =arg.ndAc;
        var nmEl =arg.ndName;
  
        acEl.innerHTML=d["@attributes"]["ac"];
        acEl.ac=d["@attributes"]["ac"];
        nmEl.innerHTML=d["name"]["#text"];
        
        YAHOO.util.Event.on( 
            acEl, "mouseover",
            function(e){
                var dom =YAHOO.util.Dom;
                dom.replaceClass( dom.get( "d3-node-"+this.ac ),
                                  "d3-select-off", "d3-select-on" );
                             });
        YAHOO.util.Event.on( 
            acEl, "mouseout",
            function(e){
                var dom =YAHOO.util.Dom;
                dom.replaceClass( dom.get( "d3-node-"+this.ac),
                                  "d3-select-on", "d3-select-off" );
            });
    },

    setEdgeDetailCallback: function( arg ){
    
        var dom = YAHOO.util.Dom;
        var gui = YAHOO.mbi.d3.gui;
        try{
            //alert(YAHOO.lang.JSON.stringify(arg.json));           
            var p1 =arg.json["dataset"]["node"]["partList"]["part"][0]["node"];
            gui.fillVertexInfo( {ndAc:dom.get("d3-info-node1-ac"), 
                                 ndName: dom.get("d3-info-node1-name"), 
                                 ndd:p1} );
            
            var p2 =arg.json["dataset"]["node"]["partList"]["part"][1]["node"];
            gui.fillVertexInfo( {ndAc:dom.get("d3-info-node2-ac"), 
                                 ndName: dom.get("d3-info-node2-name"), 
                                 ndd:p2} );

            var evlHTML="";
     
            var xrl = arg.json["dataset"]["node"]["xrefList"]["xref"];
            if( typeof(xrl.push) == "undefined") {
                var old = xrl;
                xrl = [];
                xrl.push(old);
            }
            
            for( var i=0; i<xrl.length; i++ ){
                //alert(i+": "+YAHOO.lang.JSON.stringify(xrl[i]));
                
                if( xrl[i]["@attributes"]["typeAc"] === "dxf:0008" ){
                    var xnd = xrl[i]["node"];
                    //alert("NODE: "+xnd);
                    
                    var xxrl = xnd["xrefList"]["xref"];
                    if( typeof(xxrl.push) == "undefined") {
                        var old = xxrl;
                        xxrl = [];
                        xxrl.push(old);
                    }
                    
                    var pmid="";
                    for(var j=0;j<xxrl.length;j++){
                        //alert("XREF:" + xxrl[j]["@attributes"]["ns"] + " :: " + xxrl[j]["@attributes"]["ac"]);
                        if(xxrl[j]["@attributes"]["ns"] == "PubMed"){
                            pmid=xxrl[j]["@attributes"]["ac"];
                        }
                    }
                    
                    evlHTML += xnd["@attributes"]["ac"];
                    
                    if( pmid == "" ){
                        evlHTML +="; ";
                    } else 
                       evlHTML += " (PMID:" + pmid + "); "; 
                }
            }
            dom.get( "d3-info-evid" ).innerHTML = evlHTML;
            

        } catch (x) {
            alert(x);
        }
    },

    unsetEdgeDetail: function(){
        var dom = YAHOO.util.Dom;
        try{
            
            var d3iel =dom.getElementsByClassName("d3-info-edge");
            if(d3iel !== null && d3iel.length>0){
                dom.replaceClass( d3iel[0],"d3-info-on","d3-info-off");
            }
            
            dom.get( "d3-info-edge-ac" ).innerHTML="[-----]";
        } catch (x) {
            alert(x);
        }
    },

    setEdgeDetail: function( d, ds ){
        var dom = YAHOO.util.Dom;
        if( d === null && ds === null ) { return; }        
        if( d === null) { d = ds; }
        if( YAHOO.mbi.d3.pane.ipanelOn ){
            try{
                var ac = d.key;  
                ac = ac.replace("DIP-","DIP<br/>");
                dom.get( "d3-info-edge-ac" ).innerHTML=ac;

                var d3iel =dom.getElementsByClassName( "d3-info-edge" );
                var d3inl =dom.getElementsByClassName( "d3-info-node" );

                if( d3iel !== null && d3iel.length>0 
                    && d3inl !== null && d3inl.length>0){
                    
                    dom.replaceClass( d3inl[0],"d3-info-on","d3-info-off");
                    dom.replaceClass( d3iel[0],"d3-info-off","d3-info-on");
                }

                YAHOO.mbi.d3.pane
                    .getEdgeInfo( { ns:d.ns, ac: d.key,
                                    callback: YAHOO.mbi.d3.gui.setEdgeDetailCallback });

            } catch (x) {
                alert(x);
            }
        }
    },
    
    onCanvasClick: function(){
        var oldSel = YAHOO.mbi.d3.pane.unselect();
        if( oldSel !== null ){
            YAHOO.util.Dom
                .replaceClass( oldSel ,"d3-select-on", "d3-select-off");
        }
        YAHOO.mbi.d3.gui.unsetNodeDetail();
        YAHOO.mbi.d3.gui.unsetEdgeDetail();
    },

    tinfo: function( arg, point, el ){
        if( el[1] !== undefined ){ arg = el[1]; }
        var pane = YAHOO.mbi.d3.pane;
        if( pane.ipanelOn ){
            var oldSel = YAHOO.mbi.d3.pane.select( arg, point, el );
            if( oldSel !== null ){
                YAHOO.util.Dom
                    .replaceClass( oldSel ,"d3-select-on", "d3-select-off");
            }
            YAHOO.util.Dom
                .replaceClass( el ,"d3-select-off", "d3-select-on");            
        }
    },

    info: function( arg, point, el ){
        if( el[1] !== undefined ){ arg = el[1]; }

        //var point = arg.point;
        alert( "INFO: AC=" + arg.ac 
               + "\n [x,y]=" + point[0] + " " + point[1] 
               + "\n [cx,cy]=["+ el.getAttribute('cx')+ "," 
               + el.getAttribute('cy') + "]"
               + "\ndegree=" + YAHOO.mbi.d3.pane.nnd[arg.ac].deg 
               + "\nvall=" + YAHOO.mbi.d3.pane.nnd[arg.ac].vall 
               + " vcnd=" + YAHOO.mbi.d3.pane.nnd[arg.ac].vcnd 
               + " vred=" + YAHOO.mbi.d3.pane.nnd[arg.ac].vred 
               + " :: VIS=" + YAHOO.mbi.d3.pane.nnd[arg.ac].visible 
               + "\n center=" + YAHOO.mbi.d3.pane.nnd[arg.ac].center 
               + "\n frozen=" + YAHOO.mbi.d3.pane.nnd[arg.ac].frozen 
               + "\n fix=" + YAHOO.mbi.d3.pane.nnd[arg.ac].fixed);

        var a ="";
        for( var i = 0; i < YAHOO.mbi.d3.pane.nodes.length; i++ ){
            a += "ac=" + 
                YAHOO.mbi.d3.pane.nnd[YAHOO.mbi.d3.pane.nodes[i].ac].ac +
                " fix="+ 
                YAHOO.mbi.d3.pane.nnd[YAHOO.mbi.d3.pane.nodes[i].ac].fixed +
                " frozen="+ 
                YAHOO.mbi.d3.pane.nnd[YAHOO.mbi.d3.pane.nodes[i].ac].frozen +
                "\n";            
        }
        //alert( a );
    },
    
    center: function( arg, point, el ){
        if( el[1] !== undefined ){ arg = el[1]; } 

        //var point = arg.point;
        var nl = YAHOO.mbi.d3.pane.nodes;
        //alert("C: " + nl.length);

        for( var i=0; i<nl.length; i++){
            if ( nl[i].ac === arg.ac ){
                nl[i].center = true;
            } else {
                nl[i].center = false;
            }
        }
        
        YAHOO.mbi.d3.pane.force.start();
    },
    
    tcenter: function( arg, point, el ){
        if( el[1] !== undefined ){ arg = el[1]; }
        if ( arg.center === true){
            arg.center = false;
        } else {
            arg.center = true;
        }
        
        YAHOO.mbi.d3.pane.force.start();
    },

    tpin: function( arg, point, el ){
        if( el[1] !== undefined ){ arg = el[1]; }
        // NOTE: add edge pinning

        if( arg.frozen === true ){
            arg.fixed = false;
            arg.frozen = false;
            YAHOO.util.Dom.replaceClass(el,"node-pin", "node-free");
        } else {
            arg.fixed = true;
            arg.frozen = true;          
            YAHOO.util.Dom.replaceClass(el,"node-free", "node-pin");
        }
        
        YAHOO.mbi.d3.pane.force.start();                
    },
    
    tdetails: function( arg, point, el ){
        
        if( el[1] !== undefined ){ arg = el[1]; }
        var pane = YAHOO.mbi.d3.pane;
        
        if( pane.ipanelOn === true ){
            pane.ipanelOn = false;
        } else {
            pane.ipanelOn = true;
        }

        pane.tinfo( pane.ipanelOn, null, arg );
 
    },

    append: function( arg, point, el ){
        if( el[1] !== undefined ){ arg = el[1]; }
        YAHOO.mbi.d3.pane.append( arg, point, el  );
    },

    reduce: function( arg, point, el ){
        if( el[1] !== undefined ){ arg = el[1]; }
        YAHOO.mbi.d3.pane.reduce( arg, point, el  );
    },
    
    dclkListener: function( d, i ){
        var point = d3.svg.mouse( this );
        var gui = YAHOO.mbi.d3.gui;
        try{

            for( var k = 0; k < gui.keys.length; k++ ){                
                if( gui.keys[k].down === true ){
                    gui.keys[k].fn( d, point, this  );
                    return;
                }
            }
            var a = "KEYS:";
            for( var k = 0; k < gui.keys.length; k++ ){                
                a += "\n" + gui.keys[k].chr + " : "+ gui.keys[k].down;                
            }
            
            //alert(a);

        } catch (x) {
            alert(x);
        }
    },

    clkD3Listener: function( d, i ){
        var point = d3.svg.mouse( this );
        var gui = YAHOO.mbi.d3.gui;
        var pane = YAHOO.mbi.d3.pane;
        //alert("D3L: d=" + d + " this=" + this );
        try{
            
            for( var k = 0; k < gui.keys.length; k++ ){                
                if( gui.keys[k].down === true ){
                    gui.keys[k].fn( d, point, this  );
                    return;
                }
            }
         
            // select
   
            var oldSel = YAHOO.mbi.d3.pane.select( d, point, this );
            if( oldSel !== null ){
                YAHOO.util.Dom
                    .replaceClass( oldSel ,"d3-select-on", "d3-select-off");
            } 

            pane.tinfo( pane.ipanelOn, this );
            YAHOO.util.Dom
                .replaceClass( this ,"d3-select-off", "d3-select-on");
        } catch (x) {
            alert(x);
        }
    },
    
    // keyboard state

    kdListener: function( type, args, object ){
        
        var gui = YAHOO.mbi.d3.gui;
        
        try{           
            for( var i =0; i< gui.keys.length; i++ ){
                gui.keys[i].down = false;            
                if( gui.keys[i].cd === args[0] ){
                    gui.keys[i].down = true;
                }
            }
        } catch (x) {
            alert(x);
        }
    },

    kuListener: function( type, args, object ){

        var gui = YAHOO.mbi.d3.gui;
        
        try{
            for( var i =0; i< gui.keys.length; i++ ){
                if( gui.keys[i].cd === args[0] ){
                    gui.keys[i].down = false;
                } else {
                    gui.keys[i].down = false;
                }
            }
        } catch (x) {
            alert(x);
        }
    },


    // graph context menu
    //-------------------

    onTriggerGraphContextMenu: function( p_sType, p_aArgs ){
        var target = this.contextEventTarget;
      
        var gui=YAHOO.mbi.d3.gui;
        var pane=YAHOO.mbi.d3.pane;
        
        if( this.getRoot() == this ){
            try{                   
                var arg = [target, pane.iselGui];
                var itemData = [
                    [{ text: "Reduce All", 
                       onclick: {fn: gui.reduce, obj: arg}}
                    ],
                    [
                     { text: "Preferences", 
                       submenu: {
                           id: "panel-pref",
                           itemdata: [
                               { text: "Details",checked: pane.ipanelOn ,
                                 onclick: {fn: gui.tdetails, obj: arg}
                               }]
                       }
                     }
                    ]
                ];
                this.clearContent();
                this.addItems( itemData );
                this.setItemGroupTitle ("DIP Graph", 0 );
                
                if( !gui.graphContextMenuRendered){
                    //this.render( this.cfg.getProperty("container"));
                    this.render("d3-menu");
                }else{
                    this.render("d3-menu");
                }               
            } catch (x) {
                alert(x);
            }     
        }
    },

    onGraphContextMenuHide: function(p_sType, p_aArgs){
        
    },

    onGraphContextMenuRender: function( p_sType, p_aArgs ){
        YAHOO.mbi.d3.gui.graphContextMenuRendered = true;    
    },

    // node context menu
    //------------------

    onTriggerNodeContextMenu: function(p_sType, p_aArgs){
        var target = this.contextEventTarget;
        //alert("triger");
        var gui=YAHOO.mbi.d3.gui;
        var pane=YAHOO.mbi.d3.pane;
        
        if( this.getRoot() == this ){
            try{   
                var ac= target.getAttribute("ac");
                var node = YAHOO.mbi.d3.pane.nnd[ac];
                node.vall = node.vall === undefined ? true : node.vall;
                
                var arg = [target, node];

                var itemData = [
                    [{ text: "Pin Down/Up", checked: node.frozen,
                       onclick: {fn: gui.tpin, obj: arg}},
                     { text: "Center On/Off", checked: node.center,
                       onclick: {fn: gui.tcenter, obj: arg}},
                     { text: "Collapse On/Off", checked: node.vred,
                       onclick: {fn: gui.reduce, obj: arg}},
                     { text: "Add Neighbors", 
                       submenu: {
                           id: "adn",
                           itemdata: [
                               { text: "Next", 
                                 onclick: {fn: gui.append, obj: [target, node,"1"]}
                               },
                               { text: "Next Plus",
                                 onclick: {fn: gui.append, obj: [target,node,"1+"]}
                               }]
                       }
                     }
                    ],
                    [
                     { text: "Preferences",
                       submenu: {
                           id: "prefs",
                           itemdata: [                          
                               { text: "Always Visible", checked: node.vall ,
                                 onclick: {fn:pane.tvisible, obj: arg}}]
                       }
                     }
                    ]
                ];
                this.clearContent();
                this.addItems( itemData );
                this.setItemGroupTitle (ac, 0 );
                
                if( !gui.nodeContextMenuRendered){
                    //this.render( this.cfg.getProperty("container"));
                    this.render("d3-menu");
                }else{
                    this.render("d3-menu");
                }
                //this.subscribe( "click", gui.onNodeContextMenuClick );   
            } catch (x) {
                alert(x);
            }     
        }
    },

    onNodeContextMenuHide: function(p_sType, p_aArgs){
        
    },

    onNodeContextMenuRender: function( p_sType, p_aArgs ){
        YAHOO.mbi.d3.gui.nodeContextMenuRendered = true;    
    },

    // edge context menu
    //------------------

    onTriggerEdgeContextMenu: function(p_sType, p_aArgs){
        var target = this.contextEventTarget;
        var gui=YAHOO.mbi.d3.gui;
        var pane=YAHOO.mbi.d3.pane;
        
        if( this.getRoot() == this ){
            try{                   
                var ac= target.getAttribute("ac");
                var edge = YAHOO.mbi.d3.pane.eed[ac];

                edge.vall = edge.vall === undefined ? true : edge.vall;
                var arg = [target, edge];
                
                var itemData = [
                    [{ text: "Pin Down", value: "info",
                       onclick: {fn: gui.tpin, obj: arg}}
                    ]
                ];
                this.clearContent();
                this.addItems( itemData );
                this.setItemGroupTitle (ac, 0 );
                
                if( !YAHOO.mbi.d3.gui.edgeContextMenuRendered){
                    //this.render( this.cfg.getProperty("container") );
                    this.render("d3-menu");
                }else{
                    this.render("d3-menu");
                }        
  
            } catch (x) {
                alert(x);
            }
        }     
    },

    onEdgeContextMenuHide: function(p_sType, p_aArgs){
        
    },

    onEdgeContextMenuRender: function( p_sType, p_aArgs ){
        YAHOO.mbi.d3.gui.edgeContextMenuRendered = true;    
    },

    buildNodeInfo: function( arg ){
        var nodeInfoHtml
            ="<table border='0' cellspacing='1px' cellpadding='2px' width='100%'>"+
            "<tr align='center'>" +
            "<th width='15%' class='d3-info-idcell' id='d3-info-node-ac'>[-----]</th>"+
            "<th align='right' class='d3-info-cell' width='10%'>UniProtKB</th>"+
            "<td align='left' width='18%' class='d3-info-cell' id='d3-info-node-swp'>[-----]</td>"+
            "<th align='right' width='10%' class='d3-info-cell'>RefSeq</th>"+
            "<td align='left' width='18%' class='d3-info-cell' id='d3-info-node-rsq'>[-----]</td>"+
            "<th align='right' width='10%' class='d3-info-cell' >Entrez Gene</th>"+
            "<td align='left' width='18%' class='d3-info-cell' id='d3-info-node-ezg'>[-----]</td>"+
            "</tr>"+
            "<tr>"+
            "<th align='center' rowspan='2' class='d3-info-sidcell' class='d3-info-cell' id='d3-info-node-shn'>UFE1</th>"+
            "<th align='right' width='10%' class='d3-info-cell'>Name/Description</th>"+
            "<td colspan='5' width='20%' class='d3-info-cell'id='d3-info-node-name'>[-----]</td>"+
            "</tr>"+
            "<tr>"+
            "<th align='right' class='d3-info-cell'>Organism</th>"+
            "<td colspan='5' class='d3-info-cell'id='d3-info-node-taxon'>[-----]</td>"+
            "</tr>"+
            "</table>";

        var anchor = arg.anchor;
        d3.select(anchor).append( "xhtml:div" )
            .attr("class","d3-info-node d3-info-off")
            .html(nodeInfoHtml);
    },

    buildEdgeInfo: function( arg ){
        var edgeInfoHtml 
            ="<table border='0' cellspacing='1px' cellpadding='2px' width='100%'>"+
            "<tr>"+
            "<td width='15%' class='d3-info-idcell' rowspan='2' align='center'>"+
            "<table border='0' cellpadding='0' cellspacing='0'>"+
            "<tr>"+
            "<th width='95%' class='d3-info-idcell' id='d3-info-edge-ac'>[-----]</th>"+
            "</tr>"+
            "</table>"+
            "</td>"+
            "<th width='10%' rowspan='1' class='d3-info-sidcell' id='d3-info-node1-ac'>[-----]</th>"+
            "<td colspan='5' class='d3-info-cell' id='d3-info-node1-name'>[-----]</td>"+
            "</tr>"+
            "<tr>       "+
            "<th width='10%' rowspan='1' class='d3-info-sidcell' id='d3-info-node2-ac'>[-----]</th>"+
            "<td colspan='5' class='d3-info-cell' id='d3-info-node2-name'>[-----]</td>"+
            "</tr>"+
            "<tr>"+
            "<th rowspan='1' class='d3-info-cell d3-info-cnf-core' id='d3-info-edge-conf'>CORE</th> "+
            "<th align='right' class='d3-info-cell' rowspan='2'>Evidence</th>"+
            "<td align='left' rowspan='2' class='d3-info-cell' id='d3-info-evid'>DIP-12324X (PMID:12345), DIP-3235X (PMID: 12345)</td>"+
            "</tr>"+
            "<tr>"+
            "<th rowspan='1' class='d3-info-itp-phy' id='d3-info-edge-type'>Physical</th>"+
            "</table>";
        
        var anchor = arg.anchor;
        d3.select(anchor).append( "xhtml:div" )
            .attr("class","d3-info-edge d3-info-off")
            .html( edgeInfoHtml );
    }
};
