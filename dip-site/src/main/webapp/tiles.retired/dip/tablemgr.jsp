<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<H1>Table Manager</H1>

<s:form action="tablemgr" theme="simple">
<s:hidden name="selectedColumnIdx"/>

<div>
 <span class="label-right">Table Type</span>
 <div class="inline-button" id="selectedTableType_container">
  <s:select list="tableType" name="selectedTableType"/>
 </div>
 <div class="inline-button" id="dropTable_container">
 <s:submit name="dropTable" value="Drop Table"/>
 </div>
</div>
<br>
<div id="table_details"></div>
</s:form>

<script type="text/javascript">
    // Record the currently selected table in the dropdown list
    var origTableMenu = document.getElementById("tablemgr_selectedTableType");
    var origSelectedTable =
                    origTableMenu.options[origTableMenu.selectedIndex].value;
    // Convert dropdown list to YUI button menu
    var tableSelectionButton = new YAHOO.widget.Button({
    id:"tableSelectionButton", type:"menu", menu:"tablemgr_selectedTableType",
    lazyloadmenu:false, container:"selectedTableType_container"});
    // Register function to be called when a table is selected
    tableSelectionButton.on("selectedMenuItemChange", handleTableSelection);
    // Load details for table originally selected in HTML dropdown menu
    loadTableDetails(tableSelectionButton, origSelectedTable);

    // Style "Drop Table" button using YUI widget
    var tableDropButton = new YAHOO.widget.Button("tablemgr_dropTable");


    function setButtonMenuLabel(buttonObject, buttonLabel) {
        buttonObject.set("label",
                        "<em class=\"yui-button-label\">"+buttonLabel+"</em>");
    }

    function synchronizeButtonMenuSelection(event) {
        var menuItem = event.newValue;
        setButtonMenuLabel(this, menuItem.cfg.getProperty("text"));
    }

    function formCallback(element, postLoadFunction) {
        // Callback functions to load content for selected table type
        var callbackFunctions = {
            success: function(o) {
                // On success, set element content to response text
                element.innerHTML = o.responseText;
                if (postLoadFunction != null) {
                    postLoadFunction();
                }
            },
            failure: function(o) {
                // On failure, pop up alert with failure message
                alert(o.statusText);
            }
        }
        return callbackFunctions;
    }

    function createSelectionButton(sourceMenuID, containerID) {
        var htmlMenu = document.getElementById(sourceMenuID);
        var selectedText = htmlMenu.options[htmlMenu.selectedIndex].text;
        var selectionButton = new YAHOO.widget.Button({
            id:sourceMenuID+"_buttonMenu", lazyloadmenu:false,
            type:"menu", menu:sourceMenuID, container:containerID});
        selectionButton.on("selectedMenuItemChange",
                                            synchronizeButtonMenuSelection);
        setButtonMenuLabel(selectionButton, selectedText);
    }

    function createButtonAndTooltip(buttonID) {
        var newButton = new YAHOO.widget.Button(buttonID);
        var buttonElement = newButton.get("element");
        var childElements = buttonElement.getElementsByTagName("button");
        var newTooltip = new YAHOO.widget.Tooltip("button-one-tooltip",
                                                {context:childElements[0]});
    }

    function styleColumnButtons() {
        createButtonAndTooltip("moveColumnStart");
        createButtonAndTooltip("moveColumnLeft");
        createButtonAndTooltip("moveColumnRight");
        createButtonAndTooltip("moveColumnEnd");
//        var columnStartButton = new YAHOO.widget.Button("moveColumnStart");
//        var columnLeftButton = new YAHOO.widget.Button("moveColumnLeft");
//        var columnRightButton = new YAHOO.widget.Button("moveColumnRight");
//        var columnEndButton = new YAHOO.widget.Button("moveColumnEnd");
        createSelectionButton("columnType", "columnType_container");
        createSelectionButton("columnFormatter", "columnFormatter_container");
        var columnResetButton = new YAHOO.widget.Button("resetColumn");
        var columnUpdateButton = new YAHOO.widget.Button("updateColumn");
        var columnDropButton = new YAHOO.widget.Button("dropColumn");
    }

    function loadColumnDetails(e, columnIndex) {
        // Only load details if a new column was selected
        if (e.prevValue || !e.newValue) {
            return false;
        }

        // Save index of selected column in hidden form input field
        var hiddenColumnField =
                    document.getElementById('tablemgr_selectedColumnIdx');
        hiddenColumnField.value = columnIndex;

        // Reset block containing column details
        var contentDiv = this.get('contentEl');
        contentDiv.innerHTML = "";

        // Get name of currently selected table
        var selectedTableItem = tableSelectionButton.get("selectedMenuItem");
        var tableName = selectedTableItem.cfg.getProperty("text");
        // Form URL to get contents for selected column
        var url = '/dip-portal/tablecolumn?selectedTableType=' + tableName +
                '&selectedColumnIdx=' + columnIndex;
        // Generate callback function to call after loading column details
        var columnCallback = formCallback(contentDiv, styleColumnButtons);
        // Asynchronously load column details
        YAHOO.util.Connect.asyncRequest('GET', url, columnCallback);

        return true;
    }

    function saveColumnSettings(e, columnIndex) {
        // Only save (and clear) details if a different column was selected
        if (!e.prevValue || e.newValue) {
            return true;
        }

        // Reset block containing column details
        var contentDiv = this.get('contentEl');
        contentDiv.innerHTML = "";

        return true;
    }

    function initializeColumnTabs() {
        // Convert button to add new column to YUI-styled push button
        var columnAddButton = new YAHOO.widget.Button("addColumn");

        // Get name of last selected tab, which is stored in hidden field
        var hiddenColumnField =
                    document.getElementById('tablemgr_selectedColumnIdx');
        var selectedColumnIdx = hiddenColumnField.value;
        var integerRegex = /^\d+$/;
        if (!integerRegex.test(selectedColumnIdx)) {
            selectedColumnIdx = 0;
        }

        // Create a set of tabs from generated UL element in tabledetails.jsp
        var columnTabView = new YAHOO.widget.TabView('columnTabs');
        // Loop through list of tabs that were just created
        var tabList = columnTabView.get('tabs');
        for (var idx in tabList) {
            // Add listeners to be called before and after a tab is changed
            tabList[idx].addListener('activeChange', loadColumnDetails, idx);
            tabList[idx].addListener('beforeActiveChange', saveColumnSettings,
                                    idx);
        }
        // Initialize selected tab
        columnTabView.selectTab(selectedColumnIdx);
    }

    function handleTableSelection(event) {
        // Get name of selected table
        var selectedItem = event.newValue;
        var selectedTableType = selectedItem.cfg.getProperty("text");
        // Make sure to reset index of selected column
        var hiddenColumnField =
                    document.getElementById('tablemgr_selectedColumnIdx');
        hiddenColumnField.value = "0";
        // Load details for selected table
        loadTableDetails(this, selectedTableType);
    }

    function loadTableDetails(tableButtonMenu, selectedTableType) {
        setButtonMenuLabel(tableButtonMenu, selectedTableType);
        var tableDetails = document.getElementById('table_details');
        // Reset both asynchronously loaded blocks
        tableDetails.innerHTML = "";
        //  document.getElementById('column_details').innerHTML = "";
        var url = '/dip-portal/tabledetails?selectedTableType=' +
                    selectedTableType;
        var tableCallback = formCallback(tableDetails, initializeColumnTabs);
        YAHOO.util.Connect.asyncRequest('GET', url, tableCallback);
    }
</script>
