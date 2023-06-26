<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script src="js/meta-yui.js" type="text/javascript" language="JavaScript"></script>
<script src="js/modal-yui.js" type="text/javascript" language="JavaScript"></script>

<table cellpadding="0" cellspacing="0" width="100%">
 <tr>
  <td class="pagebody" valign="top" width="100%">
   <table class="maintable" cellpadding="0" cellspacing="0" width="100%">
    <tr>
     <td width="22%" align="center">
      <table border="0" cellpadding="0" cellspacing="2" width="100%" class="fstrh">
       <tr>
        <td valign="middle">
         <table cellpadding="0" cellspacing="0" valign="middle">
          <tr>
           <td class="flinksmid">
            <a href="page?id=pl_software">
             <img class="main" src="images/main_software_40.png" alt="software" border="0">
            </a>
           </td>
           <td class="flinksmid">
            View interaction networks<br>
            <a href="page?id=pl_software">
             <b>Get software</b>
            </a>
           </td>
          </tr>
          <tr>
           <td class="flinks">
            <a href="page?id=pl_download">      
             <img class="main" src="images/main_download_40.png" alt="download" border="0">
            </a>
           </td>
           <td class="flinks">
            Get all Linkages in ProLinks<br>
            <a href="page?id=pl_download"> 
             <b>Download ProLinks data</b>
            </a>
           </td>
          </tr>
         </table>
        </td>
       </tr>
      </table>
     </td>

     <td class="fspacer" width="3%">&nbsp;</td>
<!--     <td class="fs" width="50%"> -->
     <td class="TDM fqb" width="50%">      

        <form id="query" name="query" action="php_query" method="post">
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="fstrh">
          <tr>
            <td class="fqtitle" align="left" style="white-space: nowrap;">
             SEARCH ProLinks for functional linkages
            </td>
          </tr>

         <tr>
           <td class="fsbody" align="left">

             <Table style="margin:auto" border="0px">
               <tr> 
                 <td>
                   <img style="display:block; margin-left:0; margin-right:auto"
                        src="images/man_at_work.png"
                        width="60px" />
                 </td>
            
                 <td style="padding:15px">
                   <h2 style="text-align:center; color:#8B4513">ProLinks is temporarily down</h2>
                 </td>
            
                 <td>
                   <img style="display:block; margin-right:0; margin-left:auto"
                        src="images/man_at_work.png"
                        width="60px" />
                 </td>
               </tr>
            
               <tr>
                 <td colspan="3" style="text-align:center; padding:15px;">
                   Sorry for the inconvenience, we are updating our
                   website to enhance the quality of our service.
                   <br />	<br />
            
                   Please, submit your query at a later time. 
                 </td>
               </tr>
             </table>
           </td>
         </tr>		           
       </table>
       </form>
     </td>


     <td class="fspacer">&nbsp;</td>

     <td class="TDM" style="white-space: nowrap;" width="22%">
      <table cellspacing="0" width="100%" class="fstrh">
       <tr>
        <td class="fttitle">ProLinks News</td>
       </tr>
       <tr>
        <td class="ftbody" valign="top">
          Recent ProLinks news:
          <t:insertDefinition name="newsbox">
          <t:putAttribute name="feed" value="prlnews"/>
         </t:insertDefinition>
        </td>
       </tr>
       <tr>
        <td class="ftfoot" align="right" valign="bottom">
          <i>See more <a href="prlnews">ProLinks News</a></i>
        </td>
       </tr>
      </table>
     </td>
    </tr>

    <tr><td colspan="5" class="fspacer"></td></tr>

    <tr>
     <td class="TDM" style="white-space: nowrap;" width="22%">
        <table cellspacing="0" width="100%" class="fsbrh">
         <tr>
          <td class="fbtitle">Advanced Searches</td>
         </tr>
         <tr>
          <td class="fbbody" valign="top">
            Other queries you can submit to ProLinks:
            <ul>
              <li><a href="page?id=pl_db_id">
                Submit an ID, name, or keyword</a>
              </li>

              <li><a href="page?id=pl_mult_prot">
                Submit multiple protein IDs</a>
              </li>

              <li><a href="page?id=pl_common">
                Find links shared between proteins</a>
              </li>

              <li><a href="page?id=pl_connections">
                Find paths of links connecting two 
                proteins</a>
              </li>

              <li><a href="page?id=pl_prot_seq">
                Submit a protein sequence</a>
              </li>
            </ul>
           </td>
         </tr>
         <tr>
          <td class="fbfoot" align="right" valign="bottom">
            <i>More help with <a href="page?id=pl_query">Searching</a></i>
          </td>
         </tr>
        </table>
     </td>
     <td class="fspacer" width="3%">&nbsp;</td>

     <td class="TDM" width="50%">
        <table border="0" cellpadding="3" cellspacing="0" width="100%" class="fsbrh">
         <tr>
          <td class="fbtitle">
           Inference methods in ProLinks
          </td>
         </tr>
         <tr>
          <td class="fbbody" valign="top"> 
           <table width="100%">
            <tr valign="top">
             <td class="fbcell"> 
               Rosetta Stone
             </td>

             <td class="fcell" > 
               uses gene fusion events in different organisms to infer functional linkages.
             </td>
            </tr>

            <tr valign="top">
             <td class="fbcell"> 
               Phylogenetic<br/>Profiles
             </td>

             <td class="fcell">
               uses patterns of presence and absence of proteins across multiple  
               genomes to infer functional linkages.
             </td>
            </tr>

            <tr valign="top">
             <td class="fbcell"> 
               Gene Cluster
             </td>

             <td class="fcell"> 
              uses likely memberhip to operons to infer functional linkages.
             </td>
            </tr>

            <tr valign="top">
             <td class="fbcell"> 
               Gene Neighbor
             </td>

             <td class="fcell"> 
               uses both chromosomal proximity and phylogenetic distribution to  
               infer functional linkages.
             </td>
            </tr>
           </table>
          </td>
         </tr>
         <tr valign="">
          <td class="fbfoot" align="left" valign="bottom">
           <a href="http://genomebiology.com/2004/5/5/R35">
            <i>Check the literature for more details...</i>
           </a>
          </td>
         </tr>
        </table>
     </td>
     <td class="fspacer" width="3%">&nbsp;</td>
     <td class="TDM" style="white-space: nowrap;" width="22%">
<%--
        <table cellspacing="0" width="100%" class="fsbrh">
         <tr>
          <td class="ftitle">ProLinks Contents</td>
         </tr>
         <tr>
          <td class="fview" valign="top">
           ProLinks provides information on:
           <ul>
            <li>Organisms:</li>
               <ul>
                  <li><b>900</b> Genomes</li>
               </ul>
            <li>Functional linkages:</li>
               <ul>
                  <li><b>349,246,087</b> inferences</li>
	       </ul>
           </ul>
           <br/><br/><br/><br/><br/>
          </td>
         </tr>
         <tr>
          <td class="fsbody2" align="right" valign="bottom">
           <i>See more <a href="page?id=pl_statistics">ProLinks Statistics</a></i>
          </td>
         </tr>
        </table>
--%>
        <table cellspacing="0" width="100%" class="fsbrh">
         <tr>
          <td class="fbtitle">ProLinks Contents</td>
         </tr>
         <tr>
          <td class="fbbody" valign="top">
           <t:insertDefinition name="prlcountbox"/>
         </td>
         </tr>
         <tr>
          <td class="fbfoot" align="right" valign="bottom">
           <i>See more <a href="page?id=pl_statistics">ProLinks Statistics</a></i>
          </td>
         </tr>
        </table>
     </td>
    </tr>
   </table>
   
 </td>
</tr>
</table>

<br /><br /><br /><br /><br />

