<%@ taglib prefix="s" uri="/struts-tags" %>

<s:set var="theme" value="'simple'" scope="page"/>

<p>
 <div class="two-columns" style="text-align:left;">
  <s:submit name="moveColumnStart" value="<<" title="Move column to beginning"/>
  <s:submit name="moveColumnLeft" value="<" title="Move column to the left"/>
 </div>
 <div class="two-columns" style="text-align:right;">
  <s:submit name="moveColumnRight" value=">" title="Move column to the right"/>
  <s:submit name="moveColumnEnd" value=">>" title="Move column to end"/>
 </div>
</p>

<p>
 <div class="four-columns">
  ID <s:textfield name="columnID" size="5"/>
  <s:checkbox name="recordIdentifier"/> Identifier
 </div>
 <div class="four-columns">Name <s:textfield name="columnName" size="15"/></div>
 <div class="four-columns" id="columnType_container">Type
  <s:select name="columnType" list="{'text', 'text-list', 'url', 'url-list'}"/>
 </div>
 <div class="four-columns" id="columnFormatter_container">Formatter
  <s:select name="columnFormatter" list="{'none', 'center'}"/>
 </div>
 <div style="clear:both;"></div>
</p>

<p>
<span class="label-right">Value (OGML)</span>
<s:textfield name="columnValue" size="80"/>
</p>

<p>
 <div class="six-columns"><s:checkbox name="showColumn"/> Show</div>
 <div class="six-columns"><s:checkbox name="hiddenColumn"/> Hidden</div>
 <div class="six-columns"><s:checkbox name="sortColumn"/> Sort</div>
 <div class="six-columns"><s:checkbox name="filterColumn"/> Filter</div>
 <div class="six-columns"><s:checkbox name="resizeColumn"/> Resize</div>
 <div class="six-columns">Width
  <s:textfield name="columnWidth" size="5"/>
 </div>
 <div style="clear:both;"></div>
</p>

<fieldset>
<legend>URL</legend>
<p>
 <span class="label-right"">URL (OGML)</span>
 <s:textfield name="columnURL" size="80"/>
</p>
<span class="label-right"">URL Value (OGML)</span>
<s:textfield name="columnURLValue" size="80"/>
</fieldset>

<fieldset>
<legend>List</legend>
<span class="label-right"">List (OGML)</span>
<s:textfield name="columnList" size="80"/>
</fieldset>

<fieldset>
<legend>Filter</legend>
<p>
 <span class="label-right"">Filter Value (OGML)</span>
 <s:textfield name="columnFilterValue" size="80"/>
</p>
<span class="label-right"">Filter Label (OGML)</span>
<s:textfield name="columnFilterLabel" size="80"/><br>
</fieldset>

<p>
 <div class="three-columns" style="text-align:left;">
  <input type="reset" id="resetColumn" value="Reset"/>
 </div>
 <div class="three-columns" style="text-align:center;">
  <s:submit name="updateColumn" value="Save Settings"/>
 </div>
 <div class="three-columns" style="text-align:right;">
  <s:submit name="dropColumn" value="Drop Column"/>
 </div>
</p>
