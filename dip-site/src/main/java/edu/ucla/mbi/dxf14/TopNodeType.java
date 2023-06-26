package edu.ucla.mbi.dxf14;

/*===========================================================================
 * $HeadURL: https://wyu@imex.mbi.ucla.edu/svn/dip-ws/trunk/dip-api-ws/src/#$
 * $Id: TopNodeType.java 1237 2010-09-24 23:13:12Z wyu $
 * Version: $Rev: 1237 $
 *===========================================================================
 *
 * TopNodeType:
 *
 *
 *========================================================================= */

import java.util.*;

public class TopNodeType extends NodeType {

    private Set<String> acSet = new HashSet();

    public TopNodeType(){}
    public TopNodeType (String ns, String ac){
        this.ns = ns;
        this.ac = ac;
        String key = ns + ":" + ac;
        this.acSet.add(key);    
    }
   
    public void addKey(String key){
        this.acSet.add(key);
    } 

    //setter
    public TopNodeType setAcSet (Set acSet){
        this.acSet = acSet;
        return this;
    }

    //getter
    public Set getAcSet(){
        return acSet;
    }
}
