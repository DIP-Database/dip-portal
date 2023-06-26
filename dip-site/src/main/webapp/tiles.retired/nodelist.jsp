<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!-- PROTEIN TABLE -->
   <table width="100%" class="results" cellpadding="0" cellspacing="0" border="0">
    <tr>
     <th class="results_mol">Name</th>
     <th class="results_mol">Description</th>
     <th class="results_mol">Organism</th>
     <th class="results_mol">Interactions (CORE)</th>
     <th class="results_mol">DIP Details</th>
     <th class="results_mol">UniProt ID</th>
     <th class="results_mol">RefSeq ID</th>
     <th class="results_mol">EntrezGene ID</th>
    </tr>

   <s:iterator value="pane" id="mol" status="stat">
   <!-- individual proteins -->
      <s:url id="dipnodeUrl" includeParams="none" action='record'>
        <s:param name='ac' value="#mol.ac"/>
        <s:param name='detail' value="detail"/>
        <s:param name='tp'>0</s:param>
        <s:param name='bigOn' value="bigOn"/>
        <s:param name='debugOn' value="debugOn"/>
      </s:url>
     <tr>

    <!-- name -->
      <s:if test="#mol.label!=''">
        <td class="results"><s:a href="%{dipnodeUrl}"><s:property value="#mol.label"/></s:a></td>
      </s:if>
      <s:else>
        <td class="results">-</td>
      </s:else>

    <!-- description -->
      <s:if test="#mol.name!=''">
        <td class="results"><s:property value="#mol.name"/></td>
      </s:if>
      <s:else>
        <td class="results">-</td>
      </s:else>

    <!-- organism -->
      <s:set name="orgname" value="#mol.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.name"/>
      <s:set name="species" value="#mol.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.label"/>
      <s:if test="#orgname!=''">
        <td class="results"><s:property value="#orgname"/></td> <!-- common name -->
      </s:if>
      <s:elseif test="#species!=''">
        <td class="results"><i><s:property value="#species"/></i></td> <!-- species -->
      </s:elseif>
      <s:else>
        <td class="results">-</td>
      </s:else>

    <!-- interactions -->
        <td class="results"># (#)</td>

    <!-- DIP ID -->
        <td class="results"><s:a href="%{dipnodeUrl}"><s:property value="#mol.ac"/></s:a></td>

    <!-- UniProt ID -->
      <s:set name="uniprot" value="#mol.xrefList.xref.{?#this.ns=='UniProt'}"/>
      <s:if test="#uniprot.size>0">
        <td class="results">
        <s:iterator value="#uniprot" id="ext" status="status">
           <s:if test="#status.last">
              <t:insertDefinition name="externallink"/>
           </s:if>
           <s:else>
              <t:insertDefinition name="externallink"/>,
           </s:else>
        </s:iterator>
        </td>
      </s:if>
      <s:else>
        <td class="results">-</td>
      </s:else>

    <!-- RefSeq ID -->
      <s:set name="refseq" value="#mol.xrefList.xref.{?#this.ns=='RefSeq'}"/>
      <s:if test="#refseq.size>0">
        <td class="results">
        <s:iterator value="#refseq" id="ext" status="status">
           <s:if test="#status.last">
              <t:insertDefinition name="externallink"/>
           </s:if>
           <s:else>
              <t:insertDefinition name="externallink"/>,
           </s:else>
        </s:iterator>
        </td>
      </s:if>
      <s:else>
        <td class="results">-</td>
      </s:else>

    <!-- EntrezGene ID -->
      <s:set name="entrez" value="#mol.xrefList.xref.{?#this.ns=='EntrezGene'}"/>
      <s:if test="#entrez.size>0">
        <td class="results">
        <s:iterator value="#entrez" id="ext" status="status">
           <s:if test="#status.last">
              <t:insertDefinition name="externallink"/>
           </s:if>
           <s:else>
              <t:insertDefinition name="externallink"/>,
           </s:else>
        </s:iterator>
        </td>
      </s:if>
      <s:else>
        <td class="results">-</td>
      </s:else>

     </tr>
  </s:iterator>
    </table>
