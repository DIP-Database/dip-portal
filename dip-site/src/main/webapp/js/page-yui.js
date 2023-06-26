YAHOO.namespace("mbi.page");
YAHOO.mbi.page = {
    shrt: function ( o ) {
        
        //var response = YAHOO.lang.JSON.parse( o.responseText );
        var tgid = o.argument.tgid;

        var li = document.getElementById( tgid );
        li.innerHTML = o.responseText;
        
    },   
    loadBody : function(arg){

        var name = arg.name;
        var tgid = arg.tgid;

        var shrtCallback = { cache:false, timeout: 5000,
                             success:YAHOO.mbi.page.shrt,
                             argument:{ tgid:tgid } };
        YAHOO.util.Connect.asyncRequest( 'GET','page?ret=body&id='+name, shrtCallback );     
    }
};

