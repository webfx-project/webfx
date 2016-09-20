package naga.framework.ui.mapping;

import naga.commons.util.Arrays;
import naga.framework.expression.Expression;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityList;
import naga.framework.ui.filter.ExpressionColumn;
import naga.framework.ui.format.Formatter;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplayResultSetBuilder;
import naga.toolkit.display.Label;

/**
 * @author Bruno Salmon
 */
public class EntityListToDisplayResultSetGenerator {

    public static DisplayResultSet createDisplayResultSet(EntityList entityList, ExpressionColumn[] expressionColumns, I18n i18n) {
        int rowCount = entityList == null ? 0 : entityList.size();
        int columnCount = Arrays.length(expressionColumns);
        DisplayResultSetBuilder rsb = DisplayResultSetBuilder.create(rowCount, columnCount);
        if (expressionColumns != null) {
            int columnIndex = 0;
            int inlineIndex = 0;
            for (ExpressionColumn expressionColumn : expressionColumns) {
                // First setting the display column
                DisplayColumn displayColumn = expressionColumn.getDisplayColumn();
                if (i18n != null) { // translating the label if i18n is provided
                    Label label = displayColumn.getLabel();
                    String translationKey = label.getCode(); // the code used as translation key for i18n
                    if (translationKey != null)
                        label.setText(i18n.instantTranslate(translationKey));
                }
                rsb.setDisplayColumn(columnIndex++, displayColumn);
                // Then setting the column values (including possible formatting)
                Expression expression = expressionColumn.getExpression();
                Formatter formatter = expressionColumn.getExpressionFormatter();
                if (entityList != null)
                    for (Entity entity : entityList) {
                        Object value = entity.evaluate(expression);
                        if (formatter != null)
                            value = formatter.format(value);
                        rsb.setInlineValue(inlineIndex++, value);
                    }
            }
        }
        return rsb.build();
    }

}
