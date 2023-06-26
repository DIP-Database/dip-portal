YAHOO.namespace("mbi.view");

YAHOO.mbi.view.querySummary = { 
	
	setSummary: function(query, resultCounts, element )
	{
		var h2 = '<h2>Query for "' + query + '" returned:</h2>';
		var tabNames = ['Proteins', 'Interactions', 'Experiments', 'Articles'];
		var ul = '<ul>';
		for(var i = 0; i < resultCounts.length; i++)
		{
			ul += '<li>' + resultCounts[i].toString() + ' ' + tabNames[i] + '</li>';
		}
		ul += '</ul>';
		element.setAttribute('valign','top')
		element.innerHTML = h2 + ul;
	}
};
