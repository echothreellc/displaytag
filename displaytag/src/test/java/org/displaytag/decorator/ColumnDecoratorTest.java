package org.displaytag.decorator;

import org.displaytag.test.DisplaytagCase;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;


/**
 * Tests for column decorators.
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class ColumnDecoratorTest extends DisplaytagCase
{

    /**
     * Instantiates a new test case.
     * @param name test name
     */
    public ColumnDecoratorTest(String name)
    {
        super(name);
    }

    /**
     * @see org.displaytag.test.DisplaytagCase#getJspName()
     */
    public String getJspName()
    {
        return "columndecorator.jsp";
    }

    /**
     * Checks that the generated page contains decorated values.
     * @param jspName jsp name, with full path
     * @throws Exception any axception thrown during test.
     */
    public void doTest(String jspName) throws Exception
    {
        WebRequest request = new GetMethodWebRequest(jspName);

        WebResponse response = runner.getResponse(request);

        log.debug("RESPONSE: " + response.getText());

        WebTable[] tables = response.getTables();

        assertEquals("Expected one table in result.", 1, tables.length);
        assertEquals("Expected decorated value not found.", "Tuesday", tables[0].getCellAsText(1, 0));
    }
}