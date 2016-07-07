package naga.framework.ui.mapping;

import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityList;
import naga.framework.expression.Expression;
import naga.framework.ui.format.Formatter;
import naga.toolkit.spi.display.DisplayResultSet;
import naga.toolkit.spi.display.DisplayResultSetBuilder;
import naga.framework.ui.filter.ExpressionColumn;

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
