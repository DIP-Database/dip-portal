<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
 <head>
  <style type="text/css">
         @import "/dipdb/dojo/dijit/themes/tundra/tundra.css";
         @import "/dipdb/dojo/dojo/resources/dojo.css"
         @import "/dipdb/dojo/dojox/grid/_grid/Grid.css";
         @import "/dipdb/dojo/dojox/grid/_grid/tundraGrid.css";
  </style>

  <script type="text/javascript" language="JavaScript"
          src="/dipdb/dojo/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug:true"></script>

  <script type="text/javascript" language="JavaScript"
          src="js/panetable.js"></script>

  <script type="text/javascript">
          dojo.require("dojo.data.ItemFileReadStore");
          dojo.require("dojox.grid.Grid");
          dojo.require("dojox.grid._data.model");
          dojo.require("dojo.parser");

   // a grid view is a group of columns
   var view1 = {cells:[
                  [
		   {name: 'Name', field: "label", noresize: "true", width: "10%"}, 
		   {name: 'Description', field: "name", noresize: "true",width: "30%"}, 
		   {name: 'Organism', field: "taxid"}, 
		   {name: 'Interactions (CORE)', field: "ic"}, 
		   {name: 'DIP Details', field: "dip"},
		   {name: 'UniProt ID', field: "uniprot"},
		   {name: 'RefSeq ID', field: "refseq"}
		  ],
                 [
                   {name: 'EntrezGene ID', field: "entrez",colSpan: 7}
                   
                 ]
                ],
                noscroll: "false"
              };

   var layout = [ view1 ];

  </script>

  <style>
   #grid2{
     border: 1px solid #333;
     width: 95%;
     margin: 1px;
     bgcolor: #282;
   }
  </style>
 </head>

  <body class="tundra" onresize="DIP_record_pane_table.resize();">

    <div dojoType="dojo.data.ItemFileReadStore"
         jsId="paneStore" url="dipdb/panetable.action?detail=full&first=0&max=3&ac=DIP-294N&tp=1">
    </div> 

    <div dojoType="dojox.grid.data.DojoData" jsId="tableModel"
        rowsPerPage="5" store="paneStore" query="{ dip:'DIP-*' }">
   </div>

   <div id="grid2" dojoType="dojox.Grid" model="tableModel" structure="layout" 
        autoRender="true" autoHeight="true" ></div>

   <s:form action="panemodel" theme="simple"> 
   <table width="95%">
     <tr>
      <td width="5%">
        <s:submit theme="simple" value="<<" onclick="DIP_record_pane_table.load('F'); return true;"/>  
      </td>
      <td width="5%">  
        <s:submit theme="simple" value="<" onclick="DIP_record_pane_table.load('P'); return false;"/>
      </td>
      <td width="80%">&nbsp;</td>
      <td width="5%">
        <s:submit theme="simple" value=">" onclick="DIP_record_pane_table.load('N'); return false;"/>
      </td>
      <td width="5%">
        <s:submit theme="simple" value=">>" onclick="DIP_record_pane_table.load('L'); return false;"/>
      </td>
     </tr>
  </table>
  </s:form>

  <hr/>
  <s:debug/>
 </body>
</html>