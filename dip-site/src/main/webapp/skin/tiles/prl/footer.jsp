<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="footer">
 <table width="100%" class="bottom" cellpadding="0" cellspacing="0">
  <tr>
   <td colspan="2">
    <center>
     <table width="100%" class="footer" cellpadding="0" cellspacing="0">
      <s:if test="#session['USER_ROLE'].administrator != null" >
       <%--<t:insertDefinition name="pageedit"/>  --%>
       <t:insertAttribute name="edit" ignore="true"/>
      </s:if>
      <tr>
       <td width="99%" class="citation"><b>Cite ProLinks:</b>
        Bowers PM, Pellegrini M, Thompson MJ, Fierro J,
        Yeates TO, Eisenberg D (2004)
        Prolinks: a database of protein functional linkages derived from coevolution.
        <i>Genome Biol</i> <b>5</b>(5):R35.
        [<A HREF="http://www.ncbi.nlm.nih.gov/pubmed/15128449">Pubmed</A>]
        [<A HREF="http://genomebiology.com/2004/5/5/R35">Article</A>]
       </td>
       <td class="citation"><img height="40" width="1" src="images/trans.gif"/></td>
<%--
       <td width="1%" class="imexfooter">
        <table align="center" cellpadding="4" cellspacing="0">
         <tr>
          <td width="1">
           <img height="40" width="1" src="images/trans.gif"/> 
          </td>
          <td>
           <i>
            <A HREF="page?skn=prl&id=pl_acknowledgements">Acknowledgements</A>
           </i>
          </td>
         </tr>
        </table>
       </td>
--%>
      </tr>
      <tr>
       <td class="copyright2" colspan="1" nowrap>
        <b>Copyright ${prolinks.copyright} UCLA</b>
        [<A HREF="page?skn=prl&id=pl_acknowledgments">Acknowledgments</A>]

       </td>
       <td class="version2" colspan="1" nowrap>
        <font size="-5">Ver: ${project.version} ($Build: ${portal.prof}${project.timestamp})</font>       
       </td>
      </tr>
     </table>
    </center>
   </td>
  </tr>
 </table>
</div>
