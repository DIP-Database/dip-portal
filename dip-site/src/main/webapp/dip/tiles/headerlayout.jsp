<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!-- HEADER LAYOUT -->


<table width="100%" cellpadding="0" cellspacing="0" 
       class="<t:insertAttribute name="css_class" />">

 <tr>
  <td class="data_header">
   <img src="<t:insertAttribute name="image_src" />" 
        alt="<t:insertAttribute name="image_alt" />" 
        title="<t:insertAttribute name="image_title" />" /> 
  </td>
  <td class="data_header" width="100%">
   <i><t:insertAttribute name="record_type" /></i><br />
   <t:insertAttribute name="header_title"/>
  </td>
  <t:insertAttribute name="header_comments" ignore="true"/>
  <td class="data_graphs">
   <t:insertAttribute name="graph_links"/>
  </td>

<!--   <t:insertAttribute name="cytoscape" ignore="true"/> -->
 </tr>
</table>
