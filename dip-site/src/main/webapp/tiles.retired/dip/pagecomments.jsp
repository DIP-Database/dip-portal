<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div>
 <div>
   <a onClick='YAHOO.mbi.modal.feedback({about:"<s:property value='summary.ac'/> (<s:property value="#recordname"/>)",
                                         ns:"<s:property value='summary.ns'/>",
                                         ac:"<s:property value='summary.ac'/>"});return false;' 
      class="linkoff" onmouseout="this.className='linkoff'" onmouseover="this.className='linkon'"><img src="images/comment16L.png" alt="Add comment"/> Add&nbsp;comment</a>
 </div>
 <div>
  <a onClick='YAHOO.mbi.modal.comments({ns:"<s:property value='summary.ns'/>",
                                         ac:"<s:property value='summary.ac'/>"});return false;'  
      class="linkoff" onmouseout="this.className='linkoff'" onmouseover="this.className='linkon'">Read&nbsp;comments&nbsp;(0)</a>
 </div>
</div>


