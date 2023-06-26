package edu.ucla.mbi.dxf14;

/*===========================================================================
 * $HeadURL: https://wyu@imex.mbi.ucla.edu/svn/dip-ws/trunk/dip-api-ws/src/#$
 * $Id: IndexNodeType.java 1017 2010-04-16 22:00:31Z wyu $
 * Version: $Rev: 1017 $
 *===========================================================================
 *
 * IndexNodeType:
 *
 *
 *========================================================================= */


public class IndexNodeType extends NodeType {

    private TopNodeType topNodeType;

    //constructor
    public IndexNodeType(){}
    public IndexNodeType (TopNodeType topNodeType){  
        this.topNodeType = topNodeType;
    }

    public IndexNodeType setTopNodeType(TopNodeType topNodeType){
        this.topNodeType = topNodeType;
        return this;
    }

    public TopNodeType getTopNodeType(){
        return topNodeType;
    }
}
