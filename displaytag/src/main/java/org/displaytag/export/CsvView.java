package org.displaytag.export;

import org.apache.commons.lang.StringUtils;
import org.displaytag.model.TableModel;


/**
 * Export view for comma separated value exporting.
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class CsvView extends BaseExportView
{

    /**
     * @see org.displaytag.export.BaseExportView#setParameters(TableModel, boolean, boolean, boolean)
     */
    public void setParameters(TableModel tableModel, boolean exportFullList, boolean includeHeader,
        boolean decorateValues)
    {
        super.setParameters(tableModel, exportFullList, includeHeader, decorateValues);
    }

    /**
     * @see org.displaytag.export.BaseExportView#getRowEnd()
     */
    protected String getRowEnd()
    {
        return "\n"; //$NON-NLS-1$
    }

    /**
     * @see org.displaytag.export.BaseExportView#getCellEnd()
     */
    protected String getCellEnd()
    {
        return ","; //$NON-NLS-1$
    }

    /**
     * @see org.displaytag.export.BaseExportView#getAlwaysAppendCellEnd()
     */
    protected boolean getAlwaysAppendCellEnd()
    {
        return false;
    }

    /**
     * @see org.displaytag.export.BaseExportView#getAlwaysAppendRowEnd()
     */
    protected boolean getAlwaysAppendRowEnd()
    {
        return true;
    }

    /**
     * @see org.displaytag.export.ExportView#getMimeType()
     */
    public String getMimeType()
    {
        return "text/csv"; //$NON-NLS-1$
    }

    /**
     * Escaping for csv format.
     * <ul>
     * <li>Quotes inside quoted strings are escaped with a /</li>
     * <li>Fields containings newlines or , are surrounded by ""</li>
     * </ul>
     * Note this is the standard CVS format and it's not handled well by excel.
     * @see org.displaytag.export.BaseExportView#escapeColumnValue(java.lang.Object)
     */
    protected String escapeColumnValue(Object value)
    {
        if (value != null)
        {
            String stringValue = StringUtils.trim(value.toString());
            if (!StringUtils.containsNone(stringValue, new char[]{'\n', ','}))
            {
                return "\"" + //$NON-NLS-1$
                    StringUtils.replace(stringValue, "\"", "\\\"") + "\""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }

            return stringValue;
        }

        return null;
    }

}
