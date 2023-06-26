YAHOO.namespace("mbi.meta");
YAHOO.mbi.meta.counts = {
    shrt: function ( o ) {
     
        var helpPopup = 'YAHOO.mbi.modal.help("DIP Evidence Types","guide-evtype"); return false;'; 

        var response = YAHOO.lang.JSON.parse( o.responseText );
        var id = o.argument.id;
        
        var cnt = response.meta["DIP"]["final"]["protein-count"];
        if ( cnt > -1 ){
            var li = document.getElementById( "prt-count" );
            li.innerHTML = insertCommas(cnt) + ' proteins';
        }
        
        var cnt = response.meta["DIP"]["final"]["interaction-count"];
        if ( cnt > -1 ){
            var li = document.getElementById( "int-count" );
            li.innerHTML = insertCommas(cnt) + ' interactions';
        }

        var cnt = response.meta["DIP"]["final"]["source-count"];
        if ( cnt > -1 ){
            var li = document.getElementById( "art-count" );
            li.innerHTML = insertCommas(cnt) + ' articles';
        }
        
        var cnt = response.meta["DIP"]["final"]["species-count"];
        if ( cnt > -1 ){
            var li = document.getElementById( "org-count" );
            li.innerHTML = insertCommas(cnt) + ' organisms';
        }

        var cnt = response.meta["DIP"]["final"]["evidence-count"];
        if ( cnt > -1 ){
            var li = document.getElementById( "evd-count" );
            li.innerHTML = insertCommas(cnt) + ' experiments';
        }

        var cnt = response.meta["DIP"]["final"]["inference-count"];
        if ( cnt > -1 ){
            var li = document.getElementById( "cii-count" );
            li.innerHTML = insertCommas(cnt) + " automated inferences [<b><a href='' onClick='" + 
                helpPopup + "'>?</a></b>]";
        }

        var cnt = response.meta["DIP"]["final"]["author-inference-count"];
        if ( cnt > -1 ){
            var li = document.getElementById( "aui-count" );
            li.innerHTML = insertCommas(cnt) + " authors' inferences [<b><a href='' onClick='" +
                helpPopup + "'>?</a></b>]";
        }

        function insertCommas(str){
            str = String(str); 
            if(str.length < 4){
                 return str;
             }
            return insertCommas(str.substring(0, str.length - 3)) + ","
                                 + str.substring(str.length - 3, str.length);
        }


    },


    simpleCnt: function( id, def, val ) { 
        var li = document.getElementById( id );
        if ( val > -1 && li !== undefined ) {
            li.innerHTML = val;
        } else {
            li.innerHTML = def;
        }
    },

    betaCnt: function( id, def, val ) { 
        var li = document.getElementById( id );
        if ( val > -1 && li !== undefined ) {
            li.innerHTML = ' (' + val + '<sup>*</sup>)';
        } else {
            li.innerHTML = ' (' + def + '<sup>*</sup>)';
        }
    },

    detail: function ( o ) {
        var response = YAHOO.lang.JSON.parse( o.responseText );

        YAHOO.mbi.meta.counts.simpleCnt( "prt-count","0",
                                         response.meta["DIP"]["final"]["protein-count"]);                           
        YAHOO.mbi.meta.counts.simpleCnt( "int-count","0",
                                         response.meta["DIP"]["final"]["interaction-count"]);
        YAHOO.mbi.meta.counts.simpleCnt( "art-count","0",
                                         response.meta["DIP"]["final"]["source-count"]);
        YAHOO.mbi.meta.counts.simpleCnt( "org-count","0",
                                         response.meta["DIP"]["final"]["species-count"]);
        YAHOO.mbi.meta.counts.simpleCnt( "evd-count","0",
                                         response.meta["DIP"]["final"]["evidence-count"]);
        YAHOO.mbi.meta.counts.simpleCnt( "auto-inf-count","0",
                                         response.meta["DIP"]["final"]["inference-count"]);
        YAHOO.mbi.meta.counts.simpleCnt( "auth-inf-count","0",
                                         response.meta["DIP"]["final"]["author-inference-count"]);
        YAHOO.mbi.meta.counts.simpleCnt( "evd-dip-count","0",
                                         response.meta["DIP"]["final"]["imex-evidence-count"]);
        YAHOO.mbi.meta.counts.simpleCnt( "art-dip-count","0",
                                         response.meta["DIP"]["final"]["imex-source-count"]);


        if( response.meta.DIP["provisional"] !== undefined ) {
            YAHOO.mbi.meta.counts.betaCnt( "evd-dip-beta-count","0",
                                           response.meta["DIP"]["provisional"]["imex-evidence-count"]);
             YAHOO.mbi.meta.counts.betaCnt( "art-dip-beta-count","0",
                                            response.meta["DIP"]["provisional"]["imex-source-count"]);
        }

        if( response.meta.other !== undefined ) {
            
            if( response.meta.other["final"] !== undefined ) {
                YAHOO.mbi.meta.counts.simpleCnt( "evd-other-count","0",
                                                 response.meta["other"]["final"]["imex-evidence-count"]);
                YAHOO.mbi.meta.counts.simpleCnt( "art-other-count","0",
                                                 response.meta["other"]["final"]["imex-source-count"]);                
            }
            if( response.meta.other["provisional"] !== undefined ) {
                YAHOO.mbi.meta.counts.betaCnt( "evd-other-beta-count","0",
                                               response.meta["other"]["provisional"]["imex-evidence-count"]); 
                YAHOO.mbi.meta.counts.betaCnt( "art-other-beta-count","0",
                                               response.meta["other"]["provisional"]["imex-source-count"]);
            }
        }
            
    },


    species: function ( o ) {
        var response = YAHOO.lang.JSON.parse( o.responseText );
        var spcList = response.meta.spc;
        

        

        if( spcList !== undefined ) {

            var odd = -1;

            for( var i = 0; i < spcList.length; i++ ){
                var sname = spcList[i].name;
                var cname = spcList[i].cname;
                var taxid = spcList[i].taxid;
                
                var detail = spcList[i].details;
                var disabled = !( spcList[i].details );
                var row = document.createElement("tr");

                var c0 = document.createElement("td");
                YAHOO.util.Dom.addClass(c0,"stat-spc-org");

                var html0 = "<i>" + sname + "</i>";
                if( cname.length>0 ) { html0 += "<br/>("+cname+")"; }
                html0 += "</td>";
                c0.innerHTML = html0;
                row.appendChild(c0);

                var c1 = document.createElement("td");
                c1.id = "spc-prt-" + i;
                YAHOO.util.Dom.addClass( c1, "stat-spc-num" );
                
                var html1 = "&nbsp;";
                c1.innerHTML = html1;
                row.appendChild(c1);

                var c2 = document.createElement("td");
                c2.id = "spc-int-" + i;
                YAHOO.util.Dom.addClass( c2, "stat-spc-num" );

                var html2 = "&nbsp;";
                c2.innerHTML = html2;
                row.appendChild(c2);
                
                var c3 = document.createElement("td");
                c3.id = "spc-evd-" + i;
                YAHOO.util.Dom.addClass(c3,"stat-spc-num");

                var html3 = "&nbsp;";
                c3.innerHTML = html3;
                row.appendChild(c3);
                
                var c4 = document.createElement("td");
                
                YAHOO.util.Dom.addClass( c4, "stat-spc-det" );
                row.appendChild(c4);
                var pbid = "stat-spc-det"+i;
                var pb = new YAHOO.widget.Button({ label:"Show", id:pbid, 
                                                   disabled: disabled, 
                                                   container: c4 });
                var detPopup = function () {
                    YAHOO.mbi.modal.spcstat(sname);    
                };
                
                pb.on("click", detPopup );

                if( odd >0 ) {
                    YAHOO.util.Dom.addClass(c0,"stat-spc-tbl-odd");
                    YAHOO.util.Dom.addClass(c1,"stat-spc-tbl-odd");
                    YAHOO.util.Dom.addClass(c2,"stat-spc-tbl-odd");
                    YAHOO.util.Dom.addClass(c3,"stat-spc-tbl-odd");
                    YAHOO.util.Dom.addClass(c4,"stat-spc-tbl-odd");
                }                
                odd = -odd;
                var tab = document.getElementById("spc-count-table");
                tab.appendChild( row );

                var spcRowCallback = { cache:false, timeout: 5000,
                                       success:YAHOO.mbi.meta.counts.spcRow,
                                       argument:{ prt:c1.id, edg:c2.id, evd:c3.id} };

                var spcUrl = 'stat?ns=taxid&ac='+taxid;
                YAHOO.util.Connect.asyncRequest( 'GET',spcUrl, spcRowCallback );     


            }
        }
    },

    spcRow: function ( o ) {
        var response = YAHOO.lang.JSON.parse( o.responseText );
        var prt = document.getElementById( o.argument.prt );
        var edg = document.getElementById( o.argument.edg );
        var evd = document.getElementById( o.argument.evd );
        
        var prtCnt =  response.meta["DIP"]["final"]["protein-count"];       
        var edgCnt =  response.meta["DIP"]["final"]["interaction-count"];       
        var evdCnt =  response.meta["DIP"]["final"]["evidence-count"];       
        
        prt.innerHTML = prtCnt !== undefined ? prtCnt : '0';
        edg.innerHTML = edgCnt !== undefined ? edgCnt : '0';
        evd.innerHTML = evdCnt !== undefined ? evdCnt : '0';
    },

    loadShort : function () {
        var shrtCallback = { cache:false, timeout: 5000,
                             success:YAHOO.mbi.meta.counts.shrt,
                             argument:{ id:"newscontainer"} };
        YAHOO.util.Connect.asyncRequest( 'GET','stat', shrtCallback );     
    },

    loadDetail : function () {
        var detailCallback = { cache:false, timeout: 5000,
                             success:YAHOO.mbi.meta.counts.detail,
                             argument:{ id:"newscontainer"} };
        YAHOO.util.Connect.asyncRequest( 'GET','stat', detailCallback );     
    },

    loadSpecies : function ( n ) {
        var url = 'stat?list='+n;
        var spcCallback = { cache:false, timeout: 5000,
                             success:YAHOO.mbi.meta.counts.species,
                             argument:{ id:"spc-count-table"} };
        YAHOO.util.Connect.asyncRequest( 'GET', url, spcCallback );     
    }

};

YAHOO.mbi.meta.prlcounts = {

    shrt: function ( o ) {

        var response = YAHOO.lang.JSON.parse( o.responseText );
        var id = o.argument.id;

        var cnt = response.meta["PFL"]["total"]["linkage-count"];
        
        if ( cnt > -1 ){
            var li = document.getElementById( "link-count" );
            li.innerHTML = '<b>' + insertCommas(cnt) + '</b> Inferences';
        }
        
        var cnt = response.meta["PFL"]["total"]["species-count"];
        if ( cnt > -1 ){
            var li = document.getElementById( "proc-count" );
            li.innerHTML = '<b>' + insertCommas(cnt) + '</b> Genomes';
        }

        var cnt = response.meta["PFL"]["total"]["protein-count"];
        if ( cnt > -1 ){
            var li = document.getElementById( "prt-count" );
            li.innerHTML ='<b>' + insertCommas(cnt) + '</b> Proteins';
        }

        function insertCommas(str){
            str = String(str); 
            if(str.length < 4){
                 return str;
             }
            return insertCommas(str.substring(0, str.length - 3)) + ","
                                 + str.substring(str.length - 3, str.length);
        }


    },
    
    fail :  function (o) {
        //alert( "Collecting statistics failed: " 
        //    + o.status + ": "+ o.statusText);
    },
    
    loadShort : function () {
        
        var prlShrtCallback = { cache:false, timeout: 5000,
                                success:YAHOO.mbi.meta.prlcounts.shrt,
                                failure:YAHOO.mbi.meta.prlcounts.fail,
                                argument:{ id:"newscontainer"} };

       
            YAHOO.util.Connect.asyncRequest( 'GET',
                                             'php/pl_stats.php',
                                             prlShrtCallback );   
    }

};

