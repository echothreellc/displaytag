/**
 * $Id$
 *
 * Status: Under Development
 *
 * Todo
 *   - impementation
 *   - documentation (javadoc, examples, etc...)
 *   - junit test cases
 **/

package org.apache.taglibs.display;

import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;
import javax.servlet.jsp.tagext.TagData;

/**
 * One line description of what this class does.
 *
 * More detailed class description, including examples of usage if applicable.
 **/

public class TableTagExtraInfo extends TagExtraInfo
{
   public VariableInfo[] getVariableInfo( TagData data ) {
      return new VariableInfo[] {
         new VariableInfo( "table_index",
                           "java.lang.Integer",
                           true,
                           VariableInfo.NESTED ),

         new VariableInfo( "table_item",
                           "java.lang.Object",
                           true,
                           VariableInfo.NESTED ),

      };
   }
}
