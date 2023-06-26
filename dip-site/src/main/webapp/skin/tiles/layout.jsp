<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:set var="spath" value="%{skn}"/>
<html lang="en">
 <head>
  <meta http-equiv="content-type" content="text/html; charset=utf-8">
  <title><t:getAsString name="title" ignore="true"/></title>
  <t:insertDefinition name="htmlhead"/>
 </head>
 <body class="yui-skin-sam" onLoad="var nos = document.getElementById('noscript'); if ( nos !== null ) { nos.innerHTML='';}">
  <center>
   <s:if test="mst=='' || mst==null">
     <t:insertTemplate template="/skin/tiles/${spath}/mainheader.jsp" flush="true"/>
   </s:if>
   <s:elseif test="bigOn">
    <t:insertTemplate template="/skin/tiles/${spath}/header.jsp" flush="true"/>
   </s:elseif> 
   <table width="100%" border="0" cellspacing="0" cellpadding="0" class="<t:getAsString name='body-class' ignore='true'/>">
    <tr>
     <td>
      <t:insertAttribute name="body"/>
     </td>
    </tr>
   </table>
   <div id="editor">
        <s:if test="#session['USER_ROLE'].administrator != null or
                  #session['USER_ROLE'].editor != null or
                  #session['USER_ROLE'].test != null" >
      <t:insertAttribute  name="edit" ignore="true"/>
     </s:if>
   </div>
   <div id="footer">
    <table width="100%" class="bottom" cellpadding="0" cellspacing="0">
     <tr>
      <td>
       <center>
        <table width="100%" class="footer" cellpadding="0" cellspacing="0">      
         <t:insertTemplate template="/skin/tiles/${spath}/footer.jsp" flush="true"/>
        </table>
       </center>
      </td>
     </tr>
    </table>
   </div>
  </center>
 </body>
</html>
