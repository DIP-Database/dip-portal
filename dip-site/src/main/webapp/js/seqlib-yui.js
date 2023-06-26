/* sequence rendering */

YAHOO.namespace( "mbi.seqlib" );

YAHOO.mbi.seqlib = {

    // After: Voet & Voet; Biochemistry, 2nd ed, J Wiley & Sons, 1995, pp58 

    mwavg: function( seq ){
      
        var MW = { "A": 71.1, "C":103.1, "D":115.1, "E":129.1, "F":147.2,
                   "G": 57.0, "H":137.1, "I":113.2, "K":128.2, "L":113.2,
                   "M":131.2, "N":114.1, "P": 97.1, "Q":128.1, "R":156.2,
                   "S": 87.1, "T":101.1, "V": 99.1, "W":186.2, "Y":163.2};
        
        var mw = 18.0;

        for( var i=1; i<seq.length; i++ ){
            mw += MW[seq[i]];
        }
        return mw;
    },

    pcomp: function( seq ){
        
        var res = "ACDEFGHIKLMNPQRSTVWY";
        var comp = {}; 
    

        for( var j=0; j<res.length; j++ ){
            comp[res[j]] = {"n":0};
        }

        for( var i=0; i<seq.length; i++ ){
            comp[seq[i]].n++;
        }
        
        for( var j=0; j<res.length; j++ ){  
            if( comp[res[j]].n > 0 ){
                comp[res[j]].f = 100.0 * comp[res[j]].n / seq.length;
            } else {
                comp[res[j]].f = 0.00;
            }
        }
        return comp;
    },

    cofext: function( comp ){

        var ec_trp = 5700;
        var ec_tyr = 1300;
        
        var ec = comp["W"].n* ec_trp 
            + comp["Y"].n* ec_tyr; 
        
        return ec;
    },

    getpi: function( comp ){
        
        // After: Sillero, Maldonado; Comp Biol Med,36:157 (2006)
        // with modifications according to http://isoelectric.ovh.org/   
        // Alternate pK values according EMBOSS: 
        //   http://emboss.sourceforge.net/apps/release/6.3/emboss/apps/iep.html
          
       var pkp = [ {"id":"H", "pk": 6.4},    //  6.5
                    {"id":"K", "pk":10.3},    // 10.8
                    {"id":"R", "pk":12.0}];   // 12.5
        
        var pkn = [ {"id":"D" ,"pk": 4.0},    //  3.9
                    {"id":"E" ,"pk": 4.5},    //  4.1
                    {"id":"C" ,"pk": 9.0},    //  8.5
                    {"id":"Y" ,"pk":10.0} ];  // 10.1
            
        var nn = [ {"n":1, "pk":3.2}];  // C-terminal COOH  //  3.6
        var np = [ {"n":1, "pk":8.2}];  // N-terminal NH2   //  8.6
        
        for( var i=0; i< pkp.length; i++ ){
            if( comp[pkp[i].id].n > 0 ){
                np.push( {"n":comp[pkp[i].id].n, "pk":pkp[i].pk} );
                //alert(pkp[i].id + " :: " + comp[pkp[i].id].n + " :: " + pkp[i].pk);
            }
        }

        for( var j=0; j< pkn.length; j++ ){
            if( comp[pkn[j].id].n > 0 ){
                nn.push( { "n":comp[pkn[j].id].n, "pk":pkn[j].pk} );
                //alert(pkn[j].id + " :: " + comp[pkn[j].id].n + " :: " + pkn[j].pk);
            }
        }
        
        var dpi = 0.01;
        
        var pilo = 1.0;
        var pihi = 13.0;
        var picr = 7.0;
        
        var q = this.getcharge( picr, nn, np );
        var n=0;
        while( pihi - pilo > dpi ){  
            q = this.getcharge( picr, nn, np );

            if( q > 0 ) {
                pilo = picr;
            } else {
                pihi = picr; 
            }
            picr = (pihi+pilo)/2;
            var diff= pihi-pilo;
            
            n++;
            if( n>20 ) {break;}
        }
        
        return (pihi+pilo)/2;
        
    },

    getcharge: function( ph, nn, np ){
        
        var qn=0;
        var qp=0;
        
        for(var i=0; i <nn.length; i++ ){
            qn = qn - nn[i].n/(1 + Math.exp( (nn[i].pk-ph)*Math.LN10 ));
        }

        for(var j=0; j <np.length; j++ ){
            qp = qp + np[j].n/(1 + Math.exp( (ph-np[j].pk)*Math.LN10) );
        }

        return qp+qn;
    },

    fastaStr: function( db, ac, seq){

        var length = 80;
        
        var fasta=">"+ db + "|"+ac+"\n";

        for(var i=0; i<seq.length; i+=length){
         fasta += seq.substring(i,i+length)+"\n";    
        }
        
        return fasta;
    },

    fasta: function(){ return "";},
    
    protsumm: function( base, seq, dbref ) {

        var stbl = document.createElement('table');
        YAHOO.util.Dom.addClass(stbl,'seq-summ-tbl');
        YAHOO.util.Dom.setAttribute( stbl,"width","100%");
        YAHOO.util.Dom.setAttribute( stbl,"cellspacing","0");
        //base.appendChild( stbl );
        
        if( base.firstChild !== null ){
            base.replaceChild(stbl,base.firstChild);   
        } else{
            base.appendChild( stbl );
        }
        
        var db=dbref.slst[dbref.sidx].db;
        var ac=dbref.slst[dbref.sidx].ac;

        var sel = document.createElement( 'tr' );
        YAHOO.util.Dom.addClass(sel,"seq-summ-tbl-head");
        stbl.appendChild( sel );

        YAHOO.mbi.seqlib.fasta = function(){
            return "<pre>"
                + YAHOO.mbi.seqlib.fastaStr( db, ac, seq ) + "\n</pre>";
        }();
        
        // FASTA sequence
        //---------------

        var fel = document.createElement( 'td' );
        YAHOO.util.Dom.addClass( fel, "seq-summ-tbl-lfld");
        fel.innerHTML ='<a href=""'
            + ' onclick="YAHOO.mbi.modal.window({ \'title\':\'FASTA\',\'body\':YAHOO.mbi.seqlib.fasta}); return false;">FASTA</a>';
        sel.appendChild( fel );

       
        // Sequence Length
        //----------------

        var fel = document.createElement( 'td' );
        YAHOO.util.Dom.addClass( fel, "seq-summ-tbl-lfld");
        fel.innerHTML='<b>Length:</b> '+ seq.length;
        sel.appendChild( fel );

        // Molecular Weight
        //-----------------

        var fel = document.createElement( 'td' );
        YAHOO.util.Dom.addClass( fel, "seq-summ-tbl-lfld");

        var mw = YAHOO.mbi.seqlib.mwavg(seq)/1000;
        
        fel.innerHTML='<b>MW:</b> ' + mw.toFixed(2) + ' [kD]';
        sel.appendChild( fel );

        // Sequence Source
        //----------------

        var fel = document.createElement( 'td' );
        YAHOO.util.Dom.addClass( fel, "seq-summ-tbl-rfld");
        
        if( dbref.sidx === undefined ) {
            fel.innerHTML='<b>Seq Src:</b> ' + dbref.slst[0].db;
        } else {
       
            var inh='<b>Seq Src:</b> [';
            //alert( "seqsrc: " + dbref.sidx + "::" + dbref.slst[0] + "::" + dbref.slst[1] );            
            var xref ="";
            for( var i=0; i< dbref.slst.length; i++){      

                if(i>0 || dbref.slst[i].seq !== "" ){   
                    if( i !== dbref.sidx ){
                        //var href="elink?ns="+dbref.slst[i].ns+"&ac="+dbref.slst[i].ac; 
                    
                        xref += '<a href="" onclick="try{ YAHOO.mbi.view.protein.show({sidx:' 
                            + i + '}); } catch (x) { alert(x);} return false;"';

                        if(i< dbref.slst.length-1){
                            xref += ">" + dbref.slst[i].db + "</a>,";
                        } else {
                            xref += ">" + dbref.slst[i].db + "</a>]";
                        }
                    } else {
                        xref += '<b>'+dbref.slst[i].db+'</b>';
                        if(i< dbref.slst.length-1){
                            xref += ",";
                        } else {
                            xref += "]";    
                        }
                    }
                }
            }
            fel.innerHTML = inh + xref;

            //alert( "done: " + inh + xref);

        }
        sel.appendChild( fel );
    },


    rcmp: function( base, comp, res, rlong) {
        
        var rel = document.createElement( 'tr' );
        YAHOO.util.Dom.addClass(rel,"seq-comp-tbl-head");
        base.appendChild( rel );
        
        var hel = document.createElement( 'td' );
        YAHOO.util.Dom.addClass( hel, "seq-comp-tbl-vhead" );
        YAHOO.util.Dom.setAttribute( hel, "nowrap","" );
        hel.innerHTML = "<b>" + rlong + ":</b> ";
        rel.appendChild( hel );
        
        var fel = document.createElement( 'td' );
        YAHOO.util.Dom.addClass( fel, "seq-comp-tbl-rvbody" );
        YAHOO.util.Dom.setAttribute( fel, "nowrap","" );
        fel.innerHTML = comp[res].f.toFixed(1) + "%" ;
        rel.appendChild( fel );
        
        var nel = document.createElement( 'td' );
        YAHOO.util.Dom.addClass( nel, "seq-comp-tbl-lvbody" );
        YAHOO.util.Dom.setAttribute( nel, "nowrap","" );
        nel.innerHTML = "(" + comp[res].n +")";          
        rel.appendChild( nel );
    },

    protcomp: function( base, seq ) {
        
        var comp = YAHOO.mbi.seqlib.pcomp(seq);
        
        var stbl = document.createElement('table');
        YAHOO.util.Dom.addClass(stbl,'seq-comp-tbl');
        YAHOO.util.Dom.setAttribute( stbl, "width","100%");
        YAHOO.util.Dom.setAttribute( stbl, "cellspacing","0");

        if(base.firstChild !== null ){
            base.replaceChild( stbl, base.firstChild );   
        } else{
            base.appendChild( stbl );
        }
        
        var rhead = document.createElement('tr');
        stbl.appendChild( rhead );

        var dhead = document.createElement('td');
        YAHOO.util.Dom.addClass(dhead,'seq-comp-tbl-shead');
        YAHOO.util.Dom.setAttribute( dhead, "colspan","3");
        dhead.innerHTML="<b>Composition</b>";
        rhead.appendChild( dhead );
        
        this.rcmp( stbl, comp, "M", "Met");
        this.rcmp( stbl, comp, "C", "Cys");        
        this.rcmp( stbl, comp, "W", "Trp");
        this.rcmp( stbl, comp, "F", "Phe");
        
        var rphead = document.createElement('tr');
        stbl.appendChild( rphead );
        
        var dphead = document.createElement('td');
        YAHOO.util.Dom.addClass( dphead,'seq-comp-tbl-shead');
        YAHOO.util.Dom.setAttribute( dphead, "colspan","3");
        dphead.innerHTML="<b>Properties</b>";
        rphead.appendChild( dphead );
        
        // extinction coefficient

        var cofext = this.cofext( comp )/ this.mwavg( seq );

        var rv_ec = document.createElement('tr');
        stbl.appendChild( rv_ec );
        
        var dv_ec = document.createElement('td');
        YAHOO.util.Dom.addClass( dv_ec,'seq-comp-tbl-cvbody' );
        YAHOO.util.Dom.setAttribute( dv_ec, "colspan", "3" );
        dv_ec.innerHTML = "<b>OD<sub>280</sub>:</b> "+
            cofext.toFixed(2)+ " [mg<sup>-1</sup>]";
        rv_ec.appendChild( dv_ec );
        
        // isoelectric point
        
        var pi =  this.getpi( comp ).toFixed(2);
        
        var rv_pi = document.createElement('tr');
        stbl.appendChild( rv_pi );
        
        var dv_pi = document.createElement('td');
        YAHOO.util.Dom.addClass( dv_pi,'seq-comp-tbl-cvbody' );
        YAHOO.util.Dom.setAttribute( dv_pi, "colspan", "3" );
        dv_pi.innerHTML = "<b>pI:</b> "+ pi;
        rv_pi.appendChild( dv_pi );
        
    },
    
    protview: function( base, seq ) {

        var segment = 10;
        var rowsize = 6;
        
        var smax=seq.length/segment;
        var rmax=smax/rowsize;

        var stbl = document.createElement('table');
        YAHOO.util.Dom.addClass(stbl,"seq-table");
        YAHOO.util.Dom.setAttribute( stbl,"cellspacing","0");

        if( base.firstChild !== null ){
            base.replaceChild( stbl, base.firstChild );   
        } else{
            base.appendChild( stbl );
        }
        
        var bst=0; 
        var ben=segment;

        for( var ri=0; ri<rmax; ri++ ){

            var hel = document.createElement( 'tr' );
            YAHOO.util.Dom.addClass(hel,"seq-table-head");
            stbl.appendChild( hel );
            var chel = document.createElement( 'td' );
            YAHOO.util.Dom.addClass(chel,"seq-table-chead");
            chel.innerHTML="&nbsp;";
            hel.appendChild( chel );
            
            for( var si=0; si<rowsize; si++){

                var chel = document.createElement( 'td' );
                YAHOO.util.Dom.addClass(chel,"seq-table-chead");
                hel.appendChild( chel );
            
                if( si > 0 && ri*segment*rowsize + si*segment+1 > 1 &&
                    ri*segment*rowsize + si*segment+1 <= seq.length ){
                        chel.innerHTML = ri*segment*rowsize + si*segment+1;
                    } else {
                        chel.innerHTML = "&nbsp;";
                    }
            }
            
            var rel = document.createElement( 'tr' );
            YAHOO.util.Dom.addClass(rel,"seq-table-row");
            stbl.appendChild( rel );

            var rhdel = document.createElement( 'td' );
            YAHOO.util.Dom.addClass( rhdel, "seq-table-rhead" );
            
            if( ri*segment*rowsize+1 <= seq.length ){
                rhdel.innerHTML = ri*si*segment+1;
            } else {
                rhdel.innerHTML="&nbsp;";    
            }
            rel.appendChild(rhdel);
            
            for( var si=0; si<rowsize; si++){
                var sel = document.createElement( 'td' );
                YAHOO.util.Dom.addClass(sel,"seq-table-seg");
                rel.appendChild(sel);
                var segseq = seq.substring(bst,bst+segment);
                bst=bst+segment;
                ben=ben+segment;
                if( segseq.length>0 ){
                 sel.innerHTML=segseq;   
                }
            }
        }
    }
};

