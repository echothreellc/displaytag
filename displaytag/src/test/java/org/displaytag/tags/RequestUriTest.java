package org.displaytag.tags;

import org.displaytag.test.DisplaytagCase;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;


/**
 * Tests for requestUri column attribute.
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class RequestUriTest extends DisplaytagCase
{

    /**
     * Instantiates a new test case.
     * @param name test name
     */
    public RequestUriTest(String name)
    {
        super(name);
    }

    /**
     * @see org.displaytag.test.DisplaytagCase#getJspName()
     */
    public String getJspName()
    {
        return "requesturi.jsp";
    }

    /**
     * Test link generated using requestUri.
     * @param jspName jsp name, with full path
     * @throws Exception any axception thrown during test.
     */
    public void doTest(String jspName) throws Exception
    {
        WebRequest request = new GetMethodWebRequest(jspName);

        WebResponse response = runner.getResponse(request);
        log.debug(response.getText());

        WebTable[] tables = response.getTables();
        assertEquals("Expected one table in result.", 1, tables.length);

        WebLink[] links = response.getLinks();
        assertEquals("Wrong number of links in result.", 3, links.length);

        assertEquals("Text in first link is wrong.", CONTEXT
            + "/goforit?d-2106-e=2&"
            + TableTagParameters.PARAMETER_EXPORTING
            + "=1", links[0].getURLString());
    }

}