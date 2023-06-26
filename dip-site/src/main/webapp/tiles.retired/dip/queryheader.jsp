<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<t:insertDefinition name="headerlayout">
 <s:if test='md=="TQ"'>
  <t:putAttribute name="css_class" value="search_result" />
   <t:putAttribute name="image_src" value="images/query_logo_50.png" />
   <t:putAttribute name="image_alt" value="search result" />
   <t:putAttribute name="image_title" value="search result" />
   <t:putAttribute name="record_type" value="search result" />
   <t:putAttribute name="header_title" value="/tiles/dip/query_title.jsp"/>
   <t:putAttribute name="graph_links" value=""/>
</s:if>
</t:insertDefinition>
