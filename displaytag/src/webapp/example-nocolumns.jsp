<%@ include file="inc/header.jsp" %>
<% 
	request.setAttribute( "test", new ReportList(6) );
%>

<h2><a href="./index.jsp">Examples</a> > Simple case, no columns</h2>

<display:table name="test" />

<p>
	The simplest possible usage of the table tag is to point the table
	tag at a list object and do nothing else.  The table tag will loop through the
	list and display a column for each property contained in the objects.
</p>

<p>
	Typically the only time that you would want to use the tag in this simple way
	is during development just as a sanity check, or while debugging.  For
	production, you should always define at least a single column.
</p>


<%@ include file="inc/footer.jsp" %>