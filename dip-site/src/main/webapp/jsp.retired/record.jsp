<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<HTML>
 <HEAD>
  <TITLE>DIP Record</TITLE>
  <s:head/>
 </HEAD>
 <BODY onLoad="self.name='DIP_MA'; self.focus()">
  <center>
   <table width="100%" cellspacing="5" cellpadding="5" border="0">
    <tr>
     <s:form action="record?mdf=0:0:0&mst=1:1:1">
       <td width="35%" align="right"> 
         db: <s:textfield size="5" theme="simple" name="ns" value="dip"/>
         ac: <s:textfield size="6" theme="simple" name="ac" value="DIP-2942N"/>
         md: <s:textfield size="6" theme="simple" name="md"  value="N"/>
         sl: <s:textfield size="2" theme="simple" name="sl"  value="1"/>
       </td>
       <td width="15%">  
         |<s:checkbox theme="simple" name="debug" label="Debug" value="true" fieldValue="true"/> Debug|
       </td>
       <td width="15%">  
         |<s:checkbox theme="simple" name="big" label="Big" value="true" fieldValue="true"/> Big|
       </td>
       <td width="15%" align="left">  
         Detail <s:select theme="simple" name="dl" value="'full'" list="{'stub','short','base','full','deep','mit','test'}"/>
       </td>
       <td width="15%" align="left">  
         Return <s:select theme="simple" name="ret" value="view" list="{'view','modellist','model','data','counts'}" />
       </td>

       <td align="left" width="5%">
        <s:submit theme="simple" />
       </td>  
     </s:form>
    </tr>
   </table>
   <hr/>
  </center>
 </BODY>
</HTML>

