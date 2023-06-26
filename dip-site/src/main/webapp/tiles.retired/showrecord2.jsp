<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<HTML>
 <HEAD>
  <link rel="stylesheet" href="/dip/css/dip2.css" type="text/css" title="dip2"/>
  <link rel="stylesheet" href="/dip/css/dip2tab.css" type="text/css" title="dip2"/>
  <s:if test='rtp=="N"'>
<!-- node css -->
  </s:if> 
  <s:elseif test='rtp=="E"'>
<!-- link css -->
  </s:elseif> 
  <s:elseif test='rtp=="X"'>
<!-- evidence css -->
  </s:elseif> 
  <s:elseif test='rtp=="S"'>
<!-- source css -->
  </s:elseif> 
  <TITLE>
   <t:getAsString name="title"/>:<s:property value="ac"/>
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

<s:if test="record!=null">
     <tr>
      <td class="pagebody"> 
        <t:insertDefinition name="recordbody" />
      </td>
     </tr>
</s:if>
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
    <t:insertDefinition name="footer.small" />
</s:else>

  </center>
 </BODY>
</HTML>




