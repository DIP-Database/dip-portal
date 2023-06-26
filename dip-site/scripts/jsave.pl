#!/usr/bin/perl

$SRCD="src/main/webapp/";
$JRTD="target/work/webapp/";

my @CP = ( ["css/*","css","-R"],
           ["dip/images/*","dip/images","-R"],
           ["dip/pages/*","dip/pages","-R"],
           ["etc/*","etc","-R"],
           ["images/*","images","-R"],
           ["js/*","js","-R"],
           ["prl/images/*","prl/images","-R"],
           ["prl/pages/*","prl/pages","-R"],
           ["skin/css/*","skin/css","-R"],
           ["skin/images/*","skin/images","-R"],
           ["skin/pages/*","skin/pages","-R"],
           ["skin/dip/css","skin/dip/css","-R"],
           ["skin/dip/images/*","skin/dip/images","-R"],
           ["skin/prl/css/*","skin/prl/css","-R"],
           ["skin/prl/images/*","skin/prl/images","-R"]
           );




for( my $i=0;$i<@CP;$i++ ){
    `cp $CP[$i][2] $JRTD$CP[$i][0] $SRCD$CP[$i][1]`;

} 

