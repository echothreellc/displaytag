<jsp:root version="1.2" xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:display="urn:jsptld:../../../src/main/resources/META-INF/displaytag-el.tld"
    xmlns:c="urn:jsptld:../WEB-INF/tld/c.tld">
    <jsp:text> <![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> ]]> </jsp:text>
    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
        <head>
            <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1" />
            <title>Displaytag unit test</title>
        </head>
        <body>
            <jsp:scriptlet> <![CDATA[
              java.util.List testData = new java.util.ArrayList();
              testData.add(new org.displaytag.test.KnownValue());
              request.setAttribute( "test", testData);
            ]]> </jsp:scriptlet>

            <display:table id="table" name="test" htmlId="html1">
                <display:column><c:out value="${table.bee}" /></display:column>
            </display:table>

            <display:table uid="table" name="test" htmlId="html2">
                <display:column><c:out value="${table.bee}" /></display:column>
            </display:table>

            <display:table name="test" htmlId="html3">
                <display:column property="bee" />
            </display:table>

        </body>
    </html>
</jsp:root>