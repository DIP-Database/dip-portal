/* record rendering */

YAHOO.namespace("mbi.help");

YAHOO.mbi.help = {
    
    show: function( title, id ) {

        if ( YAHOO.mbi.help.my  == null  
             || YAHOO.mbi.help.my.panel == null ) {
                 
                 YAHOO.mbi.help.my = {};
        
                 var hp = document.createElement('div');
                 document.body.appendChild(hp);
                 hp.id="help-panel";
        
                 var hphd = document.createElement('div');
                 hp.appendChild(hphd);
                 YAHOO.util.Dom.addClass(hphd,'hd');
                 
                 var hpbd = document.createElement('div');
                 hp.appendChild(hpbd);
                 YAHOO.util.Dom.addClass(hpbd,'bd');
                 
                 var hpft = document.createElement('div');
                 hp.appendChild(hpft);
                 YAHOO.util.Dom.addClass(hpft,'ft');
                 
        
                 YAHOO.mbi.help.my.panel =       
                     new YAHOO.widget.Panel("help-panel",
                                            { width: "650px",
                                              height: "450px",
                                              fixedcenter: true,
                                              close: true,
                                              draggable: true,
                                              //zindex: 4,
                                              modal: true,
                                              constraintoviewport: false,
                                              
                                              //effect:{
                                              //    effect:YAHOO.widget.ContainerEffect.FADE,duration:0.25
                                              //},
                                              visible: true 
                                            });

                 YAHOO.mbi.help.my.panel.render();
                 YAHOO.mbi.help.my.resize =
                     
                 new YAHOO.util.Resize( "help-panel",
                                        { handles: ["br"],
                                          autoRatio: false,
                                          minWidth: 350,
                                          minHeight: 100,
                                          status: false
                                        });
            
                 YAHOO.mbi.help.my.resize.on( 
                     'resize',
                     function( args ) {
                         var panelHeight = args.height; 
                         this.cfg.setProperty('height',panelHeight + "px"); 
                     }, YAHOO.mbi.help.my.panel, true ); 
                 
                 YAHOO.mbi.help.my.resize.on( 
                     'startResize',
                     function( args ) {
                         
                         if( this.cfg.getProperty('constraintoviewport') ) {
                             var D = YAHOO.util.Dom; 
                             var clientRegion = D.getClientRegion();
                             var elRegion = D.getRegion(this.element);
                             YAHOO.mbi.help.my.resize.set('maxWidth', 
                                                          clientRegion.right - elRegion.left 
                                                          - YAHOO.widget.Overlay.VIEWPORT_OFFSET );
                             YAHOO.mbi.help.my.resize.set('maxHeight', 
                                                          clientRegion.bottom - elRegion.top 
                                                          - YAHOO.widget.Overlay.VIEWPORT_OFFSET );
                         } else {
                             YAHOO.mbi.help.my.resize.set('maxWidth',null);
                             YAHOO.mbi.help.my.resize.set('maxHeight',null);
                         }
                     }, YAHOO.mbi.help.my.panel, true );   
             }

        var helpCallback = { cache:false, timeout: 5000, 
                             success: YAHOO.mbi.help.load,
                             argument: {title: title} };
        
        YAHOO.util.Connect.asyncRequest( 'GET','page?ret=body&id=' + id, 
                                         helpCallback ); 
        
        YAHOO.mbi.help.my.panel.setHeader("Help" );       
        YAHOO.mbi.help.my.panel.show();        
    },
    
    load: function( o ) {
        YAHOO.mbi.help.my.panel.setBody( '<h2>' + o.argument.title + '</h2><hr/>' + o.responseText);
        
    }
};
