/* button rendering */

YAHOO.namespace("mbi.view");

YAHOO.mbi.view.button = {
    addSimpleSelectElement: function ( container, menuList )
    {
        
       var filterButton = new YAHOO.widget.Button(
                {
                  label: menuList.list[0].text,
                  type: "menu",  
                  menu: menuList.list, 
                  container:  container
                });
                
        
        filterButton.on( "selectedMenuItemChange", 
                       YAHOO.mbi.view.button.onSelectedMenuItemChange );
                       
        var input = document.createElement('input');
        
        input.setAttribute("name", menuList.name);
        input.setAttribute("value", menuList.list[0].value );
        input.setAttribute("id",  menuList.id + "Input" );
        input.setAttribute("type", "hidden" );
        container.appendChild(input);
        
        filterButton.my = {"inputId" : menuList.id + "Input"};
    },
    onSelectedMenuItemChange : function ( menuChange ) 
    {
        var menuItem = menuChange.newValue;
        var newText = menuItem.cfg.getProperty("text");
        var input = document.getElementById(this.my.inputId);
        this.set("label", newText);
        input.value = menuItem.value;
    }
}
