package org.displaytag.filter;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.Messages;
import org.displaytag.tags.TableTag;
import org.displaytag.tags.TableTagParameters;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;


/**
 * Allow the author of an included JSP page to reset the content type to something else (like a binary stream), and then
 * write the new info back as the exclusive response, clearing the buffers of all previously added content.
 * <p>
 * The page author should write to, but not replace, the StringBuffer objects placed into request scope at
 * CONTENT_OVERRIDE_BODY and CONTENT_OVERRIDE_TYPE.
 * </p>
 * <p>
 * This interceptor allows TableTag users to perform exports from pages that are run as includes, such as from Struts or
 * a jsp:include. If that is your intention, just add this interceptor to your spring dispatcher context xml and map it
 * to the appropriate requests, using something like:
 * </p>
 * 
 * <pre>
 * &lt;bean id="handlerMapping" class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
 *   &lt;property name="interceptors">
 *     &lt;list>
 *       &lt;bean class="org.displaytag.filter.DisplayTagSpringInterceptor"/>
 *     &lt;/list>
 * &lt;/bean>
 * </pre>
 * 
 * @author Keith Garry Boyce
 * @author rapruitt
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 * @since 1.0
 */
public class DisplayTagSpringInterceptor implements HandlerInterceptor
{

    /**
     * Logger.
     */
    static Log log = LogFactory.getLog(DisplayTagSpringInterceptor.class);

    /**
     * @see HandlerInterceptor#preHandle(HttpServletRequest,HttpServletResponse, Object)
     */
    public boolean preHandle(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler)
        throws Exception
    {
        if (servletRequest.getParameter(TableTagParameters.PARAMETER_EXPORTING) == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug(Messages.getString("ResponseOverrideFilter.parameternotfound")); //$NON-NLS-1$
            }
            //don't intercept!
            return true;
        }

        HttpServletRequest request = servletRequest;

        BufferedResponseWrapper12 wrapper = new BufferedResponseWrapper12(servletResponse);

        request.setAttribute(TableTag.FILTER_CONTENT_OVERRIDE_BODY, Boolean.TRUE);

        HandlerAdapter handlerAdaptor = new SimpleControllerHandlerAdapter();
        handlerAdaptor.handle(request, wrapper, handler);

        if (wrapper.isOutRequested())
        {
            // data already written
            log.debug("Everything done, exiting");
            return false;
        }

        // if you reach this point the PARAMETER_EXPORTING has been found, but the special header has never been set in
        // response (this is the signal from table tag that it is going to write exported data)
        log.debug("Something went wrong, displaytag never requested writer as expected.");

        String pageContent;
        String contentType;

        HttpServletResponse resp = servletResponse;
        String characterEncoding = resp.getCharacterEncoding();
        if (characterEncoding != null)
        {
            characterEncoding = "; charset=" + characterEncoding; //$NON-NLS-1$
        }
        log.debug(Messages.getString("DisplayTagInterceptor.notoverriding")); //$NON-NLS-1$
        pageContent = wrapper.toString();
        contentType = wrapper.getContentType();

        if (contentType != null)
        {
            if (contentType.indexOf("charset") > -1) //$NON-NLS-1$
            {
                // charset is already specified (see #921811)
                servletResponse.setContentType(contentType);
            }
            else
            {
                servletResponse.setContentType(contentType + StringUtils.defaultString(characterEncoding));
            }
        }
        servletResponse.setContentLength(pageContent.length());

        PrintWriter out = servletResponse.getWriter();
        out.write(pageContent);
        return false;

    }

    /**
     * @see HandlerInterceptor#postHandle(HttpServletRequest,HttpServletResponse, Object, ModelAndView)
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj,
        ModelAndView modelAndView) throws Exception
    {
        // Nothing to do
    }

    /**
     * @see HandlerInterceptor#afterCompletion(HttpServletRequest,HttpServletResponse, Object, Exception)
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj,
        Exception exception) throws Exception
    {
        // Nothing to do
    }

}
