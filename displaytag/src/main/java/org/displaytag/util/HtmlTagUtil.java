package org.displaytag.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * Utility methods for dealing with html tags.
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public final class HtmlTagUtil
{

    /**
     * don't instantiate a new HtmlTagUtil.
     */
    private HtmlTagUtil()
    {
        // unused
    }

    /**
     * costruct a tag from a name and a collection of attributes.
     * @param tagName String tag name
     * @param attributes HtmlAttributeMap containing all the tag attributes
     * @return String open tag with attributes
     */
    public static String createOpenTagString(String tagName, HtmlAttributeMap attributes)
    {

        StringBuffer buffer = new StringBuffer();

        buffer.append(TagConstants.TAG_OPEN).append(tagName);

        if (attributes != null)
        {
            buffer.append(attributes.toString());
        }
        buffer.append(TagConstants.TAG_CLOSE);

        return buffer.toString();

    }

    /**
     * Strips html tags from a String.
     * @param str input string containing html tags (<code>null</code> is <strong>not </strong> handled)
     * @return input message without tags
     */
    public static String stripHTMLTags(String str)
    {
        // encode quotes, this will be used as a tag attribute value
        // @todo optimization: replace in Stringbuffer operations
        String text = StringUtils.replace(str, "\"", "&quot;");

        int startPosition = text.indexOf('<'); // encountered the first opening brace
        if (startPosition == -1)
        {
            // quick exit to avoid useless creation of a Stringbuffer
            return text;
        }

        StringBuffer returnMessage = new StringBuffer(text);

        int endPosition = text.indexOf('>'); // encountered the first closing braces
        while (startPosition != -1 && endPosition != -1)
        {
            returnMessage.delete(startPosition, endPosition + 1); // remove the tag
            startPosition = returnMessage.indexOf("<"); // look for the next opening brace
            endPosition = returnMessage.indexOf(">"); // look for the next closing brace
        }

        // delete remaining tokens
        if (startPosition != -1 && endPosition == -1)
        {
            returnMessage.setLength(startPosition);
        }

        return returnMessage.toString();
    }

    /**
     * Abbreviates a String which can contain html tags. Html tags are not counted in String length. It also try to
     * handle open tags and html entities.
     * @param str full String. <code>null</code> is handled by returning <code>null</code>
     * @param maxLength maximum number of characters (excluding tags)
     * @param byNumberOfWords if <code>true</code> maxLength will be the number of words returned, elsewhere will
     * represent the number of characters.
     * @return abbreviated String
     */
    public static String abbreviateHtmlString(String str, int maxLength, boolean byNumberOfWords)
    {
        if (str == null || str.length() <= maxLength)
        {
            // quick exit to avoid useless creation of a Stringbuffer
            return str;
        }

        int sz = str.length();
        StringBuffer buffer = new StringBuffer(sz);

        // some spaghetti code for quick & dirty tag handling and entity detection
        boolean inTag = false; // parsing a tag
        boolean inTagName = false; // parsing a tag name
        boolean endingTag = false; // parsing an ending tag
        int count = 0; // chars/words added
        boolean chopped = false; // result has been chopped?
        int entityChars = 0; // number of chars in parsed entity

        StringBuffer currentTag = new StringBuffer(5); // will contain a tag name

        List openTags = new ArrayList(5); // lit of unclosed tags found in the string

        int i;
        for (i = 0; i < sz; i++)
        {
            if (count >= maxLength)
            {
                chopped = true;
                break;
            }

            char c = str.charAt(i);

            if (c == '<')
            {
                inTag = true;
                inTagName = true;
            }
            else if (inTag)
            {
                if (inTagName && c == '/')
                {

                    if (currentTag.length() == 0)
                    {
                        // end tag found
                        endingTag = true;
                    }
                    else
                    {
                        // empty tag, reset and don't save
                        inTagName = false;
                    }

                    currentTag = new StringBuffer(5);
                }
                else if (inTagName && (c == ' ' || c == '>'))
                {
                    inTagName = false;

                    if (!endingTag)
                    {
                        openTags.add(currentTag.toString());
                    }
                    else
                    {
                        openTags.remove(currentTag.toString());
                    }
                    currentTag = new StringBuffer(5);
                    if (c == '>')
                    {
                        inTag = false;
                    }
                }
                else if (c == '>')
                {
                    inTag = false;
                }
                else if (inTagName)
                {
                    currentTag.append(c);
                }

            }
            else if (!inTag)
            {

                if (byNumberOfWords)
                {
                    if (Character.isWhitespace(c))
                    {
                        count++;
                    }
                }
                else
                {
                    // handle entities
                    if (c == '&')
                    {
                        entityChars = 1;
                    }
                    else if (entityChars == 0)
                    {
                        count++;
                    }
                    else
                    {
                        // end entity
                        if (entityChars > 0 && c == ';')
                        {
                            entityChars = 0;
                            count++;
                        }
                        else
                        {
                            entityChars++;
                        }
                        if (entityChars > 5)
                        {
                            // assume an unescaped & if entity doesn't close after max 5 chars
                            count += entityChars;
                            entityChars = 0;
                        }
                    }
                }

            }

            if (inTag || (!byNumberOfWords || count < maxLength))
            {
                buffer.append(c);
            }
        }

        if (chopped)
        {
            buffer.append("...");
        }

        if (openTags.size() > 0)
        {
            // quickly fixes closed tags
            String remainingToken = str.substring(i);

            for (int j = openTags.size() - 1; j >= 0; j--)
            {
                String closingTag = "</" + openTags.get(j) + ">";

                // we only add closing tags that exists in the original String, so we don't have to understand
                // html/xhtml differences and keep a list of html unclosed tags
                if (remainingToken.indexOf(closingTag) > -1)
                {
                    buffer.append(closingTag);
                }
            }
        }

        return buffer.toString();
    }

}