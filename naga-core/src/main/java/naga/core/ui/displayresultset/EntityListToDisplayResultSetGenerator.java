package naga.core.ui.displayresultset;

import naga.core.orm.entity.Entity;
import naga.core.orm.entity.EntityList;
import naga.core.orm.expression.Expression;
import naga.core.util.function.Converter;

/**
 * @author Bruno Salmon
 */
public class EntityListToDisplayResultSetGenerator {

    public static DisplayResultSet createDisplayResultSet(EntityList entityList, ExpressionColumn[] expressionColumns) {
        //Platform.log("createDisplayResultSet()");
        int rowCount = entityList.size();
        int columnCount = expressionColumns.length;
        DisplayColumn[] columns = new DisplayColumn[columnCount];
        Object[] values = new Object[rowCount * columnCount];
        int columnIndex = 0;
        int index = 0;
        for (ExpressionColumn expressionColumn : expressionColumns) {
            Expression expression = expressionColumn.getExpression();
            Converter formatter = expressionColumn.getExpressionFormatter();
            columns[columnIndex++] = expressionColumn.getDisplayColumn();
            for (Entity entity : entityList) {
                Object value = entity.evaluate(expression);
                if (formatter != null)
                    value = formatter.convert(value);
                values[index++] = value;
            }
        }
        DisplayResultSet displayResultSet = new DisplayResultSet(rowCount, values, columns);
        //Platform.log("Ok: " + displayResultSet);
        return displayResultSet;
    }

}
