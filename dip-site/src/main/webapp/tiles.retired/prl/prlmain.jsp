<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<table cellpadding="0" cellspacing="0" width="100%">
  <tbody>
            <tr>
              <td class="pagebody" valign="top" width="100%">

                <table class="maintable" cellpadding="0" cellspacing="0" width="100%">
                  <tbody>
                    <tr>
   <td align="center">
    <table border="0" cellpadding="0" cellspacing="2" width="100%">
     <tbody>
      <tr>
       <td valign="top">
        <table cellpadding="0" cellspacing="0">
         <tbody>
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
         </tbody>
        </table>
       </td>
                              <td class="fspacer" width="3%">&nbsp;</td>

                              <td class="fs" width="90%">
                                <table border="0" cellpadding="0" cellspacing="0">
                                  <tbody>
                                    <TR>
                                      <TD CLASS="fstitle" COLSPAN="3" ALIGN="left">
                                        SEARCH ProLinks
                                      </TD>
                                    </TR>

                                    <TR>
                                      <FORM ID="query" NAME="query" ACTION="" METHOD="post">

                <TD ALIGN="RIGHT" CLASS="fsbody" WIDTH="1%">
                                        <s:select theme="simple" name="dl" value="'full'" 
                                                  list="#{'refseq':'NCBI RefSeq', 
                                                          'gi':'GI Number', 
                                                          'uniprot':'UniProt Accession'}"/>
                </TD>

                  
                <TD ALIGN="LEFT" class="fsbody" WIDTH="1%">
                 <s:textfield theme="simple" name="query" size="32" size="32" maxlength="128"/>
                </TD>                                      

                <td class="fsbody" align="left">
                 <s:hidden name="debugOn" value="true" />
                 <s:hidden name="bigOn" value="true" />
                 <s:hidden name="dl" value="full" />
                 <s:submit type="input" tabindex="3" name="Search" value="Search ProLinks" 
                    theme="simple"/>&nbsp;<A HREF="page?skn=prl&id=pl_help">Help</A>; <A HREF="page?skn=prl&id=pl_tutorial">Tutorial</A>
               </td>
             </FORM>
           </TR>

                                    <tr>
                                      <td colspan="3" class="fsbody" align="left">
                                        Enter a public database identifier associated with a protein. 
                                      </td>

                                    </tr>
                                    <tr>

                                  </tbody>
                                </table>
                              </td>
                            </tr>

                          </tbody>
                        </table>

                        <br><br>
                        <table cellpadding="0" cellspacing="2" width="100%">
                          <tbody>
                            <tr>
                              <td class="TDM" style="white-space: nowrap;" width="22%">
                                <table cellspacing="0" width="100%">
                                  <tbody>

                                    <tr>

                                      <td class="ftitle">Search Help</td>
                                    </tr>
                                    <tr>
                                      <td class="fview">
                                        Other queries you can submit to ProLinks:
                                        <ul>
                                          <li><a href="page?skn=prl&id=pl_prot_attr">Get links by submitting a protein name</a></li>

                                          <li><a href="page?skn=prl&id=pl_mult_prot">Get links for multiple protein IDs</a></li>

                                          <li><a href="page?skn=prl&id=pl_common">Find links shared between proteins</a></li>
                                          <li><a href="page?skn=prl&id=pl_connections">Find links connecting two proteins</a>
                                          </li>
                                        </ul>
                                        <a href="page?skn=prl&id=pl_query"><i>More help with searching</i></a>
                                      </td>

                                    </tr>
                                  </tbody>
                                </table>
                              </td>
                              <td class="fspacer" width="3%">&nbsp;</td>

                              <td class="TDM" width="50%">
                                <table border="0" cellpadding="3" cellspacing="0" width="100%">
                                  <tbody>

                                    <tr>
                                      <TD CLASS="ftitle" COLSPAN="2">
                                        Inference methods in ProLinks
                                      </td>
                                    </tr>

                                    <TR VALIGN="TOP">
                                      <TD class="fbcell"> 
                                        Rosetta Stone
                                      </TD>
                                      <TD class="fcell"> 
                                        uses a gene fusion event in asecond organism to infer 
                                        functional relatedness.
                                      </TD>
                                    </TR>

                                    <TR VALIGN="TOP">
                                      <TD class="fbcell"> 
                                        Phylogenetic<br/>Profiles
                                      </TD>

                                      <TD class="fcell">
                                        uses the presence and absence
                                        of proteins across multiple  
                                        genomes to detect functional 
                                        linkages.
                                      </TD>
                                    </TR>
                                      
                                    <TR VALIGN="TOP">
                                      <TD class="fbcell"> 
                                        Gene Cluster
                                      </TD>
                                      <TD class="fcell"> 
                                        uses genome proximity to predict functional linkage.
                                      </TD>
                                    </TR>

                                    <TR VALIGN="TOP">
                                      <TD class="fbcell"> 
                                        Gene Neighbor
                                      </TD>
                                      <TD class="fcell"> 
                                        uses both gene proximity and phylogenetic distribution to  
                                        infer linkages.
                                      </TD>
                                    </TR>


                                    <TR VALIGN="TOP">
                                      <td class="fview" COLSPAN=2>
                                        <A HREF="http://genomebiology.com/2004/5/5/R35">
                                          <i>Check the literature for more details...</i>
                                        </A>
                                      </td>
                                    </TR>

                                  </tbody>
                                </table>

                              </td>
                              <td class="fspacer" width="3%">&nbsp;</td>
                              <td class="TDM" style="white-space: nowrap;" width="22%">
                                <table cellspacing="0" width="100%">
                                  <tbody>
                                    <tr>

                                      <td class="ftitle">ProLinks Contents</td>
                                    </tr>

                                    <tr>
                                      <td class="fview">
                                        ProLinks contains information on:
                                        <ul>
                                          <li> Organisms</li>

                                          <li> functional linkages</li>
                                          <li> Homology</li>                        
                                        </ul>
                                        <br/><br/><br/><br/><br/><br/>  
                                        <a href="page?skn=prl&id=pl_statistics">
                                          <i>See more ProLinks statistics</i></a>
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

              </td>
            </tr>
          </tbody>
</table>
<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>

