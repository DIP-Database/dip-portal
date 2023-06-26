<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="pane[0].type.ac=='dxf:0003'">
   <t:insertDefinition name="nodelist"/>
</s:if>

<s:elseif test="pane[0].type.ac=='dxf:0004'">  
   <t:insertDefinition name="edgelist"/>
</s:elseif>

<s:elseif test="pane[0].type.ac=='dxf:0015'">
   <t:insertDefinition name="evidencelist"/>
</s:elseif>

<s:elseif test="pane[0].type.ac=='dxf:0016'">
   <t:insertDefinition name="sourcelist"/>
</s:elseif>

<s:else>
  <u>Unknown table</u>
</s:else>
