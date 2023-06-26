/* Table formatting */

YAHOO.namespace("mbi.view");

YAHOO.mbi.view.formatter = {
    
    toggleFormatter: function(elLiner, oRecord, oColumn, oData) 
    {
        var cutoff = oColumn.toggleCutoff;
        var concatTo = oColumn.toggleConcatTo;
        var partnerListLinks = oData.split(",");
        var html = '';
        
        if( partnerListLinks.length > cutoff )
        {
            html = '<div class="toggleClosed"></div><div class="show">';
            for(var i = 0; i < concatTo; i++)
            {
                html += partnerListLinks[i];
                if(i < concatTo - 1)
                    html += ','
                else
                    html += ' ...'
            }
            html += '</div><div class="hide">';
            html += oData;
            html += '</div>';
            
            elLiner.innerHTML = html;
            elLiner.getElementsByClassName("toggleClosed")[0].onclick = YAHOO.mbi.view.formatter.toggle;
        }
        else
            elLiner.innerHTML = oData;
    },
    toggle: function()
    {
        var hiding  = this.parentElement.getElementsByClassName("show")[0];
        var showing = this.parentElement.getElementsByClassName("hide")[0];
        hiding.className = "hide";
        showing.className = "show";
        if(this.className == "toggleOpen")
            this.className = "toggleClosed";
        else
            this.className = "toggleOpen";
    },
    sigFigFormatter: function(elLiner, oRecord, oColumn, oData) 
    {
        var sigfigs = oColumn.sigfigs;
        var score = parseFloat(oData);
        if(score < 1)
            score = score.toPrecision( sigfigs );
        else
            score = score.toFixed( sigfigs );
        elLiner.innerHTML = score;
    },
    detailsFormatter: function(elLiner, oRecord, oColumn, oData) 
    {
        var formatter = YAHOO.mbi.view.formatter;
        elLiner.innerHTML = oData;
        var aLink = elLiner.children[0];
        aLink.innerText = oColumn.linkText;
        aLink.onclick = formatter[oColumn.details];
        aLink.columnDetails = oColumn;
        aLink.recordDetails = oRecord.getData();
        
    },
    alignmentDetails: function(test)
    {
        /*
        var displayOrder = ["alignHit","alignMid","alignQuery","hitFrom",
                            "hitLength","hitTo","queryFrom","queryLength",
                            "queryTo"];
                            * */
        var windowTitle = this.columnDetails.linkText;
       
        var stbl = document.createElement('div');
        
        YAHOO.mbi.modal.alignment({ "title":windowTitle,"body":stbl });
        
        YAHOO.mbi.view.formatter.protview(stbl, this.recordDetails);
    },
    protview: function( base, dataset ) {
        
        var formatter = YAHOO.mbi.view.formatter;

        var alignQuery = dataset["alignQuery"];
        var alignMid = dataset["alignMid"]; 
        var alignHit = dataset["alignHit"]; 
        var hitFrom = dataset["hitFrom"]; 
        var queryFrom = dataset["queryFrom"]; 
        
        var segmentSize = 10;
        var rowSize = 6;
        
        var numSegments = alignQuery.length/segmentSize;
        var numRows     = numSegments/rowSize;
        
        var alignmentSubject = [alignQuery, alignHit];
        var alignmentStarts  = [parseInt (queryFrom), parseInt(hitFrom)];
        var label = ["Query", "Hit &nbsp;"];

        var div = document.createElement('div');
        
        formatter.addAlignmentHeader(div,dataset);
        
        base.appendChild( div );

        var table = document.createElement('table');
        YAHOO.util.Dom.setAttribute( table,"cellspacing","5");
        YAHOO.util.Dom.addClass(table,"seq-table-seg");
        base.appendChild( table );
        
        for( var ri = 0; ri < numRows; ri++ )
        {
            for(var i = 0; i < alignmentSubject.length; i++)
            {
                var sequence     = alignmentSubject[i];
                var sequenceNum  = alignmentStarts[i];
                var subSequences = [''] ;   
                var middleRow    = [''] ;   
                var sequenceCounters =  ['',''];
                var skippingCharacters = 0;
                var buffer = sequenceNum;
                
                if(i == 0)
                {
                   
                    var numtr = document.createElement( 'tr' );
                    table.appendChild( numtr ); 
                }
                else
                {
                    var midRow = document.createElement( 'tr' );
                    table.appendChild( midRow );
                    for( var mi=0; mi<rowSize; mi++)
                    {
                        var midSequence = alignMid.substring(   ri*segmentSize*rowSize + mi*segmentSize,
                                                                ri*segmentSize*rowSize + (mi+1)*segmentSize);
                        middleRow[middleRow.length] = midSequence.replace(/ /gi, "&nbsp;");   
                    }
                    
                    formatter.populateRow(middleRow, midRow);
                }
                
                var tr = document.createElement( 'tr' );
                table.appendChild( tr );
                if( ri*segmentSize*rowSize+1 <= sequence.length ){
                    subSequences[0] = label[i] + ":" + sequenceNum ;
                }
               
                for( var si=0; si<rowSize; si++){
                    var start = ri*rowSize*segmentSize + si*segmentSize;
                    var end   = ri*rowSize*segmentSize + (si+1) * segmentSize;
                    
                    var subSec = sequence.substring(start,end);
                    subSequences[subSequences.length] = subSec;
                    //This look for the character "-" 
                    //we will skip those in sequence numbers.
                    if( subSequences[subSequences.length -1 ].length > 0)
                    {
                        var skippingCharacters = (subSec.match(/-/g)||[]).length;
                        buffer = buffer + segmentSize - skippingCharacters;
                        sequenceCounters[sequenceCounters.length] = buffer;
                        if(subSec[0] === "-")
                            sequenceCounters[sequenceCounters.length - 2] = "";
                            
                    }
                }
                
                if(i == 1)
                {
                    var numtr = document.createElement( 'tr' );
                    table.appendChild( numtr ); 
                    YAHOO.util.Dom.addClass(numtr,"line-end");
                }
                
                alignmentStarts[i] = sequenceCounters[sequenceCounters.length - 1];
                sequenceCounters.splice(sequenceCounters.length - 1,1);
                formatter.populateRow(subSequences, tr);
                
                formatter.populateRow(sequenceCounters, numtr );
            }
        }
        
    },
    addAlignmentHeader: function( element, dataset)
    {
        var formatter = YAHOO.mbi.view.formatter;
        
        formatter.sigFigFormatter(element, "", {sigfigs:3}, dataset["eVal"]) 
        var eVal = element.innerHTML;
        
        formatter.sigFigFormatter(element, "", {sigfigs:3}, dataset["blastRaw"])
        var blastRaw = element.innerHTML;
        
        formatter.sigFigFormatter(element, "", {sigfigs:3}, dataset["blastBit"]) 
        var blastBit = element.innerHTML;
        
        element.innerHTML = "E-value: " + eVal + " &nbsp;&nbsp;" +
                        "Blast Raw Score: " + blastRaw + " &nbsp;&nbsp;" +
                        "Blast Bit Score: " + blastBit;
                        
        YAHOO.util.Dom.addClass(element,"align-head");
    },
    populateRow: function( rowElements, tr)
    {
        for(var i = 0 ; i < rowElements.length;i++)
        {
            var td = document.createElement( 'td' );
            tr.appendChild(td);
            td.innerHTML = rowElements[i] + "";
        }
    },
    //--------------------------
    // Add the custom formatters 
    //--------------------------
    formatterInit: function()
    {
        var Formatters = YAHOO.widget.DataTable.Formatter;
        Formatters.toggleFormatter = YAHOO.mbi.view.formatter.toggleFormatter;
        Formatters.sigFigFormatter = YAHOO.mbi.view.formatter.sigFigFormatter;
        Formatters.detailsFormatter = YAHOO.mbi.view.formatter.detailsFormatter;
    }
};
