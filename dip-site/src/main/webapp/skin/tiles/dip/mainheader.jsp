<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<table cellpadding="0" cellspacing="0" width="100%">
<tbody>
 <tr>
  <td colspan="3">
   <table class="mainheader" cellspacing="0" width="100%">
   <tbody>
    <tr>
     <td class="headimg" valign="middle" width="100%">
      <a href="/">
       <img src="images/dip_logo_main_2_sm.png" alt="DIP logo" border="0"/>
      </a>
     </td>
     <td class="headcell">
      <table class="account" width="100%">
      <tbody>
       <tr>
        <td class="acccellmain" align="right">
         <s:if test="#session['USER_ID'] > 0">
           User:  |
           <a class="acMOFF" href='user?op=edit' onmouseover="this.className='acMON'" onmouseout="this.className='acMOFF'">
            <s:property value="#session['LOGIN']" />
           </a>
           |    
           <a class="acMOFF" href="user?op=logout" onmouseover="this.className='acMON'" onmouseout="this.className='acMOFF'">Log out</a>
         </s:if>
         <s:else>
           User:  |
           <a class="acMOFF" href="user?op=regf" onmouseover="this.className='acMON'" onmouseout="this.className='acMOFF'">Sign up</a>  
           |          
           <a class="acMOFF" href="user" onmouseover="this.className='acMON'" onmouseout="this.className='acMOFF'">Log in</a>
         </s:else>
        </td>
       </tr>
      </tbody>
      </table>
     </td>
    </tr>
   </tbody>
   </table>
  </td>
 </tr>
</tbody>
</table>
<table class="mpmenus" border="0" cellpadding="0" cellspacing="0" width="100%">
<tbody>
 <tr>
  <td align="left">
   <table class="mainmenu1" cellpadding="0" cellspacing="0" width="100%">
   <tbody>
    <tr>
     <td class="menu1first">&nbsp;</td>
     <td class="menu1" nowrap="nowrap">
      <a onmouseover="this.className='MON'" onmouseout="this.className='MOFF'" 
         class="topmenulink" href="page?id=search-record">DIP Data</a>
     </td>
     <td class="menu1" nowrap="nowrap">
      <a onmouseover="this.className='MON'" onmouseout="this.className='MOFF'" 
         class="topmenulink" href="page?id=tools">DIP Tools</a>
     </td>
     <td class="menu1" nowrap="nowrap">
      <a onmouseover="this.className='MON'" onmouseout="this.className='MOFF'" 
         class="topmenulink" href="page?id=about">About DIP</a>
     </td>

      <td class="menu1" nowrap="nowrap">
      <a onmouseover="this.className='MON'" onmouseout="this.className='MOFF'"
         class="topmenulink" href="news">News</a>
     </td>

     <td class="menu1" nowrap="nowrap">
      <a onmouseover="this.className='MON'" onmouseout="this.className='MOFF'" 
         class="topmenulink" href="page?id=links">Resources</a>
     </td>      
      <td class="menu1" nowrap="nowrap">
      <!-- <a onmouseover="this.className='MON'" onmouseout="this.className='MOFF'"  -->

       <s:if test="#session['USER_ID'] > 0">
        <a  class="topmenulink" href="userprefmgr">Preferences</a>
       </s:if>
       <s:else>
        <a  class="topmenulink disabled" >Preferences</a>
       </s:else> 
      </td>

     <s:set var="itemon" value="#session['USER_ROLE'].keySet().{? #this in {'administrator'}}.size > 0"/>
     <s:if test="#itemon">
      <td class="menu1" nowrap="nowrap">
       <a onmouseover="this.className='MON'" onmouseout="this.className='MOFF'" 
          class="topmenulink" href="usermgr">Site Manager</a>
      </td>
     </s:if>
     <td class="menu1last" width="100%">&nbsp;</td>
     <td class="menu1blank" align="right" nowrap="nowrap" width="10%">
      <a class="MOFF" onmouseover="this.className='MON'" onmouseout="this.className='MOFF'" 
         href="http://prolinks.mbi.ucla.edu">ProLinks</a> 
      | <a class="MOFF" onmouseover="this.className='MON'" onmouseout="this.className='MOFF'"
                  href="page?id=help">Help</a> 
      | <a class="MOFF" onmouseover="this.className='MON'" onmouseout="this.className='MOFF'" 
                  href="feedback">Contact Us</a>         
     </td>
     <td class="menu1blank">&nbsp;</td>
    </tr>
   </tbody>
   </table>
   <table class="mainmenubtm" cellpadding="0" cellspacing="0" width="100%">
   <tbody>
    <tr>
     <td>&nbsp;</td>
    </tr>
   </tbody>
   </table>
  </td>
 </tr>
</tbody>
</table>
