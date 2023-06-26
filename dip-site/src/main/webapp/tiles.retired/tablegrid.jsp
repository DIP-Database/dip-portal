<!-- ========================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-sit#$
 * $Id:: tablegrid.jsp 134 2009-06-18 23:53:54Z lukasz                      $
 * Version: $Rev:: 134                                                      $
 *===========================================================================
 *
 * table grid tile
 *
 *======================================================================= -->


<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

 <script type="text/javascript" src="/dipdb/dojo/dojo.js" 
          djConfig="isDebug:true, parseOnLoad:true"></script>
 <script type="text/javascript" 
          src="/dipdb/dojox/widget/PlaceholderMenuItem.js"></script>

 <script type="text/javascript" src="/dipdb/js/tablegrid.js"></script>
 <script type="text/javascript"> 
    dojo.require("dojo.data.ItemFileReadStore");        
    dojo.require("dojox.widget.PlaceholderMenuItem");
    dojo.require("dijit.form.Button");;
    dojo.require("dijit.Menu");
    dojo.require("dojox.grid.DataGrid");
    
    dojo.addOnLoad(function(){
      var dataUrl='<t:getAsString name="dataurl"/>';
      var confUrl='<t:getAsString name="confurl"/>';
      console.log("dataurl="+dataUrl);
      var url='http://dip.doe-mbi.ucla.edu:50607/dipdb/tabledata.action';
      var dtg = DipTableGrid.init(dataUrl,confUrl);

      //   url+'?detail=full&tp=1&table=NodeList&ac='+'DIP-294N',
      //   url+'?table=NodeList&conf=true'
      //);

     

    });
 </script>   

  <!-- popup: header context menu -->

  <div dojoType="dijit.Menu" jsid="dipTableGridContextMenu" 
       style="display: none;"  id="dipTableGridContextMenu">

   <div dojoType="dijit.PopupMenuItem">
    <span>Selection</span>
    <div dojoType="dijit.Menu">
      <div dojoType="dijit.MenuItem" onClick="action1('foo');">To Cytoscape</div>
    </div>
   </div>
 
   <div dojoType="dijit.PopupMenuItem">
    <span>Cytoscape</span>
    <div dojoType="dijit.Menu">
      <div dojoType="dijit.MenuItem">Send Selected</div>
      <div dojoType="dijit.MenuItem">Send All</div>
    </div>
   </div>

   <div dojoType="dijit.MenuSeparator"></div> 

   <div dojoType="dijit.PopupMenuItem">
    <span>Options...</span>
    <div dojoType="dijit.Menu">
     <div dojoType="dijit.PopupMenuItem">
      <span>Show Column</span>
      <div dojoType="dijit.Menu">
        <div dojoType="dojox.widget.PlaceholderMenuItem" label="GridColumns"></div> 
      </div>
     </div>
     <div dojoType="dijit.MenuItem">Cytoscape Port</div>
    </div>
   </div>

  </div>

  <!-- popup: row context menu -->

  <div dojoType="dijit.Menu" jsid="dipTableGridRowContextMenu" contextMenuForWindow="false"
       style="display: none;"  id="dipTableGridRowContextMenu">
 
   <div dojoType="dijit.PopupMenuItem">
    <span>Cytoscape</span>
    <div dojoType="dijit.Menu">
      <div dojoType="dijit.MenuItem">Send Selected</div>
      <div dojoType="dijit.MenuItem">Send All</div>
    </div>
   </div>

  </div>

  <!-- grid placeholder -->
  <h1 class="tt">DipTableGrid Test</h1>
  <div class="partsContainer">
   <div class="gridContainer" id="gridContainer">
    <div id="dipTableGridNode"></div>
   </div>
  </div>
 
  <!-- filter placeholder -->
  <table width="100%" border="1" cellpadding="5" class="tundra">
    <tr>
     <td align="center" id="dipTableGridFilter"><br/></td>
    </tr>
  </table>
 
