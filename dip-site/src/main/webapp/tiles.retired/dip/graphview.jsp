<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<h1>DIP Graph</h1>
<table width="98%" border ="1">
 <tr>
  <td align="left">
test 1
<iframe src="/dip-portal/graph?ret=file&amp;gf=svg&amp;gl=neato&amp;dot=digraph G{ a->b; a->c; c->c; c->d; d->b; d->a; }" 
        width="300" height="300" frameborder="2" marginwidth="0" marginheight="0">
</iframe>
  </td>
 </tr>
 <tr>
  <td align="left">
   test2
<iframe src="/dip-portal/graph.svg" 
        width="300" height="300" frameborder="2" marginwidth="0" marginheight="0">
</iframe>
  </td>
 </tr>


</table>
