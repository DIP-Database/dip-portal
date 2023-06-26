<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

   <s:set name="menu1" value='<t:getAsString name="menu1"'/>  
   <s:set name="menu2" value='<t:getAsString name="menu2"'/>  
   <s:set name="menu3" value='<t:getAsString name="menu3"'/>  
   
   <table width="100%" cellspacing="0" cellpadding="0" border="0">
    <tr>
     <td class="menulogo" rowspan="3" width="1%">

      <table width="100%" cellspacing="0" border="0">
       <tr>
        <td class="logo" valign="middle">
         <a href="index.jsp"><img height="55" border="0" src="images/dip_logo_2_sm55.png" alt="DIP"/></a>
        </td>
       </tr>
      </table>

     </td>
     <td class="topmenu" align="left">
   
      <table class="menu1_background" width="100%" cellspacing="0" cellpadding="0">
       <tr>
        <td class="menu1first">&nbsp;
        </td>
        <s:if test='menu1=="Data"'>
            <td class="menu1select">
        </s:if>
        <s:else> 
            <td class="menu1">
        </s:else>
            <a onMouseOver="this.className='mmMON'" onMouseOut="this.className='mmMOFF'" class="topmenulinkon" HREF="Data.jsp">DIP&nbsp;Data</a>
            </td>
        <td class="menu1">
         <a  onMouseOver="this.className='MON'" onMouseOut="this.className='MOFF'" class="topmenulink" HREF="/dip/Main.cgi?SM=2">DIP&nbsp;Tools</a>
        </td>
        
        <td class="menu1">
         <a  onMouseOver="this.className='MON'" onMouseOut="this.className='MOFF'" class="topmenulink" HREF="/dip/Main.cgi?SM=3">About&nbsp;DIP</a>
        </td>
        <td class="menu1">
         <a  onMouseOver="this.className='MON'" onMouseOut="this.className='MOFF'" class="topmenulink" HREF="/dip/Main.cgi?SM=4">Links</a>
        </td>
        <td width="100%" class="menu1last">&nbsp;
        </td>
        <td nowrap class="menu1blank">

         <table width="100%" cellpadding="0" cellspacing="0">
          <tr>
           <td align="right" class="acccell">
            User: | <a class="acMOFF" onMouseOver="this.className='acMON'" onMouseOut="this.className='acMOFF'" HREF="/dip/Login.cgi?R=1" >Sign up</a>
            | <a class="acMOFF" onMouseOver="this.className='acMON';" onMouseOut="this.className='acMOFF';" HREF="/dip/Login.cgi" >Log in</a>
           </td>
          </tr>
         </table>
   
        </td>
        <td class="menu1blank">&nbsp;
        </td>
       </tr>
      </table>
   
     </td>
    </tr>
    <tr>
     <td class="midmenu">
   
      <table class="menu2_background" width="100%" cellspacing="0" cellpadding="0">
       <tr>
        <td class="menu2first">&nbsp;
        </td>
        <s:if test='menu2=="Search"'>        
        <td class="menu2select" nowrap>
        </s:if>
        <s:else> <td class="menu2" nowrap>
        </s:else>
         <a onMouseOver="this.className='bmMON'" onMouseOut="this.className='bmMOFF'" class="midmenulinkon" HREF="/dip/Search.cgi">Search the Database</a>
        </td>
        <td class="menu2" nowrap>
         <a onMouseOver="this.className='mmMON'" onMouseOut="this.className='mmMOFF'" class="midmenulink" HREF="/dip/Download.cgi">Download Data</a>
        </td>
        <td class="menu2" nowrap>
         <a onMouseOver="this.className='mmMON'" onMouseOut="this.className='mmMOFF'" class="midmenulink" HREF="/dip/Submit.cgi">Submit Interactions</a>
        <td width="100%" class="menu2last">&nbsp;
        </td>
        <td class="menu2blank">
   
         <table width="100%" cellpadding="0" cellspacing="0">
          <tr>
           <td align="right" nowrap>
            <a class="midmenulink" onMouseOver="this.className='mmMON'" onMouseOut="this.className='mmMOFF'" HREF="/dip/Main.cgi">Home</a>
            | <a class="midmenulink" onMouseOver="this.className='mmMON'" onMouseOut="this.className='mmMOFF'" HREF="/dip/Guide.cgi">Help</a>
            | <a class="midmenulink" onMouseOver="this.className='mmMON'" onMouseOut="this.className='mmMOFF'" HREF="/dip/Feedback.cgi">Contact Us</a>
           </td>
          </tr>
         </table>

        </td>
        <td class="menu2blank">&nbsp;
        </td>
       </tr>
      </table>
   
     </td>
    </tr>
    <tr>
     <td class="btmmenu" colspan="2">

      <table width="100%" cellspacing="0" cellpadding="0">
       <tr>
        <td width="100%">
   
         <table class="menu3_background" width="100%" cellspacing="0" cellpadding="0">
          <tr>
           <td class="menu3first">&nbsp;
           </td>
           <td class="menu3">
            <a onMouseOver="this.className='bmMON'" onMouseOut="this.className='bmMOFF'" class="btmmenulink" HREF="">Text&nbsp;Search</a>
           </td>
           <td class="menu3">
            <a onMouseOver="this.className='bmMON'" onMouseOut="this.className='bmMOFF'" class="btmmenulink" HREF="">Sequence&nbsp;&amp;&nbsp;Motif&nbsp;Search</a>
           </td>
           <td width="100%" class="menu3last">&nbsp;
           </td>
          </tr>
         </table>

        </td>
        <td class="btmsearch">

         <table cellspacing="0" cellpadding="0">
          <tr>
           <td align="left" class="btmsearchtab" nowrap>
            <s:form action="query" theme="simple">
             <s:hidden name="debugOn" value="true" />
             <s:hidden name="bigOn" value="true" />
             <s:hidden name="tp" value="0" />
             <s:hidden name="detail" value="full" />
             <s:textfield theme="simple" name="query" size="32" size="32" maxlength="128"/>
             <s:submit type="input" tabindex="3" name="Search" value="Search DIP" theme="simple"/>
            </s:form>
           </td>
           <td class="menu3blank">&nbsp;
           </td>
          </tr>
         </table>

        </td>
       </tr>
      </table>

     </td>
    </tr>
   </table>
