package edu.ucla.mbi.dip.struts.action;

/* =========================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-si#$
 * $Id:: TableMgrAction.java 2877 2012-12-18 20:42:36Z lukasz              $
 * Version: $Rev:: 2877                                                    $
 *==========================================================================
 *
 * TableMgrAction -
 *
 *
 ======================================================================== */

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucla.mbi.util.struts.action.*;
import edu.ucla.mbi.util.struts.interceptor.*;

public class TableMgrAction extends TableMgrSupport {
    // Types of operations handles by this Action class
    private enum SubmitOperation {TABLE_ADD, TABLE_UPDATE, TABLE_DROP,
     COLUMN_ADD, COLUMN_UPDATE, COLUMN_DROP, COLUMN_MOVE_START,
     COLUMN_MOVE_LEFT, COLUMN_MOVE_RIGHT, COLUMN_MOVE_END, UNDEFINED};

    private List<String> tableType;
    private String selectedTableType;
    private String tableIdentifier;
    private String tableLabel;
    private List<String> tableColumn;
    private int selectedColumnIdx;
    private boolean isRecordIdentifier;
    private Map columnParameters;
    private Map<String, String> updatedParameters;
    private SubmitOperation submitOperation = SubmitOperation.UNDEFINED;
    private String debugString;

    //-------------------------------------------------------------------------
    // getter and setter methods
    //-------------------------------------------------------------------------

    public List<String> getTableType() {
        if (tableType == null) {
            Map allTables = this.getTables();
            Set tableKeys = allTables.keySet();
            tableType = new ArrayList(tableKeys);
            Collections.sort(tableType);
        }
        return tableType;
    }

    public void setTableType(List<String> tableType) {
        this.tableType = tableType;
    }

    public String getSelectedTableType() {
        return selectedTableType;
    }

    public void setSelectedTableType(String selectedTableType) {
        this.selectedTableType = selectedTableType;
    }

    private Map getSelectedTableDefinition() {
        TableViewContext tableContext = getTableContext();
        if ((selectedTableType == null) || (tableContext == null)) {
            return null;
        }
        // Get map of parameters for the selected table
        Map tableDefinition =
                (Map) tableContext.getTables().get(selectedTableType);

        return tableDefinition;
    }

    private List getTableColumnList() {
        // Get object containing all parameters for this table
        Map tableDefinition = getSelectedTableDefinition();
        if (tableDefinition == null) {
// REMINDER - HANDLE CASE WHEN SELECTED TABLE NOT FOUND
            return null;
        }
        // Get list of column parameters from table map
        List columnList = (List) tableDefinition.get("fields");

        return columnList;
    }

    public List<String> getTableColumn() {
        if (tableColumn == null) {
            // Get list of columns for the currently selected table
            List columnList = getTableColumnList();
            // Allocate a new list to hold display names for each column
            tableColumn = new ArrayList();
            // Loop through each column in order
            Iterator<Map> columnIterator = columnList.iterator();
            for (int idx = 0; columnIterator.hasNext(); idx++) {
                Map nextColumn = (Map) columnIterator.next();
                String columnLabel = (String) nextColumn.get("name");
                if ((columnLabel == null) || (columnLabel.length() == 0)) {
                    columnLabel = String.format("(Column %d)", idx);
                }
                tableColumn.add(columnLabel);
           }
        }
        return tableColumn;
    }

    public void setTableColumn(List<String> tableColumn) {
        this.tableColumn = tableColumn;
    }

    public String getSelectedColumnIdx() {
        return Integer.toString(selectedColumnIdx);
    }

    public void setSelectedColumnIdx(String selectedColumn) {
        try {
            this.selectedColumnIdx = Integer.parseInt(selectedColumn);
        } catch (NumberFormatException nfException) {
            this.selectedColumnIdx = 0;
        }
        lookupSelectedColumnTable();
    }

    private void lookupSelectedColumnTable() {
        // Get list of column parameters from table map
        List columnList = getTableColumnList();
        if (columnList == null) {
            return;
        }
        this.columnParameters = (Map) columnList.get(selectedColumnIdx);
// REMINDER - HANDLE CASE WHEN SELECTED COLUMN NOT FOUND
    }

    public String getColumnParameter(String field) {
        if (columnParameters == null) {
            lookupSelectedColumnTable();
        }
        String value = (String) columnParameters.get(field);
        return value;
    }

    public void setColumnParameter(String field, String value) {
        updatedParameters.put(field, value);
    }

    public String getRecordIdentifier() {
        Map tableDefinition = getSelectedTableDefinition();
        if (tableDefinition == null) {
// REMINDER - HANDLE CASE WHEN SELECTED TABLE NOT FOUND
            return null;
        }
        // Lookup current column designated as an identifier for this table
        String tableRecordIdentifier = (String) tableDefinition.get("idfield");
        String columnID = getColumnParameter("field");

        return tableRecordIdentifier.equals(columnID) ? "true" : "false";
    }

    public void setRecordIdentifier(String recordIdentifier) {
        isRecordIdentifier = (recordIdentifier.equals("true")) ? true : false;
    }

    public String getColumnID() {
        return getColumnParameter("field");
    }

    public void setColumnID(String columnID) {
        setColumnParameter("field", columnID);
    }

    public String getColumnName() {
        return getColumnParameter("name");
    }

    public void setColumnName(String columnName) {
        setColumnParameter("name", columnName);
    }

    public String getColumnType() {
        return getColumnParameter("type");
    }

    public void setColumnType(String columnType) {
        setColumnParameter("type", columnType);
    }

    public String getColumnFormatter() {
        return getColumnParameter("formatter");
    }

    public void setColumnFormatter(String columnFormatter) {
        setColumnParameter("formatter", columnFormatter);
    }

    public String getColumnValue() {
        return getColumnParameter("value");
    }

    public void setColumnValue(String columnValue) {
        setColumnParameter("value", columnValue);
    }

    public String getShowColumn() {
        return getColumnParameter("show");
    }

    public void setShowColumn(String showColumn) {
        setColumnParameter("show", showColumn);
    }

    public String getHiddenColumn() {
        return getColumnParameter("hidden");
    }

    public void setHiddenColumn(String hiddenColumn) {
        setColumnParameter("hidden", hiddenColumn);
    }

    public String getSortColumn() {
        return getColumnParameter("sort");
    }

    public void setSortColumn(String sortColumn) {
        setColumnParameter("sort", sortColumn);
    }

    public String getFilterColumn() {
        return getColumnParameter("filter");
    }

    public void setFilterColumn(String filterColumn) {
        setColumnParameter("filter", filterColumn);
        String filterType = filterColumn.equalsIgnoreCase("true") ?
                            "select" : "";
        setColumnParameter("filter-type", filterType);
    }

    public String getResizeColumn() {
        return getColumnParameter("resize");
    }

    public void setResizeColumn(String resizeColumn) {
        setColumnParameter("resize", resizeColumn);
    }

    public String getColumnWidth() {
        return getColumnParameter("width");
    }

    public void setColumnWidth(String columnWidth) {
        setColumnParameter("width", columnWidth);
    }

    public String getColumnURL() {
        return getColumnParameter("url");
    }

    public void setColumnURL(String columnURL) {
        setColumnParameter("url", columnURL);
    }

    public String getColumnURLValue() {
        return getColumnParameter("urlvalue");
    }

    public void setColumnURLValue(String columnURLValue) {
        setColumnParameter("urlvalue", columnURLValue);
    }

    public String getColumnList() {
        return getColumnParameter("list");
    }

    public void setColumnList(String columnList) {
        setColumnParameter("list", columnList);
    }

    public String getColumnFilterValue() {
        return getColumnParameter("filter-value");
    }

    public void setColumnFilterValue(String columnFilterValue) {
        setColumnParameter("filter-value", columnFilterValue);
    }

    public String getColumnFilterLabel() {
        return getColumnParameter("filter-label");
    }

    public void setColumnFilterLabel(String columnFilterLabel) {
        setColumnParameter("filter-label", columnFilterLabel);
    }


    //-------------------------------------------------------------------------
    // Constructor
    //-------------------------------------------------------------------------
    public TableMgrAction() {
        tableType = null;
        tableColumn = null;
        columnParameters = null;
        updatedParameters = new HashMap<String, String>();
    }

    public void setDropTable(String dropTable) {
        if (dropTable.length() > 0) {
            this.submitOperation = SubmitOperation.TABLE_DROP;
        }
    }

    public void setAddColumn(String addColumn) {
        if (addColumn.length() > 0) {
            this.submitOperation = SubmitOperation.COLUMN_ADD;
        }
    }

    public void setUpdateColumn(String updateColumn) {
        if (updateColumn.length() > 0) {
            this.submitOperation = SubmitOperation.COLUMN_UPDATE;
        }
    }

    public void setDropColumn(String dropColumn) {
        if (dropColumn.length() > 0) {
            this.submitOperation = SubmitOperation.COLUMN_DROP;
        }
    }

    public void setMoveColumnStart(String moveColumnStart) {
        if (moveColumnStart.length() > 0) {
            this.submitOperation = SubmitOperation.COLUMN_MOVE_START;
        }
    }

    public void setMoveColumnLeft(String moveColumnLeft) {
        if (moveColumnLeft.length() > 0) {
            this.submitOperation = SubmitOperation.COLUMN_MOVE_LEFT;
        }
    }

    public void setMoveColumnRight(String moveColumnRight) {
        if (moveColumnRight.length() > 0) {
            this.submitOperation = SubmitOperation.COLUMN_MOVE_RIGHT;
        }
    }

    public void setMoveColumnEnd(String moveColumnEnd) {
        if (moveColumnEnd.length() > 0) {
            this.submitOperation = SubmitOperation.COLUMN_MOVE_END;
        }
    }

    public String getDebugString() {
        if (columnParameters != null) {
            TableViewJsonContext tvContext =
                                    (TableViewJsonContext) getTableContext();
            Map<String,Object> jpd =
                                    tvContext.getJsonContext().getJsonConfig();
            debugString = jpd.toString();
        }
        return debugString;
    }

    public void setDebugString(String debugString) {
        this.debugString = debugString;
    }

    public String execute() throws Exception {
        initialize(true);

        switch (submitOperation) {
            case TABLE_DROP:
                dropTable();
                break;
            case COLUMN_ADD:
                addColumn();
                break;
            case TABLE_UPDATE:
            case COLUMN_UPDATE:
                updateColumn();
                break;
            case COLUMN_DROP:
                dropColumn();
                break;
            case COLUMN_MOVE_START:
                moveColumnToStart();
                break;
            case COLUMN_MOVE_LEFT:
                moveColumn(-1);
                break;
            case COLUMN_MOVE_RIGHT:
                moveColumn(1);
                break;
            case COLUMN_MOVE_END:
                moveColumnToEnd();
                break;
        }

        return SUCCESS;
    }

    public void dropTable() {
        // Get JSON table context
        TableViewContext tableContext = getTableContext();
        // Return now if no table context or table not specified
        if ((selectedTableType == null) || (tableContext == null)) {
            return;
        }
        // Get master Map containing all table definitions
        Map tableConfig = tableContext.getTables();
        // Attempt to remove named table from table context
        tableConfig.remove(selectedTableType);
// REMINDER - HANDLE CASE WHEN SELECTED TABLE NOT FOUND
        // Save updated table context to JSON file
        saveTableContext();

        setSelectedTableType("");
        setSelectedColumnIdx("0");
    }

    public void addColumn() {
        // Allocate map to hold paramaters for new column
        Map newColumn = new HashMap();
        // Initialize column with dummy name
        newColumn.put("name", "New Column");
// REMINDER - MAKE SURE DUMMY NAME DOES NOT ALREADY EXIST

        // Get list of column parameters from table map
        List columnList = getTableColumnList();
        // Append newly created column to list for selected table
        columnList.add(newColumn);
        // Save updated table context to JSON file
        saveTableContext();
        // Compute index of newly appended column
        int columnIndex = columnList.size() - 1;
        // Set name of selected column to newly created entry
        setSelectedColumnIdx(Integer.toString(columnIndex));
    }

    public void updateColumn() {
        String field, oldValue, newValue;

        // Make sure class Map variable is pointing to column selected by user
        lookupSelectedColumnTable();
        if ((columnParameters == null) || (updatedParameters == null)) {
// REMINDER - RETURN ERROR MESSAGE IF MAPS ARE NULL
            return;
        }

        // Loop through every key/value pair submitted as part of HTML form
        for (Map.Entry<String, String> entry : updatedParameters.entrySet()) {
            // Record key/value pair
            field = entry.getKey();
            newValue = entry.getValue();
            // Also record existing value for specified key
            oldValue = (String) columnParameters.get(field);
            // If old or new value exists, merge new value with column settings
            if ((newValue.length() > 0) ||
             ((oldValue != null) && (oldValue.length() > 0))) {
                columnParameters.put(field, newValue);
            }
        }

        if (isRecordIdentifier) {
            Map tableDefinition = getSelectedTableDefinition();
            String columnID = (String) columnParameters.get("field");
            if ((tableDefinition != null) && (columnID != null) &&
                                                    (columnID.length() > 0)) {
                tableDefinition.put("idfield", columnID);
                tableDefinition.put("labelfield", columnID);
            }
        }

        // Save merged table context to JSON file
        saveTableContext();
    }

    public void dropColumn() {
        // Make sure class Map variable is pointing to column selected by user
        lookupSelectedColumnTable();
        // Get list of column parameters from table map
        List columnList = getTableColumnList();
        // Delete selected column from list for selected table
        columnList.remove(columnParameters);
        // Save updated table context to JSON file
        saveTableContext();
        setSelectedColumnIdx("0");
    }

    public void moveColumnToStart() {
        // If column is already first in list, no need to make any changes
        if (selectedColumnIdx == 0) {
            return;
        }
        // Get list of column parameters from table map
        List columnList = getTableColumnList();
        // Rotate initial columns in list up to selected column forward by one
        Collections.rotate(columnList.subList(0, selectedColumnIdx+1), 1);
        // Save updated table context to JSON file
        saveTableContext();
        // Update class variable with new index
        selectedColumnIdx = 0;
    }

    public void moveColumn(int offset) {
        // Get list of column parameters from table map
        List columnList = getTableColumnList();
        // Calculate new index for selected column
        int newIndex = selectedColumnIdx + offset;
        // Make sure new index is not out of bounds
        if ((newIndex < 0) || (newIndex >= columnList.size())) {
            return;
        }
        // Swap two elements in the list
        Collections.swap(columnList, selectedColumnIdx, newIndex);
        // Save updated table context to JSON file
        saveTableContext();
        // Update class variable with new index
        selectedColumnIdx = newIndex;
    }

    public void moveColumnToEnd() {
        // Get list of column parameters from table map
        List columnList = getTableColumnList();
        // If column is already at end of list, no need to make any changes
        if (selectedColumnIdx >= (columnList.size()-1)) {
            return;
        }
        // Rotate columns in list following selected column forward by one
        Collections.rotate(columnList.subList(selectedColumnIdx,
                            columnList.size()), -1);
        // Save updated table context to JSON file
        saveTableContext();
        // Update class variable with new index
        selectedColumnIdx = columnList.size() - 1;
    }
}
