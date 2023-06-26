<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<link rel="stylesheet" type="text/css" media="screen" href = "css/tabs-no-images.css"/>

<table cellspacing="0" cellpadding="0" width="100%">
 <tr>
  <td>
   <table width="100%" cellspacing="0" cellpadding="0">
    <tr>
     <td class="yui-skin-sam">
      <div id="statview" class="stat-container"> 
       <ul class="yui-nav"> 
        <li class="selected"><a href="#summary"><em>Summary</em></a></li> 
        <li><a href="#species"><em>Species</em></a></li>  
       </ul>             
       <div class="yui-content" id="stat-content"> 
        <div id="summary" class="stat-tab">
         <table class="stat-sum-tbl" width="100%" border="0" cellspacing="0">
          <tr>
           <th width="75%" rowspan="2" align="left" class="stat-sum-header"><h2>&nbsp;&nbsp;DIP Contents</h2></th>
           <th width="5%" rowspan="2" class="stat-sum-header">All</th>
           <th width="20%" colspan="4" class="stat-sum-header">
            <a href="javascript:cop('http://imex.sourceforge.net')">IMEx Records</a>
           </th>
          </tr>
          <tr>
           <th width="10%" colspan="2" class="stat-sum-header">DIP</th>
           <th width="10%" colspan="2" class="stat-sum-header">Others</th>
          </tr> 
          <tr>
           <td valign="bottom" align="left" class="stat-sum-row">Number of proteins</td>
           <td valign="bottom" align="center" class="stat-sum-row" width="5%" id="prt-count">N/A</td>
           <td valign="bottom" align="right" width="5%">&nbsp;</td>
           <td valign="bottom" align="left" class="stat-sum-row" width="5%">&nbsp;</td>
           <td valign="bottom" align="right" width="5%">&nbsp;</td>
           <td valign="bottom" align="left" class="stat-sum-row" width="5%">&nbsp;<sup>&nbsp;</sup></td>
          </tr>
          <tr>
           <td valign="bottom" align="left" class="stat-sum-row">Number of organisms</td>
           <td valign="bottom" align="center" class="stat-sum-row" id="org-count">0</td>
           <td valign="bottom" align="right" width="5%">&nbsp;</td>
           <td valign="bottom" align="left" class="stat-sum-row" width="5%">&nbsp;</td>
           <td valign="bottom" align="right" width="5%">&nbsp;</td>
           <td valign="bottom" align="left" class="stat-sum-row" width="5%">&nbsp;<sup>&nbsp;</sup></td>
          </tr>
          <tr>
           <td valign="bottom" align="left" class="stat-sum-bottom stat-sum-row">Number of interactions</td>
           <td valign="bottom" align="center" class="stat-sum-bottom stat-sum-row" id="int-count">0</td> 
           <td valign="bottom" align="right" class="stat-sum-brow" width="5%">&nbsp;</td>
           <td valign="bottom" align="left"  class="stat-sum-bottom stat-sum-row" width="5%">&nbsp;</td>
           <td valign="bottom" align="right" class="stat-sum-brow" width="5%">&nbsp;</td>
           <td valign="bottom" align="left" class="stat-sum-bottom stat-sum-row" width="5%">&nbsp;<sup>&nbsp;</sup></td>
          </tr>
          <tr>
           <td valign="bottom" align="left" class="stat-sum-row">Number of distinct experiments describing an interaction</td>
           <td valign="bottom" align="center" class="stat-sum-row" id="evd-count">0</td> 
           <td valign="bottom" align="right" class="stat-sum-crow" width="5%" id="evd-dip-count">0</td>
           <td valign="bottom" align="left" class="stat-sum-row" width="5%" id="evd-dip-beta-count">(0<sup>*</sup>)</td>
           <td valign="bottom" align="right" class="stat-sum-crow" width="5%" id="evd-other-count">0</td>
           <td valign="bottom" align="left" class="stat-sum-row" width="5%"id="evd-other-beta-count">(0<sup>*</sup>)</td>
          </tr>
          <tr>
           <td valign="bottom" align="left" class="stat-sum-row">Number of authors' inferences</td>
           <td valign="bottom" align="center" class="stat-sum-row" id="auth-inf-count">0</td> 
           <td valign="bottom" align="right" width="5%">&nbsp;</td>
           <td valign="bottom" align="left" class="stat-sum-row" width="5%">&nbsp;</td>
           <td valign="bottom" align="right" width="5%">&nbsp;</td>
           <td valign="bottom" align="left" class="stat-sum-row" width="5%">&nbsp;<sup>&nbsp;</sup></td>
          </tr>
          <tr>
           <td valign="bottom" align="left" class="stat-sum-row stat-sum-bottom">Number of automated inferences</td>
           <td valign="bottom" align="center" class="stat-sum-bottom stat-sum-row" id="auto-inf-count">0</td> 
           <td valign="bottom" align="right" class="stat-sum-brow" width="5%">&nbsp;</td>
           <td valign="bottom" align="left" class="stat-sum-bottom stat-sum-row" width="5%">&nbsp;</td>
           <td valign="bottom" align="right" class="stat-sum-brow" width="5%">&nbsp;</td>
           <td valign="bottom" align="left" class="stat-sum-row stat-sum-bottom" width="5%">&nbsp;<sup>&nbsp;</sup></td>
          </tr>
          <tr>
           <td valign="bottom" align="left" class="stat-sum-row stat-sum-bottom">Number of data sources (articles)</td>
           <td valign="bottom" align="center" class="stat-sum-row stat-sum-bottom" id="art-count">0</td>
           <td valign="bottom" align="right" class="stat-sum-brow" width="5%" id="art-dip-count">0</td>
           <td valign="bottom" align="left" class="stat-sum-row stat-sum-bottom" width="5%" id="art-dip-beta-count">(0<sup>*</sup>)</td>
           <td valign="bottom" align="right" class="stat-sum-brow " width="5%" id="art-other-count">0</td>
           <td valign="bottom" align="left" class="stat-sum-row stat-sum-bottom" width="5%" id="art-other-beta-count">(0<sup>*</sup>)</td>
          </tr>
          <tr>
           <td colspan="1" class="stat-sum-header">&nbsp;</td>
           <td colspan="1" class="stat-sum-header">&nbsp;</td>
           <td colspan="4" valign="bottom" align="center" class="stat-sum-header">
            <sup>*</sup>provisional IMEx records 
            [<b><a href='' onClick='YAHOO.mbi.modal.help("Record Types","guide-rectype"); return false;'>?</a></b>]
           </td> 
          </tr>
         </table>
        </div> 
        <div id="species" class="stat-tab">
         <table border="0" width="100%" cellpadding="0" cellspacing="0" id="spc-count-table" class="stat-spc-tbl">
          <tr bgcolor="#A0C0F5">
           <th class="stat-spc-header" cellspan="2">ORGANISM</th>
           <th class="stat-spc-header">PROTEINS</th>
           <th class="stat-spc-header">INTERACTIONS</th>    
           <th class="stat-spc-header">EXPERIMENTS</th>
           <th class="stat-spc-header">Details</th>
          </tr>
         </table> 
        </div> 
       </div> 
      </div>
     </td>
    </tr>  
   </table>
  </td>
 </tr>
</table>
<script src="js/modal-yui.js" type="text/javascript" language="JavaScript"></script>
<script src="js/meta-yui.js" type="text/javascript" language="JavaScript"></script>
<script type="text/javascript">
  var tabView = new YAHOO.widget.TabView('statview');
  
  YAHOO.util.Event.addListener( window, "load",
  YAHOO.mbi.meta.counts.loadDetail());

  YAHOO.util.Event.addListener( window, "load",
  YAHOO.mbi.meta.counts.loadSpecies( 8 ));
</script>
