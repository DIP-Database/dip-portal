<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
      
<table width="100%" cellspacing="0" class="dstable" border="1">
 <%-- table header --%>
 <tr>
  <s:iterator value="tabData.colLbl" var="col" status="cpos" >
   <td class="dsheader">
    <s:property value='#col' escape="false"/>  
   </td>
  </s:iterator>
 </tr>
  
 <%-- table contents: grouped --%>    
 <s:iterator value="tabData.group" var="grp" status="gpos" > <!-- groups -->
  <s:iterator value="#grp" id="entry" status="epos" > <!-- groups -->
   <tr>
    <s:iterator value="tabData.colFld" var="cell" status="cpos" >     <%-- columns --%>

     <s:if test="#cpos.count==1">
      <s:if test="#epos.count==1">
       <s:if test="#gpos.isEven()">
        <td class="dsheaderA" rowspan="<s:property value='#grp.size'/>">
       </s:if>
       <s:else>
        <td class="dsheaderB" rowspan="<s:property value='#grp.size'/>">
       </s:else>
       <s:if test="tabData.colFld[#cpos.index]=='dtp'">         <%-- dataset column --%>
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
        <s:elseif test="tabData.colFld[#cpos.index]=='ftp'">     <%-- file type column --%>
         <s:if test="tabData.data[#entry][#cell]=='mif25'">
          <a href="http://www.psidev.info/index.php?q=node/60"><img src="images/mif2.5_53x16.png" border="1"></a>
         </s:if>
         <s:elseif test="tabData.data[#entry][#cell]=='mitab'">
          <a href="http://www.psidev.info/index.php?q=node/60"><img src="images/mitab_53x16.png" border="1"></a>
         </s:elseif>
         <s:elseif test="tabData.data[#entry][#cell]=='fasta'">
          <a href="https://proteomecommons.org/tranche/examples/proteomecommons-fasta/fasta.jsp">FASTA</a>
         </s:elseif>
         <s:else>
          ---
         </s:else> 
        </s:elseif>
        <s:elseif test="tabData.colFld[#cpos.index]=='unc'">   <%-- uncompressed column --%>
         [<A HREF="ftp://">FTP</A>]&nbsp;[<A HREF="file?ds=<s:property value="tabData.id"/>&fn=<s:property value="tabData.data[#entry]['file']"/>&ff=<s:property value="tabData.data[#entry]['ext']"/>">HTTP</A>]
        </s:elseif>
        <s:elseif test="tabData.colFld[#cpos.index]=='cmp'">   <%-- compressed column --%>
         [<A HREF="ftp://">FTP</A>]&nbsp;[<A HREF="file?ds=<s:property value="tabData.id"/>&&fn=<s:property value="tabData.data[#entry]['file']"/>&&ff=<s:property value="tabData.data[#entry]['ext']"/>&fc=gz">HTTP</A>]
        </s:elseif>
        <s:elseif test="tabData.colFld[#cpos.index]=='file'">          <%-- file column --%>
         <s:property value="tabData.data[#entry]['file']"/>.<s:property value="tabData.data[#entry]['ext']"/>
        </s:elseif>
        <s:elseif test="tabData.colFld[#cpos.index]=='seqcnt'">        <%-- sequence countcolumn --%>
         <s:property value="tabData.data[#entry]['seqs']"/>
        </s:elseif>
        <s:else>                                                       <%-- any other column --%>
         <s:property value='tabData.data[#entry][#cell]' escape="false"/>
        </s:else>
       </td>
      </s:if>
     </s:if>
     <s:else>
      <s:if test="#gpos.isEven()">
       <td class="dsheaderA">
      </s:if>
      <s:else>
       <td class="dsheaderB">
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
         <s:elseif test="tabData.data[#entry][#cell]=='fasta'">
          <a href="https://proteomecommons.org/tranche/examples/proteomecommons-fasta/fasta.jsp">FASTA</a>
         </s:elseif>
        <s:else>
         ---
        </s:else> 
       </s:elseif>
       <s:elseif test="tabData.colFld[#cpos.index]=='unc'">                <%-- uncompressed column --%>
         <%--[<A HREF="ftp://">FTP</A>]&nbsp;--%>[<A HREF="file?ds=<s:property value="tabData.id"/>&fn=<s:property value="tabData.data[#entry]['file']"/>&ff=<s:property value="tabData.data[#entry]['ext']"/>">HTTP</A>]
       </s:elseif>
       <s:elseif test="tabData.colFld[#cpos.index]=='cmp'">  <%-- compressed column --%>
         <%--[<A HREF="ftp://">FTP</A>]&nbsp;--%>[<A HREF="file?ds=<s:property value="tabData.id"/>&fn=<s:property value="tabData.data[#entry]['file']"/>&ff=<s:property value="tabData.data[#entry]['ext']"/>&fc=gz">HTTP</A>]
       </s:elseif>
       <s:elseif test="tabData.colFld[#cpos.index]=='file'">                       <%-- file column --%>
         <s:property value="tabData.data[#entry]['file']"/>.<s:property value="tabData.data[#entry]['ext']"/>
       </s:elseif>
        <s:elseif test="tabData.colFld[#cpos.index]=='seqcnt'">        <%-- sequence countcolumn --%>
         <s:property value="tabData.data[#entry]['seqs']"/>
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
