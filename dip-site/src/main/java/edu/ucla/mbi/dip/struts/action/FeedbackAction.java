package edu.ucla.mbi.dip.struts.action;

/* =============================================================================
 *                                                                             $
 * FeedbackAction:                                                             $
 *                                                                             $
 ============================================================================ */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.CookiesAware;

import javax.servlet.http.Cookie;
import javax.servlet.ServletContext;

import edu.ucla.mbi.util.struts.captcha.*;

import edu.ucla.mbi.util.context.*;
import edu.ucla.mbi.util.data.*;
import edu.ucla.mbi.util.struts.action.*;
import edu.ucla.mbi.util.struts.interceptor.*;

import edu.ucla.mbi.dip.*;
import edu.ucla.mbi.dip.orm.*;

import org.vps.crypt.Crypt;

import java.util.*;

public class FeedbackAction extends PortalSupport {

    public static final String ACCEPTED = "accepted";

    //---------------------------------------------------------------------
    // Captcha
    //--------
        
    Captcha captcha = null;

    public void setCaptcha( Captcha captcha ){
        this.captcha = captcha; 
    }

    public Captcha getCaptcha(){
        return this.captcha; 
    }
    
    String capresponse ="";
    
    public void setCaptchaResponse( String response ){
        this.capresponse = response;
    }

    public String getCaptchaResponse(){
        return this.capresponse;
    }

    //---------------------------------------------------------------------
    // mail config
    //------------
    
    String adminMail;
    
    public String getAdminMail() {

        Log log = LogFactory.getLog( this.getClass() );
        log.info( "AdminMail: " + adminMail);
	return adminMail;
    }

    public void setAdminMail( String mail ) {
	adminMail = mail;
        Log log = LogFactory.getLog( this.getClass() );
        log.info( "AdminMail: " + adminMail);
    }

    String mailServer;
    
    public String getMailServer() {
	return mailServer;
    }

    public void setMailServer( String server ) {
	mailServer = server;
    }

    
    //---------------------------------------------------------------------
    // registered feedback
    //--------------------

    public String regFeed() {
	
	Log log = LogFactory.getLog( this.getClass() );
        log.info( "User Feedback :");	
	
	Integer id = (Integer) getSession().get( "USER_ID" );
	DipUserDAO dao = new DipUserDAO();
        DipUser dipUser = (DipUser) dao.getUser( id );
	
	dipUser.sendComment( adminMail, mailServer, 
			     getAbout(), getComment() );
	
	return ACCEPTED;
    }

    //---------------------------------------------------------------------
    // email feedback
    //----------------

    public String mailFeed() {

	Log log = LogFactory.getLog( this.getClass() );
        log.info( "Email Feedback:" + email);
	
	String email = getEmail();
	if ( email != null ) {
	    try {
		email = email.replaceAll("^\\s+","");
		email = email.replaceAll("\\s+$","");
	    } catch (Exception e ) {
		// cannot be here
	    }
	}
	
	String comment = getComment();
	
	if ( email != null ) {
	    comment = "\n\nFrom: " + email + "\n\n" + comment;
	}    
	DipUser.sendComment( adminMail, adminMail, mailServer, 
			     getAbout(), comment );
	
	return ACCEPTED;
    }
    
    String submit;

    public String getSubmit() {
	return submit;
    }

    public void setSubmit( String submit ) {
	this.submit = submit;
    }


    String email;

    public void setEmail( String email ) {
	this.email = email;
    }

    public String getEmail() {
        return email;
    }

    String comment;

    public void setComment( String comment ) {
	this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    String about;

    public void setAbout( String about ) {
	this.about = about;
    }

    public String getAbout() {
        return about;
    }

    //--------------------------------------------------------------------------

    String ns;

    public void setNc( String ns) {
	this.ns = ns;
    }

    public String getNs() {
        return ns;
    }

    //--------------------------------------------------------------------------

    String ac;

    public void setAc( String ac) {
	this.ac = ac;
    }

    public String getAc() {
        return ac;
    }


    //---------------------------------------------------------------------
    // recaptcha
    //----------

    /*
    private ReCaptcha recaptcha = null;

    public void setReCaptcha( ReCaptcha recaptcha ) {

	this.recaptcha = recaptcha;
    }

    private String rcf;

    public void setRecaptcha_challenge_field( String field ) {
        Log log = LogFactory.getLog( this.getClass() );
        log.info("setRecaptcha_challenge: " + field );
	this.rcf = field;
    }

    private String rrf;

    public void setRecaptcha_response_field( String field ) {
        Log log = LogFactory.getLog( this.getClass() );
        log.info("setRecaptcha_response: " + field );
	this.rrf = field;
    }
    */

    public String execute() throws Exception {

	if( getSubmit() != null && getSubmit().length() > 0 ) {

	    if ( getSession().get( "USER_ID" ) != null &&
		 (Integer) getSession().get( "USER_ID" )  > 0 ) {
		return regFeed();
	    } else {
		return mailFeed();
	    }
	} 

        if ( getRet() != null && getRet().length() > 0 ) {
            return getRet();
        }
        
	return SUCCESS;
    }
    
    //---------------------------------------------------------------------
    
    public void validate() {
        
        Log log = LogFactory.getLog( this.getClass() );
        if( getSubmit() != null && getSubmit().length() > 0 ) {
            
            String comment = getComment();
            if ( comment != null ) {
                try {
                    comment = comment.replaceAll("^\\s+","");
                    comment = comment.replaceAll("\\s+$","");
                } catch ( Exception ex ) {
                    // cannot be here 
                }
                setComment( comment );
            }
            
            if ( comment == null || comment.length() == 0 ) {
                addFieldError( "comment",
                               "Comment field cannot be left empty" );
            }
	    
            if ( getSession().get( "USER_ID" ) != null &&
                 (Integer) getSession().get( "USER_ID" )  > 0 ) {
                
            } else {
		
                // test recaptcha
                //---------------
                
                boolean rvalid = true;

                if( captcha == null ){
                    rvalid = true; 
                } else {
                    rvalid = captcha.validate( capresponse );
                }
                
                if ( ! rvalid ) {  
                    addActionError("Not a good CAPTCHA");
                    log.info( "recaptcha: error" );                    
                } else {
                    log.info( "recaptcha: OK" );
                }                            

            }
        }
    }
}
