package org.displaytag.util;

import javax.servlet.jsp.JspTagException;


/**
 * An user-friendly dependency checker. This will check the presence of libraries needed by displaytag, throwing an
 * Exception with an informative message if the library is missing or the version is not compatible.
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public final class DependencyChecker
{

    /**
     * Has the commons-lang dependency been checked?
     */
    private static boolean commonsLangChecked;

    /**
     * Don't instantiate.
     */
    private DependencyChecker()
    {
        // unused
    }

    /**
     * Displaytag requires commons-lang 2.x or better; it is not compatible with earlier versions.
     * @throws JspTagException if the wrong library, or no library at all, is found.
     */
    public static void check() throws JspTagException
    {
        if (commonsLangChecked)
        {
            return;
        }
        try
        {
            // Do they have commons lang ?
            Class stringUtils = Class.forName("org.apache.commons.lang.StringUtils"); //$NON-NLS-1$
            try
            {
                // this method is new in commons-lang 2.0
                stringUtils.getMethod("capitalize", new Class[]{String.class}); //$NON-NLS-1$
            }
            catch (NoSuchMethodException ee)
            {
                throw new JspTagException(Messages.getString("DependencyChecker.lang.incompatible")); //$NON-NLS-1$
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new JspTagException(Messages.getString("DependencyChecker.lang.missing")); //$NON-NLS-1$
        }
        commonsLangChecked = true;
    }

}