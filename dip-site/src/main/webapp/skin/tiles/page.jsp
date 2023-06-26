
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ===========================================================================
 ! $HeadURL:: https://lukasz@imex.mbi.ucla.edu/svn/dip-ws/trunk/dip-portal/src#$
 ! $Id:: layout.jsp 859 2010-01-17 22:02:51Z lukasz                            $
 ! Version: $Rev:: 859                                                         $
 !========================================================================= --%>

<table class="pagebody" width="100%" cellspacing="0" cellpadding="0">
  <s:if test="hasActionErrors()">
    <tr>
      <td colspan="3">
        <div  class="upage" id="errorDiv">
          <span class="pgerror">
            <s:iterator value="actionErrors">
              <span class="errorMessage"><s:property escapeHtml="false" /></span>
            </s:iterator>
          </span>
        </div>
        <br/>
      </td>
    </tr>
  </s:if>
  <s:if test="source!=null">
     <tr> 
      <s:if test="page.showindex">
       <td rowspan="2" id="index-panel">
       </td>
      </s:if>
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
      <td align="right">  
         <s:if test="#session['USER_ROLE'].administrator != null or
               #session['USER_ROLE'].editor != null">
           <s:form theme="simple" action="page">
            <s:hidden name="id" value="newpage"/>
            <s:hidden name="site" value="%{site}"/>  
            <s:submit theme="simple" name="dummy" value="NEW PAGE" />
           </s:form>
         </s:if>
      </td>
     </tr>
     <tr>
      <td colspan="3" class="page">
       <s:property value="source" escapeHtml="false" />
      </td>
     </tr> 
   </s:if>
</table>
<%-- </center> --%>

 <s:if test="page.showindex">
  <script>
    YAHOO.mbi.view.panel.index("<s:property value="page.urlindex" escapeHtml="false" />", document.getElementById("index-panel"));     
  </script>
 </s:if>

<!--
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 </body>
</html>
-->
