<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- ===========================================================================
 ! $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-site/s#$
 ! $Id:: dip-index.jsp 3580 2014-01-22 06:35:38Z lukasz                        $
 ! Version: $Rev:: 3580                                                        $
 !========================================================================= --%>

<s:set var="spath" value="dip"/>
<html>
 <head>
  <meta http-equiv="content-type" content="text/html; charset=utf-8">
  <title>DIP Home</title>
  <t:insertDefinition name="htmlhead">
   <t:putAttribute name="skn" value="dip"/>
  </t:insertDefinition>
 </head>
 
 <body onLoad="self.name='DIP_MA'; self.focus()" class="main-page yui-skin-sam">
   <center>  
     <t:insertTemplate template="/skin/tiles/dip/mainheader.jsp" flush="true"/>
     <t:insertDefinition name="dipmain" />
     <div id="footer">
       <table width="100%" class="bottom" cellpadding="0" cellspacing="0">
         <tr>
           <td>
             <center>
               <table width="100%" class="footer" cellpadding="0" cellspacing="0">
                 <t:insertTemplate template="/skin/tiles/dip/footer.jsp" flush="true" />
               </table>
             </center>
           </td>
         </tr>
       </table>
     </div>
   </center>
 </body>
</html>

