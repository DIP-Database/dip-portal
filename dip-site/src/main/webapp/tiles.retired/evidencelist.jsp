<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!-- EVIDENCE TABLE -->
<table width="100%" cellpadding="0" cellspacing="0" class="results">
  <tr>
    <th class="results_exp">Method</th>
    <th class="results_exp">Scale</th>
    <th class="results_exp">Interaction Type</th>
  <s:if test='rtp!="E"'>
    <th class="results_exp">Interactors</th>
  </s:if>
    <th class="results_exp">DIP Details</th>
    <th class="results_exp">IMEx ID</th>
  <s:if test='rtp!="S"'>
    <th class="results_exp">Article</th>
  </s:if>
  </tr>

 <s:iterator value="pane" id="exp" status="stat">
 <!-- individual panes (experiments) -->

   <s:url id="dipexpUrl" includeParams="none" action='record'>
     <s:param name='ac' value="#exp.ac"/>
     <s:param name='detail' value="detail"/>
     <s:param name='tp'>0</s:param>
     <s:param name='bigOn' value="bigOn"/>
     <s:param name='debugOn' value="debugOn"/>
   </s:url>

  <tr>
    <td class="results"><s:property value="#exp.attrList.attr.{?#this.ac=='MI:0001'}[0].value.value"/></td>  <!-- method -->
    <td class="results"><s:property value="#exp.attrList.attr.{?#this.ac=='dip:0003'}[0].value.value"/></td>  <!-- scale -->
    <td class="results"><s:property value="#exp.attrList.attr.{?#this.ac=='dip:0001'}[0].value.value"/></td>  <!-- int type -->
  <s:if test='rtp!="E"'>
    <td class="results">&nbsp;</td>  <!-- interactors -->
  </s:if>
    <td class="results"><s:a href="%{dipexpUrl}"><s:property value="#exp.ac"/></s:a></td>  <!-- DIP ID -->

 <!-- IMEx ID -->
  <s:set name="imex" value="#exp.xrefList.xref.{?#this.typeAc=='dxf:0009'}"/>
  <s:if test="#imex.size>0">
    <td class="results"><s:property value="#imex[0].ac"/></td>
  </s:if>
  <s:else>
    <td class="results">-</td>
  </s:else>

  <s:if test='rtp!="S"'>
    <s:iterator value="#exp.xrefList.xref.{?#this.typeAc=='dxf:0014'}" id="source">
      <s:if test="#source.ns=='dip'">
        <s:set name="dipid" value="#source.ac"/>
      </s:if>
    </s:iterator>

    <s:url id="dipSourceUrl" includeParams="none" action="record">
      <s:param name='ac' value="#dipid"/>
      <s:param name='detail' value="detail"/>
      <s:param name='tp'>0</s:param>
      <s:param name='bigOn' value="bigOn"/>
      <s:param name='debugOn' value="debugOn"/>
    </s:url>
    <td class="results">
      <s:a href="%{dipSourceUrl}"><s:property value="#exp.attrList.attr.{?#this.ac=='dip:0027'}[0].value.value"/></s:a>
    </td>
  </s:if>
  </tr>
 </s:iterator>

</table> 
