<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
<!-- ======================================================================
 ! $HeadURL:: $
 ! $Id:: $
 ! Version: $Rev:: $
 !===================================================================== -->
--%>
<HTML>
 <HEAD>
 <TITLE>Group Manager</TITLE>
 <t:insertDefinition name="htmlhead"/>
 </HEAD>

 <BODY onLoad="self.name='DIP_MA'; self.focus()">
  <center>
   <s:if test="mst=='' || mst==null">
     <t:insertDefinition name="jqmainheader"/>
   </s:if>
   <s:else>
     <t:insertDefinition name="jqheader"/>
   </s:else> 
   <table width="95%" cellspacing="0" cellpadding="0">
    <tr>
     <td class="page">
      <t:insertDefinition name="jqgroupmgr" />
     </td>
    </tr>
   </table>
   <t:insertDefinition name="footer" />
  </center>
 </BODY>
</HTML>

