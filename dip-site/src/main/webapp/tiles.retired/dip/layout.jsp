<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
                      "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ===========================================================================
 ! $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-site/s#$
 ! $Id:: layout.jsp 859 2010-01-17 22:02:51Z lukasz                            $
 ! Version: $Rev:: 859                                                         $
 !========================================================================= --%>
<html lang="en">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title><t:getAsString name="title" ignore="true"/></title>
<t:insertDefinition name="htmlhead"/>
</head>

<body class="yui-skin-sam" onLoad="var nos = document.getElementById('noscript'); if ( nos !== null ) { nos.innerHTML='';}">
<center>
 <s:if test="mst=='' || mst==null">
  <s:if test="skn=='prl'">
   <t:insertDefinition name="prlmainheader"/>
  </s:if>
  <s:else>
   <t:insertDefinition name="mainheader"/> 
  </s:else>
 </s:if>
 <s:elseif test="bigOn">
     <t:insertDefinition name="header"/>
 </s:elseif> 
 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="<t:getAsString name='body-class' ignore='true'/>">
    <tr>
     <td>
       <t:insertAttribute name="body"/>
     </td>
    </tr>
 </table>
 <br/><br/><br/><br/><br/><br/>
<s:if test="skn=='prl'">
 <t:insertDefinition name="prlfooter"/>
</s:if>
<s:else>
 <t:insertAttribute name="footer"/>
</s:else>
</center>
</body>
</html>

