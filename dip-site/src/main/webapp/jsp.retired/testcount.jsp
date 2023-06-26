<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %>
<HTML>
 <HEAD>
  <link rel="stylesheet" href="css/dip2.css" type="text/css" title="dip2">
  <link rel="stylesheet" href="css/dip2tab.css" type="text/css" title="dip2">
  <TITLE>Count Test</TITLE>


  <SCRIPT TYPE="text/javascript" SRC="/dip/dojo/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug:true" LANGUAGE="JavaScript"></SCRIPT>

   <style type="text/css">
        @import "/dip/dojo/dijit/themes/tundra/tundra.css";
        @import "/dip/dojo/dojo/resources/dojo.css"
   </style>

    <script type="text/javascript">
      dojo.require("dojo.parser");
      dojo.require("dijit.form.TextBox");
      dojo.require("dijit.form.CheckBox");
      dojo.require("dijit.form.DateTextBox"); 
       
      function tabtest(ac) { // 
        dojo.xhrGet({
              url: "/dipdb/recordcount.action?ac="+ac+"&detail=full", 
              handleAs: "json",
              timeout: 5000,
              load: function(response, ioArgs) {
                      dojo.byId("t0").innerHTML = response.rc[0];
                      dojo.byId("t1").innerHTML = response.rc[1];
                      dojo.byId("t2").innerHTML = response.rc[2];
                      dojo.byId("t3").innerHTML = response.rc[3];
                      dojo.byId("t4").innerHTML = response.rc[4];
                      return response;
                     },
              error: function(response, ioArgs){
                        console.error("HTTP status code: ", ioArgs.xhr.status);
                        return response; 
                     }
          });
      }

      function loop(ac){
        setTimeout("tabtest('"+ac+"')",500);            
        setTimeout("tabtest('"+ac+"')",1000);            
        setTimeout("tabtest('"+ac+"')",2000);            
        setTimeout("tabtest('"+ac+"')",4000);            
        setTimeout("tabtest('"+ac+"')",8000);            
        setTimeout("tabtest('"+ac+"')",16000);            
        
      }

      function timedtest(ac,t0,tmax,t){
         if(t>0){
            t=t-1;
            ct=(t0-t)*(t0-t);
            if(ct>tmax){
               ct=tmax;
            } 
            setTimeout("tabtest('"+ac+"');timedtest('"+ac+"',"+t0+","+tmax+","+t+")",ct*1000);
         }  
      }
    </script>

 </HEAD>

 <BODY onLoad="self.name='DIP_MA'; self.focus()"  class="tundra">
  <center>
   <table width="100%" cellspacing="5" cellpadding="5" border="1">
    <tr>
       <td colspan="5"> 
          Count Test: 
      <form>
        <input type="checkbox" name="campaign" value="Y"  dojoType="dijit.form.CheckBox" onclick="timedtest('DIP-311N',5,16,5);" />
      </form>
       </td>
    </tr>
    <tr>
      <td width="20%">
       <div id="t0" style="font-size: big"></div>
      </td>
      <td width="20%">
       <div id="t1" style="font-size: big"></div>
      </td>
      <td width="20%">
       <div id="t2" style="font-size: big"></div>
      </td>
      <td width="20%">
       <div id="t3" style="font-size: big"></div>
      </td>
      <td width="20%">
       <div id="t4" style="font-size: big"></div>
      </td>
   </table>
  </center>
 </BODY>
</HTML>



