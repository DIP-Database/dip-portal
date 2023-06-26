<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="footer">
 <table width="100%" class="bottom" cellpadding="0" cellspacing="0">
  <tr>
   <td>
    <center>
     <table width="100%" class="footer" cellpadding="0" cellspacing="0">      
       <s:if test="#session['USER_ROLE'].administrator != null or
                   #session['USER_ROLE'].editor != null or
                   #session['USER_ROLE'].test != null" >
       <t:insertAttribute name="edit" ignore="true"/>
      </s:if>
      <tr>
       <td width="99%" class="citation"><b>Cite DIP:</b>
        Salwinski L, Miller CS, Smith AJ, Pettit FK, Bowie JU, Eisenberg D. (2004)
        The Database of Interacting Proteins: 2004 update.
        <i>NAR</i> <b>32</b>:D449-51.
        [<A HREF="http://www.ncbi.nlm.nih.gov:80/entrez/query.fcgi?cmd=Retrieve&amp;db=PubMed&amp;list_uids=14681454&amp;dopt=Abstract">Pubmed</A>]
        [<A HREF="http://nar.oupjournals.org/cgi/content/full/32/suppl_1/D449">Article</A>]
       </td>
       <td width="1%" class="imexfooter">
        <table align="center" cellpadding="4" cellspacing="0">
         <tr>
          <td>
           <a href="http://imex.sf.net/" target="vpop">
            <img height="40" src="images/imex_logo75x75.png" alt="IMEx Logo" border="0">
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
       <td class="copyright2" colspan="1"nowrap>
        <b>Copyright 1999-2012 UCLA</b>
        [<A HREF="page?id=terms">Terms of Use</A>]
        [<A HREF="page?id=acknowledgments">Acknowledgments</A>]
       </td>
       <td class="version2" colspan="1" nowrap>
        <font size="-5">Ver: ${dipportal.bld} (Rev:${dipportal.rev})</font>  
       </td>
      </tr>
     </table>
    </center>
   </td>
  </tr>
 </table>
</div>
