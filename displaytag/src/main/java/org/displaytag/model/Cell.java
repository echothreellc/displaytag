package org.displaytag.model;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>Represents a table cell</p>
 * <p>A cell is used only when the content is placed as content of the column tag and need to be evaluated during
 * iteration. If the content is set using the <code>value</code> attribute in the column tag no cell is created and
 * EMPTY_CELL is used as placeholder.</p>
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class Cell implements Comparable
{

    /**
     * empty cell object. Use as placeholder for empty cell to avoid useless object creation
     */
    public static final Cell EMPTY_CELL = new Cell();

    /**
     * content of the cell
     */
    private Object staticValue;

    /**
     * Creates a new empty cell. This should never be done, use EMPTY_CELL instead
     */
    private Cell()
    {
    }

    /**
     * Creates a cell with a static value
     * @param value Object value of the Cell object
     */
    public Cell(Object value)
    {
        this.staticValue = value;
    }

    /**
     * get the static value for the cell
     * @return the Object value of this.staticValue.
     */
    public Object getStaticValue()
    {
        return this.staticValue;
    }

    /**
     * set the static value of the cell
     * @param value - the new value for this.staticValue
     */
    public void setStaticValue(Object value)
    {
        this.staticValue = value;
    }

    /**
     * Compare the Cell value to another Cell
     * @param obj Object to compare this cell to
     * @return int
     * @see java.lang.Comparable#compareTo(Object)
     */
    public int compareTo(Object obj)
    {

        if (this.staticValue == null)
        {
            return -1;
        }

        if (obj instanceof Cell)
        {
            return ((Comparable) this.staticValue).compareTo(((Cell) obj).getStaticValue());
        }
        else
        {
            return ((Comparable) this.staticValue).compareTo(obj);
        }

    }

    /**
     * Simple toString wich output the static value
     * @return String represantation of the cell
     */
    public String toString()
    {
        return new ToStringBuilder(this).append("staticValue", this.staticValue).toString();
    }

}