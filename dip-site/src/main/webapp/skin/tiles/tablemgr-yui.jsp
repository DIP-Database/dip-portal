<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script src="js/table-mgr-yui.js" type="text/javascript"></script>
<H1>Table Manager</H1>
</hr>
<table width="100%" cellspacing="0" cellpadding="0" border="0">
 <tr>
  <td>
   <div class="yui-skin-sam">
    <div id="tmgr-col-list" class="yui-navset tmgr-tab">
     <ul class="yui-nav">
       <li><a><em><img id="dlimg" class="dlogo" src="./images/dl16.png"></em></a></li>
     </ul>
     <div class="yui-content">
      <div id="noscript"></div>
      <div id="tmgr-col-0">
       <table cellspacing="0" width="100%">
        <tr>
         <td class="tmgr-col-body" valign="top" id="tmgr-col-body-0">
          <noscript>
           <center>
            <table id="swarn" width="90%" border="0"><tr><td align="center">
             This site requires JavaScript to function properly.  Please enable scripting.
            </td></tr></table>
           </center>
          </noscript>
         </td>
        </tr>
       </table>
      </div>
     </div>
    </div>
   </div>
  </td>
 </tr>
</table>


 
<br/><br/><br/><br/>
<%--  <s:property value="tables"/> --%>
<br/><br/><br/><br/>

<script>
 try{
   YAHOO.util.Event.addListener( window, "load", YAHOO.mbi.tablemgr.load() );
 } catch(x) {
   alert(x);
 }
</script>
