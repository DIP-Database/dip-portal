<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!-- RECORD HEADER -->
<t:insertDefinition name="recordheader"/>
<table class="data" width="100%" cellpadding="0" cellspacing="0">
 <tr>
  <td class="data">
<!-- RECORD BASE -->
<t:insertDefinition name="recordbase"/>
   <table width="100%" cellpadding="0" cellspacing="0">     <!-- tabs -->
<s:if test="paneTab.size>0">
    <tr>
 <s:iterator value="paneTab" id="tab" status="stat"> 
   <s:if test="#tab.on">
     <td width="1%" nowrap class="t0tabon">
       <s:property value="#tab.label"/> 
   </s:if>
   <s:else>
     <td width="1%" nowrap class="t0tab">
      <s:url id="tuUrl" includeParams="none" action='record'>
        <s:param name='ac' value="ac"/>
        <s:param name='detail'>full</s:param>
        <s:param name='tp' value="#stat.index"/>
        <s:param name='bigOn' value="bigOn"/>
        <s:param name='debugOn' value="debugOn"/>	
      </s:url>
      <s:a href="%{tuUrl}">
       <s:property value="#tab.label"/>
      </s:a>
   </s:else>
     </td>
 </s:iterator> 
     <td width="95%" class="t0tab">
       &nbsp;
     </td>
    </tr>
</s:if>
    <tr>
     <td colspan="<s:property value="paneTab.size+1" />" class="t0cont">
<s:if test="tp>0">
  <!-- COMPLEX PANE -->
  <t:insertDefinition name="panebody"/>
</s:if>
<s:else>
   <!-- RECORD DETAIL -->
  <t:insertDefinition name="recorddetail"/>
</s:else>
     </td>
    </tr>
   </table>
  </td>
 </tr>
</table>

