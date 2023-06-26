YAHOO.namespace("mbi.view");
 
YAHOO.mbi.view.interaction  = {
	//main callling function
    setAbstract: function( responseHeader ){
 
        var responseText = YAHOO.lang.JSON.parse( responseHeader.responseText );
        var interaction = YAHOO.mbi.view.interaction;
        var xref = responseText.summary.xrefList.xref;
        var part = responseText.summary.partList.part;
        //first table datasource and table creation
        var participantsYUI = interaction.getParticipantDataYUI(part);
        var participantTable = interaction.makeParticipantsTable( participantsYUI );
        //get cleaner data then datasource then make table
        var allValues = interaction.getEvidenceData(xref);
        var evidenceDatasource = interaction.getEvidenceDatasource(allValues);
        var evidenceTable = interaction.makeEvidenceTable(evidenceDatasource);
        
    },
    //this is for the first table
    getParticipantDataYUI: function(part)
    {
		//this maps uniprot refseq and entrezgene better
        part = YAHOO.mbi.view.interaction.getIdData(part);

        var participantDataSource = new YAHOO.util.LocalDataSource(part );
        participantDataSource.responseType = YAHOO.util.LocalDataSource.TYPE_JSON; 
        participantDataSource.responseSchema = {
            resultsList: "proteins",
            fields : [
				//values in the main section of the table
                { key: "node.label" },
                { key: "node.name" },
                { key: "node.ac" },
                { key: "node.xrefList.xref[0].node.label" },
                { key: "node.xrefList.xref[0].node.name" },
                //values in the expansion area of the table
                { key: "node.uniprot" }, 
                { key: "node.refseq" }, 
                { key: "node.entrezgene" } 
            ],
        };
        return participantDataSource;
    },
    /******************************************************************
	 * this returns a cleaner map for the three expanded
	 * variables or sets the value to 'N/A' so that there is
	 * always a value
	 *****************************************************************/
    getIdData: function(part)
    {
        var interaction = YAHOO.mbi.view.interaction;
        var mapIndex;
        var partLength = part.length;
        //dip graph only works currently with a 2 proteins
        if(partLength > 2 )
			interaction.hideDipGraph();
        //for each protein 
        for(var i = 0; i < partLength; i++)
        {
			var map = ['uniprot','refseq','entrezgene'];
            var node = part[i].node;
            var xref = node.xrefList.xref;
            var xrefLength = xref.length; 
            
            for(var j = 0; j < xrefLength; j++)
            {
                if( (mapIndex = map.indexOf(xref[j].ns)) >= 0)
                {
                    node[xref[j].ns] = '<a href="elink?ns=' + map[mapIndex] + 
                    '&ac=' + xref[j].ac +'">' + xref[j].ac + '</a>';
                    map.splice(mapIndex,1);
                }
            }
            for(var k = 0; k < map.length; k++)
                node[map[k]] = 'N/A';
        }
        return {"proteins": part};

    },
    hideDipGraph: function()
    {
		var listOfElements = YAHOO.util.Dom.getElementsByClassName('data_graphs');
		if(listOfElements.length == 1)
			listOfElements[0].style.display = 'none';
	},
 
    makeParticipantsTable: function(participants)
    {
        var expansionFormatter  = function(el, oRecord, oColumn, oData)
        {
            var cell_element    = el.parentNode;

            //Set trigger
            if( oData )
            { //Row is closed
                YAHOO.util.Dom.addClass( cell_element,
                    "yui-dt-expandablerow-trigger" );
            }
        };

        var partDataTable = new YAHOO.widget.RowExpansionDataTable(
                "base", //class of DOM to stick table into
                [//main table config
                    {
                        label:"",
                        formatter:YAHOO.widget.RowExpansionDataTable.formatRowExpansion
                    },
                    {
                        key:"node.ac",
                        label:"Id",
                        sortable:true
                    },
                    {
                        key:"node.name",
                        label:"Name",
                        sortable:true
                    },
                    {   
                        key:"node.label",
                        label:"Label",
                        sortable:true
                    },
                    {   
                        key:"node.xrefList.xref[0].node.label",
                        label:"Species",
                        sortable:true
                    }   
                        
                ],
                participants,//datasource
                    { rowExpansionTemplate : //expansion code
                    '<ul>' +
                    '<li><strong>Uniprot Id: </strong>{node.uniprot}</li>' +
                    '<li><strong>RefSeq Id: </strong>{node.refseq}</li>' +
                    '<li><strong>Entrezgene Id: </strong>{node.entrezgene}</li></ul>',
                   /* '<li><strong>Uniprot Id: </strong><a href="elink?ns=uniprot&ac={node.uniprot}">{node.uniprot}</a></li>' +
                    '<li><strong>RefSeq Id: </strong><a href="elink?ns=refseq&ac={node.refseq}">{node.refseq}</a></li>' +
                    '<li><strong>Entrezgene Id: </strong><a href="elink?ns=entrezgene&ac={node.entrezgene}">{node.entrezgene}</a></li></ul>',*/
                    caption:"Participants" }
                );

        //Subscribe to a click event to bind to
        partDataTable.subscribe( 'cellClickEvent',
            partDataTable.onEventToggleRowExpansion );

        return partDataTable
    },
    //uses evidence-summary.js code to get cleaner values
    getEvidenceData: function(xref)
    {
		var interaction = YAHOO.mbi.view.interaction;
        var evidence = YAHOO.mbi.view.evidence;
        var length = xref.length;
        var allValues = new Array();

        for(var i = 0; i < length; i++)
        {
            var attrValues = evidence.getAttrValues(xref[i].node.attrList.attr);
            allValues[i]  = evidence.getXrefValues(xref[i].node.xrefList.xref, attrValues);
            
            allValues[i].id = xref[i].ac;   
            allValues[i] = interaction.hardCodeSomeValues(allValues[i]);         
        }
        
        return allValues
    },
    //maps yui table (json as well?), invalid values to values that work
    hardCodeSomeValues: function(allValues)
    {
        var map = {
        'interaction detection method':'method', 'experiment-scale':'scale',
        'link-type':'link','described-by':'described',
        'author-year':'author', 'curated-by':'curated',
        'published-by':'published','identical-to':'imex','imex curation':'curation'}

        for(value in map)
        {
                    allValues[map[value]] = allValues[value];
                    delete allValues[value];
        }
        return allValues;
    },
    //for second table
    getEvidenceDatasource: function ( allValues )
    {
        evidenceData  = {"data": allValues};
        var evidenceDataSource = new YAHOO.util.LocalDataSource( evidenceData  );
        evidenceDataSource.responseType = YAHOO.util.LocalDataSource.TYPE_JSON; 
        evidenceDataSource.responseSchema = {
            resultsList: "data",
            fields : [
                { key: "id" },           
                { key: "method" },
                { key: "PMID" },
                { key: "imex"  },
                { key: "author" },
                { key: "curated" },
                { key: "described" }, 
                { key: "scale" }, 
                { key: "link" },
                { key: "published" },
                { key: "curation" },
                { key: "supports" } 
            ],
        };
        return evidenceDataSource;
    },
    //second table
    makeEvidenceTable: function( EvidenceDatasource )
    {

        var div = document.createElement('div');
        div.id = 'evidence';
        var base    = YAHOO.mbi.view.summary.base;
        base.appendChild(div);

        var PMIDFormatter = function(elLiner, oRecord, oColumn, oData)
        {
            elLiner.innerHTML ='<a href="elink?ns=pubmed&ac=' + oData + '">'+ oData + '</a>';
        }
        var defaulter = function( elLiner, oRecord, oColumn, oData ){
            elLiner.innerHTML = oData || 'N/A';
            if(elLiner.innerHTML != 'N/A')
                elLiner.innerHTML ='<a href="elink?ns=imex&ac=' + oData + '">'+ oData + '</a>';
            
        }
        var expansionFormatter  = function(el, oRecord, oColumn, oData)
        {
            var cell_element    = el.parentNode;

            //Set trigger
            if( oData )
            { //Row is closed
                YAHOO.util.Dom.addClass( cell_element,
                    "yui-dt-expandablerow-trigger" );
            }
        };

        var evidenceDataTable = new YAHOO.widget.RowExpansionDataTable(
                "evidence",
                [
                    {
                        label:"",
                        formatter:YAHOO.widget.RowExpansionDataTable.formatRowExpansion
                    },
                    {
                        key:"id",
                        label:"Id",
                        sortable:true
                    },
                    {
                        key:"method",
                        label:"Method",
                        sortable:true
                    },
                    {   
                        key:"PMID",
                        label:"Data Source",
                        sortable:true,
                        formatter:PMIDFormatter
                    },
                    {   
                        key:"imex",
                        label:"IMEX",
                        sortable:true,
                        formatter:defaulter
                    }
                     
                        
                ],
                EvidenceDatasource,
                    { rowExpansionTemplate :
                    '<ul>'+
					'<li><strong>Experiment Scale: </strong>{scale} </li>' +
					'<li><strong>Supported Interaction: </strong><a href="elink?ns=dip&ac={supports}">{supports}</a></li>' +
					'<li><strong>Data Source: </strong><a href="elink?ns=dip&ac={described}">{author}</a></li>' +
					'<li><strong>Curated By: </strong>{curated}</li>' +
					'<li><strong>Described By: </strong>{described}</li>' +
					'<li><strong>Demonstrated Interaction Type: </strong>{link}</li>' +
					'<li><strong>Published By: </strong>{published}</li>' +
					'<li><strong>Curation Level: </strong>{curation}</li></ul>',
                    caption:"Evidence" }
                );

        //Subscribe to a click event to bind  to
        evidenceDataTable.subscribe( 'cellClickEvent',
            evidenceDataTable.onEventToggleRowExpansion );
        
        return evidenceDataTable
    }
}; 

