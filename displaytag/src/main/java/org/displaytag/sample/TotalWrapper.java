package org.displaytag.sample;

import java.util.List;

import org.displaytag.decorator.TableDecorator;

/**
 * This decorator only does a summing of different groups in the reporting style examples...
 * @author epesh
 * @version $Revision$ ($Author$)
 */
public class TotalWrapper extends TableDecorator
{
    /**
     * total amount
     */
    private double grandTotal = 0;
    
    /**
     * total amount for city
     */
    private double cityTotal = 0;

    /**
     * After every row completes we evaluate to see if we should be drawing a new total line and summing the results
     * from the previous group.
     * @return String
     */
    public final String finishRow()
    {
        int listindex = ((List) getDecoratedObject()).indexOf(this.getCurrentRowObject());
        ReportableListObject reportableObject = (ReportableListObject) this.getCurrentRowObject();
        String nextCity = "";

        cityTotal += reportableObject.getAmount();
        grandTotal += reportableObject.getAmount();

        if (listindex == ((List) getDecoratedObject()).size() - 1)
        {
            nextCity = "XXXXXX"; // Last row hack, it's only a demo folks...
        }
        else
        {
            nextCity = ((ReportableListObject) ((List) getDecoratedObject()).get(listindex + 1)).getCity();
        }

        StringBuffer buffer = new StringBuffer(1000);

        // City subtotals...
        if (!nextCity.equals(reportableObject.getCity()))
        {
            buffer.append("\n<tr>\n<td>&nbsp;</td><td>&nbsp;</td><td><hr noshade size=\"1\"></td>");
            buffer.append("\n<td>&nbsp;</td></tr>");

            buffer.append("\n<tr><td>&nbsp;</td>");
            buffer.append("\n<td align=\"right\"><b>" + reportableObject.getCity() + " Total:</b></td>\n<td><b>");
            buffer.append(cityTotal);
            buffer.append("</b></td>\n<td>&nbsp;</td>\n</tr>");
            buffer.append("\n<tr>\n<td colspan=\"4\">&nbsp;\n</td>\n</tr>");

            cityTotal = 0;
        }

        // Grand totals...
        if (getViewIndex() == ((List) getDecoratedObject()).size() - 1)
        {
            buffer.append("<tr><td colspan=\"4\"><hr noshade size=\"1\"></td></tr>");
            buffer.append("<tr><td>&nbsp;</td>");
            buffer.append("<td align=\"right\"><b>Grand Total:</b></td><td><b>");
            buffer.append(grandTotal);
            buffer.append("</b></td><td>&nbsp;</td></tr>");
        }

        return buffer.toString();
    }

}
