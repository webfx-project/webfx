package naga.core.ngui.displayresultset;

import naga.core.orm.domainmodel.DomainModel;
import naga.core.orm.entity.Entity;
import naga.core.orm.entity.EntityList;
import naga.core.orm.expression.Expression;
import naga.core.type.Type;

/**
 * @author Bruno Salmon
 */
public class EntityListToDisplayResultSetGenerator {

    public static DisplayResultSet createDisplayResultSet(EntityList entityList, String columnExpressionsDefinition, DomainModel domainModel, Object classId) {
        return createDisplayResultSet(entityList, domainModel.parseExpressionArray(columnExpressionsDefinition, classId).getExpressions());
    }

    public static DisplayResultSet createDisplayResultSet(EntityList entityList, Expression[] columnExpressions) {
        DisplayColumn[] displayColumns = new DisplayColumn[columnExpressions.length];
        int columnIndex = 0;
        for (Expression columnExpression : columnExpressions)
            displayColumns[columnIndex++] = new DisplayColumn(columnExpression, columnExpression);
        return createDisplayResultSet(entityList, displayColumns);
    }

    public static DisplayResultSet createDisplayResultSet(EntityList entityList, DisplayColumn[] displayColumns) {
        int rowCount = entityList.size();
        int columnCount = displayColumns.length;
        Type[] columnTypes = new Type[columnCount];
        Object[] headerValues = new Object[columnCount];
        Object[] values = new Object[rowCount * columnCount];
        int columnIndex = 0;
        int index = 0;
        for (DisplayColumn displayColumn : displayColumns) {
            Expression expression = displayColumn.getExpression();
            columnTypes[columnIndex] = expression.getType();
            headerValues[columnIndex++] = displayColumn.getLabel().getText();
            for (Entity entity : entityList)
                values[index++] = entity.evaluate(expression);
        }
        return new DisplayResultSet(rowCount, values, columnTypes, headerValues, null);
    }
}
