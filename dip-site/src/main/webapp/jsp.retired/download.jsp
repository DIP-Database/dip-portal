<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!-- ======================================================================
 ! $HeadURL:: $
 ! $Id:: $
 ! Version: $Rev:: $
 !===================================================================== -->

<HTML>
 <HEAD>
 <TITLE>DIP Download</TITLE>
 <t:insertDefinition name="htmlhead"/>

 
  <link rel="stylesheet" type="text/css" media="screen" href="jquery/jqgrid/themes/basic/grid.css" /> 
  <link rel="stylesheet" type="text/css" media="screen" href="jquery/jqgrid/themes/jqModal.css" />

  <link rel="stylesheet" type="text/css" media="screen" href="css/tabs.css" />

  <script type="text/javascript" src="jquery/jquery-1.3.2.min.js"></script>

  <script type="text/javascript" src="jquery/jquery.tools.min.js"></script>

  <script src="jquery/jqgrid/jquery.jqGrid.js" type="text/javascript"></script> 
  <script src="jquery/jqgrid/js/jqModal.js" type="text/javascript"></script> 
  <script src="jquery/jqgrid/js/jqDnR.js" type="text/javascript"></script>
 
 </HEAD>

 <BODY onLoad="self.name='DIP_MA'; self.focus()">
  <center>
   <s:if test="mmd=='main'">
    <t:insertDefinition name="mainheader"/>
   </s:if>

   <s:if test="mmd=='big'">
    <t:insertDefinition name="header"/>
   </s:if>
  
   <table width="95%" cellspacing="0" cellpadding="0">
    <tr>
     <td class="page">
      <t:insertDefinition name="download" />
     </td>
    </tr>
   </table>
   <t:insertDefinition name="footer" />
  </center>
 </BODY>
</HTML>

