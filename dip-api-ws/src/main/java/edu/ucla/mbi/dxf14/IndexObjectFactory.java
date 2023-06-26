package edu.ucla.mbi.dxf14;

/*===========================================================================
 * $HeadURL: https://wyu@imex.mbi.ucla.edu/svn/dip-ws/trunk/dip-api-ws/src/#$
 * $Id: IndexObjectFactory.java 1237 2010-09-24 23:13:12Z wyu $
 * Version: $Rev: 1237 $
 *===========================================================================
 *
 * IndexObjectFactory:
 *
 *========================================================================= */


public class IndexObjectFactory extends ObjectFactory {

    public IndexObjectFactory(){}

    public NodeType createIndexNodeType ( TopNodeType topNodeType,
                                          int nodeid,
                                          String ac,
                                          String ns,
                                          String label,
                                          TypeDefType type
                                          ) {

        String key = ns + ":" + ac;

        if( topNodeType == null ){
            topNodeType = createTopNodeType ( nodeid,
                                              ac,
                                              ns,
                                              label,
                                              type
                                              );


            return topNodeType;
        }

        if(topNodeType.getAcSet().contains( key )){
            return null;
        }else{

            IndexNodeType indexNodeT = new IndexNodeType(topNodeType);
            indexNodeT.setId ( nodeid );
            indexNodeT.setAc( ac );
            indexNodeT.setNs( ns );
            indexNodeT.setLabel( label );
            indexNodeT.setType( type );
            topNodeType.addKey(key);

            return indexNodeT;
        }

    }

    public TopNodeType createTopNodeType ( int nodeid,
                                           String ac,
                                           String ns,
                                           String label,
                                           TypeDefType type
                                           ) {


        TopNodeType topNodeType = new TopNodeType(ns, ac);

        topNodeType.setId ( nodeid );
        topNodeType.setLabel( label );
        topNodeType.setType( type );

        return topNodeType;
    }

    public NodeType createNodeType ( int nodeid,
                                     String ac,
                                     String ns,
                                     String label,
                                     TypeDefType type
                                     ) {


        NodeType nodeType = super.createNodeType ();

        nodeType.setId ( nodeid );
        nodeType.setAc( ac );
        nodeType.setNs( ns );
        nodeType.setLabel( label );
        nodeType.setType( type );

        return nodeType;
    }

    public TypeDefType createTypeDefType ( String ac,
                                           String ns,
                                           String name
                                           ) {

        TypeDefType tdt = super.createTypeDefType();
        tdt.setAc( ac );
        tdt.setNs( ns );
        tdt.setName( name );

        return tdt;
    }

    public AttrType createAttrType ( String ac,
                                     String ns,
                                     String name,
                                     String value,
                                     String valueAc,
                                     String valueNs
                                     ) {


        AttrType attrT = super.createAttrType();
        attrT.setAc ( ac );
        attrT.setNs ( ns );
        attrT.setName ( name );

        edu.ucla.mbi.dxf14.AttrType.Value valueAT =
                        new edu.ucla.mbi.dxf14.AttrType.Value();

        valueAT.setValue ( value );
        if( valueAc != null && valueNs != null ){
            valueAT.setAc (valueAc);
            valueAT.setNs(valueNs);
        }

        attrT.setValue ( valueAT );

        return attrT;
    }

    public XrefType createXrefType ( String typeAc,
                                     String typeNs,
                                     String typeName,
                                     String ac,
                                     String ns
                                     ) {

        XrefType xrefT = super.createXrefType();
        xrefT.setType( typeName );
        xrefT.setTypeAc( typeAc );
        xrefT.setTypeNs( typeNs );

        xrefT.setAc( ac );
        xrefT.setNs( ns );

        return xrefT;
    }

}
