package edu.ucla.mbi.services;

/*===========================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-api#$
 * $Id:: ServiceException.java 85 2009-06-09 23:40:40Z lukasz               $
 * Version: $Rev:: 85                                                       $
 *===========================================================================
 *
 * FaultDef:
 *
 *    fault definitions
 *
 *========================================================================= */

import java.lang.Exception;

public class ServiceException extends Exception {

    private ServiceFault fault;

    public ServiceException( ServiceFault fault ) {
	this.fault = fault;
    }

    public ServiceFault getServiceFault() {
	return fault;
    }

}
