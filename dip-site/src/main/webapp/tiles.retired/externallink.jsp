<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:url id="extUrl" includeParams="none" action='elink'>
   <s:param name="ns" value="#ext.ns"/>
   <s:param name="ac" value="#ext.ac"/>
</s:url>
<s:a href="%{extUrl}"><s:property value="#ext.ac"/></s:a>
