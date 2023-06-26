package edu.ucla.mbi.dxf14;

/*===========================================================================
 * $HeadURL: https://wyu@imex.mbi.ucla.edu/svn/dip-ws/trunk/dip-api-ws/src/#$
 * $Id: DxfException.java 1017 2010-04-16 22:00:31Z wyu $
 * Version: $Rev: 1017 $
 *===========================================================================
 *
 * DxfException:
 *
 *
 *========================================================================= */

import java.lang.Exception;

public class DxfException extends Exception {

    public DxfException(){
        super("The IndexNodeType cannot be created because ns:ac already exists in its topNodeType.");
    }

}
