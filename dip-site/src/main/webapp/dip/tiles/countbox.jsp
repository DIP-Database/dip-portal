<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<table cellspacing="0" width="100%" id="countbox">
 <tr>
  <td class="cview">
   DIP contains information on:
   <ul class="clist">
    <li id="prt-count">(N/A) proteins</li>
    <li id="int-count">(N/A) interactions</li>
    <li id="art-count">(N/A) articles</li>
    <li id="org-count">(N/A) organisms</li>
   </ul>
  </td>
 </tr>
 <tr>
  <td class="cview">
   It is based on: 
   <ul class="clist">
    <li id="evd-count">(N/A) experiments</li> 
    <li id="cii-count">
      (N/A) automated inferences 
      [<b><a href='' onClick='YAHOO.mbi.modal.help("DIP Evidence Types","guide-evtype"); return false;'>?</a></b>]
    </li>
    <li id="aui-count">
       (N/A) authors' inferences 
       [<b><a href='' onClick='YAHOO.mbi.modal.help("DIP Evidence Types","guide-evtype"); return false;'>?</a></b>]
    </li>
   </ul>
  </td>
 </tr>
</table>
<script type="text/javascript">
  YAHOO.util.Event.addListener( window, "load",
  YAHOO.mbi.meta.counts.loadShort());
</script>
