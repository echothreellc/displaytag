package org.displaytag.tags;

import org.displaytag.test.DisplaytagCase;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;


/**
 * Verify that the TableProperties will show values from the proper locale.
 * @author rapruitt
 * @version $Revision$ ($Author$)
 */
public class I18nPropertiesTest extends DisplaytagCase
{

    /**
     * No results for an en locale.
     */
    private static final String MSG_DEFAULT = "There are no results.";

    /**
     * No results for an it locale.
     */
    private static final String MSG_IT = "Non ci sono risultati.";

    /**
     * Instantiates a new test case.
     * @param name test name
     */
    public I18nPropertiesTest(String name)
    {
        super(name);
    }

    /**
     * @see org.displaytag.test.DisplaytagCase#getJspName()
     */
    public String getJspName()
    {
        return "i18nProperties.jsp";
    }

    /**
     * Check that the "no results" property is loaded from the correct locale file.
     * @param jspName jsp name, with full path
     * @throws Exception any axception thrown during test.
     */
    public void doTest(String jspName) throws Exception
    {

        WebRequest request = new GetMethodWebRequest(jspName);
        request.setHeaderField("Accept-Language", "en-us,en;q=0.5");

        WebResponse response = runner.getResponse(request);

        if (log.isDebugEnabled())
        {
            log.debug("RESPONSE: " + response.getText());
        }

        assertTrue(response.getText().indexOf(MSG_DEFAULT) > -1);
        assertTrue(response.getText().indexOf(MSG_IT) == -1);

        // Now, with an Italian locale.
        request = new GetMethodWebRequest(jspName);
        request.setHeaderField("Accept-Language", "it-it,it;q=0.5");

        response = runner.getResponse(request);

        if (log.isDebugEnabled())
        {
            log.debug("RESPONSE: " + response.getText());
        }

        assertTrue(response.getText().indexOf(MSG_IT) > -1);
        assertTrue(response.getText().indexOf(MSG_DEFAULT) == -1);
    }
}
