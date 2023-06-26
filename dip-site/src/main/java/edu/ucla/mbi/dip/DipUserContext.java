package edu.ucla.mbi.dip;

/* ========================================================================
 # $Id:: UserContext.java 2871 2012-12-15 02:38:02Z lukasz                $
 # Version: $Rev:: 2871                                                   $
 #=========================================================================
 #                                                                        $
 # DipUserContext: JSON-based configuration                                  $
 #                                                                        $
 #     TO DO:                                                             $
 #                                                                        $
 #======================================================================= */

import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.data.dao.*;
import edu.ucla.mbi.util.context.*;

import edu.ucla.mbi.dip.orm.*;

public class DipUserContext extends UserContext {

    private EorelDAO eorelDao;

    public EorelDAO getEorelDao() {
        return eorelDao;
    }

    public void setEorelDao( EorelDAO dao ) {
        eorelDao = dao;
    }

}