<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<HTML>
 <HEAD>
  <link rel="stylesheet" href="/dip/css/dip2.css" type="text/css" title="dip2"/>
  <link rel="stylesheet" href="/dip/css/dip2tab.css" type="text/css" title="dip2"/>
  <TITLE>
   DIP:<t:getAsString name="title"/>:<s:property value="ac"/>
  </TITLE>
 </HEAD>

 <BODY onLoad="self.name='DIP_MA'; self.focus()">
  <SCRIPT TYPE="text/javascript" SRC="script/djs.js"        LANGUAGE="JavaScript"/>
  <SCRIPT TYPE="text/javascript" SRC="script/mitap.js"      LANGUAGE="JavaScript"/>
  <SCRIPT TYPE="text/javascript"  SRC="script/tabSubmit.js" LANGUAGE="JavaScript"/>
  <center>

<s:if test="bigOn">
   <t:insertAttribute name="header" />
</s:if>
         RTP:<s:property value="rtp"/>:PTR
   <table width="100%" cellspacing="0" cellpadding="0" border="1">

<s:if test="debugOn">
     <tr>
      <td class="pagebody">
       Result size: <s:property value="result.size" />
     </td>
     </tr>
     <tr>
      <td class="pagebody">
        <t:insertAttribute name="query" />
      </td>
     </tr>
</s:if>

<s:if test="record!=null">
     <tr>
      <td class="pagebody">         
        <!-- recordbody.jsp -->
        <t:insertDefinition name="recordbody" />
      </td>
     </tr>
</s:elseif>
<s:else>
      <td class="pagebody">         
        Query failed
        <!-- errorbody.jsp -->
<!--
     <t:insertDefinition name="errorbody" />
-->
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
   <t:insertAttribute name="footer" />
</s:if>
<s:else>
    <t:insertAttribute name="footer_small" />
</s:else>
  </center>
 </BODY>
</HTML>

