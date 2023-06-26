<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<h2>Comments on: <s:property value="about"/></h2>
<hr/>
<table width="98%" cellspacing="10">
<tr>
  <td align="left" colspan="2">
   We would be grateful for any corrections or updates to the information on
   the experiments and interactions we report.  You can also 
   <a href="feedback">send us</a> general questions and suggestions related
   to our user interface, bugs and new features.    
   <s:if test="#session['USER_ID'] <= 0" >
    <p>Please, note, that we will be able to respond to this message and take 
     your comments into account only if you provide us with your valid e-mail.
   </s:if>
   <br/>
  </td>
 </tr>
 <tr>
  <td colspan="1" align="center">
   <s:form action="feedback" theme ="simple">
    <table width="96%">
     <s:if test="#session['USER_ID'] <= 0" >
      <tr>
       <th width="10%"  align="right">Your Name</th>
       <td width="60%"  align="left">
        <s:textfield  name="name"  size="32" maxlength="80"/>
       </td>
       <td align="center" valign="middle" rowspan="2">
         <s:submit name="submit" value="Submit" />
       </td> 
      </tr>
      <tr>
       <th width="10%"  align="right">Your E-mail</th>
       <td width="60%"  align="left">
        <s:textfield  name="email" size="32" maxlength="80"/>
       </td>
      </tr>
     </s:if>
     <tr>
      <th width="10%" colspan="1" align="right" valign="top"><br/>Message</th>
      <td width="90%" colspan="2"  align="left">
       <s:if test="hasFieldErrors()">
        <s:if test="fieldErrors['comment']!=null">
         <div id="errorDiv" style="padding-left: 10px; margin-bottom: 5px">
          <span class="error">
           <span class="errorMessage">
            <s:property value="fieldErrors['comment'][0]" />
           </span>
          </span>
         </div>
        </s:if>
       </s:if>
       <s:textarea name="comment"  rows="12" cols="64"/>
      </td>
     </tr>
     <s:if test="#session['USER_ID'] > 0" >
      <tr>
       <td align="left" colspan="2">
        <s:submit name="submit" value="Submit" />     
       </td>
      </tr>
     </s:if>
    </table>
   </s:form>
  </td>
 </tr>
</table>


