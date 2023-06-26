<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

 <s:set name="spath" value="%{skn}"/>

<t:insertAttribute name="jq-css" ignore="true"/>
<t:insertAttribute name="yui-css" ignore="true"/>
<t:insertAttribute name="dip-yui-css" ignore="true"/>

<link rel="stylesheet" href="skin/css/seqlib.css" type="text/css">


<link rel="stylesheet" href="skin/${spath}/css/dip2.css" type="text/css" title="dip2">
<link rel="stylesheet" href="skin/${spath}/css/dip2tab.css" type="text/css" title="dip2">
<link rel="stylesheet" href="skin/${spath}/css/dip2struts.css" type="text/css" title="dip2">
<link rel="stylesheet" href="skin/${spath}/css/dip2debug.css" type="text/css" title="dip2">

<t:insertAttribute name="jq-js" ignore="true"/>
<t:insertAttribute name="yui-js" ignore="true"/>
<t:insertAttribute name="dip-yui-js" ignore="true"/>

  