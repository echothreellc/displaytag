package org.displaytag.pagination;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Object representing a page.
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class NumberedPage
{

    /**
     * page number.
     */
    private int number;

    /**
     * is the page selected?
     */
    private boolean selected;

    /**
     * Creates a new page with the specified number.
     * @param pageNumber page number
     * @param isSelected is the page selected?
     */
    public NumberedPage(int pageNumber, boolean isSelected)
    {
        this.number = pageNumber;
        this.selected = isSelected;
    }

    /**
     * Returns the page number.
     * @return the page number
     */
    public int getNumber()
    {
        return this.number;
    }

    /**
     * is the page selected?
     * @return true if the page is slected
     */
    public boolean getSelected()
    {
        return this.selected;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .append("selected", this.selected)
            .append("number", this.number)
            .toString();
    }
}