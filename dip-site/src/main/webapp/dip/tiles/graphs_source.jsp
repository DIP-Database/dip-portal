<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<a href="graph" onClick='YAHOO.mbi.modal.graph({ns:"<s:property value='summary.ns'/>",
                                                ac:"<s:property value='summary.ac'/>"});
                         return false;'><img src="images/dipgraph3_50.png" border="0" alt="DIP graph"
        title="View <s:property value='#node'/> interactions"
        onMouseOver="this.src='images/dipgraph3_50.png';"
        onMouseOut="this.src='images/dipgraph3_50.png';"
        onClick="this.src='images/dipgraph2_click_50.png';"/></a>
