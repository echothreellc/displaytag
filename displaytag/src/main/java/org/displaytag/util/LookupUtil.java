package org.displaytag.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.exception.ObjectLookupException;

/**
 * Utility class with methods for object & properties retrieving
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public final class LookupUtil
{

    /**
     * logger
     */
    private static Log log = LogFactory.getLog(LookupUtil.class);

    /**
     * don't instantiate a LookupUtil
     */
    private LookupUtil()
    {
    }

    /**
     * Read an object from the pagecontext with the specified scope and eventually lookup a property in it
     * @param pageContext PageContext
     * @param beanAndPropertyName String expression with bean name and attributes
     * @param scope One of the following values:
     * <ul>
     *   <li>PageContext.PAGE_SCOPE</li>
     *   <li>PageContext.REQUEST_SCOPE</li>
     *   <li>PageContext.SESSION_SCOPE</li>
     *   <li>PageContext.APPLICATION_SCOPE</li>
     * </ul>
     * @return Object
     * @throws ObjectLookupException for errors while retrieving a property in the bean
     */
    public static Object getBeanValue(PageContext pageContext, String beanAndPropertyName, int scope)
        throws ObjectLookupException
    {

        if (beanAndPropertyName.indexOf(".") != -1)
        {
            // complex: property from a bean

            String objectName = beanAndPropertyName.substring(0, beanAndPropertyName.indexOf("."));
            String beanProperty = beanAndPropertyName.substring(beanAndPropertyName.indexOf(".") + 1);
            Object beanObject;

            if (log.isDebugEnabled())
            {
                log.debug("getBeanValue - bean: {" + objectName + "}, property: {" + beanProperty + "}");
            }

            // get the bean
            beanObject = pageContext.getAttribute(objectName, scope);

            // if null return
            if (beanObject == null)
            {
                return null;
            }

            // go get the property
            return getBeanProperty(beanObject, beanProperty);

        }
        else
        {
            // simple, only the javabean

            if (log.isDebugEnabled())
            {
                log.debug("getBeanValue - bean: {" + beanAndPropertyName + "}");
            }

            return pageContext.getAttribute(beanAndPropertyName, scope);
        }
    }

    /**
     * <p>Returns the value of a property in the given bean</p>
     * <p>This method is a modificated version from commons-beanutils PropertyUtils.getProperty(). It allows
     * intermediate nulls in expression without throwing exception (es. it doesn't throw an exception for the property
     * <code>object.date.time</code> if <code>date</code> is null)</p>
     * @param bean javabean
     * @param name name of the property to read from the javabean
     * @return Object
     * @throws ObjectLookupException for errors while retrieving a property in the bean
     */
    public static Object getBeanProperty(Object bean, String name) throws ObjectLookupException
    {

        if (log.isDebugEnabled())
        {
            log.debug("getProperty [" + name + "] on bean " + bean);
        }

        try
        {
            if (bean == null)
            {
                throw new IllegalArgumentException("No bean specified");
            }
            if (name == null)
            {
                throw new IllegalArgumentException("No name specified");
            }

            int indexOfINDEXEDDELIM = -1;
            int indexOfMAPPEDDELIM = -1;
            int indexOfMAPPEDDELIM2 = -1;
            int indexOfNESTEDDELIM = -1;
            while (true)
            {

                indexOfNESTEDDELIM = name.indexOf(PropertyUtils.NESTED_DELIM);
                indexOfMAPPEDDELIM = name.indexOf(PropertyUtils.MAPPED_DELIM);
                indexOfMAPPEDDELIM2 = name.indexOf(PropertyUtils.MAPPED_DELIM2);
                if (indexOfMAPPEDDELIM2 >= 0
                    && indexOfMAPPEDDELIM >= 0
                    && (indexOfNESTEDDELIM < 0 || indexOfNESTEDDELIM > indexOfMAPPEDDELIM))
                {
                    indexOfNESTEDDELIM = name.indexOf(PropertyUtils.NESTED_DELIM, indexOfMAPPEDDELIM2);
                }
                else
                {
                    indexOfNESTEDDELIM = name.indexOf(PropertyUtils.NESTED_DELIM);
                }
                if (indexOfNESTEDDELIM < 0)
                {
                    break;
                }
                String next = name.substring(0, indexOfNESTEDDELIM);
                indexOfINDEXEDDELIM = next.indexOf(PropertyUtils.INDEXED_DELIM);
                indexOfMAPPEDDELIM = next.indexOf(PropertyUtils.MAPPED_DELIM);
                if (bean instanceof Map)
                {
                    bean = ((Map) bean).get(next);
                }
                else if (indexOfMAPPEDDELIM >= 0)
                {

                    bean = PropertyUtils.getMappedProperty(bean, next);

                }
                else if (indexOfINDEXEDDELIM >= 0)
                {
                    bean = PropertyUtils.getIndexedProperty(bean, next);
                }
                else
                {
                    bean = PropertyUtils.getSimpleProperty(bean, next);
                }

                if (bean == null)
                {
                    log.debug("Null property value for '" + name.substring(0, indexOfNESTEDDELIM) + "'");
                    return null;
                }
                name = name.substring(indexOfNESTEDDELIM + 1);

            }

            indexOfINDEXEDDELIM = name.indexOf(PropertyUtils.INDEXED_DELIM);
            indexOfMAPPEDDELIM = name.indexOf(PropertyUtils.MAPPED_DELIM);

            if (bean == null)
            {
                log.debug("Null property value for '" + name.substring(0, indexOfNESTEDDELIM) + "'");
                return null;
            }
            else if (bean instanceof Map)
            {
                bean = ((Map) bean).get(name);
            }
            else if (indexOfMAPPEDDELIM >= 0)
            {
                bean = PropertyUtils.getMappedProperty(bean, name);
            }
            else if (indexOfINDEXEDDELIM >= 0)
            {
                bean = PropertyUtils.getIndexedProperty(bean, name);
            }
            else
            {
                bean = PropertyUtils.getSimpleProperty(bean, name);
            }
        }
        catch (IllegalAccessException e)
        {
            throw new ObjectLookupException(LookupUtil.class, bean, name, e);
        }

        catch (InvocationTargetException e)
        {
            throw new ObjectLookupException(LookupUtil.class, bean, name, e);
        }
        catch (NoSuchMethodException e)
        {
            throw new ObjectLookupException(LookupUtil.class, bean, name, e);
        }

        return bean;

    }

}