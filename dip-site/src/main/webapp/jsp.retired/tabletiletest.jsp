<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- ========================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-sit#$
 * $Id:: tabletiletest.jsp 2403 2012-06-19 13:44:20Z lukasz                 $
 * Version: $Rev:: 2403                                                     $
 *===========================================================================
 *
 * tab agent watchdog
 *
 *======================================================================= -->

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
 <head>
  <title>GridTableTest</title>
  <style type="text/css">
        @import "/dipdb/dijit/themes/tundra/tundra.css";
        @import "/dipdb/dojox/grid/resources/Grid.css";
        @import "/dipdb/dojox/grid/resources/tundraGrid.css";
        @import "/dipdb/css/general.css";
  </style>
 </head>
 <body class="tundra" id="body">
  <h1>TableTileTest</h1>
  <hr/>
  <center>
  <table border="0" width="95%"> 
   <tr>
    <td>
     
     <table width="100%">
      <s:form action="tabletest" theme="simple">
      <tr>
       <td> 
         Accession: <s:textfield theme="simple" name="ac"  label="Accession"/>     
       </td>
       <td> 
         Tab No: <s:textfield theme="simple" name="tp"  label="Tab"/>     
       </td>
       <td> 
         TableName: <s:textfield theme="simple" name="table"  label="Table"/>     
       </td>
       <td> 
         <s:submit theme="simple" />
       </td>
      </tr>
      </s:form>
      <tr>
       <td>
        Accession: <s:property value="%{ac}"/>
       </td>
       <td>
        Tab No: <s:property value="%{tp}"/>
       </td>
       <td>
        TableName: <s:property value="%{table}"/>
       </td>
       <td>
        <br/>
       </td>
      </tr>
     </table>
    </td>
   </tr>
  </table>



  <hr/>
  <table border="0" width="95%">
   <tr>
    <td>

     <!-- table grid area starts -->

     <t:insertDefinition name="dipgridtable">
      <t:putAttribute name="title" value="GridTableTest" />
      <t:putAttribute name="dataurl">/dipdb/tabledata.action?table=<s:property value="%{table}"/>&ac=<s:property value="%{ac}"/>&detail=full&tp=<s:property value="%{tp}"/></t:putAttribute>
      <t:putAttribute name="confurl">/dipdb/tabledata.action?conf=true&table=<s:property value="%{table}"/></t:putAttribute>

     </t:insertDefinition>
     </td>

     <!-- table grid area ends -->

    </tr>
  </table>
  </center>
  <hr/>
 </body>
</html>
