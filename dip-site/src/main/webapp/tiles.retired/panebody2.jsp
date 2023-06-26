<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="pane[0].type.ac=='dxf:0004'">  
   <table width="100%" cellpadding="0" cellspacing="0" class="results_int">
     <tr>
      <th class="results_int" colspan="3">INTERACTION</th>
      <th class="results" colspan="9">INTERACTOR(S)</th>
     </tr>
     <tr>
      <th class="results_int">Type</th>

      <th class="results_core">CORE</th>
      <th class="results_int">Details</th>
      <th class="results_mol">#</th>
      <th class="results_mol">Name</th>
      <th class="results_mol">Class</th>
      <th class="results_mol">Description</th>

      <th class="results_mol">Organism</th>
      <th class="results_mol">Interactions (CORE)</th>
      <th class="results_mol">Details</th>
      <th class="results_mol">UniProt ID</th>
      <th class="results_mol">RefSeq ID</th>
     </tr>

<!--pane rows -->

 <s:iterator value="pane" id="cr" status="stat">
       <tr>
        <td class="results">physical</td>
        <td class="results_core">yes</td>
        <td class="results"><a href="Guide.cgi?SM=0:19"><s:property value="#cr.ac"/></a></td>
        <td class="results">1</td>
        <td class="results"><a href=""><s:property value="#cr.label"/></a></td>

        <td class="results">protein</td>
        <td class="results">protein name</td>
        <td class="results">yeast (<i>S.c.</i>)</td>
        <td class="results"># (#)</td>
        <td class="results"><a href="">DIP-2523N</a></td>

        <td class="results"><a href="http://beta.uniprot.org/uniprot/P47110">P47110</a></td>
        <td class="results"><a href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?val=NP_012577">NP_012577</a></td>
       </tr>
 </s:iterator>
</table>
</s:if>
<s:elseif test="pane[0].type.ac=='dxf:0003'">
  <u>Protein table</u>
</s:elseif>
<s:elseif test="pane[0].type.ac=='dxf:0015'">
  <u>Evidence table</u>
</s:elseif>
<s:else>
  <u>Unknown table</u>
</s:else>


