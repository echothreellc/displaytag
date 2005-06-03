<jsp:root version="1.2" xmlns:jsp="http://java.sun.com/JSP/Page" 
  xmlns:display="urn:jsptld:../../../src/main/resources/META-INF/displaytag-12.tld">
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
          for (int j=0; j<12; j++)
          {
            testData.add(new org.displaytag.test.KnownValue());
          }
          request.setAttribute( "test", testData);
      ]]> </jsp:scriptlet>
      <display:table name="requestScope.test" id="table" pagesize="1">
        <display:setProperty name="paging.banner.placement" value="both"/>
        <display:column property="ant"/>
      </display:table>
      <display:table name="requestScope.test" id="table2" pagesize="1">
        <display:setProperty name="paging.banner.placement" value="both"/>
        <display:column property="ant"/>
      </display:table>
    </body>
  </html>
</jsp:root>