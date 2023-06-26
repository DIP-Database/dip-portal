YAHOO.namespace("mbi.view");

YAHOO.mbi.view.article  = {

    setAbstract: function( responseHeader ){

        var responseText = YAHOO.lang.JSON.parse( responseHeader.responseText );
        var pubMedID = responseText.summary.xrefList.xref[0].ac;
        var proxyUrl = 'proxy?db=ncbi&ns=pmid&dl=full&ac=' + pubMedID;

        var proxyCallback = { cache:false, timeout: 5000,
                              success:YAHOO.mbi.view.article.createAbstract,
                              /*argument: { "sidx": sidx } */};
        YAHOO.util.Connect.asyncRequest( 'GET', proxyUrl,
                              proxyCallback );
    },
    createAbstract: function( proxyResponse )
    {
        var article = YAHOO.mbi.view.article;
        var responseText  =  YAHOO.lang.JSON.parse(proxyResponse.responseText);
      
        var cleanData = article.getKeyValues( responseText );
        article.addDOMElements( cleanData );
    },
    //get the key:value pairs from the response text
    getKeyValues: function( responseText )
    {
        var article = YAHOO.mbi.view.article;
        var articleData = responseText.summary.attrList.attr;
        var length = articleData.length;
        var cleanData = {};
        for(var i = 0; i < length; i++)
        {
            cleanData[articleData[i].name] = articleData[i].value.value;
        }

        cleanData.pubDate = article.normalizePubDate(cleanData['publication-date']);
        cleanData.PMID = responseText.summary.label;
        return cleanData;
    },
    //takes the odd formatting I recieve the date from pubmed and turns it into a 
    //string ex: 2006-04-4 = " April 4 " year is in another field and date is missing
    //randomly so I check for undefined.
    normalizePubDate: function( pubDate )
    {
        if(typeof pubDate != 'undefined')
        {
            var monthNames = [ "January", "February", "March", "April", "May", "June",
             "July", "August", "September", "October", "November", "December" ];
            var baseTen = 10;
            var month = monthNames[parseInt(pubDate.substr(5, 2), baseTen) - 1 ];
            var day = pubDate.substr(8, 2); 
            return ' ' + month + ' ' + day;
        }
        // I return a null string because the pubDate will be used in the Dom creation
        //this way it does not affect the string and I dont have to check for it.
        return '';
    },
    //because firefox doesn't support innerText, if we have to, we'll use textContet
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
    addDOMElements: function ( cleanData )
    {
        var article = YAHOO.mbi.view.article;
        var base    = YAHOO.mbi.view.summary.base;
        var journalP = document.createElement('p');
        journalP = article.addText(journalP,
                             cleanData['journal-title'] + '. ' + 
                             cleanData.volume + '(' +  
                             cleanData.issue + '): ' + 
                             cleanData.pages + ' (' +
                             cleanData.year +  ')' +
                             cleanData.pubDate );
        base.appendChild( journalP );

        var titleH1 = document.createElement('h1');
        titleH1 = article.addText(titleH1, cleanData.title);
 
        base.appendChild( titleH1 );

        var authorsP = document.createElement('p');
        authorsP = article.addText(authorsP,  cleanData.authors + '.');
        base.appendChild( authorsP );

        var pubMedIDP = document.createElement('p');
        pubMedIDP = article.addText(pubMedIDP,  'PMID: ' + cleanData.PMID);
        var link = document.createElement('a');
        link.href = 'elink?ns=pubmed&ac=' + cleanData.PMID;
        link.appendChild( pubMedIDP );
        base.appendChild( link );
 
        var abstractH2 = document.createElement('h2');
        abstractH2 = article.addText(abstractH2, 'Abstract');
        base.appendChild( abstractH2 );
     
        var abstractP = document.createElement('p');
        abstractP = article.addText(abstractP, cleanData.abstract);
        base.appendChild( abstractP );
    }
};
