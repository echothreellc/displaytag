package org.displaytag.sample;

import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;
import org.displaytag.decorator.ColumnDecorator;

/**
 * Simple column decorator which formats a date
 * @author epesh
 * @version $Revision$ ($Author$)
 */
public class LongDateWrapper implements ColumnDecorator
{
    /**
     * FastDateFormat used to format the date object
     */
    private FastDateFormat dateFormat = FastDateFormat.getInstance("MM/dd/yyyy HH:mm:ss");

    /**
     * transform the given object into a String representation. The object is supposed to be a date
     * @param columnValue Object
     * @return String
     */
    public final String decorate(Object columnValue)
    {
        Date date = (Date) columnValue;
        return dateFormat.format(date);
    }
}
