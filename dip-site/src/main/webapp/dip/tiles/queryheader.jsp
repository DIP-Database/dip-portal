<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test='md=="txt-query-result"'>
 <t:insertDefinition name="queryheaderlayout">
  <t:putAttribute name="css_class" value="search_result" />
   <t:putAttribute name="image_src" value="images/query_logo_50.png" />
   <t:putAttribute name="image_alt" value="search result" />
   <t:putAttribute name="image_title" value="search result" />
   <t:putAttribute name="record_type" value="search result" />
   <t:putAttribute name="header_title" value="/dip/tiles/query-title.jsp"/>
   <t:putAttribute name="graph_links" value=""/>
 </t:insertDefinition>

</s:if>

<s:if test='md=="miql-query-result"'>
 <t:insertDefinition name="queryheaderlayout">
  <t:putAttribute name="css_class" value="search_result" />
   <t:putAttribute name="image_src" value="images/query_logo_50.png" />
   <t:putAttribute name="image_alt" value="search result" />
   <t:putAttribute name="image_title" value="search result" />
   <t:putAttribute name="record_type" value="search result" />
   <t:putAttribute name="header_title" value="/dip/tiles/miql-query-title.jsp"/>
   <t:putAttribute name="graph_links" value=""/>
 </t:insertDefinition>
</s:if>

<s:if test='md=="seq-query-result"'>
 <t:insertDefinition name="queryheaderlayout">
  <t:putAttribute name="css_class" value="search_result" />
   <t:putAttribute name="image_src" value="images/query_logo_50.png" />
   <t:putAttribute name="image_alt" value="sequence search result" />
   <t:putAttribute name="image_title" value="sequence search result" />
   <t:putAttribute name="record_type" value="search result" />
   <t:putAttribute name="header_title" value="/dip/tiles/sequence-query-title.jsp"/>
   <t:putAttribute name="graph_links" value=""/>
 </t:insertDefinition>
</s:if>

