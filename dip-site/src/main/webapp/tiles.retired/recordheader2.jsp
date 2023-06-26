<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<table width="100%" cellpadding="0" cellspacing="0" class="data_mol">
 <tr>
  <td class="data">
<s:if test='rtp=="N"'>
    <img src="/dip/images/bullet10.png" alt="node" />
</s:if>
<s:elseif test='rtp=="E"'>
    <img src="/dip/images/bullet10.png" alt="edge" />
</s:elseif>
<s:elseif test='rtp=="X"'>
    <img src="/dip/images/bullet10.png" alt="experiment" />
</s:elseif>
<s:elseif test='rtp=="S"'>
    <img src="/dip/images/bullet10.png" alt="source" />
</s:elseif>
  </td>
  <td class="data_header" width="100%">
    <i>
     <s:property value="record.type.name" />
    </i><br/>
   <span class="data_title"><span class="dipid"><s:property value="record.ac" />:</span>
   <s:property value="record.label" /></span>
  </td>
 </tr>
</table>

