package org.displaytag.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.tags.TableTag;


/**
 * Allow the author of an included JSP page to reset the content type to something else (like a binary stream), and then
 * write the new info back as the exclusive response, clearing the buffers of all previously added content.
 * <p>
 * The page author should write to, but not replace, the StringBuffer objects placed into request scope at
 * CONTENT_OVERRIDE_BODY and CONTENT_OVERRIDE_TYPE.
 * </p>
 * <p>
 * This filter allows TableTag users to perform exports from pages that are run as includes, such as from Struts or a
 * jsp:include. If that is your intention, just add this Filter to your web.xml and map it to the appropriate requests,
 * using something like:
 * </p>
 * <pre>
 *  &lt;filter&gt;
 *      &lt;filter-name&gt;ResponseOverrideFilter&lt;/filter-name&gt;
 *      &lt;filter-class&gt;org.displaytag.filter.ResponseOverrideFilter&lt;/filter-class&gt;
 *  &lt;/filter&gt;
 *  &lt;filter-mapping&gt;
 *      &lt;filter-name&gt;ResponseOverrideFilter&lt;/filter-name&gt;
 *      &lt;url-pattern&gt;*.do&lt;/url-pattern&gt;
 *  &lt;/filter-mapping&gt;
 *  &lt;filter-mapping&gt;
 *      &lt;filter-name&gt;ResponseOverrideFilter&lt;/filter-name&gt;
 *      &lt;url-pattern&gt;*.jsp&lt;/url-pattern&gt;
 *  &lt;/filter-mapping&gt;
 * </pre>
 * @author rapruitt
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 * @since 1.0
 */
public class ResponseOverrideFilter implements Filter
{

    /**
     * Logger.
     */
    private Log log;

    /**
     * {@inheritDoc}
     */
    public void init(FilterConfig filterConfig)
    {
        log = LogFactory.getLog(ResponseOverrideFilter.class);
    }

    /**
     * {@inheritDoc}
     * @todo don't filter when not needed. Table tag should add a fixed attribute for exporting, and filter should work
     * only if the parameter is found
     */
    public void doFilter(ServletRequest srequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) srequest;

        BufferedResponseWrapper wrapper = new BufferedResponseWrapper((HttpServletResponse) servletResponse);

        // In a portlet environment, you do not have direct access to the actual request object, so any attribute that
        // is added will not be visible outside of your portlet. So instead, users MUST append to the StringBuffer, so
        // that the portlets do not have to bind a new attribute to the request.
        request.setAttribute(TableTag.FILTER_CONTENT_OVERRIDE_BODY, new StringBuffer(8096));
        request.setAttribute(TableTag.FILTER_CONTENT_OVERRIDE_TYPE, new StringBuffer(80));
        request.setAttribute(TableTag.FILTER_CONTENT_OVERRIDE_FILENAME, new StringBuffer(80));

        filterChain.doFilter(request, wrapper);

        String pageContent;
        String contentType;
        StringBuffer buf = (StringBuffer) request.getAttribute(TableTag.FILTER_CONTENT_OVERRIDE_BODY);
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String characterEncoding = resp.getCharacterEncoding();
        if (characterEncoding != null)
        {
            characterEncoding = "; charset=" + characterEncoding;
        }
        else
        {
            characterEncoding = "";
        }
        if (buf != null && buf.length() > 0)
        {
            pageContent = buf.toString();
            contentType = ObjectUtils.toString(request.getAttribute(TableTag.FILTER_CONTENT_OVERRIDE_TYPE));
            if (log.isDebugEnabled())
            {
                log.debug("Overriding output, writing new output with content type " + contentType);
            }

            StringBuffer filename = (StringBuffer) request.getAttribute(TableTag.FILTER_CONTENT_OVERRIDE_FILENAME);

            try
            {
                // needed to reset headers (be sure there are no "no-cache" headers, else export will not work)
                resp.reset();
            }
            catch (IllegalStateException ise)
            {
                log.debug("Can't reset response headers.");
            }

            if (filename != null && StringUtils.isNotEmpty(filename.toString()))
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Filename specified as " + filename);
                }
                resp.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            }
        }
        else
        {
            log.debug("NOT overriding input. ");
            pageContent = wrapper.toString();
            contentType = wrapper.getContentType();
        }

        if (contentType != null)
        {
            if (contentType.indexOf("charset") > -1)
            {
                // charset is already specified (see #921811)
                servletResponse.setContentType(contentType);
            }
            else
            {
                servletResponse.setContentType(contentType + characterEncoding);
            }
        }
        servletResponse.setContentLength(pageContent.length());

        PrintWriter out = servletResponse.getWriter();
        out.write(pageContent);
        out.close();
    }
    /**
     * {@inheritDoc}
     */
    public void destroy()
    {
    }
}