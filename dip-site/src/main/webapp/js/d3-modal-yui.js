/* dip-portal: modal windows */

YAHOO.namespace("mbi.d3.modal");

// YAHOO.mbi.d3.modal.init({ns:"dip", ac:"DIP-2942N"});

YAHOO.mbi.d3.modal = {

    w: 800,
    h: 600,    

    d3pane: null,

    init: function( arg ) {
        var ns = arg.ns;
        var ac = arg.ac;
        //alert(ns+"::"+ac);
        YAHOO.mbi.d3.modal.show({ mtitle:'DIP Graph', ns:ns, ac:ac }); 
    },
    
    show: function( arg ) {
      
        var title = arg.title;
        var mtitle = arg.mtitle === undefined ? title : arg.mtitle; 
        var mbody = arg.mbody === undefined ? "" : arg.mbody; 
        var bodyfc = arg.mbodyfc;

        var id = arg.id;
        var mid = arg.mid === undefined ? "panel-d3" : arg.mid;

        if ( YAHOO.mbi.d3.modal.my  == null  
             || YAHOO.mbi.d3.modal.my.panel == null ) {
                 
                 YAHOO.mbi.d3.modal.my = {};
        
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
                 hpbd.id="modal-body-d3";
                 hp.appendChild(hpbd);
                 YAHOO.util.Dom.addClass(hpbd,'bd');
                 YAHOO.util.Dom.addClass(hpbd,'modal-panel-d3');
                 
                 var hpft = document.createElement('div');
                 hp.appendChild(hpft);
                 YAHOO.util.Dom.addClass(hpft,'ft');
                 
        
                 YAHOO.mbi.d3.modal.my.panel =       
                     new YAHOO.widget.Panel( mid,
                                             { width: YAHOO.mbi.d3.modal.w+"px",
                                               height: YAHOO.mbi.d3.modal.h+"px",
                                               fixedcenter: true,
                                               close: true,
                                               draggable: true,
                                               zindex: 40,
                                               modal: true,
                                               constraintoviewport: false,
                                               visible: true 
                                             });
                 YAHOO.mbi.d3.modal.my.panel.render();

                 YAHOO.mbi.d3.modal.my.resize =
                     new YAHOO.util.Resize( mid,
                                            { handles: ["br"],
                                              autoRatio: false,
                                              minWidth: 350,
                                              minHeight: 100,
                                              status: false
                                            });

                 //start resizing
                 YAHOO.mbi.d3.modal.my.resize.on( 
                     'startResize',
                     function( args ) {
                        
                         if( this.cfg.getProperty('constraintoviewport') ) {
                             var D = YAHOO.util.Dom; 
                             var clientRegion = D.getClientRegion();
                             var elRegion = D.getRegion(this.element);
                             YAHOO.mbi.d3.modal.my.resize.set('maxWidth', 
                                                          clientRegion.right - elRegion.left 
                                                          - YAHOO.widget.Overlay.VIEWPORT_OFFSET );
                             YAHOO.mbi.d3.modal.my.resize.set('maxHeight', 
                                                          clientRegion.bottom - elRegion.top 
                                                          - YAHOO.widget.Overlay.VIEWPORT_OFFSET );
                         } else {
                             YAHOO.mbi.d3.modal.my.resize.set('maxWidth',null);
                             YAHOO.mbi.d3.modal.my.resize.set('maxHeight',null);
                         }
                     }, YAHOO.mbi.d3.modal.my.panel, true ); 
                     
                 //while resizing
				YAHOO.mbi.d3.modal.my.resize.on( 
                     'resize',
                     function( args ) {
                         var panelHeight = args.height; 
                         this.cfg.setProperty('height',panelHeight + "px"); 
                     }, YAHOO.mbi.d3.modal.my.panel, true );   
                 // at the end of resizing 
                YAHOO.mbi.d3.modal.my.resize.on( 
                     'endResize',
                     function( args ) {
                         graphWindow = YAHOO.util.Dom.getRegion(this.element);
                         YAHOO.mbi.d3.pane.force.size([graphWindow.width,graphWindow.height]);
						 YAHOO.mbi.d3.pane.force.resume();
                     }, YAHOO.mbi.d3.modal.my.panel, true ); 
                     
             }
        
        
        if( YAHOO.mbi.d3.modal.d3pane === null ){
            try{    
                YAHOO.mbi.d3.modal.d3pane =
                    YAHOO.mbi.d3.pane.init( 
                        { anchor:'modal-panel-d3', ns: arg.ns, ac: arg.ac,
                          w:YAHOO.mbi.d3.modal.w-25, h:YAHOO.mbi.d3.modal.h-55 });
            } catch(x){
                alert( x );
            }
        } else {
            try{
                YAHOO.mbi.d3.pane.load({ ns: arg.ns, ac: arg.ac});
            } catch (x) {
                alert(x);
            }
        }
        
        YAHOO.mbi.d3.modal.my.panel.setHeader( mtitle );       
        YAHOO.mbi.d3.modal.my.panel.show();        
        YAHOO.mbi.d3.modal.my.panel.cfg.setProperty("zindex",49);
        YAHOO.mbi.d3.modal.my.panel.stackMask();        
    }
};
