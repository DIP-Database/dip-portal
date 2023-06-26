<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!-- QUERY HEADER -->
<t:insertDefinition name="queryheader"/>
<s:form>
 <s:hidden name="ac" value="%{ac}"/>
</s:form>

<table class="data" width="100%" cellpadding="0" cellspacing="0">
 <tr>
  <td class="data">
<!-- QUERY BASE -->
<t:insertDefinition name="querybase"/>
   <table width="100%" cellpadding="0" cellspacing="0">     <!-- tabs -->
<s:if test="paneTab.size>0">
    <tr>
     <td class="recordtab">&nbsp;</td> <!-- spacer -->
 <s:iterator value="paneTab" id="tab" status="stat"> 
   <s:if test="#tab.on">
     <td width="1%" nowrap class="recordtabon">
       <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
         <td>
          <s:property value="#tab.label"/> 
          <s:property value="#tab.vlabel"/>
         </td> 
   </s:if>
   <s:else>
     <td width="1%" nowrap class="recordtab">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
       <tr>
        <td> 
         <s:url id="tuUrl" includeParams="none" action='query'>
          <s:param name='query' value="query"/>
          <s:param name='detail'>full</s:param>
          <s:param name='tp' value="#stat.index"/>
          <s:param name='bigOn' value="bigOn"/>
          <s:param name='debugOn' value="debugOn"/>	
         </s:url>
         <s:a href="%{tuUrl}">
          <s:property value="#tab.label"/>
         </s:a>
         <s:property value="#tab.vlabel"/>
        </td> 
   </s:else>
        <td>
         <s:div id="ptab%{#stat.index}"/>
        </td>
       </tr>
      </table>
     </td>
 </s:iterator> 
     <td width="93%" class="recordtab">
       &nbsp;
     </td>
    </tr>
</s:if>
    <tr>
     <td colspan="<s:property value="paneTab.size+2" />" class="recordcont">
<s:if test="tp>0">
  <!-- COMPLEX PANE  -->
  <t:insertDefinition name="panebody"/>
</s:if>
<s:else>
   <!-- RECORD DETAIL -->
<!--  LS: only complex panes defined so far <t:insertDefinition name="recorddetail"/> -->
  <t:insertDefinition name="panebody"/>
</s:else>
     </td>
    </tr>
   </table>
  </td>
 </tr>
</table>

