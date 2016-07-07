package mongoose.activities.tester.metrics.model;

import naga.platform.spi.Platform;
import naga.toolkit.spi.display.DisplayResultSet;
import naga.toolkit.spi.display.DisplayResultSetBuilder;
import naga.framework.ui.filter.ExpressionColumn;

import java.util.List;

/**
 * @author Jean-Pierre Alonso.
 */
public class SysBeanToDisplayResultSetGenerator {

    public static DisplayResultSet createDisplayResultSet (List<SysBean> sysList, ExpressionColumn[] expressionColumns){
        Platform.log("createDisplayResultSet(SysBean)");
        int rowCount = sysList.size();
        int columnCount = expressionColumns.length;
        DisplayResultSetBuilder rsb = DisplayResultSetBuilder.create(rowCount, columnCount);
        int columnIndex = 0;
        int inlineIndex = 0;
        for (ExpressionColumn expressionColumn : expressionColumns) {
            rsb.setDisplayColumn(columnIndex++, expressionColumn.getDisplayColumn());
            for (SysBean sysBean : sysList) {
                Object value = sysBean.getTotalMem();
                rsb.setInlineValue(inlineIndex++, value);
            }
        }
        return rsb.build();
    }
}
