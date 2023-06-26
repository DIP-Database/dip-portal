<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<HTML>
 <HEAD>
  <link rel="stylesheet" href="css/dip2.css" type="text/css" title="dip2">
  <link rel="stylesheet" href="css/dip2tab.css" type="text/css" title="dip2">
  <TITLE>DIP Record</TITLE>
  <s:head/>
 </HEAD>

 <BODY onLoad="self.name='DIP_MA'; self.focus()">
  <SCRIPT TYPE="text/javascript" SRC="script/djs.js"        LANGUAGE="JavaScript"></SCRIPT>
  <SCRIPT TYPE="text/javascript" SRC="script/mitap.js"      LANGUAGE="JavaScript"></SCRIPT>
  <SCRIPT TYPE="text/javascript" SRC="script/tabSubmit.js" LANGUAGE="JavaScript"></SCRIPT>
  <center>
   
  <s:if test="big">
    <t:insertDefinition name="jqheader" />
  </s:if>

   <table width="100%" cellspacing="5" cellpadding="5" border="0">
    <tr>
     <s:form action="jqrecord?mdf=0:0:0&mst=1:1:0">
       <td width="45%" align="right"> 
         db: <s:textfield size="5" theme="simple" name="ns" value="dip"/>
         ac: <s:textfield size="6" theme="simple" name="ac" value="DIP-378N"/>
         md: <s:textfield size="6" theme="simple" name="md"  value="N"/>
         sl: <s:textfield size="2" theme="simple" name="sl"  value="1"/>
       </td>
       <td width="10%">  
         |<s:checkbox theme="simple" name="debug" label="Debug" value="true" fieldValue="true"/> Debug|
       </td>
       <td width="10%">  
         |<s:checkbox theme="simple" name="big" label="Big" value="true" fieldValue="true"/> Big|
       </td>
       <td width="15%" align="left">  
         Detail <s:select theme="simple" name="dl" value="full" list="{'stub','short','base','full','deep','mit','test'}"/>
       </td>
       <td width="15%" align="left">  
         Return <s:select theme="simple" name="ret" value="view" list="{'view','jqmodellist','jqmodel','jqdata','jqcounts'}" />
       </td>

       <td align="left" width="5%">
        <s:submit theme="simple" />
       </td>  
     </s:form>
    </tr>
   </table>
   <t:insertDefinition name="footer" />
  </center>
 </BODY>
</HTML>

