<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!-- ARTICLE TABLE -->
   <table width="100%" cellpadding="0" cellspacing="0" class="results">
      <tr>
         <th class="results_source">First&nbsp;Author, Year</th>
         <th class="results_source">Title</th>
         <th class="results_source">Authors</th>
         <th class="results_source">Journal</th>
         <th class="results_source">Year</th>
         <th class="results_source">DIP Details</th>
         <th class="results_source">PubMed</th>
      </tr>

 <s:iterator value="pane" id="article" status="stat">
 <!-- individual panes (experiments) -->

   <s:url id="dipartUrl" includeParams="none" action='record'>
     <s:param name='ac' value="#article.ac"/>
     <s:param name='detail' value="detail"/>
     <s:param name='tp'>0</s:param>
     <s:param name='bigOn' value="bigOn"/>
     <s:param name='debugOn' value="debugOn"/>
   </s:url>

      <tr>
         <td class="results"><s:a href="%{dipartUrl}"><s:property value="#article.name"/></s:a></td>  <!-- first author, year -->
         <td class="results"><s:property value="#article.attrList.attr.{?#this.ac=='dip:0004'}[0].value.value"/></td>  <!-- title -->
         <td class="results"><s:property value="#article.attrList.attr.{?#this.ac=='dip:0010'}[0].value.value"/></td>  <!-- authors -->
         <td class="results"><i><s:property value="#article.attrList.attr.{?#this.ac=='dip:0009'}[0].value.value"/></i></td>  <!-- journal -->
         <td class="results"><s:property value="#article.attrList.attr.{?#this.ac=='dip:0013'}[0].value.value"/></td>  <!-- year -->
         <td class="results"><s:a href="%{dipartUrl}"><s:property value="#article.label"/></s:a></td>  <!-- DIP ID -->
         <td class="results">
      <s:iterator value="#article.xrefList.xref.{?#this.typeAc=='dxf:0009'}" id="ext" status="status">  <!-- PubMed ID -->
         <s:if test="#status.last">
            <t:insertDefinition name="externallink"/>
         </s:if>
         <s:else>
            <t:insertDefinition name="externallink"/>,
         </s:else>
      </s:iterator>
         </td>
      </tr>
 </s:iterator>
     </table>
