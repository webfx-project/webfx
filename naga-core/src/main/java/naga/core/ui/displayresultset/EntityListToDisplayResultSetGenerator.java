package naga.core.ui.displayresultset;

import naga.core.orm.domainmodel.DomainModel;
import naga.core.orm.entity.Entity;
import naga.core.orm.entity.EntityList;
import naga.core.orm.expression.Expression;
import naga.core.util.function.Converter;

/**
 * @author Bruno Salmon
 */
public class EntityListToDisplayResultSetGenerator {

    public static DisplayResultSet createDisplayResultSet(EntityList entityList, String columnExpressionsDefinition, DomainModel domainModel, Object classId) {
        return createDisplayResultSet(entityList, domainModel.parseExpressionArray(columnExpressionsDefinition, classId).getExpressions());
    }

    public static DisplayResultSet createDisplayResultSet(EntityList entityList, Expression[] columnExpressions) {
        ExpressionColumn[] expressionColumns = new ExpressionColumn[columnExpressions.length];
        int columnIndex = 0;
        for (Expression columnExpression : columnExpressions)
            expressionColumns[columnIndex++] = ExpressionColumn.create(columnExpression);
        return createDisplayResultSet(entityList, expressionColumns);
    }

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
            for (Entity entity : entityList)
                values[index++] = formatValue(entity.evaluate(expression), formatter);
        }
        DisplayResultSet displayResultSet = new DisplayResultSet(rowCount, values, columns);
        //Platform.log("Ok: " + displayResultSet);
        return displayResultSet;
    }

    private static Object formatValue(Object value, Converter formatter) {
        if (formatter != null)
            value = formatter.convert(value);
        return value;
    }
}
