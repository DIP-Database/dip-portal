<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<table width="100%" cellspacing="0" class="dstable" border="1">
 <%-- table header --%>
 <tr>
  <s:iterator value="tabData.colLbl" id="col" status="cpos" >
   <td class="dsheader">
    <s:property value='#col' escape="false"/>  
   </td>
  </s:iterator>
 </tr>
  
 <%-- table contents: grouped --%>    
 <s:iterator value="tabData.group" id="grp" status="gpos" > <!-- groups -->
  <s:iterator value="#grp" id="entry" status="epos" > <!-- groups -->
   <tr>
    <s:iterator value="tabData.colFld" id="cell" status="cpos" >     <%-- columns --%>

     <s:if test="#cpos.count==1">
      <s:if test="#epos.count==1">
       <s:if test="tabData.data[#entry]['dtp']=='' or #gpos.count>1">
        <td class="dsheaderB" rowspan="<s:property value='#grp.size'/>">
       </s:if>
       <s:else>
        <td class="dsheaderA" rowspan="<s:property value='#grp.size'/>">
       </s:else>
       <s:if test="tabData.colFld[#cpos.index]=='sources'">         <%-- dataset column --%>
        <s:if test="tabData.data[#entry]['dtp']=='YT'">
         year total<br/>(<s:property value="tabData.data[#entry][#cell]"/> articles)
        </s:if>
        <s:elseif test="tabData.data[#entry]['dtp']=='CT'"> 
         cumulative<br/>(<s:property value="tabData.data[#entry][#cell]"/> articles)
        </s:elseif>
        <s:else>
         incremental<br/>(<s:property value="tabData.data[#entry][#cell]"/> articles)
        </s:else>
        </s:if>
        <s:elseif test="tabData.colFld[#cpos.index]=='ftp'">     <%-- file type column --%>
         <s:if test="tabData.data[#entry][#cell]=='mif25'">
          <a href="http://www.psidev.info/index.php?q=node/60"><img src="images/mif2.5_53x16.png" border="1"></a>
         </s:if>
         <s:elseif test="tabData.data[#entry][#cell]=='mitab'">
          <a href="http://www.psidev.info/index.php?q=node/60"><img src="images/mitab_53x16.png" border="1"></a>
         </s:elseif>
         <s:else>
          ---
         </s:else> 
        </s:elseif>
        <s:elseif test="tabData.colFld[#cpos.index]=='unc'">   <%-- uncompressed column --%>
         [<A HREF="ftp://">FTP</A>]&nbsp;[<A HREF="file?FN=<s:property value="tabData.data[#entry]['file']"/>.xml">HTTP</A>]
        </s:elseif>
        <s:elseif test="tabData.colFld[#cpos.index]=='cmp'">   <%-- compressed column --%>
         [<A HREF="ftp://">FTP</A>]&nbsp;[<A HREF="file?FN=<s:property value="tabData.data[#entry]['file']"/>.xml.gz">HTTP</A>]
        </s:elseif>
        <s:elseif test="tabData.colFld[#cpos.index]=='file'">          <%-- file column --%>
         <s:property value="tabData.data[#entry]['file']"/>.xml
        </s:elseif>
        <s:else>                                                  <%-- any other column --%>
         <s:property value='tabData.data[#entry][#cell]' escape="false"/>
        </s:else>
       </td>
      </s:if>
     </s:if>
     <s:else>
      <s:if test="tabData.data[#entry]['dtp']=='' or #gpos.count>1">
       <td class="dsheaderB">
      </s:if>
      <s:else>
       <td class="dsheaderA">
      </s:else>
       <s:if test="tabData.colFld[#cpos.index]=='dtp'">                   <%-- dataset column --%>
        <s:if test="tabData.data[#entry][#cell]=='CR'">
         Core data<br/>
        </s:if>
        <s:elseif test="tabData.data[#entry][#cell]=='HT'"> 
         High-throughput data<br/>
        </s:elseif>
        <s:else>
         All data<br/>
        </s:else>
         (<i><s:property value="tabData.data[#entry]['edges']"/> interactions</i>) 
       </s:if>
       <s:elseif test="tabData.colFld[#cpos.index]=='ftp'">             <%-- file type column --%>
        <s:if test="tabData.data[#entry][#cell]=='mif25'">
         <a href="http://www.psidev.info/index.php?q=node/60"><img src="images/mif2.5_53x16.png" border="1"></a>
        </s:if>
        <s:elseif test="tabData.data[#entry][#cell]=='mitab'">
         <a href="http://www.psidev.info/index.php?q=node/60"><img src="images/mitab_53x16.png" border="1"></a>
        </s:elseif>
        <s:else>
         ---
        </s:else> 
       </s:elseif>
       <s:elseif test="tabData.colFld[#cpos.index]=='unc'">                <%-- uncompressed column --%>
         [<A HREF="ftp://">FTP</A>]&nbsp;[<A HREF="file?FN=<s:property value="tabData.data[#entry]['file']"/>.xml">HTTP</A>]
       </s:elseif>
       <s:elseif test="tabData.colFld[#cpos.index]=='cmp'">  <%-- compressed column --%>
         [<A HREF="ftp://">FTP</A>]&nbsp;[<A HREF="file?FN=<s:property value="tabData.data[#entry]['file']"/>.xml.gz">HTTP</A>]
       </s:elseif>
       <s:elseif test="tabData.colFld[#cpos.index]=='file'">                       <%-- file column --%>
         <s:property value="tabData.data[#entry]['file']"/>.xml
       </s:elseif>
       <s:else>            <%-- any other column --%>
        <s:property value='tabData.data[#entry][#cell]' escape="false"/>
       </s:else>
      </td>
     </s:else>
    </s:iterator>
   </tr>
  </s:iterator>
 </s:iterator>        <%-- table contents: END --%> 
 <tr>
  <td class="dsheader" colspan="<s:property value='tabData.colLbl.size'/>">  
   NOTE: To speed up downloads, use .gz version of the files, compressed with <a href="http://www.gzip.org/">gzip</a> program.
  </td>
 </tr>
</table>
