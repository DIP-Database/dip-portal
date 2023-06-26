<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<HTML>
 <HEAD>
  <link rel="stylesheet" href="css/dip2.css" type="text/css" title="dip2">
  <link rel="stylesheet" href="css/dip2tab.css" type="text/css" title="dip2">
  <TITLE>DIP Query</TITLE>
  <s:head/>
 </HEAD>

 <BODY onLoad="self.name='DIP_MA'; self.focus()">
  <SCRIPT TYPE="text/javascript" SRC="script/djs.js"        LANGUAGE="JavaScript"></SCRIPT>
  <SCRIPT TYPE="text/javascript" SRC="script/mitap.js"      LANGUAGE="JavaScript"></SCRIPT>
  <SCRIPT TYPE="text/javascript" SRC="script/tabSubmit.js" LANGUAGE="JavaScript"></SCRIPT>
  <center>
   <t:insertDefinition name="header" />
   <table width="100%" cellspacing="5" cellpadding="5" border="0">
    <tr>
     <s:form action="query">
       <td width="30%" align="right"> 
         Query: <s:textfield theme="simple" name="query"  label="Query"/>
       </td>
       <td width="10%">  
         |<s:checkbox theme="simple" name="debugOn" label="Debug" value="true" fieldValue="true"/> Debug|
       </td>
       <td width="10%">  
         |<s:checkbox theme="simple" name="bigOn" label="Big" value="false" fieldValue="true"/> Big|
       </td>
       <td width="20%" align="left">  
         Detail level <s:select theme="simple" name="detail" label="Detail" list="{'stub','short','base','full','deep','mit','test'}"/>
       </td>
       <td align="left" width="30%">
        <s:submit theme="simple" />
       </td>  
     </s:form>
    </tr>
   </table>
   <t:insertDefinition name="footer" />
  </center>
 </BODY>
</HTML>

