package mongoose.activities.tester.monitor.controller;

import mongoose.activities.tester.monitor.model.SysBean;
import naga.core.format.Formatter;
import naga.core.orm.expression.Expression;
import naga.core.ui.displayresultset.DisplayColumn;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.ui.displayresultset.ExpressionColumn;

import java.util.List;

/**
 * @author Jean-Pierre Alonso.
 */
public class SysBeanToDisplayResultSetGenerator {

    public static DisplayResultSet createDisplayResultSet (List<SysBean> sysList, ExpressionColumn[] expressionColumns){
        //Platform.log("createDisplayResultSet()");
        int rowCount = sysList.size();
        int columnCount = expressionColumns.length;
        DisplayColumn[] columns = new DisplayColumn[columnCount];
        Object[] values = new Object[rowCount * columnCount];
        int columnIndex = 0;
        int index = 0;
        for (ExpressionColumn expressionColumn : expressionColumns) {
            Expression expression = expressionColumn.getExpression();
            Formatter formatter = expressionColumn.getExpressionFormatter();
            columns[columnIndex++] = expressionColumn.getDisplayColumn();
            for (SysBean sysBean : sysList) {
                Object value = sysBean.getTotalMem();
                if (formatter != null)
                    value = formatter.format(value);
                values[index++] = value;
            }
        }
        DisplayResultSet displayResultSet = new DisplayResultSet(rowCount, values, columns);
        //Platform.log("Ok: " + displayResultSet);
        return displayResultSet;
    }
}
