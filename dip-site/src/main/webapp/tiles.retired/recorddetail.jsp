<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test='rtp=="N"'>
       <table width="100%" class="detail" cellspacing="0" cellpadding="0" border="0">
        <caption><b>Summary of Interactions</b></caption>
        <tr>
         <td class="detail">
   <s:if test="record.label!=''">
          <s:property value="record.label"/>:
   </s:if>
   <s:else>
          <s:property value="record.ac"/>:
   </s:else>
          <ul class="detail">
           <li># total interactions (# CORE interactions)
           <li><s:property value="record.attrList.attr.{?#this.ac=='dip:0023'}[0].value.value"/> binary interactions; 
             <s:property value="record.attrList.attr.{?#this.ac=='dip:0024'}[0].value.value"/> complexes</li>
           <li># covalent interactions, # direct interactions, # physical interactions, # assemblies</li>
           <li># interacting partners</li>
          </ul>
          <i><s:property value="record.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.label"/></i>:
          <ul class="detail">
           <li># interacting proteins</li>
           <li># total interactions (# CORE interactions)</li>
          </ul>
         </td>
        </tr>
       </table>

       <table width="100%" class="detail" cellspacing="0" cellpadding="0" border="0">
        <caption><b>Sequence</b></caption>
        <tr>
         <td class="detail"><br/><br/><br/><br/><br/></td>
        </tr>
       </table>
</s:if>

<s:elseif test='rtp=="E"'>
        <ul>
         <li># imex records</li>
         <li># and % of core links for this organism</li>
        </ul>
        <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
</s:elseif>
<s:elseif test='rtp=="X"'>
        <ul>
         <li><i>IMEx record:</i> <s:property value="record.xrefList.xref.{?#this.typeAc=='dxf:0009'}[0].ac"/></li>
         <li><i>Experiment mode:</i> (directed, screen)</li>
         <li><i>Interaction type:</i> <s:property value="record.attrList.attr.{?#this.ac=='dip:0001'}[0].value.value"/>
             (<s:property value="record.attrList.attr.{?#this.ac=='dip:0001'}[0].value.ac"/>)
         </li>
         <li><i>Interaction detection method:</i> <s:property value="record.attrList.attr.{?#this.ac=='MI:0001'}[0].value.value"/>
             (<s:property value="record.attrList.attr.{?#this.ac=='MI:0001'}[0].value.ac"/>)
         </li>
        </ul>
        <br/><br/><br/><br/><br/>
</s:elseif>
<s:elseif test='rtp=="S"'>
        <p><s:property value="record.attrList.attr.{?#this.ac=='dip:0014'}[0].value.value"/></p>
</s:elseif>
