<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script src="js/meta-yui.js" type="text/javascript" language="JavaScript"></script>
<script src="js/modal-yui.js" type="text/javascript" language="JavaScript"></script>
<script src="js/page-yui.js" type="text/javascript" language="JavaScript"></script>
<table cellpadding="0" cellspacing="0" width="100%">
<tbody>
 <tr>
  <td class="pagebody" valign="top" width="100%">
   <table class="maintable" cellspacing="0" cellpadding="0"  width="100%">
    <tbody>
     <tr>
      <td width="22%" align="center">
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
        </tr>
       </table>
      </td>
      <td class="fspacer">&nbsp;</td>
<!--      <td class="fs" width="50%">        -->
      <td class="TDM fqb" style="white-space: nowrap;" width="50%">

      <s:form action="query" theme="simple" method="get">
       <table cellspacing="0" width="100%" class="fsbrh">
        <tr>
         <td class="fqtitle" colspan="3">SEARCH DIP</td>
        </tr>
        <tr>
         <td class="fqbody1" width="1%">
          <s:textfield theme="simple" name="query" size="32" maxlength="128"/>
         </td>
         <td class="fqbody1" align="left" width="1%">
           <s:hidden name="qt" value="dip-txt-query" />
           <s:hidden name="dl" value="full" />
           <s:hidden name="mst" value="1:1:1" />
<!--           <s:hidden name="md" value="TQ" /> -->
           <s:submit type="input" tabindex="3" name="Search" value="Search DIP" theme="simple"/>
          </td>
          <td class="fqbody1" align="left" width="98%">
           <a href="page?id=help">Help</a>; <a href="page?id=tutorial">Tutorial</a>
         </td>
        </tr>
        <tr>
         <td colspan="3" class="fqbody2" align="left" valign="top">
           Enter names, keywords, or identifiers associated with a protein, gene,
           interaction, or publication.
           (See all <a href="page?id=search">identifier types</a> recognized by DIP.)
         </td>
        </tr>
        <tr>
          <td class="fqfoot" align="right" valign="bottom" colspan="3">
           <i>For more search options, try <a href="page?id=search-record">DIP Search</a></i>
          </td>
         </tr>
       </table>
      </s:form>
      </td>
      
      <td class="fspacer">&nbsp;</td>
      <td class="TDM" style="white-space: nowrap;" width="22%">
        <table cellspacing="0" width="100%" class="fsbrh">
         <tr>
          <td class="fttitle">DIP News</td>
         </tr>
         <tr>
          <td class="ftbody" valign="top">
             Recent DIP news:

            <t:insertDefinition name="newsbox">
              <t:putAttribute name="feed" value="dipnews"/>
            </t:insertDefinition>
          </td>
         </tr>
         <tr>
          <td class="ftfoot" align="right" valign="bottom">
             <i>See more <a href="dipnews">DIP News</a></i>
          </td>
         </tr>
        </table>

      </td>
     </tr>

     <tr><td class="fspacer" colspan="5"></td></tr>

     <tr>
      <td class="TDM" style="white-space: nowrap;" width="22%">
        <table cellspacing="0" width="100%" class="fsbrh">
         <tr>
          <td class="fbtitle">Search Help</td>
         </tr>
         <tr>
          <td class="fbbody" valign="top">
             Example DIP searches:
                <ul>
                 <li>Protein name: 
                  <a href="query?query=MVP&amp;dl=full&amp;sl=0&amp;mst=1:1:2">MVP</a></li>
                 <li>Gene name: 
                  <a href="query?query=SUP35&amp;dl=full&amp;sl=0&amp;mst=1:1:2">SUP35</a></li>
                 <li>Keywords: 
                  <a href="query?query=yeast%20actin&amp;dl=full&amp;sl=0&amp;mst=1:1:2">yeast actin</a></li>
                 <li>UniProt ID: 
                  <a href="query?query=P52960&amp;dl=full&amp;sl=0&amp;mst=1:1:2">P52960</a></li>
                 <li>PubMed ID: 
                  <a href="query?query=8676499&amp;dl=full&amp;sl=3&amp;mst=1:1:2">8676499</a></li>
                 <li>PDB ID: 
                  <a href="query?query=1PQS&amp;dl=full&amp;tp=0&amp;mst=1:1:2">1PQS</a></li>
                </ul>
          </td>
         </tr>
         <tr>      
          <td class="fbfoot" align="right" valign="bottom">
             <br/> 
             <br/> 
             <br/> 
             <i>More help with <a href="page?id=help">DIP Searches</a></i>
          </td>
         </tr>
        </table>
       </td>

      <td class="fspacer">&nbsp;</td>
      <td class="TDM" width="50%" id="motd">

       <script type="text/javascript">
        YAHOO.util.Event.addListener( window, "load",
        YAHOO.mbi.page.loadBody({name:"motd", tgid:"motd"}));
      </script>

<!--
       <table border="0" cellpadding="0" cellspacing="0" width="100%" class="fsbrh">
        <tr>
         <td class="fbtitle">
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
       </table>

-->

      </td>
      <td class="fspacer">&nbsp;</td>
      <td class="TDM" style="white-space: nowrap;" width="22%">
        <table cellspacing="0" width="100%" class="fsbrh">
         <tr>
          <td class="fbtitle">DIP Contents</td>
         </tr>
         <tr>
          <td class="fbbody" valign="top">
            <t:insertDefinition name="countbox"/>
          </td>
         </tr>
         <tr>
          <td class="fbfoot" align="right" valign="bottom">
           <br/>
           <br/>
           <br/>
           <i>See more <a href="contents">DIP Statistics</a></i>
          </td>
         </tr>
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
