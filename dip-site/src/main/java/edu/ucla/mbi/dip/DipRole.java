package edu.ucla.mbi.dip;

/* =========================================================================
 # $HeadURL:: https://imex.mbi.ucla.edu/svn/dip-ws/dip-portal/trunk/dip-si#$
 # $Id:: DipRole.java 2877 2012-12-18 20:42:36Z lukasz                     $
 # Version: $Rev:: 2877                                                    $
 #==========================================================================
 #
 # User
 #
 #
 #======================================================================= */

import edu.ucla.mbi.util.data.Role;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;



public class DipRole extends Role{

    public DipRole() {}
        
    public DipRole( Role role ) {
        setId( role.getId() );
        setName( role.getName() );
        setComments( role.getComments() == null ? "" : role.getComments() );
    }

    public String toString() {
	
	StringBuffer sb = new StringBuffer();
	
	sb.append( " DipRole(id=" + getId() );
	sb.append( " name=" + getName() );
	sb.append( " comments=" + getComments() );
	sb.append( ")" );

	return sb.toString();
    }

}
