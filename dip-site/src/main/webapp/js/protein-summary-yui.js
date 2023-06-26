/* record rendering */

YAHOO.namespace("mbi.view");

YAHOO.mbi.view.protein = {

    show: function( o ){
        //alert("sidx=" + o.sidx);        
        var sidx = o.sidx;
        var cnt = o.cnt;

        //alert("cnt=" + o.cnt);
       
        if( cnt === undefined ){
            cnt = 3;
        } else {                       
            cnt = cnt -1;
        }
        
        if( sidx === undefined ){
            sidx = 0;
        }
        
        var seq = YAHOO.mbi.view.summary.slst[sidx].seq;

        if( seq !== undefined && seq !== "" ){
            
            var sdf = YAHOO.mbi.view.protein
                .getSeqTbl( YAHOO.mbi.view.summary.base );
            if( sdf === undefined ){
                sdf = YAHOO.mbi.view.protein
                    .buildSeqTbl( YAHOO.mbi.view.summary.base );
            }
            YAHOO.mbi.seqlib.protview( sdf.vd, seq );
            YAHOO.mbi.seqlib.protcomp( sdf.cd, seq );
            YAHOO.mbi.seqlib.protsumm( sdf.sd, seq, 
                                       { slst: YAHOO.mbi.view.summary.slst,
                                         sidx: sidx });
        } else {
            //alert("cnt=" + cnt);
            if( cnt > 0 ){
                            
                var proxyCallback = { cache:false, timeout: 5000, 
                                      success:YAHOO.mbi.view.protein.setProtSeq,
                                      argument: { "sidx": sidx, "cnt": cnt } };
                //alert( "XXX:"+YAHOO.mbi.view.summary.slst[sidx].proxyUrl );
                YAHOO.util.Connect.asyncRequest( 'GET', 
                                                 YAHOO.mbi.view.summary.slst[sidx].proxyUrl, 
                                                 proxyCallback );            
            }
        }
    },
    
    setProtDetail: function( o ){ 
        
        var base =  YAHOO.mbi.view.summary.base;
        if( base === undefined ){
            return;
        }

        var slst =  YAHOO.mbi.view.summary.slst;
        
        var messages = YAHOO.lang.JSON.parse( o.responseText );
        var alist = null;
            
        if( messages.summary.attrList !== null ){
            alist =  messages.summary.attrList.attr; 
        }
        
        var ns = o.argument.ns;
        var ac = o.argument.ac;
        
        var seq="";
        try{
            
            if( alist !== null ){
                for( var i=0; i<alist.length; i++ ){
                    var aac=alist[i].ac;
                    
                    
                    if( alist[i].value !== null ){
                        var aval = alist[i].value.value;
                        
                        if( aac === "dip:0007" || aac === "dip:0008" ){  //  NOTE: should be dip:0008 !!!!
                            seq = aval;
                            break;
                        }
                    }
                    
                    //} catch (x) {
                    //    alert(x + ":::" + alist[i].value);
                    //}
                }
            }
            
        } catch (x) {
          //alert(x);   
        }
        // native (ie DIP record) sequence
        
        slst.push( { db:"DIP", ns:ns, ac:ac, seq:seq } );
        
        // x-ref based sequences
        
        var mxlst = messages.summary.xrefList.xref;
            
        for( var i=0; i<mxlst.length; i++ ){
            var xns=mxlst[i].ns;
            var xac=mxlst[i].ac;
                
            var xtpac=mxlst[i].typeAc;
            
            if( xtpac === "dxf:0006" ){
                if( xns === "uniprot" ){
                    var sref ={"db":"UniProt","ns":"uniprot" ,"ac":xac };
                    slst.push( sref );
                }
                if( xns === "refseq" ){
                    var sref = { "db":"RefSeq","ns":"refseq" ,"ac":xac};
                    slst.push( sref );
                }                       
            }
        }
            
        var sidx = -1;
        var upidx = -1;
        var rsidx = -1;
        
        for( var j=0; j< slst.length; j++ ){
            if( slst[j].seq !== undefined && slst[j].seq !== ""){
                sidx = j;
                break;
            }
            
            if(slst[j].db === "UniProt" ){  
                upidx = j;
                slst[j].proxyUrl = 'proxy?db=ebi&ns=' + slst[j].ns 
                    + '&ac=' + slst[j].ac  + '&dl=full';   
            }
            if(slst[j].db === "RefSeq" ){ 
                rsidx = j; 
                slst[j].proxyUrl = 'proxy?db=ncbi&ns=' + slst[j].ns 
                    + '&ac=' + slst[j].ac  + '&dl=full';
            }
        }

        if( sidx === -1 && upidx > 0 ){
            sidx=upidx;
        }

        if( sidx === -1 && rsidx > 0 ){
            sidx=rsidx;
        }

        if( slst[sidx].seq !== undefined && slst[sidx].seq !== "" ){
            YAHOO.mbi.view.protein.show(sidx);            
            return;
        }
        
        //alert("purl=" + slst[sidx].proxyUrl);

        var proxyCallback = { cache:false, timeout: 5000, 
                              success:YAHOO.mbi.view.protein.setProtSeq,
                              argument: { "sidx": sidx } };
        
        YAHOO.util.Connect.asyncRequest( 'GET', slst[sidx].proxyUrl, 
                                         proxyCallback );
    },

    setProtSeq: function( o ){

        var messages = YAHOO.lang.JSON.parse( o.responseText );
        var alist = null;
        
        var nseq = "";
        
        // get seq from the response
            
        if( messages.summary.attrList !== null ){
            alist =  messages.summary.attrList.attr; 
        }
            
        if( alist !== null ){
            for( var i=0; i<alist.length; i++ ){
                var aac=alist[i].ac;

                if( alist[i].value != null ){
    
                    var aval=alist[i].value.value;
                
                    if( aac === "dip:0008" || aac === "dip:0007" ){
                        nseq = aval;
                        break;
                    }
                }
            }
        }
        
        YAHOO.mbi.view.summary.slst[o.argument.sidx].seq = nseq;
        YAHOO.mbi.view.protein.show({"sidx":o.argument.sidx,"cnt":o.argument.cnt });

    },

    setProtDetailCallback: function( o ){ 

        var base =  YAHOO.mbi.view.summary.base;
        if( base === undefined ){
            return;
        }
            
        var seq = "";
        
        var xide = -1;
            
        //alert("arg: " + o.argument.db + " ::: " + o.argument.ac);
        
        var db = o.argument.db;
        var ns = o.argument.ns;
        var ac = o.argument.ac;
        var xlst = o.argument.xlst;
        var xidx = o.argument.xidx;
        
        if( o.seq === undefined ){
            var messages = YAHOO.lang.JSON.parse( o.responseText );
            var alist = null;
            
            // get seq from the response
            
            if( messages.summary.attrList !== null ){
                alist =  messages.summary.attrList.attr; 
            }
            
            if( alist !== null ){
                for( var i=0; i<alist.length; i++ ){
                    var aac=alist[i].ac;
                    if( alist[i].value !== null ){
                        
                        var aval=alist[i].value.value;
                        
                        if( aac === "dip:0008" ||  aac === "dip:0007"){
                            seq = aval;
                            break;
                        }
                    }
                }
            }             
        }
        
        if( seq !== undefined && seq !== "" ){
            
            var sdf = YAHOO.mbi.view.protein.buildSeqTbl( base );
            
            YAHOO.mbi.seqlib.protview( sdf.vd, seq );
            YAHOO.mbi.seqlib.protcomp( sdf.cd, seq );
            YAHOO.mbi.seqlib.protsumm( sdf.sd, seq, 
                                       {"db":db, "ac":ac,
                                        "xlst":xlst, "xidx":xidx} );
        }
    },
    
    buildSeqTbl: function( base ){
        
        

        var stbl = document.createElement('table');
        YAHOO.util.Dom.addClass(stbl,'seq-info');
        YAHOO.util.Dom.setAttribute( stbl,"cellspacing","0");
        YAHOO.util.Dom.setAttribute( stbl,"cellpadding","5");
        base.appendChild( stbl );
        
        var srow = document.createElement('tr');
        var sdta = document.createElement('td');
        YAHOO.util.Dom.setAttribute( sdta,"colspan","2");
        srow.appendChild(sdta);
        stbl.appendChild(srow);
        
        var vcrow = document.createElement('tr');
        var vdta = document.createElement('td');
        YAHOO.util.Dom.setAttribute( vdta,"valign","top");
        var cdta = document.createElement('td');
        YAHOO.util.Dom.setAttribute( cdta,"valign","top");
        vcrow.appendChild(vdta);
        vcrow.appendChild(cdta);
        stbl.appendChild(vcrow);
        
        base.my = {"sd":sdta, "cd":cdta, "vd":vdta };
        
        return {"sd":sdta, "cd":cdta, "vd":vdta };
    }, 

    getSeqTbl: function( base ){
        return base.my;
    }

};
