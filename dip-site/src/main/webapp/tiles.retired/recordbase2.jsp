<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

   <table width="100%" cellpadding="0" cellspacing="0">
<s:if test='rtp=="N"'>
    <tr>
     <td class="data_header">
      <b>Description:</b> <s:property value="record.name" />
      <br />
      <b>Organism:</b>
      <i><s:property value="record.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.label" /></i>
      <s:if test="record.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.name!=''">
      (<s:property value="record.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.name" />)
      </s:if>
      </td>
      <td class="data_header"><b>UniProt:</b>
      <a href="http://beta.uniprot.org/uniprot/<s:property value="record.xrefList.xref.{?#this.ns=='UniProt'}[0].ac" />">
      <s:property value="record.xrefList.xref.{?#this.ns=='UniProt'}[0].ac" /></a>
      <br />
      <b>RefSeq:</b>
      <a href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?val=<s:property value="record.xrefList.xref.{?#this.ns=='RefSeq'}[0].ac" />">
      <s:property value="record.xrefList.xref.{?#this.ns=='RefSeq'}[0].ac" /></a>
     </td>
    </tr>
</s:if>
<s:elseif test='rtp=="E"'>
    <tr>
     <td class="data_header">
      <b>Interaction type:</b> <s:property value="record.name" />
      <br />
      <b>Core:</b>
      <i><s:property value="record.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.label" /></i>
      <s:if test="record.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.name!=''">
      (<s:property value="record.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.name" />)
      </s:if>
      </td>
      <td class="data_header"><b>UniProt:</b>
      <a href="http://beta.uniprot.org/uniprot/<s:property value="record.xrefList.xref.{?#this.ns=='UniProt'}[0].ac" />">
      <s:property value="record.xrefList.xref.{?#this.ns=='UniProt'}[0].ac" /></a>
      <br />
      <b>RefSeq:</b>
      <a href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?val=<s:property value="record.xrefList.xref.{?#this.ns=='RefSeq'}[0].ac" />">
      <s:property value="record.xrefList.xref.{?#this.ns=='RefSeq'}[0].ac" /></a>
     </td>
    </tr>
</s:elseif>
<s:elseif test='rtp=="X"'>
    <tr>
     <td class="data_header">
      <b>Experiment type:</b> <s:property value="record.name" />
      <br />
      <b>Organism:</b>
      <i><s:property value="record.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.label" /></i>
      <s:if test="record.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.name!=''">
      (<s:property value="record.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.name" />)
      </s:if>
      </td>
      <td class="data_header"><b>UniProt:</b>
      <a href="http://beta.uniprot.org/uniprot/<s:property value="record.xrefList.xref.{?#this.ns=='UniProt'}[0].ac" />">
      <s:property value="record.xrefList.xref.{?#this.ns=='UniProt'}[0].ac" /></a>
      <br />
      <b>RefSeq:</b>
      <a href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?val=<s:property value="record.xrefList.xref.{?#this.ns=='RefSeq'}[0].ac" />">
      <s:property value="record.xrefList.xref.{?#this.ns=='RefSeq'}[0].ac" /></a>
     </td>
    </tr>
</s:elseif>
<s:elseif test='rtp=="S"'>
    <tr>
     <td class="data_header">
      <b>Authors:</b> <s:property value="record.name" />
      <br />
      <b>Title:</b>
      <i><s:property value="record.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.label" /></i>
      <s:if test="record.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.name!=''">
      (<s:property value="record.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.name" />)
      </s:if>
      </td>
      <td class="data_header"><b>PubMed:</b>
      <a href="http://beta.uniprot.org/uniprot/<s:property value="record.xrefList.xref.{?#this.ns=='UniProt'}[0].ac" />">
      <s:property value="record.xrefList.xref.{?#this.ns=='UniProt'}[0].ac" /></a>
      <br />
      <b>RefSeq:</b>
      <a href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?val=<s:property value="record.xrefList.xref.{?#this.ns=='RefSeq'}[0].ac" />">
      <s:property value="record.xrefList.xref.{?#this.ns=='RefSeq'}[0].ac" /></a>
     </td>
    </tr>
</s:elseif>
   </table>
