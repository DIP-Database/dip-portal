<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html lang="en">
 <head>
  <title>
   <s:property value="title"/>
  </title>

  <link rel="stylesheet" href="css/dip2.css" type="text/css" title="dip2">
  <link rel="stylesheet" href="css/dip2tab.css" type="text/css" title="dip2">
<!--
  <link rel="stylesheet" type="text/css" media="screen" href="jquery/jqgrid/themes/basic/grid.css" /> 
  <link rel="stylesheet" type="text/css" media="screen" href="jquery/jqgrid/themes/jqModal.css" />
  <link rel="stylesheet" type="text/css" media="screen" href="jquery/css/tabs-accordion.css"/> 
-->
  <link rel="stylesheet" type="text/css" media="screen" href="css/edit.css"/>

 </head>
 <body>
  <center>
   <t:insertDefinition name="header" />
   <table width="98%" cellspacing="0" cellpadding="0">
    <s:if test="#session['USER_ROLE'].administrator != null" >
     <tr>
      <td>    
       <t:insertDefinition name="editmenu" />
      </td>
     </tr>
    </s:if>
   </table>
   <t:insertDefinition name="footer" />
  </center>
 </body>
</html>




