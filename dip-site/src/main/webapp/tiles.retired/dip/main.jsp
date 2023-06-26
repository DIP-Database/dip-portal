<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script src="js/meta-yui.js" type="text/javascript" language="JavaScript"></script>
<table cellpadding="0" cellspacing="0" width="100%">
<tbody>
 <tr>
  <td class="pagebody" valign="top" width="100%">
   <table class="maintable" cellpadding="0" cellspacing="0" width="100%">
   <tbody>
    <tr>
     <td align="center">
      <table border="0" cellpadding="0" cellspacing="2" width="100%">
       <tr>
        <td valign="top">
         <table cellpadding="0" cellspacing="0">
          <tr>
           <td class="flinks">
            <a href="page?id=submit">                     
             <img class="main" src="images/main_submit_40.png" alt="submit" border="0">
            </a>
           </td>
           <td class="flinks">
            Contribute to DIP!<br>
            <a href="page?id=submit">
             <b>Submit your interactions</b>
            </a>
           </td>
          </tr>
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
            <a href="page?id=download">                     
             <img class="main" src="images/main_download_40.png" alt="download" border="0">
            </a>
           </td>
           <td class="flinks">
            Get all interactions in DIP<br>
            <a href="page?id=download"> 
             <b>Download DIP data</b>
            </a>
           </td>
          </tr>
         </table>
        </td>
        <td class="fspacer" width="3%">&nbsp;</td>
        <td class="fs" width="90%">
         <s:form action="query" theme="simple">
          <table border="0" cellpadding="0" cellspacing="0">
           <tr>
            <td class="fstitle" colspan="3" align="left">SEARCH DIP</td>
           </tr>
           <tr>
            <td class="fsbody" width="1%">        
             <s:textfield theme="simple" name="query" size="32" size="32" maxlength="128"/>
            </td>
            <td class="fsbody" align="left">
             <s:hidden name="mdf" value="0:0:0" />
             <s:hidden name="mst" value="1:1:2" />
             <s:hidden name="md" value="TQ" />
             <s:hidden name="dl" value="full" />
             <s:submit type="input" tabindex="3" name="Search" value="Search DIP" theme="simple"/>
            </td>
            <td class="fsbody" align="left" width="100%">
             <a href="page?id=help">Help</a>; <a href="page?id=tutorial">Tutorial</a>
            </td>
           </tr>
           <tr>
            <td colspan="3" class="fsbody" align="left">
             Enter names, keywords, or identifiers associated with a protein, gene,
             interaction, or publication. 
              (<a href="page?id=search">
               See all identifier types recognized by DIP.
              </a>)
            </td>
           </tr>
           <tr>
            <td colspan="3" class="fsbody" align="left">
             <a href="page?id=search">For structured searching, try Advanced Search</a>
            </td>
           </tr>
          </table>
        </s:form> 
       </td>
       </tr>
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
           Example DIP searches:
           <ul>
            <li>Protein name: 
               <a href="query?query=MVP&amp;dl=full&amp;sl=0">MVP</a></li>
            <li>Gene name: 
               <a href="query?query=SUP35&amp;dl=full&amp;sl=0">SUP35</a></li>
            <li>Keywords: 
               <a href="query?query=yeast%20actin&amp;dl=full&amp;sl=0">yeast actin</a></li>
            <li>UniProt ID: 
               <a href="query?query=P52960&amp;dl=full&amp;sl=0">P52960</a></li>
            <li>PubMed ID: 
               <a href="query?query=8676499&amp;dl=full&amp;sl=3">8676499</a></li>
            <li>PDB ID: 
               <a href="query?query=1PQS&amp;dl=full&amp;tp=0">1PQS</a></li>
           </ul>
           <a href="page?id=help"><i>More help with searching</i></a>
          </td>
         </tr>
        </tbody>
        </table>
       </td>
       <td class="fspacer" width="3%">&nbsp;</td>
       <td class="TDM" width="50%">
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tbody>
         <tr>
          <td class="ftitle">
           Featured Interaction: Dscam dimer
          </td>
         </tr>
         <tr>
          <td class="fview">
           <img src="images/dscam_200.png" alt="dscam" style="float: left;">
           Dscam is an S-shaped neuronal surface protein whose dimeric interaction is important
           for cell-cell recognition. <a href="page?id=featured"><i>Read&nbsp;more...</i></a>
          </td>
         </tr>
        </tbody>
        </table>
       </td>
      <td class="fspacer" width="3%">&nbsp;</td>
      <td class="TDM" style="white-space: nowrap;" width="22%">
       <table cellspacing="0" width="100%">
       <tbody>
        <tr>
         <td class="ftitle">DIP Contents</td>
        </tr>
        <tr>
         <td class="fview">
          DIP contains information on:
          <ul>
           <li id="prt-count">(N/A) proteins</li>
           <li id="int-count">(N/A) interactions</li>
           <li id="art-count">(N/A) articles</li>
           <li id="org-count">(N/A) organisms</li>
          </ul>
          <a href="page?id=datastat">
          <i>See more DIP statistics</i></a>
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
<br/><br/><br/><br/><br/>
<script type="text/javascript">
  YAHOO.util.Event.addListener( window, "load",
  YAHOO.mbi.meta.counts.load());
</script>
