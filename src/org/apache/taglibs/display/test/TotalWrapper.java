package org.apache.taglibs.display.test;

import org.apache.taglibs.display.Decorator;

/**
 * This decorator only does a summing of different groups in the reporting
 * style examples.
 *
 * @version $Revision$
 */
public class TotalWrapper extends Decorator {
    private double cityTotal = 0;
    private double grandTotal = 0;

    /**
     * After every row completes we evaluate to see if we should be drawing a
     * new total line and summing the results from the previous group.
     */
    public String finishRow() {
        int listindex = this.getList().indexOf(this.getObject());
        ReportableListObject o1 = (ReportableListObject) this.getObject();
        String nextCity = "";

        cityTotal += o1.getAmount();
        grandTotal += o1.getAmount();

        if (listindex == this.getList().size() - 1) {
            nextCity = "XXXXXX"; // Last row hack, it's only a demo folks...
        }
        else {
            nextCity = ((ReportableListObject) this.getList().get(listindex + 1)).getCity();
        }

        StringBuffer sb = new StringBuffer(1000);

        // City subtotals...
        if (!nextCity.equals(o1.getCity())) {
            sb.append("<tr><td>&nbsp;</td><td>&nbsp;</td><td><hr noshade size=\"1\"></td>");
            sb.append("<td>&nbsp;</td></tr>");

            sb.append("<tr><td>&nbsp;</td>");
            sb.append("<td align=\"right\"><b>" + o1.getCity() + " Total:</b></td><td><b>");
            sb.append(cityTotal);
            sb.append("</b></td><td>&nbsp;</td></tr>");
            sb.append("<tr><td colspan=\"4\">&nbsp;</td></tr>");

            cityTotal = 0;
        }

        // Grand totals...
        if (this.getViewIndex() == this.getList().size() - 1) {
            sb.append("<tr><td colspan=\"4\"><hr noshade size=\"1\"></td></tr>");
            sb.append("<tr><td>&nbsp;</td>");
            sb.append("<td align=\"right\"><b>Grand Total:</b></td><td><b>");
            sb.append(grandTotal);
            sb.append("</b></td><td>&nbsp;</td></tr>");
        }

        return sb.toString();
    }
}
