<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test='rtp=="N"'>
        <u>Protein Summary:</u>

	<s:url id="recUrl" includeParams="none" action='record'>
           <s:param name='ac' value="record.ac"/>
           <s:param name='detail'>full</s:param>
        </s:url>
        <s:a href="%{recUrl}"><s:property value="record.ac"/></s:a> 
        <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
</s:if>
<s:elseif test='rtp=="E"'>
        <u>Link Summary</u>  
        
  <s:if test="record.partList.part.size>0">
   <table border="1" width="100%">
     <tr>
       <td>DIP ID</td>
       <td>Label</td>
       <td>Description</td>
     </tr>
   <s:iterator value="record.partList.part" id="cp">
     <tr> 
       <td>
         <s:url id="cpUrl" includeParams="none" action='record'>
           <s:param name='ac' value="#cp.node.ac"/>
           <s:param name='detail'>full</s:param>
         </s:url>
         <s:a href="%{cpUrl}">
          <s:property value="#cp.node.ac"/>
         </s:a>    
       </td> 
       <td>
         <s:property value="#cp.node.label"/>
       </td> 
       <td>
         <s:property value="#cp.node.name"/>
       </td> 
     </tr>
   </s:iterator>
   </table>
  </s:if>
        <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
</s:elseif>
<s:elseif test='rtp=="X"'>
        <u>Evidence Summary</u>  
        <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
</s:elseif>
<s:elseif test='rtp=="S"'>
        <u>Source Summary</u>  
        <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
</s:elseif>
   
