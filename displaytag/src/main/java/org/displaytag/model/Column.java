package org.displaytag.model;

import java.util.StringTokenizer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.displaytag.decorator.TableDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.exception.ObjectLookupException;
import org.displaytag.util.Anchor;
import org.displaytag.util.Href;
import org.displaytag.util.HtmlAttributeMap;
import org.displaytag.util.HtmlTagUtil;
import org.displaytag.util.LinkUtil;
import org.displaytag.util.LookupUtil;
import org.displaytag.util.TagConstants;
/**
 * <p>Represents a column in a table</p>
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class Column
{

    /**
     * Row this column belongs to
     */
    private Row row;

    /**
     * Header of this column. The header cell contains all the attributes common to all cells in the same column
     */
    private HeaderCell header;

    /**
     * copy of the attribute map from the header cell. Needed to change attributes (title) in this cell only
     */
    private HtmlAttributeMap htmlAttributes;

    /**
     * contains the evaluated body value. Filled in getOpenTag
     */
    private String stringValue;

    /**
     * Cell
     */
    private Cell cell;

    /**
     * Constructor for Column
     * @param headerCell HeaderCell
     * @param currentCell Cell
     * @param parentRow Row
     */
    public Column(HeaderCell headerCell, Cell currentCell, Row parentRow)
    {
        this.header = headerCell;
        this.row = parentRow;
        this.cell = currentCell;

        // also copy html attributes
        this.htmlAttributes = headerCell.getHtmlAttributes();
    }

    /**
     * Gets the value, after calling the table / column decorator is requested
     * @param decorated boolean
     * @return Object
     * @throws ObjectLookupException for errors in bean property lookup
     * @throws DecoratorException if a column decorator is used and an exception is thrown during value decoration
     */
    public Object getValue(boolean decorated) throws ObjectLookupException, DecoratorException
    {
        // a static value has been set?
        if (this.cell.getStaticValue() != null)
        {
            return this.cell.getStaticValue();
        }

        Object object = null;
        TableDecorator tableDecorator = this.row.getParentTable().getTableDecorator();

        // if a decorator has been set, and if decorator has a getter for the requested property only, check decorator
        if (decorated && tableDecorator != null && tableDecorator.hasGetterFor(this.header.getBeanPropertyName()))
        {

            object = LookupUtil.getBeanProperty(tableDecorator, this.header.getBeanPropertyName());
        }
        else
        {
            // else check underlining oblject
            object = LookupUtil.getBeanProperty(this.row.getObject(), this.header.getBeanPropertyName());
        }

        if (decorated && (this.header.getColumnDecorator() != null))
        {
            object = this.header.getColumnDecorator().decorate(object);
        }

        if (object == null || object.equals("null"))
        {
            if (!this.header.getShowNulls())
            {
                object = "";
            }
        }

        return object;
    }

    /**
     * Generates the cell open tag (&lt;td attribute1="value" ... >)
     * @return String td open tag
     * @throws ObjectLookupException for errors in bean property lookup
     * @throws DecoratorException if a column decorator is used and an exception is thrown during value decoration
     */
    public String getOpenTag() throws ObjectLookupException, DecoratorException
    {
        this.stringValue = createChoppedAndLinkedValue();

        return HtmlTagUtil.createOpenTagString(TagConstants.TAGNAME_COLUMN, this.htmlAttributes);
    }

    /**
     * Generates the cell close tag (&lt;/td>)
     * @return String td closing tag
     */
    public String getCloseTag()
    {
        this.stringValue = null;
        return this.header.getCloseTag();
    }

    /**
     * Calculates the cell content, cropping or linking the value as needed
     * @return String
     * @throws ObjectLookupException for errors in bean property lookup
     * @throws DecoratorException if a column decorator is used and an exception is thrown during value decoration
     */
    public String createChoppedAndLinkedValue() throws ObjectLookupException, DecoratorException
    {

        Object choppedValue = getValue(true);

        boolean isChopped = false;
        String fullValue = "";
        if (choppedValue != null)
        {
            fullValue = choppedValue.toString();
        }

        // trim the string if a maxLength or maxWords is defined
        if (this.header.getMaxLength() > 0 && fullValue.length() > this.header.getMaxLength())
        {
            choppedValue = fullValue.substring(0, this.header.getMaxLength()) + "...";
            isChopped = true;
        }
        else if (this.header.getMaxWords() > 0)
        {
            StringBuffer buffer = new StringBuffer();
            StringTokenizer tokenizer = new StringTokenizer(fullValue);
            int tokensNum = tokenizer.countTokens();
            if (tokensNum > this.header.getMaxWords())
            {
                int wordsCount = 0;
                while (tokenizer.hasMoreTokens() && (wordsCount < this.header.getMaxWords()))
                {
                    buffer.append(tokenizer.nextToken() + " ");
                    wordsCount++;
                }
                buffer.append("...");
                choppedValue = buffer;
                isChopped = true;
            }
        }

        // chopped content? add the full content to the column "title" attribute
        if (isChopped)
        {
            // clone the attribute map, don't want to add title to all the columns
            this.htmlAttributes = (HtmlAttributeMap) this.htmlAttributes.clone();
            // add title
            this.htmlAttributes.put(TagConstants.ATTRIBUTE_TITLE, fullValue);
        }

        // Are we supposed to set up a link to the data being displayed in this column...
        if (this.header.getAutoLink())
        {
            choppedValue = LinkUtil.autoLink(choppedValue.toString());
        }
        else if (this.header.getHref() != null) // add link?
        {
            // copy href
            Href colHref = new Href(this.header.getHref());

            // do we need to add a param?
            if (this.header.getParamName() != null)
            {

                Object paramValue;

                if (this.header.getParamProperty() != null)
                {
                    // different property, go get it
                    paramValue = LookupUtil.getBeanProperty(this.row.getObject(), this.header.getParamProperty());

                }
                else
                {
                    // same property as content
                    paramValue = fullValue;
                }

                colHref.addParameter(this.header.getParamName(), paramValue);

            }
            Anchor anchor = new Anchor(colHref, choppedValue.toString());

            choppedValue = anchor.toString();
        }

        if (choppedValue != null)
        {
            return choppedValue.toString();
        }
        return null;
    }

    /**
     * get the final value to be displayed in the table. This method can only be called after getOpenTag(), where the
     * content is evaluated
     * @return String final value to be displayed in the table
     */
    public String getChoppedAndLinkedValue()
    {
        return this.stringValue;
    }

    /**
     * returns the grouping order of this column or -1 if the column is not grouped
     * @return int grouping order of this column or -1 if the column is not grouped
     */
    public int getGroup()
    {
        return this.header.getGroup();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .append("cell", this.cell)
            .append("header", this.header)
            .append("htmlAttributes", this.htmlAttributes)
            .append("stringValue", this.stringValue)
            .toString();
    }
}