package org.displaytag.util;

/**
 * Anchor object used to output an html link (an &lt;a> tag)
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class Anchor
{

    /**
     * Href object to be written in the "href" html attribute
     */
    private Href href;

    /**
     * link body text
     */
    private String linkText;

    /**
     * HashMap containing all the html attributes
     */
    private HtmlAttributeMap attributeMap = new HtmlAttributeMap();

    /**
     * Creates a new anchor with the supplied body text
     * @param linkBody String body text
     */
    public Anchor(String linkBody)
    {
        this.linkText = linkBody;
    }

    /**
     * Creates a new Anchor whit the supplied Href
     * @param linkHref Href
     */
    public Anchor(Href linkHref)
    {
        this.href = linkHref;
    }

    /**
     * Creates a new Anchor whit the supplied Href and body text
     * @param linkHref baseHref
     * @param linkBody String link body
     */
    public Anchor(Href linkHref, String linkBody)
    {
        this.href = linkHref;
        this.linkText = linkBody;
    }

    /**
     * setter the anchor Href
     * @param linkHref Href
     */
    public void setHref(Href linkHref)
    {
        this.href = linkHref;
    }

    /**
     * setter for the link body text
     * @param linkBody String
     */
    public void setText(String linkBody)
    {
        this.linkText = linkBody;
    }

    /**
     * add a "class" attribute to the html link
     * @param cssClass String
     */
    public void setClass(String cssClass)
    {
        this.attributeMap.put(TagConstants.ATTRIBUTE_CLASS, cssClass);
    }

    /**
     * add a "style" attribute to the html link
     * @param style String
     */
    public void setStyle(String style)
    {
        this.attributeMap.put(TagConstants.ATTRIBUTE_STYLE, style);
    }

    /**
     * add a "title" attribute to the html link
     * @param title String
     */
    public void setTitle(String title)
    {
        this.attributeMap.put(TagConstants.ATTRIBUTE_TITLE, title);
    }

    /**
     * returns the href attribute, surrounded by quotes and prefixed with " href="
     * @return String <code> href ="<em>href value</em>"</code> or an emty String if Href is null
     */
    private String getHrefString()
    {
        if (this.href == null)
        {
            return "";
        }
        return " href=\"" + this.href.toString() + "\"";
    }

    /**
     * returns the &lt;a&gt; tag, with rendered href and any ther setted html attribute
     * @return String
     */
    public String getOpenTag()
    {

        // shortcut for links with no attributes
        if (this.attributeMap.size() == 0)
        {
            return TagConstants.TAG_OPEN + TagConstants.TAGNAME_ANCHOR + getHrefString() + TagConstants.TAG_CLOSE;
        }

        // append all attributes
        StringBuffer buffer = new StringBuffer();

        buffer.append(TagConstants.TAG_OPEN).append(TagConstants.TAGNAME_ANCHOR).append(getHrefString());

        buffer.append(this.attributeMap);

        buffer.append(TagConstants.TAG_CLOSE);

        return buffer.toString();
    }

    /**
     * returns the &lt;/a&gt; tag
     * @return String
     */
    public String getCloseTag()
    {
        return TagConstants.TAG_OPENCLOSING + TagConstants.TAGNAME_ANCHOR + TagConstants.TAG_CLOSE;
    }

    /**
     * returns the full &lt;a href=""&gt;body&lt;/a&gt;
     * @return String html link
     */
    public String toString()
    {
        return getOpenTag() + this.linkText + getCloseTag();
    }

}