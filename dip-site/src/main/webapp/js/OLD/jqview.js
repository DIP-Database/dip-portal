var jqview = function( init ) {

  var jqview = {};
  
  jqview.globalCounts = false;
  jqview.tabHasGrid = [];
  jqview.query = init.query;
  jqview.ns = init.ns;
  jqview.ac = init.ac;
  jqview.md = init.md;
  jqview.sl = init.sl;
  jqview.timeout = 0;

  if ( init.query != null ){
    jqview.baseURL = init.URL + "?md=" + init.md + "&query=" + init.query;
  } else {
    jqview.baseURL = init.URL + "?md=" + init.md + "&ac=" + init.ac;
  }
 
  jqview.getModel = function( slot, view ) {
    if (view.tabHasGrid[slot]<=1) {
      var callback = view.setModel;
      $.getJSON( view.baseURL + "&sl=" + slot + "&ret=jqmodel",
                 function(data){
                   callback( slot, data, view );
                });
    };
  };
  
  jqview.getCounts = function( view ) {
    var callback = view.setTabs;
    $.getJSON( view.baseURL + "&ret=jqcounts",
               function( data ) {
                 view.globalCounts = data;
                 callback( view, data );
               });
  };

  jqview.initializeTabs = function( view, tcnf ) {
        
    for( var j = 0; j < tcnf.jqModelList.length; j++ ) {
      $("#tabs").tabs( 'add', "#tab" + j, tcnf.jqModelList[j] );
      view.tabHasGrid[j] = 0;
    } 

    
   
    $("#tabs").bind('tabsselect', 
                      function(event, ui) {
                        view.getModel(ui.index,view);
                      });
    view.setTabs( view, tcnf );
  }; 
  
  jqview.setTabs = function( view, tcnf) {
    for( var j=0; j < tcnf.jqModelList.length; j++ ) {
      if( tcnf.jqModelType[j] == "table" ) {
        if ( view.tabHasGrid[j] == 0) {
          if (tcnf.jqCountList[j] > 0 ) {
            document.getElementById( "anchorlinks" )
                      .getElementsByTagName( "span" )
                        .item(j).innerHTML =  
              tcnf.jqModelList[j] + " (" + tcnf.jqCountList[j]+")";
        
            view.tabHasGrid[j]=1;
          } else if (tcnf.jqCountList[0] > 0) {
            document.getElementById( "anchorlinks" )
                      .getElementsByTagName( "span" )
                        .item(j).innerHTML =  
              tcnf.jqModelList[j] + " (" + tcnf.jqCountList[j]+")" + 
            '<img src="images/gridloading.gif" style="border: 0" />';
          }
        }
      }
    }

    if( tcnf.jqCountList[0] > 0 ) {   
      jqview.timeout = jqview.timeout+1000;
      if (jqview.timeout>10000) { jqview.timeout = 10000; }
      setTimeout( function(){view.getCounts(view)}, jqview.timeout );
    } else {
      for( var j=tcnf.jqModelList.length-1; j>1; j-- ) {
        if( tcnf.jqCountList[j] == 0 ) {
          $("#tabs").tabs( 'remove',  j );
        }
      }
    }
  };
  
  jqview.setModel = function( grid, model, view ) {
                 
    $("#mygrid" + grid).jqGrid({
      datatype: "json",
      url: view.baseURL + "&sl=" + grid + "&ret=jqgriddata",
      height: 250,  
//         width: 800,
      colNames: model.jqModelView.colNames, 
      colModel: model.jqModelView.colModel,
      rowNum: 10,
//         rowList: [10,20,30],
      viewrecords: true,    
      imgpath: 'jquery/jqgrid/themes/steel/images',               
      pager: jQuery('#tpgr'+grid), 
//         multiselect: false,
//         loadonce: true,
      shrinkToFit: true,
      caption: view.globalCounts.jqModelList[grid]
//         loadtext: "Loading"
     }).navGrid('#tpgr'+grid,{edit:false,add:false,del:false});
    
     view.tabHasGrid[grid]=2;
  };

  jqview.start = function() {
    $("#tabs").tabs();
    $.getJSON( jqview.baseURL + "&ret=jqmodellist",
            function( data ) {
              jqview.globalCounts = data;
              jqview.initializeTabs (jqview, data );
            });
  }
  return jqview;
};
