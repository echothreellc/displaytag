package org.displaytag.tags;

import org.apache.commons.lang.StringUtils;
import org.displaytag.test.DisplaytagCase;
import org.displaytag.test.KnownValue;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;


/**
 * Tests for different kind of "data sources".
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class DataSourceMapTest extends DisplaytagCase
{

    /**
     * Instantiates a new test case.
     * @param name test name
     */
    public DataSourceMapTest(String name)
    {
        super(name);
    }

    /**
     * @see org.displaytag.test.DisplaytagCase#getJspName()
     */
    public String getJspName()
    {
        return "map.jsp";
    }

    /**
     * Test with a Map[].
     * @param jspName jsp name, with full path
     * @throws Exception any axception thrown during test.
     */
    public void doTest(String jspName) throws Exception
    {

        WebRequest request = new GetMethodWebRequest(jspName);

        WebResponse response = runner.getResponse(request);

        if (log.isDebugEnabled())
        {
            log.debug("RESPONSE: " + response.getText());
        }

        WebTable[] tables = response.getTables();

        assertEquals("Expected one table in result.", 1, tables.length);

        assertEquals("Bad number of generated columns.", 3, tables[0].getColumnCount());

        assertEquals("Bad value in column header.", //
            StringUtils.capitalize(KnownValue.ANT), tables[0].getCellAsText(0, 0));
        assertEquals("Bad value in column header.", //
            StringUtils.capitalize(KnownValue.BEE), tables[0].getCellAsText(0, 1));
        assertEquals("Bad value in column header.", //
            StringUtils.capitalize(KnownValue.CAMEL), tables[0].getCellAsText(0, 2));

        assertEquals("Bad value in column content.", KnownValue.ANT, tables[0].getCellAsText(1, 0));
        assertEquals("Bad value in column content.", KnownValue.BEE, tables[0].getCellAsText(1, 1));
        assertEquals("Bad value in column content.", KnownValue.CAMEL, tables[0].getCellAsText(1, 2));
    }

}