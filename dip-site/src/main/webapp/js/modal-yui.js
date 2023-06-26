/* dip-portal: modal windows */

YAHOO.namespace("mbi.modal");

YAHOO.mbi.modal = {

    window: function( arg ) {
        var title = arg.title;
        var body = arg.body;
        var bodyfc = arg.bodyfc;
        YAHOO.mbi.modal.show({ "mtitle":title, "mbody":body, "mbodyfc":bodyfc });            
    },
    
    feedback: function( arg ) {
        var ns = arg.ns;
        var ac = arg.ac;
        var about = arg.about;

        var url = 'feedback?about=' + about +
            "&ret=modal" + "&ns=" + ns + "&ac=" + ac;
        YAHOO.mbi.modal.show({ mtitle:'Feedback', url: url });
    },

    cvterm: function( arg ) {
        var url = arg.url;
        YAHOO.mbi.modal.show({ mtitle:'CVTerm', url: url });
    },

    comments: function( arg ) {
        var ns = arg.ns;
        var ac = arg.ac;

        YAHOO.mbi.modal.show({ mtitle:'Comments' });            
    },

    spcstat: function( arg ) {
        var ttl = 'Details: '+ arg;
        YAHOO.mbi.modal.show({ mtitle:ttl });            
    },

    graph: function( arg ) {
        var ns = arg.ns;
        var ac = arg.ac;

        YAHOO.mbi.modal.show({ mtitle:'DIP Graph',urlXML:'graph.svg' });            
    },

    help: function( title, id ) {
        var url = 'page?ret=body&id=' + id;
        YAHOO.mbi.modal.show({ mtitle: 'Help', title: title, url: url } );
    },

    iview: function( image ) {
        var url = 'image?op.view=view&opp.name=' + image ;
        YAHOO.mbi.modal.show({ mtitle: 'Image Preview', mid: 'modal-panel', url: url } );
    },
    alignment: function( arg ) {
        var title = arg.title;
        var body = arg.body;
        
        YAHOO.mbi.modal.show({ "mtitle":title, "mbody":body, "width":"850px" });            
    },
    attachment:function( arg ){
    
        var rid = arg.rid;
        var aid = arg.aid;

        var url = 'attachmgr?op.cidg=cidg'
            + '&opp.cid=' + aid 
            + '&id=' + rid;
        
        var attSuccess = function( o ){

            var messages = YAHOO.lang.JSON.parse( o.responseText);
            if( messages.attach[0] !== undefined ){
                
                var subject = messages.attach[0].subject;
                var body = messages.attach[0].body;
                var author = messages.attach[0].author;
                var date =  messages.attach[0].date;

                var bodyHTML = '<table width="99%">'
                    + '<tr><td class="att-field-head" width="10%" nowrap>Subject:</td>'
                    + '<td>'+subject+'</td></tr>'
                    + '<tr><td class="att-field-head" nowrap>Author:</td>'
                    + '<td>' + author + '</td></tr>'
                    + '<tr><td class="att-field-head" nowrap>Date:</td>'
                    + '<td>' + date + '</td></tr>'
                    + '<tr><td colspan="2"><hr/></td></tr>'
                    + '<tr><td nowrap>&nbsp;</td>'
                    + '<td class="att-body">' + body + '</td></tr>'
                    + '</table>';

                YAHOO.mbi.modal.show( { mtitle: 'Attachment', 
                                        title: "Comment", 
                                        body: bodyHTML } );
            } 
            
        };

        var attFail = function( o ){
            
        };

        var attCallback = { cache:false, timeout: 5000, 
                            success: attSuccess,
                            failure: attFail
                          };   
        try{
            YAHOO.util.Connect.asyncRequest( 'GET', url, attCallback );        
        } catch (x) {
            alert("AJAX Error:"+x);
        }
    },
    
    show: function( arg ) {
      
        var title = arg.title;
        var mtitle = arg.mtitle === undefined ? title : arg.mtitle; 
        var mbody = arg.mbody === undefined ? "" : arg.mbody; 
        var bodyfc = arg.mbodyfc;

        var id = arg.id;
        var mid = arg.mid === undefined ? "modal-panel" : arg.mid;
        var modalWidth = arg.width === undefined ? "650px" : arg.width;
        
        if ( YAHOO.mbi.modal.my  == null  
             || YAHOO.mbi.modal.my.panel == null ) {
                 
                 YAHOO.mbi.modal.my = {};
        
                 var hp = document.createElement('div');

                 var modal = YAHOO.util.Dom.get('modal');
                 if( modal === undefined || modal === null ){
                     document.body.appendChild( hp );
                 } else {
                     modal.appendChild( hp );
                 }

                 hp.id = mid;
        
                 var hphd = document.createElement('div');
                 hp.appendChild(hphd);
                 YAHOO.util.Dom.addClass(hphd,'hd');
                 
                 var hpbd = document.createElement('div');
                 hp.appendChild(hpbd);
                 YAHOO.util.Dom.addClass(hpbd,'bd');
                 YAHOO.util.Dom.addClass(hpbd,'modal-panel-ip');
                 
                 var hpft = document.createElement('div');
                 hp.appendChild(hpft);
                 YAHOO.util.Dom.addClass(hpft,'ft');
                 
        
                 YAHOO.mbi.modal.my.panel =       
                     new YAHOO.widget.Panel( mid,
                                             { width: modalWidth,
                                               height: "450px",
                                               fixedcenter: true,
                                               close: true,
                                               draggable: true,
                                               zindex: 40,
                                               modal: true,
                                               constraintoviewport: false,
                                               visible: true 
                                             });

                 YAHOO.mbi.modal.my.panel.render();
                 YAHOO.mbi.modal.my.resize =
                     new YAHOO.util.Resize( mid,
                                            { handles: ["br"],
                                              autoRatio: false,
                                              minWidth: 350,
                                              minHeight: 100,
                                              status: false
                                            });
                 
                 YAHOO.mbi.modal.my.resize.on( 
                     'resize',
                     function( args ) {
                         var panelHeight = args.height; 
                         this.cfg.setProperty('height',panelHeight + "px"); 
                     }, YAHOO.mbi.modal.my.panel, true ); 
                 
                 YAHOO.mbi.modal.my.resize.on( 
                     'startResize',
                     function( args ) {
                        
                         if( this.cfg.getProperty('constraintoviewport') ) {
                             var D = YAHOO.util.Dom; 
                             var clientRegion = D.getClientRegion();
                             var elRegion = D.getRegion(this.element);
                             YAHOO.mbi.modal.my.resize.set('maxWidth', 
                                                          clientRegion.right - elRegion.left 
                                                          - YAHOO.widget.Overlay.VIEWPORT_OFFSET );
                             YAHOO.mbi.modal.my.resize.set('maxHeight', 
                                                          clientRegion.bottom - elRegion.top 
                                                          - YAHOO.widget.Overlay.VIEWPORT_OFFSET );
                         } else {
                             YAHOO.mbi.modal.my.resize.set('maxWidth',null);
                             YAHOO.mbi.modal.my.resize.set('maxHeight',null);
                         }
                     }, YAHOO.mbi.modal.my.panel, true );   
             }
        
        YAHOO.mbi.modal.my.panel.setBody("");

        if ( arg.url != undefined ) {
            var helpCallback = { cache:false, timeout: 5000, 
                                 success: YAHOO.mbi.modal.load,
                                 argument: {title: title} };
            YAHOO.util.Connect.asyncRequest( 'GET', arg.url, helpCallback ); 
            YAHOO.mbi.modal.my.panel.setHeader( mtitle );       
            YAHOO.mbi.modal.my.panel.show();        
            return;
        } 
        
        if ( arg.urlXML != undefined ) {
            var xmlCallback = { cache:false, timeout: 5000, 
                                 success: YAHOO.mbi.modal.loadXml,
                                 argument: {title: title} };
            YAHOO.util.Connect.asyncRequest( 'GET', arg.urlXML, xmlCallback ); 
            YAHOO.mbi.modal.my.panel.setHeader( mtitle );       
            YAHOO.mbi.modal.my.panel.show();        
            return;
        } 
        

  
        if( bodyfc !== undefined ){
                bodyfc();         
        } else {
            YAHOO.mbi.modal.my.panel.setBody( mbody );
        }
        
        YAHOO.mbi.modal.my.panel.setHeader( mtitle );       
        YAHOO.mbi.modal.my.panel.show();        
    },
    
    load: function( o ) {
        var body = o.responseText;
        
        if ( o.argument.title !== undefined && o.argument.title.length > 0 ){
            body = '<h2>' + o.argument.title + 
                '</h2><hr/>' + body;               
        } 
        YAHOO.mbi.modal.my.panel.setBody( body );               
    },

    loadXml: function( o ) {
        
        var body='<iframe src="/dip-portal/graph.svg" width="280" height="280" frameborder="0">';
        //var body = o.responseXML;
        //alert(body);

        if ( o.argument.title !== undefined && o.argument.title.length > 0 ){
            body = '<h2>' + o.argument.title + 
                '</h2><hr/>' + body;               
        } 
        YAHOO.mbi.modal.my.panel.setBody( body );               
    }
};
