<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<table cellspacing="0" width="100%" id="countbox">
 <tr>
  <td class="cview">
   ProLinks provides information on:
   <ul class="clist">
     <li>Organisms:</li>
      <ul class="clist">
        <li id="proc-count">(N/A)</li>
	<li id="prt-count">(N/A)</li>
      </ul>
     <li>Functional linkages:</li>
     <ul class="clist">      
       <li id="link-count">(N/A)</li>
     </ul>
    </ul>
  </td>
 </tr>
</table>
<script type="text/javascript">
try{
  YAHOO.util.Event.addListener( window, "load", YAHOO.mbi.meta.prlcounts.loadShort() );
}catch(x) {
  alert(x);
}
</script>
