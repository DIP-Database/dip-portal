YAHOO.namespace("mbi.view");

YAHOO.mbi.view.evidence = {
	//main calling function
    setEvidenceDetail: function( responseHeader ){
        var evidence = YAHOO.mbi.view.evidence;
        var responseText = YAHOO.lang.JSON.parse( responseHeader.responseText );
        var pubMedID = responseText.summary.xrefList.xref[0].ac;
        var attrValues = evidence.getAttrValues( responseText.summary.attrList.attr );
        var allValues = evidence.getXrefValues( responseText.summary.xrefList.xref, attrValues );
        allValues = evidence.hardCodeSomeValues(allValues);
        
        evidence.addDOMElements( allValues, responseText.summary.ac );
        evidence.addStyle();
    },
    //clean up the data for use
    getAttrValues: function( attr  )
    {
        var evidence = YAHOO.mbi.view.evidence;
        var length = attr.length;
        var cleanData = {};
        for(var i = 0; i < length; i++)
        {
            cleanData[attr[i].name] = attr[i].value.value;
        }
        //hard coded values per request
        if(typeof cleanData['imex curation'] != 'undefined')
        {
            if(cleanData['imex curation'] == 'final')
                cleanData['imex curation'] = 'IMEX final';
            else if(cleanData['imex curation'] == 'none')
                cleanData['imex curation'] = 'MIMIX';
            else
                cleanData['imex curation'] = 'IMEX provisional';
        }
        return cleanData;
    },
    getXrefValues: function( xref , cleanData)
    {
        var evidence = YAHOO.mbi.view.evidence;
        var length = xref.length;
        
        for(var i = 0; i < length; i++)
        {
            if(xref[i].type == 'described-by' && typeof cleanData['described-by'] !== 'undefined')
                cleanData.PMID = cleanData['described-by'];
            if(xref[i].type == 'curated-by' || xref[i].type == "published-by")
                cleanData[xref[i].type] = xref[i].ns;
            else
                cleanData[xref[i].type] = xref[i].ac;
        }
        return cleanData;
    },
    hardCodeSomeValues: function(allValues)
    {
        var map = {
        'interaction detection method':'Experimental Method', 'experiment-scale':'Experiment Scale',
        'supports':'Supported Interaction', 'link-type':'Demonstrated Interaction Type',
        'identical-to':'IMEx Record','author-year':'Data Source', 'curated-by':'Curated by',
        'published-by':'Published by','imex curation':'Curation Level'}

        for(value in map)
        {
                    allValues[map[value]] = allValues[value];
                    delete allValues[value];
        }
        return allValues;
    },
    addDOMElements: function(attrValues, IMExRecordLink)
    {
        var base    = YAHOO.mbi.view.summary.base;
        var evidence = YAHOO.mbi.view.evidence
        var list = document.createElement('ul');
        base.appendChild( list );
        var attrOrder = ['Experimental Method', 'Experiment Scale', 'Supported Interaction',
            'Demonstrated Interaction Type', 'IMEx Record', 'Data Source', 'PMID',
            'Published by', 'Curated by', 'Curation Level'];
        var length = attrOrder.length;
        //makes elements and handles linking needs based on key values
        for(var i = 0; i < length; i++)
        {
            var listItem = document.createElement('li');
            //check to see if links need to be made
            if(attrOrder[i] == 'PMID' || attrOrder[i] == 'IMEx Record' ||
               attrOrder[i] == 'Supported Interaction' && typeof attrValues[attrOrder[i]] != 'undefined' ||
               attrOrder[i] == 'Data Source')
            {
                var link = document.createElement('a');
                if(attrOrder[i] == 'PMID')
                    link.href = 'elink?ns=pubmed&ac=' + attrValues['PMID'];
                else if (attrOrder[i] == 'IMEx Record' && attrValues[i] != undefined)
                    link.href = 'elink?ns=imex&ac=' + IMExRecordLink; 
                else if (attrOrder[i] == 'Supported Interaction')
                    link.href = 'elink?ns=dip&ac=' + attrValues['Supported Interaction']; 
                else if (attrOrder[i] == 'Data Source')
                    link.href = 'elink?ns=dip&ac=' + attrValues['described-by']; 
                link = evidence.addText( link, attrValues[attrOrder[i]] + '');
                listItem.innerHTML = '<strong>' + attrOrder[i] + ': </strong>';
                listItem.appendChild(link);
                list.appendChild(listItem);
            }
            //or just add bold to the key values
            else
            {
                listItem = evidence.addText( listItem, attrValues[attrOrder[i]] + '');
                listItem.innerHTML = '<strong>' + attrOrder[i] + ': </strong>' + listItem.innerHTML;
                list.appendChild( listItem );
            }
            if(i==4)
                listItem.className = 'pad';
            else
                listItem.className = 'group';
                
        }
        return
    },
    //this is to mitigate browser incompatibility
    //firefox does not use innerText
    addText: function ( htmlElement, text )
    {
        if(document.all){
            htmlElement.innerText = text;
            return htmlElement;
        } else{
            htmlElement.textContent = text;
            return htmlElement;
        }
    },
    //this should be placed in css
    addStyle: function()
    {
        var sheet = document.createElement('style');
        sheet.innerHTML = "div#base ul {list-style:none;}" +
                          "li.pad { padding: 15px 0 15px 3px ;}"+
                          "li.group {display: inline-block;"+
                          "padding: 0 9em 0 0  ;}";
        document.body.appendChild(sheet);

    }
};
