<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- ===========================================================================
 ! $HeadURL:: https://lukasz@imex.mbi.ucla.edu/svn/dip-ws/trunk/dip-portal/src#$           
 ! $Id:: prolinks.jsp 1403 2011-01-07 23:41:36Z lukasz                         $
 ! Version: $Rev:: 1403                                                        $
 !========================================================================= --%>

<html>
 <head>
  <meta http-equiv="content-type" content="text/html; charset=utf-8">
  <title>ProLinks Home</title>
  <t:insertDefinition name="htmlhead">
   <t:putAttribute name="skn" value="pfl"/>  
  </t:insertDefinition>
 </head>

 <body onLoad="self.name='DIP_MA'; self.focus()" class="main-page yui-skin-sam">
  <center> 
   <t:insertTemplate template="/skin/tiles/pfl/mainheader.jsp" flush="true"/> 
   <t:insertDefinition name="prlmain" />
   <t:insertTemplate template="/skin/tiles/pfl/footer.jsp" flush="true"/> 
  </center>
 </body>
</html>

