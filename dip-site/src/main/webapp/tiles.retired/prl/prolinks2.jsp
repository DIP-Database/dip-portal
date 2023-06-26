<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<table cellpadding="0" cellspacing="0" width="100%">
 <tr>
  <td class="pagebody" valign="top" width="100%">
   <table class="maintable" cellpadding="0" cellspacing="0" width="100%">
    <tr>
     <td align="center">
      <table border="0" cellpadding="0" cellspacing="2" width="100%">
       <tr>
        <td valign="top">
         <table cellpadding="0" cellspacing="0">
          <tr>
           <td class="flinksmid">
            <a href="page?id=software">
             <img class="main" src="images/main_software_40.png" alt="software" border="0">
            </a>
           </td>
           <td class="flinksmid">
            View interaction networks<br>
            <a href="page?id=software">
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
            <a href="page?skn=prl&id=pl_download"> 
             <b>Download ProLinks data</b>
            </a>
           </td>
          </tr>
         </table>
        </td>
        <td class="fspacer" width="3%">&nbsp;</td>
        <td class="fs" width="90%">
         <table border="0" cellpadding="0" cellspacing="0">
          <tr>
           <td class="fstitle" colspan="3" align="left">
            SEARCH ProLinks for functional linkages
           </td>
         </tr>
         <tr>
          <form id="query" name="query" action="http://codd.mbi.ucla.edu/prolinks/php/pl_form_reader.php" method="post">
           <td align="right" class="fsbody" width="1%">
            <select name="id_type" id="dl">
             <option value="auto_detect" selected> Detect ID type automatically</option> 
             <option value="refseq">NCBI RefSeq</option>
             <option value="gi">GI Number</option>
             <option value="sp">UniProt Accession</option>
            </select>
           </td>
           <td align="left" class="fsbody" width="1%">
            <input type="text" name="id" size="32" maxlength="128" value="" id="query"/>
           </td>        
           <td class="fsbody" align="left">
            <input type="hidden" name="debugOn" value="true" id="debugOn"/>
            <input type="hidden" name="bigOn" value="true" id="bigOn"/>
            <input type="hidden" name="dl" value="full" id="dl"/>
            <input type="submit" id="Search" name="Search" value="Search ProLinks" tabindex="3"/>
            &nbsp;<A HREF="page?skn=prl&id=pl_help">Help</A>; <A HREF="page?skn=prl&id=pl_tutorial">Tutorial</A>
            <input type="hidden" name="form" value="s_db_id">
           </td>
          </form>
         </tr>
         <tr>
          <td colspan="3" class="fsbody" align="left">
           Enter a public database identifier associated with a protein. 
          </td>
         </tr>
        </table>
       </td>
      </tr>
     </table>
     <br/><br/>
     <table cellpadding="0" cellspacing="2" width="100%">
      <tr>
       <td class="TDM" style="white-space: nowrap;" width="22%">
        <table cellspacing="0" width="100%">
         <tr>
          <td class="ftitle">Search Help</td>
         </tr>
         <tr>
          <td class="fview">
            Other queries you can submit to ProLinks:
            <ul>
             <li><a href="page?skn=prl&id=pl_prot_attr">
              Get links by submitting a protein attribute</a>
             </li>

             <li><a href="page?skn=prl&id=pl_mult_prot">
              Get links for multiple protein IDs</a>
             </li>

             <li><a href="page?skn=prl&id=pl_common">
              Find links shared between proteins</a>
             </li>

             <li><a href="page?skn=prl&id=pl_connections">
              Find links connecting two proteins</a>
             </li>
            </ul>
            <br/><br/><br/><br/><br/><br/><br/>
            <a href="page?id=pl_query"><i>More help with searching</i></a>
          </td>
         </tr>
        </table>
       </td>
       <td class="fspacer" width="3%">&nbsp;</td>
       <td class="TDM" width="50%">
        <table border="0" cellpadding="3" cellspacing="0" width="100%">
         <tr>
          <td class="ftitle" colspan="2">
           Inference methods in ProLinks
          </td>
         </tr>
         <tr valign="top">
          <td class="fbcell"> 
           Rosetta Stone
          </td>
          <td class="fcell"> 
           uses a gene fusion event in asecond organism to infer functional relatedness.
          </td>
         </tr>
         <tr valign="top">
          <td class="fbcell"> 
           Phylogenetic<br/>Profiles
          </td>
          <td class="fcell">
           uses the presence and absence of proteins across multiple  
           genomes to detect functional linkages.
          </td>
         </tr>
         <tr valign="top">
          <td class="fbcell"> 
           Gene Cluster
          </td>
          <td class="fcell"> 
           uses genome proximity to predict functional linkage.
          </td>
         </tr>
         <tr valign="top">
          <td class="fbcell"> 
           Gene Neighbor
          </td>
          <td class="fcell"> 
           uses both gene proximity and phylogenetic distribution to  
           infer linkages.
          </td>
         </tr>
         <tr valign="top">
          <td class="fview" COLSPAN=2>
           <a href="http://genomebiology.com/2004/5/5/R35">
            <i>Check the literature for more details...</i>
           </a>
          </td>
         </tr>
        </table>
       </td>
       <td class="fspacer" width="3%">&nbsp;</td>
       <td class="TDM" style="white-space: nowrap;" width="22%">
        <table cellspacing="0" width="100%">
         <tr>
          <td class="ftitle">ProLinks Contents</td>
         </tr>
         <tr>
          <td class="fview">
           ProLinks contains information on:
           <ul>
            <li> Organisms</li>
            <li> Functional linkages</li>
            <li> Homology</li>    
           </ul>
           <a href="page?skn=prl&id=pl_statistics"><i>See more ProLinks statistics</i></a>
          </td>
         </tr>
        </table>
       </td>
      </tr>
     </table>
    </td>
   </tr>
  </table>
 </td>
</tr>
</table>

