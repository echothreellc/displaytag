package org.displaytag.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Test case for org.displaytag.util.Href.
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class HrefTest extends TestCase
{

    /**
     * logger.
     */
    private static Log log = LogFactory.getLog(HrefTest.class);

    /**
     * instantiate a new test.
     * @param name test name
     */
    public HrefTest(String name)
    {
        super(name);
    }

    /**
     * Test a simple URL without parameters.
     */
    public final void testSimpleHref()
    {
        String url = "http://www.displaytag.org/displaytag";
        Href href = new Href(url);
        String newUrl = href.toString();
        compareUrls(newUrl, url);
    }

    /**
     * Test for URLs containing parameters.
     */
    public final void testHrefWithParameters()
    {
        String url = "http://www.displaytag.org/displaytag/index.jsp?param1=1&param2=2";
        Href href = new Href(url);
        String newUrl = href.toString();
        compareUrls(newUrl, url);
    }

    /**
     * Test for URLs containing parameters with multiple values.
     */
    public final void testHrefWithMultipleParameters()
    {
        String url = "http://www.displaytag.org/displaytag/index.jsp?param1=1&param2=2&param2=3&param2=4&param2=";
        Href href = new Href(url);
        String newUrl = href.toString();
        compareUrls(newUrl, url);
    }

    /**
     * Test for urls containing anchors.
     */
    public final void testHrefWithAnchor()
    {
        String url = "http://www.displaytag.org/displaytag/index.jsp#thisanchor";
        Href href = new Href(url);
        String newUrl = href.toString();
        compareUrls(newUrl, url);
    }

    /**
     * Test href with empty anchor.
     */
    public final void testHrefWithEmptyAnchor()
    {
        String url = "http://www.displaytag.org/displaytag/index.jsp?param1=1&param2=2#";
        Href href = new Href(url);
        String newUrl = href.toString();
        compareUrls(newUrl, url);
    }

    /**
     * Test for urls containin anchors and parameters.
     */
    public final void testHrefWithAnchorAndParameters()
    {
        String url = "http://www.displaytag.org/displaytag/index.jsp?param1=1&param2=2#thisanchor";
        Href href = new Href(url);
        String newUrl = href.toString();
        compareUrls(newUrl, url);
    }

    /**
     * Test the generation of an Href object from another Href.
     */
    public final void testHrefCopy()
    {
        String url = "http://www.displaytag.org/displaytag/index.jsp?param1=1&param2=2#thisanchor";
        Href href = new Href(url);
        Href copy = new Href(href);
        compareUrls(href.toString(), copy.toString());
    }

    /**
     * Test for added parameters.
     */
    public final void testAddParameter()
    {
        String url = "http://www.displaytag.org/displaytag/index.jsp?param1=1&param2=2#thisanchor";
        Href href = new Href(url);
        href.addParameter("param3", "value3");
        href.addParameter("param4", 4);
        String newUrl = href.toString();
        compareUrls(newUrl,
            "http://www.displaytag.org/displaytag/index.jsp?param1=1&param2=2&param3=value3&param4=4#thisanchor");
    }

    /**
     * test for setParameterMap().
     */
    public final void testSetParameterMap()
    {
        String url = "http://www.displaytag.org/displaytag/index.jsp#thisanchor";
        Href href = new Href(url);

        Map parameterMap = new HashMap();
        parameterMap.put("new1", "new1value");
        parameterMap.put("new2", "new2value");
        parameterMap.put("new3", null);
        href.setParameterMap(parameterMap);

        String newUrl = href.toString();
        compareUrls(newUrl,
            "http://www.displaytag.org/displaytag/index.jsp?new1=new1value&new2=new2value&new3=#thisanchor");
    }

    /**
     * test for addParameterMap().
     */
    public final void testAddParameterMap()
    {
        String url = "http://www.displaytag.org/displaytag/index.jsp?param1=1#thisanchor";
        Href href = new Href(url);

        Map parameterMap = new HashMap();
        parameterMap.put("new1", "new1value");
        parameterMap.put("new2", "new2value");
        parameterMap.put("new3", null);
        href.addParameterMap(parameterMap);

        String newUrl = href.toString();
        compareUrls(newUrl,
            "http://www.displaytag.org/displaytag/index.jsp?param1=1&new1=new1value&new2=new2value&new3=#thisanchor");

    }

    /**
     * test for base url extraction.
     */
    public final void testGetBaseUrl()
    {
        String url = "http://www.displaytag.org/displaytag/index.jsp?param1=1&param2=2#thisanchor";
        Href href = new Href(url);
        assertEquals(href.getBaseUrl(), "http://www.displaytag.org/displaytag/index.jsp");
    }

    /**
     * utility method for comparing two URLs.
     * @param generated generated URL
     * @param expected expected URL
     */
    private void compareUrls(String generated, String expected)
    {
        log.debug(generated);

        // if urls contains parameters they could be written in different order
        String[] generatedSplit = StringUtils.split(generated, "?#");
        String[] expectedSplit = StringUtils.split(expected, "?#");

        assertEquals(generatedSplit.length, expectedSplit.length);

        // same base url
        assertEquals(generatedSplit[0], expectedSplit[0]);

        // same anchor #
        if (generatedSplit.length > 2)
        {
            assertEquals("Anchor is different", generatedSplit[2], expectedSplit[2]);
        }

        // same parameters
        if (generatedSplit.length > 1)
        {
            // compare parameters
            String[] generatedParameters = StringUtils.split(StringUtils.replace(generatedSplit[1], "&amp;", "&"), '&');
            String[] expectedParameters = StringUtils.split(StringUtils.replace(expectedSplit[1], "&amp;", "&"), '&');

            assertEquals("Compared urls have different number of parameters", expectedParameters.length,
                generatedParameters.length);

            for (int j = 0; j < expectedParameters.length; j++)
            {
                assertTrue("Expected paramter " + expectedParameters[j] + " could not be fount in generated URL",
                    ArrayUtils.contains(generatedParameters, expectedParameters[j]));

            }
        }
    }

}
