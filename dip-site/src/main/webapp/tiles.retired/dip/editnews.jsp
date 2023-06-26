<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<center>
<br/>
 <s:form theme="simple" action="news">
 <div id="edit_acc">
  <h2>Add News Item</h2>
  <div class="pane">
   <br/>
   <table cellpadding="3">
    <tr>
     <th align="left" nowrap>
      Title: <s:textfield size="64" value="%{id}" name="edit.title"/>
     </th>
     <th align="right" nowrap>
      Submitted by: <s:textfield size="4" name="edit.init"/>
     </td>
    </tr>
    <tr>
     <td colspan="4" align="center">
      <s:textarea theme="simple" name="edit.mesg" cols="110" rows="12"/>
     </td>
    </tr>
    <tr>
     <td colspan="4">
      <table width="100%" cellpadding="3">
       <tr>     
        <td align="left">
         <s:submit theme="simple" name="edit.pageAttStore" value="ADD" />
        </td> 
        <td align="right">
         <s:submit theme="simple" name="edit.pageAttReset" value="RESET" />
        </td>
       </tr>
      </table>
     </td>
    </tr>
   </table>
  </div>
  <h2>Hide</h2>
  <div class="pane"></div>    
  </div>
 </s:form>
 <br/>
</center>