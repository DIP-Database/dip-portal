<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:form action="query" theme="simple" method="get">
<span class="search_title">
 <span class="dipid">Query:</span>
 <span>
  <s:textfield size="64" theme="simple" name="query" value="%{queryResult.attrList.attr[0].value.value}"/>
  <s:submit theme="simple" value="Refine Search"/>
 </span>
 <s:hidden name="mst" value="1:1:2" />
 <s:hidden name="md" value="TQ"/> 
 <s:hidden name="dl" value="full"/>
 <s:hidden name="ret" value="view"/>
</span>
</s:form>
