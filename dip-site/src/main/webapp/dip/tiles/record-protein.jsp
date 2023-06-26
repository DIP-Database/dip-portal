<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<tr>
  <td>
      <b>Description:</b> <s:property value="summary.name"/>
      <br />
      <b>Organism:</b>
      <i><s:property value="summary.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.label"/></i>
      <s:set var="commonname" value="summary.xrefList.xref.{?#this.typeAc=='dxf:0007'}[0].node.name"/>
      <s:if test="#commonname!=''">
        (<s:property value="#commonname"/>)
      </s:if>
  </td>

  <td nowrap><b>UniProt:</b>
      <s:iterator value="summary.xrefList.xref.{?#this.ns=='uniprot'}" var="ext" status="status">
        <s:if test="#status.last">
          <t:insertDefinition name="externallink"/>
        </s:if>
        <s:else>
          <t:insertDefinition name="externallink"/>,
        </s:else>
      </s:iterator>
      <br />

      <b>RefSeq:</b>
      <s:iterator value="summary.xrefList.xref.{?#this.ns=='refseq'}" var="ext" status="status">
        <s:if test="#status.last">
           <t:insertDefinition name="externallink"/>
        </s:if>
        <s:else>
           <t:insertDefinition name="externallink"/>,
        </s:else>
      </s:iterator>
<!--    </td>

  <td style="vertical-align: top">
--><br />      <b>EntrezGene:</b>
      <s:iterator value="summary.xrefList.xref.{?#this.ns=='entrezgene'}" var="ext" status="status">
        <s:if test="#status.last">
           <t:insertDefinition name="externallink"/>
        </s:if>
        <s:else>
           <t:insertDefinition name="externallink"/>,
        </s:else>
      </s:iterator>
  </td>
</tr>
<tr>
   <td colspan=2" valign="top" align ="left" id="seq-info-base"></td>
</tr>

<script src="js/seqlib-yui.js" type="text/javascript" language="JavaScript"></script>
<script>
   YAHOO.util.Event.addListener( window, "load",
     YAHOO.mbi.view.summary.load( { "db":"<s:property value='siteDef'/>",
                                    "type":"protein",
                                    "ns":"<s:property value='ns'/>", 
                                    "ac":"<s:property value='ac'/>",
                                    "base-id":"seq-info-base"
                                   } ));
</script>


