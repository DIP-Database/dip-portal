<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

 <table border="1" width="100%">
  <tr>
   <th>
    DEBUG
   </th>
  </tr> 
  <tr>
   <th>
    RECORD
   </th>
  </tr>
  <tr>
   <td>
    <pre>
      <s:property value="resultStr" />
    </pre>
   </td>
  </tr>
  <tr>
   <th>
    PANE
   </th>
  </tr>
  <tr>
   <td>
    <pre>
      <s:property value="paneStr" />
    </pre>
   </td>
  </tr>
 </table>
