YAHOO.mbi.d3.pane = {

    timeout: 60000,
    w: 960,
    h: 500,
    dMAX: 1000,
    //fill: d3.scale.category20(),
    nodes: [],
    links: [],
    vnodes: [],
    vlinks: [],

    anchor: null,
    vis: null,


    ipanel: null,
    ipanelOn: false,
    iselGui: null,
    iselType: null,
    iselEl: null,

    inode: null,
    iedge: null,

    force: null,
    nnd: {},
    eed: {},

    ork: null,  // origin (node or edge)


    iconStat: null,
    iconAnim: null,

    //--------------------------------------------------------------------------

    init: function( arg ){
                    
        var pane = YAHOO.mbi.d3.pane;
        pane.nid = { ns:arg.ns, ac: arg.ac };
        pane.w = arg.w;
        pane.h = arg.h;

        var alist = YAHOO.util.Dom.getElementsByClassName(arg.anchor);
        //pane.anchor = YAHOO.util.Dom.get(arg.anchor);
        //alert("AL"+alist);
        if( alist !== undefined && alist.length>0){
            pane.anchor=alist[0]; 
        }

        //alert("A[0]"+alist[0]);
        
        var url = "rex?ns=dip&ac=DIP-311N&md=N&sl=2&nbh=1+&xf.d3j=0";        
  
        if( pane.nid.ac !== "" ){
            url = "rex?ns="+pane.nid.ns 
                + "&ac="+pane.nid.ac+"&md=N&sl=2&nbh=1+&xf.d3j=0";        
        }

        pane.vis = d3.select( "."+arg.anchor ).insert( "svg:svg")
            .attr( "width", "100%" )
            .attr( "height", "100%" );
               
        pane.vis.append( "svg:rect" )
            .attr( "width", pane.w )
            .attr( "height", pane.h )
            .attr( "id", "d3-canvas" )
            .attr( "class", "d3c");
        
        pane.force = d3.layout.force();

        pane.force.nodes( pane.vnodes )
            .links( pane.vlinks )
            .linkDistance(40)
            .linkStrength(0.5)
            .gravity(0.05)
            .charge(-100)
            .size( [ pane.w, pane.h ] );

        var cursor = pane.vis.append("svg:circle")
            .attr("r", 30)
            .attr("transform", "translate(-100,-100)")
            .attr("class", "cursor");

        pane.iconAnim = pane.vis.append("svg:image")
            .attr("x", 0)
            .attr("y", 0)
            .attr("width", 16)
            .attr("height", 16)
            .attr("xlink:href","images/dla16.gif")
            .attr("class", "dip-icon-on");

        pane.iconStat = pane.vis.append("svg:image")
            .attr("x", 0)
            .attr("y", 0)
            .attr("width", 16)
            .attr("height", 16)
            .attr("xlink:href","images/dl16.png")
            .attr("class", "dip-icon-off");
        
        YAHOO.mbi.d3.gui
            .init( { anchor: pane.anchor,
                     vis: YAHOO.mbi.d3.pane.vis, 
                     force: YAHOO.mbi.d3.pane.force });

        pane.ipanel = YAHOO.util.Dom.get('d3-info');
        pane.inode = YAHOO.util.Dom.get('d3-info-node');
        pane.iedge = YAHOO.util.Dom.get('d3-info-edge');

        pane.force.on( "tick", YAHOO.mbi.d3.pane.onTick );
        
        pane.vis.on( "mousemove", 
                     function() {
                         //cursor.attr( "transform", 
                         // "translate(" + d3.svg.mouse(this) + ")");                 
                     });

        var initCallback = { cache:false, timeout: YAHOO.mbi.d3.pane.timeout, 
                             success: pane.add,
                             failure: pane.fail,
                             argument: { op:"init", ork: pane.nid.ac } };
        
        YAHOO.util.Connect.asyncRequest( 'GET', url, initCallback );         
        
        return pane.vis;
    },

    fail: function( o ){        
        alert( JSON.stringify(o) );
    },

    getNodeInfo: function( arg ){
        
        var ns = arg.nc !== undefined ? arg.nc : 'dip';
        var ac = arg.ac;
        var callback = arg.callback;

        var url = 'rex?ns=' + ns + '&ac=' + ac + '&md=N&sl=0&nbh=0&xf.dxf=0';
        
        var nodeInfoCallback = { cache:false, timeout: YAHOO.mbi.d3.pane.timeout, 
                                 success: YAHOO.mbi.d3.pane.nodeInfoCallback,
                                 argument: { callback: callback } };
                
        YAHOO.util.Connect.asyncRequest( 'GET', url, nodeInfoCallback );         
    },

    getEdgeInfo: function( arg ){
        
        var ns = arg.nc !== undefined ? arg.nc : 'dip';
        var ac = arg.ac;
        var callback = arg.callback;

        var url = 'rex?ns=' + ns + '&ac=' + ac + '&md=N&sl=0&nbh=0&xf.dxf=0';
        
        //alert("URL=" + url);

        var edgeInfoCallback = { cache:false, timeout: YAHOO.mbi.d3.pane.timeout,  
                                 success: YAHOO.mbi.d3.pane.edgeInfoCallback,
                                 argument: { callback: callback } };
        
        YAHOO.util.Connect.asyncRequest( 'GET', url, edgeInfoCallback );
    },

    nodeInfoCallback: function( o ){
        //alert( "nodeInfoCallback:\nRT="+o.responseText );        
       
        var rdom = null;
        if( window.DOMParser ){
            var parser=new DOMParser();
            rdom = parser.parseFromString(o.responseText,"text/xml");
        } else {
            rdom = new ActiveXObject("Microsoft.XMLDOM");
            rdom.async="false";
            rdom.loadXML(o.responseText);
        }
        
        var json = YAHOO.mbi.d3.pane.xml2json(rdom);
        //alert(json);
        o.argument.callback({json:json});
    },

    edgeInfoCallback: function( o ){
        //alert( "edgeInfoCallback:\nRT="+o.responseText );        
        var rdom = null;
        if( window.DOMParser ){
            var parser=new DOMParser();
            rdom = parser.parseFromString(o.responseText,"text/xml");
        } else {
            rdom = new ActiveXObject("Microsoft.XMLDOM");
            rdom.async="false";
            rdom.loadXML(o.responseText);
        }
        
        var json = YAHOO.mbi.d3.pane.xml2json( rdom );
        //alert(json);
        o.argument.callback({json:json});
    },

    onTick: function(){
        var pane = YAHOO.mbi.d3.pane;
        pane.vis.selectAll("line.link")
            .attr( "x1", function(d) { return d.source.x; })
            .attr( "y1", function(d) { return d.source.y; })
            .attr( "x2", function(d) { return d.target.x; })
            .attr( "y2", function(d) { return d.target.y; });
        pane.vis.selectAll("circle.node")
            .attr( "cx", function(d) {                                    
                       return d.x; 
                   })
            .attr( "cy", function(d) { 
                       return d.y; 
                   });
    },

    load: function( arg ){
        // current canvas, new network

        var pane = YAHOO.mbi.d3.pane;
        pane.clean();
        pane.nid = { ns:arg.ns, ac: arg.ac };

        var url = "rex?ns=dip&ac=DIP-3256N&md=N&sl=2&nbh=1+&xf.d3j=0";        
  
        if(pane.nid.ac !== ""){
            url = "rex?ns="+pane.nid.ns 
                + "&ac="+pane.nid.ac+"&md=N&sl=2&nbh=1+&xf.d3j=0";        
        }

        //alert("NURL: "+ url);
        pane.force = d3.layout.force()
            .nodes( pane.nodes )
            .links( pane.links )
            .linkDistance(40)
            .linkStrength(0.5)
            .gravity(0.05)
            .charge(-100)
            .size( [ pane.w, pane.h ] );

        pane.force.on( "tick", YAHOO.mbi.d3.pane.onTick);
        
        pane.vis.on( "mousemove", 
                     function() {
                         //cursor.attr( "transform", 
                         // "translate(" + d3.svg.mouse(this) + ")");                 
                     });

        var initCallback = { cache:false, timeout: YAHOO.mbi.d3.pane.timeout,  
                             success: pane.add,
                             argument: { op:"init", ork: pane.nid.ac } };
        
        YAHOO.util.Connect.asyncRequest( 'GET', url, initCallback );         

        return pane.vis;
    },

    addFail: function( o ){

        YAHOO.mbi.d3.pane.busy( false );       
        
    },

    add: function( o ){
        
        var pane = YAHOO.mbi.d3.pane;
        var nnd = pane.nnd;
        var eed = pane.eed;

        var op = o.argument.op;
        if( op === "init" ){
            if( o.argument.ork !== undefined && o.argument.ork !== null ){
                pane.ork = o.argument.ork;
            }
        }
        //alert( "d3.pane.add");        

        YAHOO.mbi.d3.pane.busy( false );
       
        try{            
            //alert( o.responseText );
            var snet = YAHOO.lang.JSON.parse( o.responseText );        
            
            for( var i=0;i <snet.node.length; i++ ){
                if( nnd[ snet.node[i].key ] === undefined ){   

                    if( o.argument.point === undefined){
                        snet.node[i].x = pane.w/2 + Math.cos((Math.PI*2)/snet.node.length*i)*100;
                        snet.node[i].y = pane.h/2 + Math.sin((Math.PI*2)/snet.node.length*i)*100;
                    } else {
                        snet.node[i].x = o.argument.point[0];
                        snet.node[i].y = o.argument.point[1];                        
                    }
                    snet.node[i].vall = false;  // always visible 
                    snet.node[i].vcnd = true;   // conditionally visible
                    snet.node[i].vred = false;  // visibility reduced/collapsed 
                    snet.node[i].visible= true; // visible  
                    var n = pane.nodes.push( snet.node[i] );
                    nnd[ snet.node[i].key ] = snet.node[i];
                }
            }
            
            for( var j=0; j < snet.edge.length; j++ ){
           
                if( eed[ snet.edge[j].key ] === undefined ){                    
                    var ced = { key: snet.edge[j].key,
                                source: nnd[ snet.edge[j].source ], 
                                target: nnd[ snet.edge[j].target ] };
                   
                    pane.links.push( ced );
                    eed[ snet.edge[j].key ] = ced;
                }
            }            
            
            pane.calcDist({limit: 6});
            pane.visup();
            
            //alert("n="+pane.nodes.length + "l=" + pane.links.length);     
            //alert("vn="+pane.vnodes.length + "vl=" + pane.vlinks.length);

            if(o.argument.op === "append"){
                pane.start();
            } else {
                pane.start();
            }
        } catch (x) {
            alert( "CATCH(add):" + x );
        }
    },

    calcDist: function( o ){
      
        var pane = YAHOO.mbi.d3.pane;
        if( pane.ork === null ) return;
        var limit = o.limit === undefined ? 3 : o.limit;
        
        var cl = [];
        var md = 0;
        
        var nodes = pane.nodes;
        var links = pane.links;
        var nnd = pane.nnd;
        var eed = pane.eed;
        var MAX = pane.dMAX;
        
        if( nnd[pane.ork] !== undefined ){
            cl = [ nnd[pane.ork] ];
            nnd[pane.ork].dist = 0;
            md = 1;  // mode: node
        }
        if( eed[pane.ork] !== undefined ){
            cl = [ eed[pane.ork] ];
            eed[pane.ork].dist = 0;
            md = -1; // mode: edge
        }
                
        if( md === 0){
            return;
        } else {
            for( var i=0;i<links.length; i++ ){
                links[i].dist = MAX;
            }
            for( var i=0; i<nodes.length; i++ ){
                nodes[i].dist = MAX;
            }
        }

        for(var i=0;i< cl.length; i++){
            cl[i].dist=0;        
        }

        //alert("md=" + md + " :: " + cl[0].key + " dist(ork)=" + cl[0].dist);
        
        for( var l = 0; l < limit; l++ ){ 
            if( md == 1 ){ // looking for adjacent, unvisited edges...
                var ne = [];
                for( var ci=0; ci < cl.length; ci++ ){
                    var cn = cl[ci];
                    //alert( "cn(i)=" + cn.key );
                    for( var ei=0; ei < links.length; ei++ ){
                        var ce = links[ei];
                        //alert( "cn(ci)=" + cn.key  + " :::: ce(i): " + ce.source + " :: " + ce.target );
                        if( ce.dist > l+1 ){  // unvisited edge
                            if( ce.source.key === cn.key 
                                || ce.target.key === cn.key ){
                                    ce.dist = l+1;
                                    //alert( "cn(ci)=" + cn.key  + "("+cn.dist+") --> dist(ce)=" +ce.dist+" :::: ce(i): " + ce.source.key + " :: " + ce.target.key );
                                    ne.push( ce );
                                }
                        }
                    }
                }
                cl = ne;               
            } else { // looking for adjacent, unvisited nodes...
                var nn = [];
                for( var ci=0; ci < cl.length; ci++ ){
                    var ce = cl[ci];
                    //alert( "ce(i)=" + ce.key );
                    for( var ni=0; ni < nodes.length; ni++ ){
                        var cn = nodes[ni];
                        //alert( "ce(ci): " + ce.source + " :: " + ce.target + " :::: cn(i)=" + cn.key );
                        if( cn.dist > l+1 ){ // unvisited node
                            if( ce.source.key === cn.key || 
                                ce.target.key === cn.key ){
                                    cn.dist = l+1;
                                    //alert( "ce(ci): " + ce.key  + "("+ce.dist+") --> "+ ce.source.key + " :: " + ce.target.key + " dist(cn)="+cn.dist+" :::: cn(i)=" + cn.key );
                                    nn.push(cn);
                                } 
                        }
                    }
                }
                cl = nn;
            }
            md=-md;
        }  
    },

    busy: function( s ){
        var pane = YAHOO.mbi.d3.pane;
        if( s ){
            if( pane.iconAnim !== undefined ){
                YAHOO.util.Dom.replaceClass( pane.iconAnim, 
                                             "dip-icon-off", "dip-icon-on");
            }
            if( pane.iconStat !== undefined ){
                YAHOO.util.Dom.replaceClass( pane.iconStat, 
                                             "dip-icon-on","dip-icon-off");
            }       
        } else {
            if( pane.iconStat !== undefined ){
                YAHOO.util.Dom.replaceClass( pane.iconStat, 
                                             "dip-icon-off","dip-icon-on");
            }
            
            if( pane.iconAnim !== undefined ){
                YAHOO.util.Dom.replaceClass( pane.iconAnim, 
                                             "dip-icon-on","dip-icon-off");
            }
        } 
    },

    tinfo: function( status, el, d ){
        try{
            
            var pane = YAHOO.mbi.d3.pane;
            var gui = YAHOO.mbi.d3.gui;
            var dom = YAHOO.util.Dom;
            pane.ipanelOn = status;
            
            //alert( "pane.tinfo: el=" + el +  " d=" + d + " pane.ipanel=" + pane.ipanel );
            if( status ){                        
                if( pane.ipanel !== undefined ){
                    dom.replaceClass( pane.ipanel, 
                                      "d3-info-off", "d3-info-on");
                }       
                if( dom.hasClass( el, "node") ){  
                    gui.setNodeDetail( pane.iselEl, d ); 
                    
                    dom.replaceClass( pane.inode, 
                                      "d3-info-off", "d3-info-on");
                    dom.replaceClass( pane.iedge, 
                                      "d3-info-on", "d3-info-off");

                }
                if( dom.hasClass(el, "link") ){
                    gui.setEdgeDetail( pane.iselEl, d );                 
                    dom.replaceClass( pane.iedge, 
                                      "d3-info-off", "d3-info-on");
                    dom.replaceClass( pane.inode, 
                                      "d3-info-on", "d3-info-off");
                }
            } else {
                if( pane.ipanel !== undefined ){
                    dom.replaceClass( pane.ipanel, 
                                      "d3-info-on","d3-info-off");
                    dom.replaceClass( pane.inode, 
                                      "d3-info-on", "d3-info-off");
                    dom.replaceClass( pane.iedge, 
                                      "d3-info-on", "d3-info-off");
                }
            } 
            
        } catch (x) {
            alert(x);            
        }
    },

    append: function( arg, point ){
        //alert("append: AC=" + arg.ac + " X=" + arg.x);
        var appendCallback = { cache:false, timeout: YAHOO.mbi.d3.pane.timeout,  
                               success: YAHOO.mbi.d3.pane.add,
                               failure:YAHOO.mbi.d3.pane.addFail,
                               argument: arg };
        
        YAHOO.mbi.d3.pane.busy( true );       
        YAHOO.util.Connect.asyncRequest( 'GET', arg.url, appendCallback ); 
    },

    complete: function( arg, pAoint ){
        //alert("append: AC=" + arg.ac + " X=" + arg.x);
        var completeCallback = { cache:false, timeout: YAHOO.mbi.d3.pane.timeout,  
                                 success: YAHOO.mbi.d3.pane.add,
                                 failure:YAHOO.mbi.d3.pane.addFail,
                                 argument: arg };
        
        var completeUrl="";

        YAHOO.mbi.d3.pane.busy( true );
        YAHOO.util.Connect.asyncRequest( 'GET', completeUrl, completeCallback );
    },

    reduce: function( node, point, el ){
        try{
            if( el[1] !== undefined ){ node = el[1]; }
        
            //alert("reduce: reduced=" + node.vred + " el=" + el );
            if( node.vred === true ){
                node.vred = false;
                YAHOO.util.Dom.replaceClass(el,"node-reduced-on", "node-reduced-off");
            } else {
                node.vred = true;
                YAHOO.util.Dom.replaceClass(el,"node-reduced-off", "node-reduced-on");
            }
            
            // visibility update
            
            YAHOO.mbi.d3.pane.visup( { node:[node]} );                
            
            // force network update
            //---------------------

            YAHOO.mbi.d3.pane.start();

        } catch (x) {
            alert("reduce EX=" + x);
        }
    },

    vcndup: function(){
      
        // leafs only (NOTE: other variants posible)
        
        var pane = YAHOO.mbi.d3.pane;
        for( var j=0; j<pane.nodes.length; j++ ){
            pane.nodes[j].deg=0;
        }
        for( var j=0; j<pane.links.length; j++ ){
            var ln = pane.links[j]; 
            if( ln.source.key !== ln.target.key ){
                pane.nnd[ln.source.key].deg++;
                pane.nnd[ln.target.key].deg++;
            }
        }

        for( var j=0; j<pane.nodes.length; j++ ){
            pane.nodes[j].vcnd =
                pane.nodes[j].deg === 1 ? true : false;           
        }
    },

    visup: function( arg ){

        var pane = YAHOO.mbi.d3.pane;
        //alert("visup: n="+ pane.nodes.length+ " l=" + pane.links.length);
        //alert(typeof pane.nodes);
        
        pane.vcndup();

        for( var j=0; j<pane.nodes.length; j++ ){
            //pane.nodes[j].visible = true;
        }

        if( arg !== undefined && arg.node !== undefined){
            
            var ndl = arg.node;
            for( var i=0;i<ndl.length;i++ ){
                var nd = ndl[i];
                for( var j=0; j<pane.links.length; j++ ){
                    if( pane.links[j].source.key === nd.key ){
                        var nnd = pane.links[j].target;
                        nnd.visible = nd.vred === true ? 
                            (nnd.vall || !nnd.vcnd)
                            :
                            true; //nnd.visible;
                    }
                    
                    if( pane.links[j].target.key === nd.key ){
                        var nnd = pane.links[j].source;
                        nnd.visible = nd.vred === true ? 
                            (nnd.vall || !nnd.vcnd)
                            :
                            true; //nnd.visible;
                    }
                }       
            }
        }
        
        var vlinks=[];
        for( var j=0; j<pane.links.length; j++ ){
            var ln = pane.links[j];
            if( ln.source.visible && ln.target.visible){
                vlinks.push(ln);
            }
        }
        pane.vlinks=vlinks;
        pane.force.links(vlinks);

        var vnodes=[];
        for( var j=0; j<pane.nodes.length; j++ ){
            if(pane.nodes[j].visible){
                vnodes.push(pane.nodes[j]);
            }
        }
        pane.vnodes = vnodes;
        pane.force.nodes(vnodes);
        

        // NOTE: might need to update x,y coords of the reappearing
        //       nodes to that of the neighbor(s) ?
        
    },

    tvisible: function( node, point, el ){
        if( el[1] !== undefined ){ node = el[1]; }
        try{
            if( node.vall === undefined || node.vall === true){
                node.vall = false;
            } else {
                node.vall = true;
            }
            
        } catch (x) {
            alert("tvisible EX="+x);
        }
        //YAHOO.mbi.d3.pane.force.update();                
    },
    
    select: function( arg, point, el ){

        var pane = YAHOO.mbi.d3.pane;

        var oldSel = pane.iselGui;
        pane.iselGui=el;
        pane.iselEl = arg;

        if( arg.source === undefined ){
            pane.iselType = "node";
        } else {
            pane.iselType = "edge";
        }
        return oldSel;

    },

    unselect: function( ){

        var pane = YAHOO.mbi.d3.pane;
        var oldSel = pane.iselGui;

        pane.iselGui = null;
        pane.iselEl = null;
        pane.iselType = null;

        return oldSel;
    },

    update: function(){
     
        
   
    },
    
    start: function() {

        try{
            //alert("starting");
            //alert("vl2="+YAHOO.mbi.d3.pane.vlinks.length);

            var links = YAHOO.mbi.d3.pane.vis.selectAll( "g.link" )  // linke.link
                .data( YAHOO.mbi.d3.pane.vlinks, function(d) { return d.key; } );


            links.enter().insert( "svg:g","circle.cursor")
                .attr(  "class", "link")            
                .append("svg:a")
                .attr("xlink:title",function(d) { return d.key; })
                .append( "svg:line")                             
                .classed( "link",true)
                .attr( "id",function(d) { return "d3-edge-"+d.key; })
                .attr( "x1", function(d) { return d.source.x; })
                .attr( "y1", function(d) { return d.source.y; })
                .attr( "x2", function(d) { return d.target.x; })
                .attr( "y2", function(d) { return d.target.y; })
                .attr("ac", function(d) { return d.key; })
                .on( "click", YAHOO.mbi.d3.gui.clkD3Listener );
            
            links.exit().remove();
            //links.exit().attr( "class", "hidden");

            //alert("vn2="+YAHOO.mbi.d3.pane.vnodes.length);
            var nodes = YAHOO.mbi.d3.pane.vis.selectAll("g.node")  // circle.node ?
                .data( YAHOO.mbi.d3.pane.vnodes, function(d) { return d.key; } );
            
            nodes.enter().append( "svg:g")
                .attr(  "class", "node")
                .append("svg:a")
                .attr("xlink:title",function(d) { return d.tt; })
                .append( "svg:circle")
                .classed("node",true)
                .classed("node-pin", function(d){ return d.frozen; })
                .classed("node-d0", function(d){ return (d.dist === 0); })
                .classed("node-d1", function(d){ return (d.dist === 2); })
                .classed("node-d2", function(d){ return (d.dist === 4); })
                        
                //.attr( "class", function(d) { if(d.frozen ){
                //                                  return ["node","node-pin"];
                //                              } else { return ["node"];}
                //                            })
            
                .attr( "cx", function(d) { return d.x; })
                .attr( "cy", function(d) { return d.y; })
                .attr( "r", 5)
                .attr( "id",function(d) { return "d3-node-"+d.key; })
                .attr("ac", function(d) { return d.key; })                  
                .on( "click", YAHOO.mbi.d3.gui.clkD3Listener )
                .call( YAHOO.mbi.d3.pane.force.drag );        
            
            nodes.exit().remove();
            //nodes.exit().attr( "class", "hidden");

            if( YAHOO.mbi.d3.gui.nodeContextMenu !== undefined ){ 
                var ncm = YAHOO.mbi.d3.gui.nodeContextMenu;
                ncm.cfg
                    .setProperty( 'trigger',
                                  YAHOO.util.Dom.getElementsByClassName( "node" ));
            }
            
            if( YAHOO.mbi.d3.gui.edgeContextMenu !== undefined ){ 
                var ncm = YAHOO.mbi.d3.gui.edgeContextMenu;
                ncm.cfg
                    .setProperty( 'trigger',
                                  YAHOO.util.Dom.getElementsByClassName( "link" ));
            }
            
            if( YAHOO.mbi.d3.gui.svgContextMenu !== undefined ){ 
                var ncm = YAHOO.mbi.d3.gui.svgContextMenu;
                ncm.cfg
                    .setProperty( 'trigger',
                                  YAHOO.util.Dom.getElementsByClassName( "d3c" ));
            }
            YAHOO.mbi.d3.pane.force.start();
            
            
        } catch (x) {
            alert(x);   
        }
    },

    clean: function(){
        var pane = YAHOO.mbi.d3.pane;        

        YAHOO.mbi.d3.pane.vis.selectAll( "g.link" ).remove();
        YAHOO.mbi.d3.pane.vis.selectAll( "g.node" ).remove();

        pane.force = null;
        pane.nodes = [];        
        pane.links = [];        
        pane.nnd=[];        
        pane.eed=[];
    },

    xml2json: function( xml ){

        // After: http://davidwalsh.name/convert-xml-json

        var obj = {};

        var xmlToJson = YAHOO.mbi.d3.pane.xml2json;
        try{
            if( xml.nodeType == 1 ) { // element
                // do attributes
                if( xml.attributes.length > 0 ){
                    obj[ "@attributes" ] = {};
                    for (var j = 0; j < xml.attributes.length; j++) {
                        var attribute = xml.attributes.item(j);
                        obj[ "@attributes" ][ attribute.nodeName ] = attribute.nodeValue;
                    }
                }
            } else if (xml.nodeType == 3) { // text
                obj = xml.nodeValue;
            }
            // do children
            if( xml.hasChildNodes() ) {
                for(var i = 0; i < xml.childNodes.length; i++) {
                    var item = xml.childNodes.item(i);
                    var nodeName = item.nodeName;
                    if (typeof(obj[nodeName]) == "undefined") {
                        obj[nodeName] = xmlToJson(item);
                    } else {
                        if (typeof(obj[nodeName].push) == "undefined") {
                            var old = obj[nodeName];
                            obj[nodeName] = [];
                            obj[nodeName].push(old);
                        }
                        if(obj[nodeName]!== undefined)
                        obj[nodeName].push(xmlToJson(item));
                    }
                }
            }
            
        } catch (x) {
            alert(x);
            
        }
        return obj;  
    }
};
