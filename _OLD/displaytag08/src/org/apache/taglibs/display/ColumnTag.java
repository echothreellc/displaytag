/**
 * $Id$
 *
 * Todo:
 *   - provide filters in some way?  Instead of just getting some bit of data
 *     also provide a way to feed that data through some other object that
 *     will reformat it in some way (like converting dates to another format)
 *   - update documentation, HTML column attributes are not set.
 *   - specify groupings.
 *   - error checking, value or property must be set.
 **/

package org.apache.taglibs.display;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/**
 * This tag works hand in hand with the SmartListTag to display a list of 
 * objects.  This describes a column of data in the SmartListTag.  There can
 * be any (reasonable) number of columns that make up the list.<p>
 *
 * This tag does no work itself, it is simply a container of information.  The
 * TableTag does all the work based on the information provided in the
 * attributes of this tag.<p>
 *
 * Usage:<p>
 *   <display:column property="title"
 *	 	           title="College Title" width="33%"
 *               href="/osiris/pubs/college/edit.page"
 *		           paramId="OID"
 *               paramProperty="OID" />
 *
 * Attributes:<p>
 *
 *   property       - the property method that is called to retrieve the 
 *                    information to be displayed in this column.  This method
 *                    is called on the current object in the iteration for
 *                    the given row.  The property format is in typical struts
 *                    format for properties (required)
 *
 *   title          - the title displayed for this column.  if this is omitted
 *                    then the property name is used for the title of the column
 *                    (optional)
 *
 *   nulls          - by default, null values don't appear in the list, by
 *                    setting viewNulls to 'true', then null values will 
 *                    appear as "null" in the list (mostly useful for debugging)
 *                    (optional)
 *
 *   width          - the width of the column (gets passed along to the html
 *                    td tag). (optional)
 *
 *   group          - the grouping level (starting at 1 and incrementing) of
 *                    this column (indicates if successive contain the same
 *                    values, then they should not be displayed).  The level
 *                    indicates that if a lower level no longer matches, then
 *                    the matching for this higher level should start over as
 *                    well. If this attribute is not included, then no grouping
 *                    is performed. (optional)
 *
 *   decorator      - a class that should be used to "decorate" the underlying
 *                    object being displayed. If a decorator is specified for
 *                    the entire table, then this decorator will decorate that
 *                    decorator. (optional)
 *
 *   autolink       - if set to true, then any email addresses and URLs found
 *                    in the content of the column are automatically converted
 *                    into a hypertext link.
 *
 *   href           - if this attribute is provided, then the data that is 
 *                    shown for this column is wrapped inside a <a href>
 *                    tag with the url provided through this attribute.  
 *                    Typically you would use this attribute along with one
 *                    of the struts-like param attributes below to create
 *                    a dynamic link so that each row creates a different
 *                    URL based on the data that is being viewed. (optional)
 *
 *   paramId        - The name of the request parameter that will be dynamically
 *                    added to the generated href URL. The corresponding value 
 *                    is defined by the paramProperty and (optional) paramName 
 *                    attributes, optionally scoped by the paramScope attribute.
 *                    (optional)
 *
 *   paramName      - The name of a JSP bean that is a String containing the 
 *                    value for the request parameter named by paramId (if 
 *                    paramProperty is not specified), or a JSP bean whose 
 *                    property getter is called to return a String (if 
 *                    paramProperty is specified). The JSP bean is constrained 
 *                    to the bean scope specified by the paramScope property, 
 *                    if it is specified.  If paramName is omitted, then it is
 *                    assumed that the current object being iterated on is the
 *                    target bean. (optional)
 *
 *   paramProperty  - The name of a property of the bean specified by the 
 *                    paramName attribute (or the current object being iterated
 *                    on if paramName is not provided), whose return value must 
 *                    be a String containing the value of the request parameter 
 *                    (named by the paramId attribute) that will be dynamically 
 *                    added to this href URL. (optional)
 *
 *   paramScope     - The scope within which to search for the bean specified by
 *                    the paramName attribute. If not specified, all scopes are 
 *                    searched.  If paramName is not provided, then the current
 *                    object being iterated on is assumed to be the target bean.
 *                    (optional)
 *
 *
 *   maxLength      - If this attribute is provided, then the column's displayed
 *                    is limited to this number of characters.  An elipse (...)
 *                    is appended to the end if this column is linked, and the
 *                    user can mouseover the elipse to get the full text.
 *                    (optional)
 *
 *   maxWords       - If this attribute is provided, then the column's displayed
 *                    is limited to this number of words.  An elipse (...) is
 *                    appended to the end if this column is linked, and the user
 *                    can mouseover the elipse to get the full text. (optional)
 */
 
public class ColumnTag extends BodyTagSupport implements Cloneable {
   
   private String property;
   private String title;
   private String nulls;
   private String sort;
   private String autolink;
   private String group; /* If this property is set then the values have to be grouped */
   
   private String href;
   private String paramId;
   private String paramName;
   private String paramProperty;
   private String paramScope;
   private int maxLength;
   private int maxWords;

   private String width;
   private String align;
   private String background;
   private String bgcolor;
   private String height;
   private String nowrap;
   private String valign;
   private String clazz;
   private String headerClazz;

   private String value;

   private String doubleQuote ;

   private String decorator;

   // -------------------------------------------------------- Accessor methods

   public void setProperty( String v ) { this.property = v; }
   public void setTitle( String v ) { this.title = v; }
   public void setNulls( String v ) { this.nulls = v; }
   public void setSort( String v ) { this.sort = v; }
   public void setAutolink( String v ) { this.autolink = v; }
   public void setGroup( String v ) { this.group = v; }


   public void setHref( String v ) { this.href = v; }
   public void setParamId( String v ) { this.paramId = v; }
   public void setParamName( String v ) { this.paramName = v; }
   public void setParamProperty( String v ) { this.paramProperty = v; }
   public void setParamScope( String v ) { this.paramScope = v; }

   public void setMaxLength( int v ) { this.maxLength = v; }
   public void setMaxWords( int v ) { this.maxWords = v; }


   public void setWidth( String v ) { this.width = v; }
   public void setAlign( String v ) { this.align = v; }
   public void setBackground( String v ) { this.background = v; }
   public void setBgcolor( String v ) { this.bgcolor = v; }
   public void setHeight( String v ) { this.height = v; }
   public void setNowrap( String v ) { this.nowrap = v; }
   public void setValign( String v ) { this.valign = v; }
   public void setStyleClass( String v ) { this.clazz = v; }
   public void setHeaderStyleClass( String v ) { this.headerClazz = v; }

   public void setValue( String v ) { this.value = v; }

   public void setDoubleQuote ( String v ) { this.doubleQuote = v ; }
   public void setDecorator(String v) {this.decorator = v; }


   public String getProperty() { return this.property; }
   public String getTitle() { return this.title; }
   public String getNulls() { return this.nulls; }
   public String getSort() { return this.sort; }
   public String getAutolink() { return this.autolink; }
   public String getGroup() { return this.group; }
		
   public String getHref() { return this.href; }
   public String getParamId() { return this.paramId; }
   public String getParamName() { return this.paramName; }
   public String getParamProperty() { return this.paramProperty; }
   public String getParamScope() { return this.paramScope; }

   public int getMaxLength() { return this.maxLength; }
   public int getMaxWords() { return this.maxWords; }

   public String getWidth() { return this.width; }
   public String getAlign() { return this.align; }
   public String getBackground() { return this.background; }
   public String getBgcolor() { return this.bgcolor; }
   public String getHeight() { return this.height; }
   public String getNowrap() { return this.nowrap; }
   public String getValign() { return this.valign; }
   public String getStyleClass() { return this.clazz; }
   public String getHeaderStyleClass() { return this.headerClazz; }

   public String getValue() { return this.value; }

   public String getDoubleQuote () { return this.doubleQuote ; }
   public String getDecorator() { return this.decorator ;}

   // --------------------------------------------------------- Tag API methods
   
   /**
    * Passes attribute information up to the parent TableTag.<p>
    *
    * When we hit the end of the tag, we simply let our parent (which better
    * be a TableTag) know what the user wants to do with this column.
    * We do that by simple registering this tag with the parent.  This tag's
    * only job is to hold the configuration information to describe this 
    * particular column.  The TableTag does all the work.
    *
    * @throws JspException if this tag is being used outside of a 
    *    <display:list...> tag.
    **/

   public int doEndTag() throws JspException {
      Object parent = this.getParent();

      if( parent == null ) {
         throw new JspException( "Can not use column tag outside of a " +
                                 "TableTag. Invalid parent = null" );
      }

      if( !( parent instanceof TableTag ) ) {
         throw new JspException( "Can not use column tag outside of a " +
                                 "TableTag. Invalid parent = " +
                                 parent.getClass().getName() );
      }

      // Need to clone the ColumnTag before passing it to the TableTag as
      // the ColumnTags can be reused by some containers, and since we are
      // using the ColumnTags as basically containers of data, we need to
      // save the original values, and not the values that are being changed
      // as the tag is being reused...

      ColumnTag copy = this;
      try {
         copy = (ColumnTag)this.clone();
      } catch( CloneNotSupportedException e ) {} // shouldn't happen...

      ((TableTag)parent).addColumn( copy );

      return super.doEndTag();
   }


   /**
    * Takes all the column pass-through arguments and bundles them up as a
    * string that gets tacked on to the end of the td tag declaration.<p>
    **/

   protected String getCellAttributes()
   {
      StringBuffer results = new StringBuffer();

      if( this.clazz != null ) {
          results.append( " class=\"" );
          results.append( this.clazz );
          results.append( "\"" );
      } else {
          results.append( " class=\"tableCell\"" );
      }

      if( this.width != null ) {
         results.append( " width=\"" );
         results.append( this.width );
         results.append( "\"" );
      }

      if( this.align != null ) {
         results.append( " align=\"" );
         results.append( this.align );
         results.append( "\"" );
      } else {
          results.append( " align=\"left\"" );
      }

      if( this.background != null ) {
         results.append( " background=\"" );
         results.append( this.background );
         results.append( "\"" );
      }

      if( this.bgcolor != null ) {
         results.append( " bgcolor=\"" );
         results.append( this.bgcolor );
         results.append( "\"" );
      }

      if( this.height != null ) {
         results.append( " height=\"" );
         results.append( this.height );
         results.append( "\"" );
      }

      if( this.nowrap != null ) {
         results.append( " nowrap" );
      }

      if( this.valign != null ) {
         results.append( " valign=\"" );
         results.append( this.valign );
         results.append( "\"" );
      } else {
          results.append( " valign=\"top\"" );
      }

      return results.toString();
   }


   /**
    * Returns a String representation of this Tag that is suitable for
    * printing while debugging.  The format of the string is subject to change
    * but it currently:<p>
    *
    * <code>SmartColumnTag([title],[property],[href])</code><p>
    *
    * Where the placeholders in brackets are replaced with their appropriate
    * instance variables
    **/
   
   public String toString() {
      return "SmartColumnTag(" + title + "," + property + "," + href + ")";
   }

}

