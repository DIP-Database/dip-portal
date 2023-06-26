<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="%{skn.length()>0}">
 <s:set var="spath" value="%{skn}"/>
</s:if>
<s:else>
 <t:importAttribute toName="spath" name='skn' ignore='true'/>  
</s:else>

<t:insertAttribute name="yui-css" ignore="true"/>
<t:insertAttribute name="dip-yui-css" ignore="true"/>

<t:insertAttribute name="tmgr-yui-css" ignore="true"/>

<link rel="stylesheet" href="skin/css/portal.css">
<link rel="stylesheet" href="skin/css/seqlib.css">

<link rel="stylesheet" href="skin/${spath}/css/skin.css">
<link rel="stylesheet" href="skin/${spath}/css/dip2tab.css">
<link rel="stylesheet" href="skin/${spath}/css/dip2struts.css">
<link rel="stylesheet" href="skin/${spath}/css/d3-dip.css">

<link rel="stylesheet" href="skin/${spath}/css/dip2debug.css">

<t:insertAttribute name="profile-css" ignore="true"/>

<t:insertAttribute name="yui-js" ignore="true"/>
<%--<t:insertDefinition name="portal-yui-js" ignore="true"/> --%>
<t:insertDefinition name="portal-yui-js"/> 

