<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">

 <head>

  <link rel="stylesheet" href="css/dip2.css" type="text/css" title="dip2">
  <link rel="stylesheet" href="css/dip2tab.css" type="text/css" title="dip2">
  <title>
   <t:getAsString name="title"/>:<s:property value="ac"/>
  </title>

  <script type="text/javascript" language="JavaScript"
          src="script/djs.js"></script>
  <script type="text/javascript" language="JavaScript"
          src="script/mitap.js"></script>
  <script type="text/javascript" language="JavaScript"
          src="script/tabSubmit.js"></script>

  <script type="text/javascript" language="JavaScript"
          src="dojo/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug:false"></script>

  <script type="text/javascript" language="JavaScript"
          src="js/recstate.js"></script>

   <style type="text/css">
        @import "dijit/themes/tundra/tundra.css";
        @import "dojo/resources/dojo.css"
   </style>

  <script type="text/javascript" language="JavaScript">
      dojo.require("dojo.parser");
      dojo.require("dijit.form.TextBox");
      dojo.require("dijit.form.CheckBox");
      dojo.require("dijit.form.DateTextBox");
  </script>
 </head>

 <body onLoad="self.name='DIP_MA'; self.focus()">
  <center>

   <t:insertDefinition name="header" />

   <table width="100%" cellspacing="0" cellpadding="0">

<s:if test="record!=null">
     <tr>
      <td class="pagebody_record"> 
       <t:insertDefinition name="recordbody" />
      </td>
     </tr>
</s:if>
<s:else>
      <td class="pagebody_record">         
        Query failed
      </td>
     </tr>
</s:else>

<s:if test="debugOn">
     <tr>
      <td class="pagebody_record">
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
 </body>
</html>




