$(document).ready(function(){

 $("ul.css-tabs:first").tabs("div.css-panes:first > div", function(i) { 
    // get the pane to be opened 
    var pane = this.getPanes().eq(i); 
 
    // if it is empty .. 
    if (pane.is(":empty")) { 
 
    // load it with a page specified in the tab's href attribute 
            pane.load(this.getTabs().eq(i).attr("href")); 
   } 
 
 }); 

});

var jqnewsedit = function( init ) {

  var jqnewsedit = {};

  jqnewsedit.edt = init.edt;

  jqnewsedit.start = function() {

   $("#edit_acc").tabs("#edit_acc div.pane",
                        {tabs: 'h2'});
   var tapi = $("#edit_acc").tabs();
   switch (jqnewsedit.edt) {
      case "ATR":
        tapi.click(0);
        break;
      default:
        tapi.click(1);
    }
  };
  return jqnewsedit;
};
