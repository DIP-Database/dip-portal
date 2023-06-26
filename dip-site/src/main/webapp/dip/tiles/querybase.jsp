<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<table width="100%" cellpadding="0" cellspacing="0" class="data_base">
<s:if test='rtp=="N"' >
    <tr>
     <td>
      <b>Description:</b> <s:property value="record.name"/>
      <br/>
      <b>Organism:</b>
      <i><s:property value="summary.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.label"/></i>
      <s:if test="summary.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.name!=''">
        (<s:property value="summary.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.name"/>)
      </s:if>
     </td>
     <td><b>UniProt:</b>
   <s:iterator value="summary.xrefList.xref.{?#this.ns=='UniProt'}" var="ext" status="status"> 
      <s:if test="#status.last">
         <t:insertDefinition name="externallink"/>
      </s:if>
      <s:else>
         <t:insertDefinition name="externallink"/>,
      </s:else>
   </s:iterator>
      <br />
      <b>RefSeq:</b>
   <s:iterator value="summary.xrefList.xref.{?#this.ns=='RefSeq'}" var="ext" status="status">
      <s:if test="#status.last">
         <t:insertDefinition name="externallink"/>
      </s:if>
      <s:else>
         <t:insertDefinition name="externallink"/>,
      </s:else>
   </s:iterator>
     </td>
    </tr>
</s:if>
<s:elseif test='rtp=="E"'>
    <tr>
     <td>
      <b><s:property value="summary.partList.part.size"/> Interactors:</b>

  <s:if test="summary.partList.part.size>0">
<%--   <ol class="int_list">

   <s:iterator value="summary.partList.part" var="mol" status="status">
      <s:url anchor="dipmolUrl" includeParams="none" action='record'>
        <s:param name='ac' value="#mol.node.ac"/>
        <s:param name='dl' value="dl"/>
        <s:param name='tp'>0</s:param>
        <s:param name='bigOn' value="bigOn"/>
        <s:param name='debugOn' value="debugOn"/>
      </s:url>
      <s:set var="common_org" value="#mol.node.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.name"/>

    < ! - - INTERACTOR: name: description (DIP ID) from organism, UniProt ID, RefSeq ID - - >
   <s:if test="#status.last">
    <li class="int_list_last">
   </s:if>
   <s:else>
    <li class="int_list">
   </s:else>
     <b><s:property value="#mol.node.label"/>:</b>
     <s:property value="#mol.node.name"/>
     (<s:a href="%{dipmolUrl}"><s:property value="#mol.node.ac"/> </s:a>)
     from 
      <s:if test="#common_org!=''">
         <b><s:property value="#common_org"/></b>; <!-- organism common name -->
      </s:if>
      <s:else>
         <b><i><s:property value="#mol.node.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.label"/></i></b>;
         < ! - - species - - >
      </s:else>
     UniProt:&nbsp;<a href="1"><s:property value="#mol.node.xrefList.xref.{?#this.ns=='UniProt'}[0].ac"/></a>;
     RefSeq:&nbsp;<a href="1"><s:property value="#mol.node.xrefList.xref.{?#this.ns=='RefSeq'}[0].ac"/></a>
    </li>
   </s:iterator>
   </ol>
--%>

   <table width="100%" cellpadding="0" cellspacing="0" border="0">
   <s:iterator value="summary.partList.part" var="mol" status="status">
      <s:url anchor="dipmolUrl" includeParams="none" action='record'>
        <s:param name='ac' value="#mol.node.ac"/>
        <s:param name='dl' value="dl"/>
        <s:param name='tp'>0</s:param>
        <s:param name='bigOn' value="bigOn"/>
        <s:param name='debugOn' value="debugOn"/>
      </s:url>
      <s:set var="common_org" value="#mol.node.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.name"/>
    <tr>
     <td><s:property value="#status.value"/></td>
    <!-- INTERACTOR: name: description (DIP ID) from organism, UniProt ID, RefSeq ID -->
     <td><s:property value="#mol.node.label"/></td>
     <td><s:property value="#mol.node.name"/> from
      <s:if test="#common_org!=''">
         <s:property value="#common_org"/> <!-- organism common name -->
      </s:if>
      <s:else>
         <i><s:property value="#mol.node.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.label"/></i> <!-- species -->
      </s:else>
     (<s:a href="%{dipmolUrl}"><s:property value="#mol.node.ac"/> </s:a>)</td>
     <td nowrap>UniProt: <s:property value="#mol.node.xrefList.xref.{?#this.ns=='UniProt'}[0].ac"/></td>
     <td nowrap>RefSeq: <s:property value="#mol.node.xrefList.xref.{?#this.ns=='RefSeq'}[0].ac"/></td>
    </tr>
   </s:iterator>
   </table>
  </s:if>

     </td>
    </tr>
</s:elseif>
<s:elseif test='rtp=="X"'>
    <tr>
     <td>
   <s:set var="int_ac" value="summary.xrefList.xref.{?#this.typeAc=='dxf:0021'}[0].ac"/>
   <s:url anchor="dipintUrl" includeParams="none" action='record'>
     <s:param name='ac' value="#int_ac"/>
     <s:param name='dl' value="dl"/>
     <s:param name='tp'>0</s:param>
     <s:param name='bigOn' value="bigOn"/>
     <s:param name='debugOn' value="debugOn"/>

   </s:url>
      <b>Interaction Details:</b> <s:a href="%{dipintUrl}"><s:property value="int_ac"/></s:a>
      <br />
      <b>Interactors:</b> label-1 (DIPID-1), label-2 (DIPID-2), ... 
      <br />
      <b>Article:</b> FirstAuthorYear (DIPID), PubMed: 
   <s:iterator value="summary.xrefList.xref.{?#this.typeAc=='dxf:0015'}" var="ext" status="status">
      <s:if test="#status.last">
         <t:insertDefinition name="externallink"/>
      </s:if>
      <s:else>
         <t:insertDefinition name="externallink"/>,
      </s:else>
   </s:iterator>
     </td>
    </tr>
</s:elseif>
<s:elseif test='rtp=="S"'>
    <tr>
     <td>
      <b>Title:</b> <s:property value="summary.attrList.attr.{?#this.ac=='dip:0004'}[0].value.value"/>
      <br />
      <b>Authors:</b> <s:property value="summary.attrList.attr.{?#this.ac=='dip:0010'}[0].value.value"/>
      <br />
      <b>Citation:</b> <i><s:property value="summary.attrList.attr.{?#this.ac=='dip:0009'}[0].value.value"/></i> <!-- journal -->
       <s:property value="summary.attrList.attr.{?#this.ac=='dip:0013'}[0].value.value"/>, <!-- year -->
       <b><s:property value="summary.attrList.attr.{?#this.ac=='dip:0011'}[0].value.value"/></b>(<s:property value="summary.attrList.attr.{?#this.ac=='dip:0012'}[0].value.value"/>): <!-- volume and issue -->
       <s:property value="summary.attrList.attr.{?#this.ac=='dip:0015'}[0].value.value"/> <!-- pages -->
      <br />
      <b>PubMed:</b>
   <s:iterator value="summary.xrefList.xref.{?#this.typeAc=='dxf:0009'}" var="ext" status="status">
      <s:if test="#status.last">
         <t:insertDefinition name="externallink"/>
      </s:if>
      <s:else>
         <t:insertDefinition name="externallink"/>,
      </s:else>
   </s:iterator>
     </td>
    </tr>
</s:elseif>
   </table>
