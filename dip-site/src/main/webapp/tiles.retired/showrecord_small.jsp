<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<HTML>
 <HEAD>
  <link rel="stylesheet" href="/dip/css/dip2.css" type="text/css" title="dip2"/>
  <link rel="stylesheet" href="/dip/css/dip2tab.css" type="text/css" title="dip2"/>
  <TITLE>
   DIP:<t:getAsString name="title"/>
  </TITLE>
 </HEAD>

 <BODY onLoad="self.name='DIP_MA'; self.focus()">
  <SCRIPT TYPE="text/javascript" SRC="script/djs.js"        LANGUAGE="JavaScript"/>
  <SCRIPT TYPE="text/javascript" SRC="script/mitap.js"      LANGUAGE="JavaScript"/>
  <SCRIPT TYPE="text/javascript"  SRC="script/tabSubmit.js" LANGUAGE="JavaScript"/>
  <center>

<s:if test="bigOn">
   <t:insertDefinition name="header" />
</s:if>

   <table width="100%" cellspacing="0" cellpadding="0" border="1">

<s:if test="debugOn">
     <tr>
      <td class="pagebody">
       Result size: <s:property value="result.size" />
     </td>
     </tr>
</s:if>

     <tr>
      <td class="pagebody">
        <t:insertAttribute name="query" />
      </td>
     </tr>

<s:if test="result.size>1">
     <tr>
      <td class="pagebody">          
       Multi hit table <!-- fallback - should be handled somewhere else --> 
      </td>
     </tr>
</s:if>
<s:elseif test="result.size==1">
     <tr>
      <td class="pagebody">
        <t:insertDefinition name="recordbody" />	
      </td>
     </tr>
</s:elseif>
<s:else>
      <td class="pagebody">         
        Query failed
      </td>
     </tr>
</s:else>
<s:if test="debugOn">
     <tr>
      <td class="pagebody">
        <t:insertAttribute name="debug" />	
      </td>
     </tr>
</s:if>

    </table>

<s:if test="bigOn">
    <t:insertDefinition name="footer" />
</s:if>
<s:else>
    <t:insertDefinition name="footer_small" />
</s:else>

  </center>
 </BODY>
</HTML>

