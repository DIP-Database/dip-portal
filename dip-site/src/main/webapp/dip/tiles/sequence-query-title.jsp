<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test='qt=="dip-motif-query"'>
<s:form action="sequenceQuery" theme="simple">
<span class="search_title">
 <span class="dipid">Query Motif:</span>
 <span class="qseq">
  <s:set var="seq" value="%{queryResult.attrList.attr.{?#this.name=='motif-query-string'}[0].value.value}" />   
  <s:if test="#seq.length() > 32">
    <s:property value="#seq.substring(0,32)"/>...
  </s:if>
  <s:else>
   <s:property value="#seq"/>
  </s:else>
  <s:hidden name="query" value="#seq"/> 
 </span>
 <span class="dipid">
<%--
<!--  (length: <s:property value="#seq.length()"/>) -->
<!-- Sequence DB: DIP -->
<!--  <s:select name="qt" list="#{'dip-seq-query':'DIP'}"/> -->
<!--  <s:submit theme="simple" value="Refine Search"/> -->
--%>
 </span>
 <s:hidden name="mst" value="1:1:1" />
 <s:hidden name="md" value="seq-query-result"/> 
 <s:hidden name="dl" value="full"/>
 <s:hidden name="ret" value="view"/>
</span>
</s:form>
</s:if>
<s:else>
<s:form action="sequenceQuery" theme="simple">
<span class="search_title">
 <span class="dipid">Query Sequence:</span>
 <span class="qseq">
  <s:set var="seq" value="%{queryResult.attrList.attr.{?#this.name=='sequence-query-string'}[0].value.value}" />   
  <s:if test="#seq.length() > 32">
    <s:property value="#seq.substring(0,32)"/>...
  </s:if>
  <s:else>
   <s:property value="#seq"/>
  </s:else>
  <s:hidden name="query" value="#seq"/> 
 </span>
 <span class="dipid">
<%--
<!--  (length: <s:property value="#seq.length()"/>) -->
<!-- Sequence DB: DIP -->
<!--  <s:select name="qt" list="#{'dip-seq-query':'DIP'}"/> -->
<!--  <s:submit theme="simple" value="Refine Search"/> -->
--%>
 </span>
 <s:hidden name="mst" value="1:1:1" />
 <s:hidden name="md" value="seq-query-result"/> 
 <s:hidden name="dl" value="full"/>
 <s:hidden name="ret" value="view"/>
</span>
</s:form>
</s:else>
