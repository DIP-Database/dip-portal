package edu.ucla.mbi.orm;

/*===========================================================================
 * $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/trunk/orm-hibernate-comp#$
 * $Id:: DAOException.java 40 2009-05-14 15:06:40Z                          $
 * Version: $Rev:: 40                                                       $
 *===========================================================================
 *
 * DAOException:
 *
 *========================================================================= */

public class DAOException extends RuntimeException {
    // Other Constructors omitted
    public DAOException(Throwable cause) {
        super(cause);
    }

    public DAOException(String message) {
        super(message);
    }
}
