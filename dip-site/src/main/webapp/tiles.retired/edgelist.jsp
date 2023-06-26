<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script language="javascript">
function toggle(id) {
    var tr = document.getElementById(id);        //define variable 'tr' as row ID
    if (tr==null) { return; }
    var bExpand = tr.style.display == '';        //define variable 'bExpand' as rowID's display status, set to 'true' if row is now displayed
    tr.style.display = (bExpand ? 'none' : '');  //if bExpand true (row is being displayed), then close row, and vice versa
}

function expand(id1, id2) {
    toggle(id1);
    toggle(id2);
}

</script>

<!-- INTERACTION TABLE -->
   <table width="100%" cellpadding="0" cellspacing="0" class="results">
      <tr style="display:none">
         <th class="results_int" colspan="4">INTERACTION</th>
   <s:if test='rtp=="N"'>
      <s:if test="record.label!=''">
         <th class="results_int" colspan="9">PROTEINS INTERACTING WITH <s:property value="record.label"/></th>
      </s:if>
      <s:else>
         <th class="results_int" colspan="9">PROTEINS INTERACTING WITH <s:property value="record.name"/></th>
      </s:else>
   </s:if>
   <s:else>
         <th class="results_int" colspan="9">PROTEIN INTERACTOR(S)</th>
   </s:else>
      </tr>

      <tr>
         <th class="results_int">Type</th>
         <th class="results_int">CORE</th>
         <th class="results_int" title="Number of experiments supporting the interaction">Experiments</th>
         <th class="results_int">Interaction Details</th>

   <s:if test='rtp=="N"'>
      <s:if test="record.label!=''">
         <th class="results_int" colspan="9">Interaction partners of <s:property value="record.label"/></th>
      </s:if>
      <s:else>
         <th class="results_int" colspan="9">Interaction partners of <s:property value="record.name"/></th>
      </s:else>
   </s:if>
   <s:else>
         <th class="results_int" colspan="9">Protein interactor(s)</th>
   </s:else>
      </tr>

      <tr style="display:none">
         <th class="results_int">Type</th>
         <th class="results_int">CORE</th>
         <th class="results_int" title="Number of experiments supporting the interaction">Experiments</th>
         <th class="results_int">DIP ID (Get Details)</th>

         <th class="results_int">#</th>
         <th class="results_int">Name</th>
         <th class="results_int" style="display:none">Class</th>
         <th class="results_int">Description</th>
         <th class="results_int">Organism</th>
         <th class="results_int">Interactions (CORE)</th>
         <th class="results_int">DIP ID (Get Details)</th>
         <th class="results_int">UniProt ID</th>
         <th class="results_int">RefSeq ID</th>
      </tr>

 <s:iterator value="pane" id="cr" status="stat">
 <!-- individual panes (interactions) -->

   <s:set name="parts" value="#cr.partList.part.size"/>
   <s:if test='#parts>1&&rtp=="N"'>
     <s:set name="rowspan" value="#parts-1"/>
   </s:if>
   <s:else>
     <s:set name="rowspan" value="#parts"/>
   </s:else>

   <s:url id="dipedgeUrl" includeParams="none" action='record'>
     <s:param name='ac' value="#cr.ac"/>
     <s:param name='detail' value="detail"/>
     <s:param name='tp'>0</s:param>
     <s:param name='bigOn' value="bigOn"/>
     <s:param name='debugOn' value="debugOn"/>
   </s:url>

 <!-- SUMMARY RECORD -->
      <tr id="<s:property value="#cr.ac"/>_sum">

      <!-- interaction type -->
       <s:set name="inttype" value="#cr.attrList.attr.{?#this.ac=='dip:0001'}"/> <!-- inttype is an array -->
       <s:if test="#inttype.size>0">
         <td class="results"><s:property value="#inttype[0].value.value"/></td>
       </s:if>
       <s:else>
         <td class="results">-</td>
       </s:else>

      <!-- core -->
       <s:if test="#cr.attrList.attr.{?#this.ac=='dip:0304'}[0].value.ac=='dip:0305'">
         <td class="results_core">yes</td>
       </s:if>
       <s:else>
         <td class="results">no</td>
       </s:else>

      <!-- # of experiments --> 
       <s:set name="expct" value="#cr.xrefList.xref.{?#this.typeAc=='dxf:0008'}.size+#cr.xrefList.xref.{?#this.typeAc=='dxf:0021'}.size"/>
       <s:if test="#expct>0">
         <td class="results"><s:property value="#expct"/></td> 
       </s:if>
       <s:else>
         <td class="results">-</td> 
       </s:else>

      <!-- link to interaction (DIP ID) -->
         <td class="results">
            <s:a href="%{dipedgeUrl}"><s:property value="#cr.ac"/></s:a></td>

      <!-- # of interactors -->
         <td class="results"><s:property value="#rowspan"/>:
<!--            [<a href="javascript:expand('<s:property value="#cr.ac"/>_sum','<s:property value="#cr.ac"/>_1');"
               style="text-decoration:none; font-weight:bold">+</a>]
-->
<!--         </td>

         <td class="results" colspan="8">
-->  <s:iterator value="#cr.partList.part" id="mol" status="molstat">
  <!-- individual parts (interactors, or molecules) -->
      <s:url id="dipnodeUrl" includeParams="none" action='record'>
        <s:param name='ac' value="#mol.node.ac"/>
        <s:param name='detail' value="detail"/>
        <s:param name='tp'>0</s:param>
        <s:param name='bigOn' value="bigOn"/>
        <s:param name='debugOn' value="debugOn"/>
      </s:url>
    <s:if test="#parts==1||record.ac!=#mol.node.ac">
      <s:if test="#mol.node.label!=''">
         <s:a href="%{dipnodeUrl}"><s:property value="#mol.node.label"/></s:a> <!-- name -->
      </s:if>
      <s:else>
         <s:a href="%{dipnodeUrl}"><s:property value="#mol.node.name"/></s:a> <!-- description -->
      </s:else>
    </s:if>
  </s:iterator>
         </td>
      </tr>

 <!-- FULL RECORD -->
      <tr id="<s:property value="#cr.ac"/>_1" style="display:none">
         <td class="results" rowspan="<s:property value="#rowspan"/>"><s:property value="#cr.attrList.attr.{?#this.ac=='dip:0001'}[0].value.value"/></td> <!-- interaction type -->
   <s:if test="#cr.attrList.attr.{?#this.ac=='dip:0304'}[0].value.ac=='dip:0305'"> <!-- core? -->
         <td class="results_core" rowspan="<s:property value="#rowspan"/>">yes</td>
   </s:if>
   <s:else>
         <td class="results" rowspan="<s:property value="#rowspan"/>">no</td>
   </s:else>
         <td class="results" rowspan="<s:property value="#rowspan"/>">
            <s:property value="#cr.xrefList.xref.{?#this.typeAc=='dxf:0008'}.size+#cr.xrefList.xref.{?#this.typeAc=='dxf:0021'}.size"/>
         </td> <!-- # of experiments -->
         <td class="results" rowspan="<s:property value="#rowspan"/>">
            <s:a href="%{dipedgeUrl}"><s:property value="#cr.ac"/></s:a></td> <!-- link to interaction (DIP ID) -->
         <td class="results" rowspan="<s:property value="#rowspan"/>"><s:property value="#rowspan"/> <!-- # of interactors -->
<!--            [<a href="javascript:expand('<s:property value="#cr.ac"/>_sum','<s:property value="#cr.ac"/>_1');" 
               style="text-decoration:none; font-weight:bold">-</a>]</td>
-->
  <s:iterator value="#cr.partList.part" id="mol" status="molstat">
  <!-- individual parts (interactors, or molecules) -->
      <s:url id="dipnodeUrl" includeParams="none" action='record'>
        <s:param name='ac' value="#mol.node.ac"/>
        <s:param name='detail' value="detail"/>
        <s:param name='tp'>0</s:param>
        <s:param name='bigOn' value="bigOn"/>
        <s:param name='debugOn' value="debugOn"/>
      </s:url>
    
    <s:if test="#parts==1||record.ac!=#mol.node.ac">
         <td class="results"><s:a href="%{dipnodeUrl}"><s:property value="#mol.node.label"/></s:a></td> <!-- name -->
         <td class="results" style="display:none"><s:property value="#mol.node.type.name"/></td> <!-- class -->
         <td class="results"><s:property value="#mol.node.name"/></td> <!-- description -->
      <s:if test="#mol.node.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.name!=''"> <!-- for organism -->
         <td class="results"><s:property value="#mol.node.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.name"/></td> <!-- common name -->
      </s:if>
      <s:else>
         <td class="results"><i><s:property value="#mol.node.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.label"/></i></td> <!-- species -->
      </s:else>
         <td class="results"># (#)</td> <!-- interactions -->
         <td class="results"><s:a href="%{dipnodeUrl}"><s:property value="#mol.node.ac"/></s:a></td> <!-- DIP ID -->
         <td class="results">
      <s:iterator value="#mol.node.xrefList.xref.{?#this.ns=='UniProt'}" id="ext" status="status">
         <s:if test="#status.last">
            <t:insertDefinition name="externallink"/>
         </s:if>
         <s:else>
            <t:insertDefinition name="externallink"/>,
         </s:else>
      </s:iterator>
         </td> <!-- UniProt ID -->
         <td class="results">
      <s:iterator value="#mol.node.xrefList.xref.{?#this.ns=='RefSeq'}" id="ext" status="status">
         <s:if test="#status.last">
            <t:insertDefinition name="externallink"/>
         </s:if>
         <s:else>
            <t:insertDefinition name="externallink"/>,
         </s:else>
      </s:iterator>
         </td> <!-- RefSeq ID -->
      <s:if test="#molstat.index<#parts-1">
      </tr>
      <tr id="<s:property value="#cr.ac"/>_" style="display:none">
      </s:if>
    </s:if>
  </s:iterator>

      </tr>
 </s:iterator>
   </table>
