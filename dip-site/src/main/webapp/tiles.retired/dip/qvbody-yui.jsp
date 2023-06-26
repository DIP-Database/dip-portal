<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<table width="100%" cellspacing="0" cellpadding="0" border="0">
 <tr>
  <td>
   <div>
    <t:insertDefinition name="queryheader"/> 
   </div>
   <div class="yui-skin-sam">
    <div id="recTab" class="yui-navset rec-tab">
     <ul id="rec-tab-list" class="yui-nav">
       <li><a><em><img id="dlimg" class="dlogo" src="./images/dl16.png"></em></a></li>
<!--       <li class="selected"><a href="#summary"><em>Summary</em></a></li> -->
     </ul>
     <div id="rec-tab-content" class="yui-content">
      <div id="noscript">
        <table cellspacing="0" width="100%">
        <tr>
         <td class="rec-tab-filter-col" id="summary-panel">
         </td>
         <td class="rec-tab-body" valign="top" id="summary-body">
          <noscript>
           <center>
            <table id="swarn" width="90%" border="0"><tr><td align="center">
             This site requires JavaScript to function properly.  Please enable scripting.
            </td></tr></table>
           </center>
          </noscript>
          <t:insertDefinition name="querybase"/>
         </td>
        </tr>
       </table>
      </div>
<!--
      <div id="summary">
       <table cellspacing="0" width="100%">
        <tr>
         <td class="rec-tab-filter-col" id="summary-panel">
         </td>
         <td class="rec-tab-body" valign="top" id="summary-body">
          <noscript>
           <center>
            <table id="swarn" width="90%" border="0"><tr><td align="center">
             This site requires JavaScript to function properly.  Please enable scripting.
            </td></tr></table>
           </center>
          </noscript>
          <t:insertDefinition name="querybase"/>
         </td>
        </tr>
       </table>
      </div>
-->
     </div>
    </div>
    <s:if test="#stat.index==0">

    </s:if>
    <s:else>

    </s:else>
   </div>
  </td>
 </tr>
</table>
<iframe name="cifr" id="cifr" class="cifr" onLoad="YAHOO.mbi.view.cyt.timerReset();"></iframe>
<script>
  YAHOO.util.Event.addListener( window, "load",
  YAHOO.mbi.view.query( 'query?query=<s:property value="query"/>', '<s:property value="sl"/>' ));
</script>
