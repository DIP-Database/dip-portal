<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="base">
<script>
   YAHOO.util.Event.addListener( window, "load",
     YAHOO.mbi.view.summary.load( { "db":"<s:property value='siteDef'/>",
                                    "type":"evidence",
                                    "ns":"<s:property value='ns'/>",
                                    "ac":"<s:property value='ac'/>",
                                    "base-id":"base"
                                   } ));
</script>
<noscript>
    <ul>
        <s:iterator value="summary.attrList.attr">
            <li>
                <strong><s:property value="name"/>: </strong>
                <s:property value="value.value"/>
            </li>
        </s:iterator>
    </ul>
</noscript>
</div> 
