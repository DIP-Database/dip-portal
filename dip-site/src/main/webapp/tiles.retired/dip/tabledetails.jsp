<%@ taglib prefix="s" uri="/struts-tags" %>

<s:set name="theme" value="'simple'" scope="page"/>

<div class="upper-right-button">
 <s:submit name="addColumn" value="Add Column"/>
</div>
<div id="columnTabs" class="yui-navset">
 <ul class="yui-nav">
  <s:iterator value="tableColumn">
   <li><a href=#><em><s:property/></em></a></li>
  </s:iterator>
 </ul>
 <div class="yui-content"></div>
</div>
