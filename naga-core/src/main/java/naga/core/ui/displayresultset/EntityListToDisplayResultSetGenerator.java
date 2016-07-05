package naga.core.ui.displayresultset;

import naga.core.orm.entity.Entity;
import naga.core.orm.entity.EntityList;
import naga.core.orm.expression.Expression;
import naga.core.format.Formatter;

/**
 * @author Bruno Salmon
 */
public class EntityListToDisplayResultSetGenerator {

    public static DisplayResultSet createDisplayResultSet(EntityList entityList, ExpressionColumn[] expressionColumns) {
        int rowCount = entityList.size();
        int columnCount = expressionColumns.length;
        DisplayResultSetBuilder rsb = DisplayResultSetBuilder.create(rowCount, columnCount);
        int columnIndex = 0;
        int inlineIndex = 0;
        for (ExpressionColumn expressionColumn : expressionColumns) {
            Expression expression = expressionColumn.getExpression();
            Formatter formatter = expressionColumn.getExpressionFormatter();
            rsb.setDisplayColumn(columnIndex++, expressionColumn.getDisplayColumn());
            for (Entity entity : entityList) {
                Object value = entity.evaluate(expression);
                if (formatter != null)
                    value = formatter.format(value);
                rsb.setInlineValue(inlineIndex++, value);
            }
        }
        return rsb.build();
    }

}
