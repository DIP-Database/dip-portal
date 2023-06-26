<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<t:insertDefinition name="headerlayout">

 <s:if test='md=="N"'>
  <!-- protein -->
   <t:putAttribute name="css_class" value="data_mol" />
   <t:putAttribute name="image_src" value="images/prot_logo_50.png" />
   <t:putAttribute name="image_alt" value="molecule" />
   <t:putAttribute name="image_title" value="molecule" />
   <t:putAttribute name="record_type" value="protein" />
   <s:if test="summary.label==''">
    <s:set name="recordname" value="summary.name" />
   </s:if>
   <s:else>
    <s:set name="recordname" value="summary.label" />
   </s:else>
   <t:putAttribute name="header_title" value="/tiles/dip/record_title.jsp"/>
   <t:putAttribute name="header_comments" value="/tiles/dip/headercomments.jsp"/>
   <t:putAttribute name="graph_links" value="/tiles/dip/graphs_node.jsp"/>
 </s:if>

 <s:elseif test='md=="E"'>
  <!-- interaction -->
   <t:putAttribute name="image_src" value="images/int_logo_50.png" />
   <t:putAttribute name="image_alt" value="interaction" />
   <t:putAttribute name="image_title" value="interaction" />
   <t:putAttribute name="databody" value="/tiles/linkbody.jsp" />
   <s:if test="summary.attrList.attr.{?#this.ac=='dip:0304'}[0].value.ac=='dip:0305'">
    <t:putAttribute name="record_type" value="interaction" />
    <t:putAttribute name="css_class" value="data_core" />
    <t:putAttribute name="record_name" value="CORE" />
   </s:if>
   <s:else>
    <t:putAttribute name="record_type" value="interaction" />
    <t:putAttribute name="css_class" value="data_int" />
   </s:else>
   <s:set name="recordname" value="summary.attrList.attr.{?#this.ac=='dip:0001'}[0].value.value"/>
   <t:putAttribute name="header_title" value="/tiles/dip/record_title.jsp"/>
   <t:putAttribute name="header_comments" value="/tiles/dip/headercomments.jsp"/>
   <t:putAttribute name="graph_links" value="/tiles/dip/graphs_edge.jsp"/>
 </s:elseif>

 <s:elseif test='md=="X"'>
  <!-- evidence -->
   <t:putAttribute name="css_class" value="data_exp" />
   <t:putAttribute name="image_src" value="images/exp_logo_50.png" />
   <t:putAttribute name="image_alt" value="experiment" />
   <t:putAttribute name="image_title" value="experiment" />
   <t:putAttribute name="record_type" value="experiment" />
   <s:if test="summary.attrList.attr.{?#this.ac=='dip:0003'}[0].value.ac=='dip:0002'">
    <t:putAttribute name="record_name" value="Small-scale" />
   </s:if>
   <s:elseif test="summary.attrList.attr.{?#this.ac=='dip:0003'}[0].value.ac=='dip:0005'">
    <t:putAttribute name="record_name" value="High-throughput" />
   </s:elseif>
   <s:set name="recordname" value="summary.attrList.attr.{?#this.ac=='MI:0001'}[0].value.value"/>
   <t:putAttribute name="header_title" value="/tiles/dip/record_title.jsp"/>
   <t:putAttribute name="header_comments" value="/tiles/dip/headercomments.jsp"/>
   <t:putAttribute name="graph_links" value="/tiles/dip/graphs_evidence.jsp"/>
 </s:elseif>

 <s:elseif test='md=="S"'>
  <!-- source -->
   <t:putAttribute name="css_class" value="data_source" />
   <t:putAttribute name="image_src" value="images/art_logo_50.png" />
   <t:putAttribute name="image_alt" value="article" />
   <t:putAttribute name="image_title" value="article" />
   <t:putAttribute name="record_type" value="article" />
   <s:set name="recordname" value="summary.name" />
   <t:putAttribute name="header_title" value="/tiles/dip/record_title.jsp"/>
   <t:putAttribute name="header_comments" value="/tiles/dip/headercomments.jsp"/>
   <t:putAttribute name="graph_links" value="/tiles/dip/graphs_source.jsp"/>
 </s:elseif>

</t:insertDefinition>  
