package edu.ucla.mbi.dip;

/* =============================================================================
 # $Id:: DownloadContext.java 3609 2014-03-15 18:51:56Z lukasz                 $
 # Version: $Rev:: 3609                                                        $
 #==============================================================================
 #                                                                             $
 # DownloadContext: JSON-based file download configuration                     $
 #                                                                             $
 #     TO DO:                                                                  $
 #                                                                             $
 #=========================================================================== */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import java.util.*;
import java.util.regex.*;
import java.io.*;
import org.json.*;

import edu.ucla.mbi.util.context.JsonContext;
 
public class DownloadContext extends JsonContext {
    
    public Map getData( String datasetName ) {
        
        List dtsList = (List) getJsonConfig().get( "dataset" );
        int dts=-1;
        for( ListIterator ii = dtsList.listIterator(); ii.hasNext();){
            
            Map iid = (Map) ii.next();
            if( datasetName.equals( (String) iid.get("id") ) && 
                datasetName.equals( (String) iid.get("id") )){
                dts = ii.previousIndex();
                break;
            }
        }
        
        if( dts != -1 ){
            return getData( dts, null );
        } else {
            return null;
        }
    }

    public Map getData( int dts, List pane ) {

	//----------------------------------------------------------------------

	Log log = LogFactory.getLog( this.getClass() );
	log.info( "DownloadContext.getData" );
	
	log.info( " dataset #" + dts );
	if ( pane != null ) {
	    log.info( " tbs=" + pane.get(0) + ":" +pane.get(1) );
	}
	
	Map datasetConfig = 
	    (Map) ((List) getJsonConfig().get( "dataset")).get( dts );

	String dtsId = (String) datasetConfig.get( "id" );
	String label = (String) datasetConfig.get( "label" );
	String header = (String) datasetConfig.get( "header" );
	String comment = (String) datasetConfig.get( "comment" );
	String logFile = (String) datasetConfig.get( "log" );
	String filter = (String) datasetConfig.get( "filter" );

	List tab0lst = (List) datasetConfig.get( "tab0lst" );
	Map tab0lbl = (Map) datasetConfig.get( "tab0lbl" );
	String tab0fld = (String) datasetConfig.get( "tab0fld" );
	    
	List tab1lst = (List) datasetConfig.get( "tab1lst" );
	Map tab1lbl = (Map) datasetConfig.get( "tab1lbl" );
	String tab1fld = (String) datasetConfig.get( "tab1fld" );

	String tableDef = (String) datasetConfig.get( "tableDef" );
	List colLbl = (List) datasetConfig.get( "colLbl" );
	List colFld = (List) datasetConfig.get( "colFld" );

	String grp = (String) datasetConfig.get( "group" );
	if( grp == null ) { grp="file"; }
	
	logFile = logFile.replaceAll("^\\s+","" );
	logFile = logFile.replaceAll("\\s+$","" );
	List<String> fList = readFile( logFile );
	log.info( " file: " + logFile + 
		  " (" + fList.size() + " entries)");

	log.info( " tab0lst: " + tab0lst ); 
	log.info( " tab0fld: " + tab0fld ); 


	String reader = (String) datasetConfig.get( "reader" );

	List<Map<String,String>> all = null;

	if ( reader != null && reader.equalsIgnoreCase( "mif25" ) ) {
	    all = readMif25( fList );
	}

	if ( reader != null && reader.equalsIgnoreCase( "seq" ) ) {
	    all = readSeq( fList );
	}

	if ( reader != null && reader.equalsIgnoreCase( "imex" ) ) {
	    all = readImex( fList );
        }
	
	if ( all == null ) { return null; }

        
	List<Map<String,String>> data =
	    new ArrayList<Map<String,String>>();

        
        if( pane == null ){

            for( Iterator<Map<String,String>> i = all.iterator();
                 i.hasNext(); ){
                
                Map<String,String> entry = i.next();
                data.add( entry );
            }

            
            Map rmap = new HashMap();

            // data

            rmap.put( "data", data );
            log.debug( "DownloadContext->getData: rmap:\n" + rmap );
            
            return rmap;
            
        }


	//----------------------------------------------------------------------
	// filter tab0 content
	//--------------------

	
	String t0val = 
	    (String) tab0lst.get( (Integer)pane.get(0) );
	
	for( Iterator<Map<String,String>> i = all.iterator();
	     i.hasNext(); ){
	    Map<String,String> entry = i.next();
	    
	    if( entry.get( tab0fld ) != null &&
		entry.get( tab0fld ).equals( t0val ) ) {
		
		data.add( entry );
	    }
	}

	//----------------------------------------------------------------------
	// filter tab1 content
	//--------------------
	
	if ( tab1fld != null && tab1fld.length() > 0 ) {
	    
	    List<Map<String,String>> data2 =
		new ArrayList<Map<String,String>>();
	    
	    String t1val = 
		(String) tab1lst.get( (Integer) pane.get( 1 ) );
	    
	    for( Iterator<Map<String,String>> i = data.iterator();
		 i.hasNext(); ){
		Map<String,String> entry = i.next();
		
		if( entry.get( tab1fld ) != null &&
		    entry.get( tab1fld ).equals( t1val ) ) {
		    
		    data2.add( entry );
		}
	    }
	    
	    data = data2;
	}
	
	//----------------------------------------------------------------------
	// global filter
	//---------------
	
	if ( filter != null && filter.equals( "last" ) ) {
	    
	    String last = "1990/01/01";
	    
	    for( Iterator<Map<String,String>> i = data.iterator();
		 i.hasNext(); ){
		Map<String,String> entry = i.next();
		if ( entry.get("date").compareTo( last ) > 0 ) {
		    last = entry.get("date");
		}
	    }
	    
	    if ( !last.equals( "1990/01/01" ) ) {
		
		List<Map<String,String>> lastData =
		    new ArrayList<Map<String,String>>();
		
		for( Iterator<Map<String,String>> i = data.iterator();
		     i.hasNext(); ){
		    Map<String,String> entry = i.next();
		    if ( entry.get("date").equals( last ) ) {
			lastData.add( entry );
		    }
		}
		data = lastData;
	    }
	}

	//----------------------------------------------------------------------
	// group
	//--------------------
	
	Map<String,List<Integer>> grData =
	    new TreeMap<String,List<Integer>>();
	
	if ( grp != null && grp.length() > 0 ){
	    
	    int index = 0;
	    for( Iterator<Map<String,String>> i = data.iterator();
		 i.hasNext(); ){
		
		Map<String,String> entry = i.next();
		
		if ( grData.get( entry.get( grp ) ) == null ){
		    grData.put( entry.get( grp ), new ArrayList<Integer>());
		}
		grData.get( entry.get( grp ) ).add( index );
		index++;
	    }
	} else {
	    for( int i = 0; i < data.size(); i++ ) {
                
                String key = String.valueOf( i );
                if( i<10 ) { key="0" + key; }
                if( i<100 ) { key="0" + key; }
                if( i<1000 ) { key="0" + key; }
                if( i<10000 ) { key="0" + key; }
		grData.put( key, new ArrayList<Integer>());
		grData.get( key ).add( i );
	    }
	}

	//----------------------------------------------------------------------
	//  convert hash to list
	//----------------------
	
	//Collections.sort( grData );
        
        //List<List<Integer>> group = new ArrayList<List<Integer>>();

        //Set<String> grks = grData.keySet();
        //
        //for( Iterator<String> ii = grks.iterator(); ii.hasNext(); ) {
        //    List<Integer> k = ii.next();
        //    group.add( 0, k );
        //}

	List group = new ArrayList(grData.values());
	

	//----------------------------------------------------------------------
	// bulid return map
	//-----------------
	
	Map rmap = new HashMap();
	
	// data

	rmap.put("data",data);
	
	// dataset info

	rmap.put("id",dtsId);
	rmap.put("label",label);
	rmap.put("header",header);
	rmap.put("comment",comment);

	// tabs info
	
	rmap.put("tab0",tab0lst);
	rmap.put("tab0lbl",tab0lbl);

	rmap.put("tab1",tab1lst);
	rmap.put("tab1lbl",tab1lbl);

	// table info

	rmap.put("tableDef",tableDef);
	rmap.put("colLbl",colLbl);
	rmap.put("colFld",colFld);
	rmap.put("group",group);
	rmap.put("groupFld",grp);
	
	return rmap;
	
    }


    // log file readers
    //--------------------------------------------------------------------------
    // MIF25 reader
    //-------------
    
    private List<Map<String,String>> readMif25( List<String> fList ) {
	
	Log log = LogFactory.getLog( this.getClass() );
        log.info( "DownloadContext(readMif25)" );
	
	//File: /imex1/DIP/files/2008/mif25/dip20080114.mif25
	//                        NodeCount: 19585        EdgeCount:  56314
	//File: /imex1/DIP/files/2008/mif25/Scere20080114.mif25   
	//                        NodeCount: 4939 EdgeCount:  18384
	
	List<Map<String,String>> data = new ArrayList();
	
	for ( Iterator<String> ii = fList.iterator(); ii.hasNext(); ) {
	    String ll = ii.next();

            if( ll != null && ll.startsWith( "File:" ) ) {
                
                Scanner sc = new Scanner( ll );
                String s0 = sc.next();
                String path = sc.next();
                String s1 = sc.next();
                int nodeCnt = sc.nextInt();
                String s2 = sc.next();
                int edgeCnt = sc.nextInt();
                
                //String s3 = sc.next();
                //String txtPath = sc.next();
                
                if ( nodeCnt > 0 || edgeCnt > 0 ) {
                    
                    String file = null;
                    String year = null;
                    
                    String date = null;
                    String species =null;
                    String dtp = null;
                    String ftp = "mif25";
                    
                    if( path != null ) {
                        String[] pelem = path.split( File.separator );
                        file = pelem[ pelem.length-1 ];
                        year = pelem[ pelem.length-3 ];
                        
                        Pattern p = 
                            Pattern.compile("((\\D+)(\\d{4})(\\d{2})(\\d{2})" + 
                                            "(\\D*))\\.mif25");
                        Matcher m = p.matcher( file );
                        if ( m.matches() ){
                            file = m.group(1);
                            species = m.group(2);
                            date = 
                                m.group(3) + "/" +m.group(4) + "/" + m.group(5);
                            dtp = m.group(6);
                        }
                        
                        try {
                            path = path.replaceAll( file + "\\.mif25", "" );
                        } catch( PatternSyntaxException pe ) {
                            // no exception expected
                        }
                    }
                    
                    String pathMitab = path;
                    
                    try {
                        pathMitab = pathMitab.replaceFirst("mif25","tab25");
                    } catch (Exception ex ) {
                        // should not happen
                    }
                    
                    
                    if (  dtp == null ) { dtp = ""; }
                    log.info( " " + date + " " + dtp + " " + ftp + " " + 
                              file + " " + year + " " + species );
                    
                    Map<String,String> entry = new HashMap();
                    
                    entry.put( "file", file ); 
                    entry.put( "ext", ftp );
                    entry.put( "ftp", ftp );
                    entry.put( "dtp", dtp );
                    entry.put( "file", file ); 
                    entry.put( "path", path ); 
                    entry.put( "year", year ); 
                    entry.put( "date", date ); 
                    entry.put( "species", species ); 
                    entry.put( "nodes", String.valueOf(nodeCnt)); 
                    entry.put( "edges", String.valueOf(edgeCnt)); 
                    
                    Map<String,String> txtEntry = new HashMap();
                    
                    txtEntry.put( "file", file ); 
                    txtEntry.put( "ext", "txt" );
                    txtEntry.put( "ftp", "mitab" );
                    txtEntry.put( "dtp", dtp );
                    txtEntry.put( "file", file );
                    txtEntry.put( "path", pathMitab );
                    txtEntry.put( "year", year ); 
                    txtEntry.put( "date", date ); 
                    txtEntry.put( "species", species ); 
                    txtEntry.put( "nodes", String.valueOf(nodeCnt)); 
                    txtEntry.put( "edges", String.valueOf(edgeCnt)); 
                    
                    data.add( entry );
                    data.add( txtEntry );
                }
                
            }	
        }
	return data;
    }


    //--------------------------------------------------------------------------
    // Sequence reader
    //----------------

    private List<Map<String,String>> readSeq( List<String> fList ) {
        
	// File: /imex1/DIP/files/2008/fasta/Hsapi20080708.seq     
	//          NodeCount:      SequenceCount:  1183
        
	Log log = LogFactory.getLog( this.getClass() );
        log.info( "DownloadContext(readSeq)" );

        List<Map<String,String>> data = new ArrayList();

        for ( Iterator<String> ii = fList.iterator(); ii.hasNext(); ) {
            String ll = ii.next();

            if( ll != null && ll.startsWith( "File:" ) ) {

                Scanner sc = new Scanner( ll );
                String s0 = sc.next();
                String path = sc.next();
                String s1 = sc.next();
                String s2 = sc.next();
                int seqCnt = sc.nextInt();

                if ( seqCnt > 0 ) {
		
                    String file = null;
                    String year = null;

                    String date = null;
                    String species =null;
                    String ftp = "fasta";

                    if( path != null ) {
                        String[] pelem = path.split( File.separator );
                        file = pelem[ pelem.length-1 ];
                        year = pelem[ pelem.length-3 ];

                        Pattern p = Pattern
                            .compile("((\\D+)(\\d{4})(\\d{2})(\\d{2}))" +
                                     "\\.seq");
                        Matcher m = p.matcher( file );
                        if ( m.matches() ){
                            file = m.group(1);
                            species = m.group(2);
                            date = m.group(3) + "/" +
                                m.group(4) + "/" + m.group(5);
                        }
                        
                        try {
                            path = path.replaceAll( file + "\\.seq", "" );
                        } catch( PatternSyntaxException pe ) {
                            // no exception expected
                        }
                    }
                    
                    log.info( " " + date + " " + " " + ftp + " " +
                              file + " " + year + " " + species );
                    
                    Map<String,String> entry = new HashMap();
                    
                    entry.put( "file", file );
                    entry.put( "ext", "seq" );
                    entry.put( "ftp", ftp );
                    entry.put( "file", file );
                    entry.put( "path", path );
                    entry.put( "year", year );
                    entry.put( "date", date );
                    entry.put( "species", species );
                    entry.put( "seqs", String.valueOf(seqCnt));
                    
                    data.add( entry );
                }
            }
        }
	return data;
    }


    //--------------------------------------------------------------------------
    // IMEX reader
    //------------

    private List<Map<String,String>> readImex(  List<String> fList ) {
        
        //File: /imex1/DIP/files/2008/imex/dip_imex20081215.xml   
        //                                                   SourceCount: 86
        //File: /imex1/DIP/files/2008/imex/dip_imex20081215YT2008.xml     
        //                                                   SourceCount: 824
        //File: /imex1/DIP/files/2008/imex/dip_imex20081215CT2008.xml     
        //                                                   SourceCount: 824
        //File: /imex1/DIP/files/2008/imex/dip_imex20081221.xml   
        //                                                   SourceCount: 50
        //File: /imex1/DIP/files/2008/imex/dip_imex20081221YT2008.xml     
        //                                                   SourceCount: 874
        //File: /imex1/DIP/files/2008/imex/dip_imex20081221CT2008.xml     
        //                                                   SourceCount: 874

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "DownloadContext(readImex)" );

        List<Map<String,String>> data = new ArrayList();

        NavigableMap<String,Map<String,String>> ctd = 
            new TreeMap<String,Map<String,String>>();

        NavigableMap<String,Map<String,String>> ytd = 
            new TreeMap<String,Map<String,String>>();
	
	for ( Iterator<String> ii = fList.iterator(); ii.hasNext(); ) {
	    String ll = ii.next();

            if( ll != null && ll.startsWith( "File:" ) ) {
                
                Scanner sc = new Scanner( ll );
                String s0 = sc.next();
                String path = sc.next();
                String s1 = sc.next();
                int srcCnt = sc.nextInt();
                 
                if ( srcCnt > 0 ) {
                    
                    String file = null;
                    String year = null;
                    
                    String date = null;
                    String dtp = null;
                    String ftp = "mif25";

                    String ftpPath = "/";
                    
                    
                    if( path != null ) {
                        String[] pelem = path.split( File.separator );
                        file = pelem[ pelem.length-1 ];
                        year = pelem[ pelem.length-3 ];
                        ftpPath += year + "/";
                        Pattern p1 = 
                            Pattern.compile("(\\D+(\\d{4})(\\d{2})(\\d{2})" + 
                                            "(\\D*))\\.xml");
                        Matcher m1 = p1.matcher( file );

                        Pattern p2 = 
                            Pattern.compile("(\\D+(\\d{4})(\\d{2})(\\d{2})" + 
                                            "(\\D*)(\\d{4}))\\.xml");
                        Matcher m2 = p2.matcher( file );
                        
                        if ( m1.matches() ){
                            file = m1.group(1);
                            date = m1.group(2) + "/" +
                                m1.group(3) + "/" + m1.group(4);
                            dtp = m1.group(5);
                            if( dtp != null && dtp.equals( "CT" ) ) {
                                year = "cumul";
                            }                            
                        }
                        
                        if ( m2.matches() ){
                            file = m2.group(1);
                            date = m2.group(2) + "/" +
                                m2.group(3) + "/" + m2.group(4);
                            dtp = m2.group(5);
                            if( dtp != null && dtp.equals( "CT" ) ) {
                                //year = "cumul";
                            }
                            if( dtp != null && dtp.equals( "YT" ) ) {
                                //year = "cumul";
                            }                            
                        }
                        
                      
                        try {
                            path = path.replaceAll( file + "\\.xml", "" );
                        } catch( PatternSyntaxException pe ) {
                            // no exception expected
                        }
                    
                                        
                        if (  dtp == null ) { dtp = ""; }
                        log.info( "IMEX LOG: " + date + " : " + dtp + " : " + 
                                  ftp + " : " + file + " " + year + " ");
                    
                        Map<String,String> entry = new HashMap();
                        
                        entry.put( "file", file ); 
                        entry.put( "ext", ftp );
                        entry.put( "ftp", ftp );
                        entry.put( "dtp", dtp );
                        entry.put( "file", file ); 
                        entry.put( "path", path );
                        entry.put( "ftp-path", ftpPath ); 
                        entry.put( "year", year ); 
                        entry.put( "date", date ); 
                        entry.put( "sources", String.valueOf(srcCnt)); 
                    
                        log.info("path=" + path );
                        log.info("ftp-path=" + ftpPath );
                       
                        if( dtp.equals( "" ) ) {  // add incremental
                            data.add( 0, entry );
                        } else {
                            if( dtp.equals( "CT" ) ) { // store year-cumulative
                                ctd.put( year, entry );
                            }
                            if( dtp.equals( "YT" ) ) { // store year-to-date
                                ytd.put( year, entry );
                            }
                        }   
                    }
                }
            }	
        }

        // add year-to-date
        //-----------------
        
        NavigableSet<String> nyks = ytd.navigableKeySet();

        for( Iterator<String> ii = nyks.iterator(); 
             ii.hasNext(); ) {
            String k = ii.next();
            Map<String,String> entry = ytd.get( k );
            data.add( 0, entry );
        }

        // add cumulative-to-date
        //-----------------------
        NavigableSet<String> ncks = ctd.navigableKeySet();

        for( Iterator<String> ii = ncks.iterator(); 
             ii.hasNext(); ) {
            String k = ii.next();
            Map<String,String> entry = ctd.get( k );
            entry.put( "year", "cumul" );
            data.add( 0, entry );
        }
        

	return data;
    }

    //--------------------------------------------------------------------------
    // file reader
    //------------

    private List<String> readFile( String file) {
	
	List<String> contents = new ArrayList();
	
	try {
	    BufferedReader input =  
		new BufferedReader( new FileReader( file ) );
	    try {
		String line = null; 
		while (( line = input.readLine()) != null){
		    contents.add( line );
		}
	    } finally {
		input.close();
	    }
	} catch ( IOException ex ) {
	    Log log = LogFactory.getLog( this.getClass() );
	    log.info( "File reading error: " + 
		      file + " (" + ex.toString() + ")" );
	}
	Collections.sort( contents );
	return contents;
    }
    
}
