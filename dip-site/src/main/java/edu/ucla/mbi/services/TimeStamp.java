package edu.ucla.mbi.services;

/*===========================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-api#$
 * $Id:: TimeStamp.java 110 2009-06-13 14:18:01Z lukasz                     $
 * Version: $Rev:: 110                                                      $
 *===========================================================================
 *
 * TimeStamp:
 *
 *    utility class - time format conversions
 *
 *========================================================================= */

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;

import javax.xml.datatype.XMLGregorianCalendar;

public class TimeStamp {
    
    private static DatatypeFactory df;
    
    static {
	try {
	    df = DatatypeFactory.newInstance();
	} catch( DatatypeConfigurationException dce ) {
	    df = null;
	}
    }

    private TimeStamp() {}
    
    public static XMLGregorianCalendar toXmlDate( Date d ) {
	
	GregorianCalendar gc = new GregorianCalendar();
	gc.setTime( d );
	
	XMLGregorianCalendar xgc = df.newXMLGregorianCalendar( gc );
	return xgc.normalize() ;
    }
}
