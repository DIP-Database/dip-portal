<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%--
<SCRIPT TYPE="text/javascript" SRC="js/jqdownload.js" LANGUAGE="JavaScript">
</SCRIPT>
--%>

<!-- <table class="pagebody" width="100%" cellpadding="0" cellspacing="0"> -->
<tr>
 <td class="pagettl">
  <h1><s:property value='tabData.header' /></h1>
 </td>
</tr>

<tr>
 <td>
  <table width="98%">
   <tr>
    <td align="left">  
      <s:property value='tabData.comment' escape="false"/>
      <br/><br/>
    </td>
   </tr>
   <tr>
    <td align="left">

     <%-- tabs: level 0 --%>

     <table width="99%" align="center" cellspacing="0">
      <tr>
       <s:iterator value="tabData.tab0" id="tab" status="mpos0" > 
         
        <s:if test="tabSelect[0] == #mpos0.index">
         <td width="1%" nowrap class="t0tabon">
        </s:if> 
        <s:else>
         <td width="1%" nowrap class="t0tab">
        </s:else>
         <a class="tlnkon" 
            href="download?mst=<s:property value="mst"/>&tbs=<s:property value='#mpos0.index'/>:<s:property value='tabSelect[1]'/>">
          <s:property value='tabData.tab0lbl[#tab]' />
         </a>
        </td>
       </s:iterator>
       <td width="99%" class="t0tab">&nbsp;</td>
      </tr>
       
      <%-- tabs: top level 1 --%>

     <tr>
       <s:if test="tabData.tab1.size > 0">
     
        <td colspan="<s:property value='tabData.tab0.size+1'/>" class="t0cont">
         <table width="99%" align="center" cellspacing="0">
          <tr>
           <s:iterator value="tabData.tab1" id="tab" status="mpos1" >
            <s:if test="tabSelect[1] == #mpos1.index">
             <td width="1%" nowrap class="t0tabon">
            </s:if>
            <s:else>
             <td width="1%" nowrap class="t0tab">
            </s:else>
            <a class="tlnkon" 
               href="download?mst=<s:property value="mst"/>&tbs=<s:property value='tabSelect[0]'/>:<s:property value='#mpos1.index'/>">
             <s:property value='tabData.tab1lbl[#tab]' />
            </a>
           </td>
           </s:iterator>
           <td width="99%" class="t0tab">&nbsp;</td>
          </tr>
          <tr>
         <td colspan="<s:property value='tabData.tab1.size+1'/>" class="t0cont">
       </s:if>
       <s:else>
        <td colspan="<s:property value='tabData.tab0.size+1'/>" class="t0cont">
       </s:else>

        <s:if test="tabData.tableDef=='mifTable'">
         <t:insertDefinition name="miftable" />
        </s:if>
        <s:elseif test="tabData.tableDef=='imexTable'">
         <t:insertDefinition name="imextable"/>
        </s:elseif>
        <s:elseif test="tabData.tableDef=='seqTable'">
         <t:insertDefinition name="seqtable" />
        </s:elseif>
      
        </td>
        <s:if test="tabData.tab1.size > 0" >
           </tr> 
          </table>
         </td> 
        </s:if> 
       </tr>
     </table>
     <br/><br/><%--<s:property value="tabData"/>--%><br/>
    </td>
   </tr>
  </table>
 </td>
</tr>

