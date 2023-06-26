/* record rendering */

YAHOO.namespace("mbi.view");

YAHOO.mbi.view.summary  = {

    base: null,
    xlst: [],
    slst: [],
    
    load: function( o ){

        try{
            
            var db = o.db;
            var type = o.type;
            var ns = o.ns;
            var ac = o.ac;
            
            var basid = o['base-id'];
            
            this.base = YAHOO.util.Dom.get( basid );
        
            if( this.base !== undefined) {
                
                if( db ==="dip" && ns ==="dip" && type ==="protein" ){
               
                    var valCallback = { cache:false, timeout: 5000, 
                                        success: YAHOO.mbi.view.protein.setProtDetail,
                                        argument:{ "db":"DIP","ns":ns,"ac":ac} };
                    
                    var data_url= 'record?ns=dip&ac='+ ac 
                        + '&dl=full&sl=0&ret=data';
                    //alert("data_url: "+data_url);
                    YAHOO.util.Connect.asyncRequest( 'GET', data_url
                                                     , valCallback ); 
                }
                if( db ==="dip" && ns ==="dip" && type ==="interaction" ){
                    
                    var valCallback = { cache:false, timeout: 5000, 
                                        success: YAHOO.mbi.view.interaction.setAbstract,
                                        argument:{ "db":"DIP","ns":ns,"ac":ac} };
                    
                    YAHOO.util.Connect.asyncRequest( 'GET', 'record?ns=dip&ac='+ ac 
                                                     + '&dl=full&sl=0&ret=data'
                                                     , valCallback ); 
                }
                if( db ==="dip" && ns ==="dip" && type ==="evidence" ){
                    var valCallback = { cache:false, timeout: 5000, 
                                        success: YAHOO.mbi.view.evidence.setEvidenceDetail,
                                        argument:{ "db":"DIP","ns":ns,"ac":ac} };
                    
                    YAHOO.util.Connect.asyncRequest( 'GET', 'record?ns=dip&ac='+ ac 
                                                     + '&dl=full&sl=0&ret=data'
                                                     , valCallback ); 
                    
                }
                if( db ==="dip" && ns ==="dip" && type ==="article" ){
                    var valCallback = { cache:false, timeout: 5000, 
                                        success: YAHOO.mbi.view.article.setAbstract,
                                        argument:{ "db":"DIP","ns":ns,"ac":ac} };
                    
                    YAHOO.util.Connect.asyncRequest( 'GET', 'record?ns=dip&ac='+ ac 
                                                     + '&dl=full&sl=0&ret=data'
                                                     , valCallback ); 
                    
                }      
            }
            
        }catch( x ){
            alert( x );
        }
    }
};
