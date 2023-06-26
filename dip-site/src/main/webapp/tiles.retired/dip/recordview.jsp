<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script src="js/modal-yui.js" type="text/javascript" language="JavaScript"></script>
<script src="js/help-yui.js" type="text/javascript" language="JavaScript"></script>
<script src="js/side-panel-yui.js" type="text/javascript" language="JavaScript"></script>
<script src="js/view-yui.js" type="text/javascript" language="JavaScript"></script>
<script src="js/view-summary-yui.js" type="text/javascript" language="JavaScript"></script>

<script type="text/javascript" src="http://mbostock.github.com/d3/d3.min.js"></script>
<script type="text/javascript" src="http://mbostock.github.com/d3/d3.geom.min.js"></script>
<script type="text/javascript" src="http://mbostock.github.com/d3/d3.layout.min.js"></script>

<script src="js/d3-force.js" type="text/javascript" language="JavaScript"></script>
<script src="js/d3-modal-yui.js" type="text/javascript" language="JavaScript"></script>
<script src="js/d3-gui.js" type="text/javascript" language="JavaScript"></script>
<script src="js/d3.js" type="text/javascript" language="JavaScript"></script>

<s:if test="debug">
 <s:form action="record?mdf=0:0:0&mst=1:1:1" theme="simple">
  <table class="debug" width="100%" cellspacing="0">
   <tr>
    <td width="45%" align="center">
         db: <s:textfield size="5" theme="simple" name="ns"  label="Database"/>
         ac: <s:textfield size="6" theme="simple" name="ac"  label="Acccession"/>
         md: <s:textfield size="6" theme="simple" name="md"  label="Model"/>
         sl: <s:textfield size="2" theme="simple" name="sl"  label="Slot"/>
    </td>
    <td width="10%">
         |<s:checkbox theme="simple" name="debug" label="Debug" value="true" fieldValue="true"/> Debug|
    </td>
    <td width="10%">
         |<s:checkbox theme="simple" name="big" label="Big" value="true" fieldValue="true"/> Big|
    </td>
    <td width="15%" align="left">
         Detail <s:select theme="simple" name="dl" label="Detail" list="{'stub','short','base','full','deep','mit','test'}"/>
    </td>
    <td width="15%" align="left">
         Return <s:select theme="simple" name="ret" label="Retun" list="{'view','modellist','model','data','counts'}" />
    </td>
    <td align="left" width="5%">
        <s:submit theme="simple" />
    </td>
   </tr>
  </table>
 </s:form>
</s:if>
<div id="modal">
  <div id="d3-modal"></div>
  <div id="d3-menu"></div>
</div>
<table width="100%" cellspacing="0">
 <s:if test="summary!=null">
  <tr>
   <td class="pagebody_record"> 
       <t:insertDefinition name="rvbody" />
   </td>
  </tr>
 </s:if>
 <s:else>
  <tr>
   <td class="pagebody_record">         
        Query failed
   </td>
  </tr>
 </s:else>
 <s:if test="debug">
   <tr>
    <td class="pagebody_record">
      <t:insertDefinition name="debug" />	
    </td>
   </tr>
 </s:if>
</table>
