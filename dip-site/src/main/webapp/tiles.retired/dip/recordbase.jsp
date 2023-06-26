<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<table width="100%" cellpadding="0" cellspacing="0" class="data_base">
 <s:if test='md=="N"'>
  <t:insertDefinition name="record-protein"/>
 </s:if>
 <s:elseif test='md=="E"'>
  <t:insertDefinition name="record-interaction"/>
 </s:elseif>
 <s:elseif test='md=="X"'>
  <t:insertDefinition name="record-evidence"/>
 </s:elseif>
 <s:elseif test='md=="S"'>
  <t:insertDefinition name="record-article"/>
 </s:elseif>
</table>
