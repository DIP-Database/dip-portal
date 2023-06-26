<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<HTML>
 <HEAD>
  <link rel="stylesheet" href="css/dip2.css" type="text/css" title="dip2">
  <link rel="stylesheet" href="css/dip2tab.css" type="text/css" title="dip2">
 <TITLE>
  <t:getAsString name="title"/>
</TITLE>
 </HEAD>

 <BODY onLoad="self.name='DIP_MA'; self.focus()">
  <SCRIPT TYPE="text/javascript" SRC="script/djs.js"           LANGUAGE="JavaScript">
  </SCRIPT>
  <SCRIPT TYPE="text/javascript" SRC="script/mitap.js"           LANGUAGE="JavaScript">
  </SCRIPT>
<SCRIPT TYPE="text/javascript"  SRC="script/tabSubmit.js" LANGUAGE="JavaScript"></SCRIPT>
  <center>
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
     <tr>
      <td class="menulogo" rowspan="3" width="1%">
      <table width="100%" cellspacing="0" border="0">
       <tr>
        <td class="logo" valign="middle">
         <A HREF="/dip/Main.cgi"><img height="55" border="0" src="/dip/images/dip_logo_2_sm55.png" alt="DIP"></A>        </td>
       </tr>
      </table>
      </td>
      <td class="topmenu" align="left">
       <table class="menu1_background" width="100%" cellspacing="0" cellpadding="0">
        <tr>
         <td class="menu1first">&nbsp;</td>
         <td class="menu1select">
<A onMouseOver="this.className='mmMON'"   onMouseOut="this.className='mmMOFF'" class="topmenulinkon" HREF="Main.cgi?SM=1">DIP&nbsp;Data</A>
         </td>
         <td class="menu1">
<A  onMouseOver="this.className='MON'"    onMouseOut="this.className='MOFF'" class="topmenulink" HREF="Main.cgi?SM=2">DIP&nbsp;Tools</A>
         </td>
         <td class="menu1">
<A  onMouseOver="this.className='MON'"    onMouseOut="this.className='MOFF'" class="topmenulink" HREF="Main.cgi?SM=3">About&nbsp;DIP</A>
         </td>
         <td class="menu1">
<A  onMouseOver="this.className='MON'"    onMouseOut="this.className='MOFF'" class="topmenulink" HREF="Main.cgi?SM=4">Links</A>
         </td>
         <td width="100%" class="menu1last">&nbsp;</td>
<td nowrap class="menu1blank">
<table width="100%" cellpadding="0" cellspacing="0">
 <tr>
  <td align="right" class="acccell">
   User: 
none
 | <A onMouseOver="this.className='acMON'"       onMouseOut="this.className='acMOFF'"  HREF="Login.cgi?R=1" class="acMOFF">Sign up</a>  | <A onMouseOver="this.className='acMON';" onMouseOut="this.className='acMOFF';"  HREF="Login.cgi" class="acMOFF">Log in</a>   </td>
 </tr>
</table>
</td>
         <td class="menu1blank">&nbsp;</td>
        </tr>
       </table>
      </td>
     </tr>
     <tr>
      <td class="midmenu">
       <table class="menu2_background" width="100%" cellspacing="0" cellpadding="0">
        <tr>
         <td class="menu2first">&nbsp;</td>
         <td class="menu2select">
<A  onMouseOver="this.className='bmMON'"      onMouseOut="this.className='bmMOFF'" class="midmenulinkon" HREF="Search.cgi"><nobr>Search the Database</nobr></A>
         </td>
         <td class="menu2">
<A  onMouseOver="this.className='mmMON'"      onMouseOut="this.className='mmMOFF'" class="midmenulink" HREF="Download.cgi"><nobr>Download Data</nobr></A>
         </td>
         <td class="menu2">
<A  onMouseOver="this.className='mmMON'"      onMouseOut="this.className='mmMOFF'" class="midmenulink" HREF="Submit.cgi"><nobr>Submit Interactions</nobr></A>
         <td width="100%" class="menu2last">&nbsp;</td>
<td class="menu2blank">
<table width="100%" cellpadding="0" cellspacing="0">
 <tr>
  <td align="right" nowrap>
    <A class="midmenulink" onMouseOver="this.className='mmMON'" onMouseOut="this.className='mmMOFF'" HREF="Main.cgi">Home</A>  | <A class="midmenulink" onMouseOver="this.className='mmMON'" onMouseOut="this.className='mmMOFF'" HREF="Guide.cgi">Help</A>  | <A class="midmenulink" onMouseOver="this.className='mmMON'" onMouseOut="this.className='mmMOFF'" HREF="Feedback.cgi">Contact Us</A>   </td>
 </tr>
</table>
</td>
         <td class="menu2blank">&nbsp;</td>
        </tr>
       </table>
      </td>
    </tr>
     <tr>
      <td class="btmmenu" colspan="2">
       <table class="menu3_background" width="100%" cellspacing="0" cellpadding="0">
        <tr>
         <td width="94%" class="menu3null">&nbsp;</td>
        </tr>
       </table>
      </td>
     </tr>
    </table>
     <table width="100%" cellspacing="0" cellpadding="0">
      <tr>
      <td class="pagebody">
         
       Query:   <s:property value="query" />


       </ul>
      </td>
     </tr>
    </table>
    <table width="100%" class="bottom" cellpadding="0" cellspacing="0">
      <tr>
       <td colspan="2">
        <center>
         <table width="100%" class="footer" cellpadding="0" cellspacing="0">
          <tr>
           <td width="99%" class="citation"><b>Cite DIP:</b>
            Salwinski L, Miller CS, Smith AJ, Pettit FK, Bowie JU, Eisenberg D. (2004)
            The Database of Interacting Proteins: 2004 update.
            <i>NAR</i> <b>32</b>:D449-51.
            [<A HREF="http://www.ncbi.nlm.nih.gov:80/entrez/query.fcgi?cmd=Retrieve&db=PubMed&list_uids=14681454&dopt=Abstract">Pubmed</A>]
            [<A HREF="http://nar.oupjournals.org/cgi/content/full/32/suppl_1/D449">Article</A>]
           </td>
           <td width="1%" class="imexfooter">
            <table align="center" cellpadding="4" cellspacing="0">
             <tr>
              <td>
               <a href="http://imex.sf.net/" target="vpop">
                <img height="40" src="/dip/images/imex_logo75x75.png" alt="IMEx Logo" border="0">
               </a>
              </td>
              <td align="left" nowrap>
               A member of the IMEx Consortium<br />of Molecular Interaction providers
              </td>
             </tr>
            </table>
           </td>
          </tr>
          <tr>
           <td class="copyright2" colspan="2">
             <b>Copyright 1999-2008 UCLA</b>             [<A HREF="files/termsofuse.html">Legal Notices</A>]
             With the exception of IMEx source records, the DIP database is 
             the property of the Regents of the University of California.
             It is forbidden to redistribute, derivatize, or encapsulate 
             the DIP in another database without permission from UCLA and 
             David Eisenberg.
             The IMEx source records are freely available under the terms set
             by <A HREF="http://imex.sourceforge.net/">The IMEx Consortium</A>.
           </td>
          </tr>
         </table>
        </center>
       </td>
      </tr>
      <tr>
       <td align="center" width="100%" colspan="2">
        <table width="98%" cellspacing="0">
         <tr>
          <td align="right">
            <font size="-5"><i>             Design by:
              <A HREF="mailto:rebecca@mbi.ucla.edu">RN</A> and
              <A HREF="mailto:lukasz@mbi.ucla.edu">LS</A>.
            </i></font>
          </td>
         </tr>
        </table>
       </td>
      </tr>
     </table>
  </center>
 </BODY>
</HTML>

