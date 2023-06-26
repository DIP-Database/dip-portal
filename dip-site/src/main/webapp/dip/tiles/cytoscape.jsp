<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<td>
      <table class="cytoscape_links" width="100%" cellpadding="0" cellspacing="0" border="0">
        <tr>
          <td rowspan="3" class="cytoscape">
            <img src="images/cytoscape3_60.png" alt="Cytoscape" 
              title="Software for graphing interaction networks"/>
          </td>
          <td class="graph_button_labels">CORE</td>
          <td class="graph_buttons"><input type="radio" name="primary" value="core" checked /></td>
          <td class="graph_buttons"><input type="radio" name="secondary" value="core" checked /></td>
        </tr>
        <tr>
          <td class="graph_button_labels">all</td>
          <td class="graph_buttons"><input type="radio" name="primary" value="all" /></td>
          <td class="graph_buttons"><input type="radio" name="secondary" value="all" /></td>
        </tr>
        <tr>
          <td class="lgraph"><a href="??"><img src="images/cyto1.5_25.png" border="0" alt="graph node" 
                title="Graph <s:property value="#node"/>"
                onMouseOver="this.src='images/cyto1.5_over_25.png';"
                onMouseOut="this.src='images/cyto1.5_25.png';"
                onClick="this.src='images/cyto1.5_click_25.png';"/></a></td>
          <td class="graph">
            <a href="??">
              <img src="images/cyto2.5_25.png" border="0" alt="primary interacting nodes" 
                title="Graph <s:property value="#node"/> and its interactions"
                onMouseOver="this.src='images/cyto2.5_over_25.png';"
                onMouseOut="this.src='images/cyto2.5_25.png';"
                onClick="this.src='images/cyto2.5_click_25.png';"/></a></td>
          <td class="graph">
            <a href="??">
              <img src="images/cyto3.5_25.png" border="0" alt="secondary interacting nodes" 
                title="Graph <s:property value="#node"/> and its 1st & 2nd degree interactions"
                onMouseOver="this.src='images/cyto3.5_over_25.png';"
                onMouseOut="this.src='images/cyto3.5_25.png';"
                onClick="this.src='images/cyto3.5_click_25.png';"/></a></td>
        </tr>
      </table>
</td>
 