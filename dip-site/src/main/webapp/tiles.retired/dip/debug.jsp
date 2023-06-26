<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

 <table border="1" width="100%">
  <tr>
   <th>
    DEBUG
   </th>
  </tr> 
  <tr>
   <td>
     BIG:<s:property value="big" />::model:<s:property value="md" />::slot:<s:property value="sl" />:ret:<s:property value="ret" />:
   </td>
  </tr>
  <tr>
   <td>
     Cache size: <s:property value="cacheSize" />
   </td>
  </tr>
  <tr>
   <th>
    RESULT
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
    VIEW
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
