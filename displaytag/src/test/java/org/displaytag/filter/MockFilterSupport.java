package org.displaytag.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Simulates the behaviour of a filter using a simple servlet. The servlet must be mapped to the "*.filtered" extension;
 * request include this extension after the name of the tested jsp. Since servletunit doesn't support filter testing, we
 * are passing the request to this servlet which calls the filter and then forward the request to the given path without
 * ".filtered".
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class MockFilterSupport extends HttpServlet
{

    /**
     * extension mapped to this servlet.
     */
    public static final String FILTERED_EXTENSION = ".filtered";

    /**
     * logger.
     */
    private static Log log = LogFactory.getLog(MockFilterSupport.class);

    /**
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        log.info("Mock servlet called, simulating filter");
        Filter filter = new ResponseOverrideFilter();
        filter.init(null);
        filter.doFilter(request, response, new MockFilterChain());
    }

    /**
     * Simple FilterChain used to test Filters.
     */
    public class MockFilterChain implements FilterChain
    {

        /**
         * @see javax.servlet.FilterChain#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
         */
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException
        {
            String uri = ((HttpServletRequest) request).getRequestURI();
            uri = StringUtils.replace(uri, FILTERED_EXTENSION, "");
            RequestDispatcher dispatcher = request.getRequestDispatcher(uri);
            dispatcher.forward(request, response);
        }

    }

}