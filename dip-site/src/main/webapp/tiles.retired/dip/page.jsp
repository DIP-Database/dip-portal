<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html lang="en">
 <head>
  <meta http-equiv="content-type" content="text/html; charset=utf-8">
  <title><s:property value="page.title"/></title>
  <t:insertDefinition name="htmlhead"/>

  <script src="js/modal-yui.js" type="text/javascript" language="JavaScript"></script>
  <script src="js/help-yui.js" type="text/javascript" language="JavaScript"></script>
  <script src="js/view-yui.js" type="text/javascript" language="JavaScript"></script>
 </head>
 <body class="yui-skin-sam">
  <center>
  <s:if test="big">
   <s:if test="skn=='prl'">
    <t:insertDefinition name="jqheader"/>
   </s:if>
   <s:else>
    <t:insertDefinition name="jqheader"/>
   </s:else>
  </s:if>
  <table class="pagebody" width="100%" cellspacing="0" cellpadding="0">
   <s:if test="hasActionErrors()">
    <tr>
     <td>
      <div  class="upage" id="errorDiv">
       <span class="pgerror">
        <s:iterator value="actionErrors">
         <span class="errorMessage"><s:property escape="false" /></span>
        </s:iterator>
       </span>
      </div>
      <br/>
     </td>
    </tr>
    <s:if test="#session['USER_ROLE'].administrator != null" >
     <tr>
      <td align="right">
       <s:form theme="simple" action="edit">
        <s:hidden name="mst" value="%{mst}"/>
        <s:hidden name="pageid" value="%{id}"/>
        <s:hidden name="newid" value="%{id}" />
        <s:hidden name="page.showtitle" value="true" />
        <s:hidden name="page.showcomment" value="false" />
        <s:hidden name="page.viewpath" value="/pages/newpage.html" />
        <s:hidden name="page.viewtype" value="file-html" />
        <s:hidden name="page.title" value="%{id}" />
        <s:hidden name="page.menusel" value="0:0" />
        <s:submit theme="simple" name="opr.pageAttStore" value="CREATE" />
       </s:form>
      </td> 
     </tr>
    </s:if>
   </s:if>
   <s:else>
    <s:if test="source!=null">
     <tr> 
      <td width="99%" class="pagettl">
       <s:if test="page.showtitle">
        <h1><s:property value="page.title"/></h1>
       </s:if>
      </td>
      <td class="pagecom">
       <s:if test="page.showcomment"> 
        <t:insertDefinition name="pagecomments" />
       </s:if>
      </td>
     </tr>
     <tr>
      <td colspan="2" class="page"> 
       <s:property value="source" escape="false" />
      </td>
     </tr> 
    </s:if>
   </s:else>
  </table>
  <br/><br/><br/><br/><br/><br/><br/><br/>
  <s:if test="big">
   <s:if test="skn=='prl'">
    <t:insertDefinition name="prlfooter-edit"/>
   </s:if>
   <s:else>
    <t:insertDefinition name="footer-edit"/>
   </s:else>
  </s:if>
  <s:else>
   <s:if test="skn=='prl'">
    <t:insertDefinition name="prlfooter"/>
   </s:if>
   <s:else>
    <t:insertDefinition name="footer.small"/>
   </s:else>
  </s:else>
  </center>
 </body>
</html>




