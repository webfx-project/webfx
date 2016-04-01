package naga.core.ngui.displayresult;

import naga.core.orm.domainmodel.DomainModel;
import naga.core.orm.entity.Entity;
import naga.core.orm.entity.EntityList;
import naga.core.orm.expression.Expression;
import naga.core.type.Type;

/**
 * @author Bruno Salmon
 */
public class EntityListToDisplayResultGenerator {

    public static DisplayResult createDisplayResult(EntityList entityList, String columnExpressionsDefinition, DomainModel domainModel, Object classId) {
        return createDisplayResult(entityList, domainModel.parseExpressionArray(columnExpressionsDefinition, classId).getExpressions());
    }

    public static DisplayResult createDisplayResult(EntityList entityList, Expression[] columnExpressions) {
        DisplayColumn[] columnMappings = new DisplayColumn[columnExpressions.length];
        int columnIndex = 0;
        for (Expression columnExpression : columnExpressions)
            columnMappings[columnIndex++] = new DisplayColumn(columnExpression, columnExpression);
        return createDisplayResult(entityList, columnMappings);
    }

    public static DisplayResult createDisplayResult(EntityList entityList, DisplayColumn[] columnMappings) {
        int rowCount = entityList.size();
        int columnCount = columnMappings.length;
        Type[] columnTypes = new Type[columnCount];
        Object[] headerValues = new Object[columnCount];
        Object[] values = new Object[rowCount * columnCount];
        int columnIndex = 0;
        int index = 0;
        for (DisplayColumn columnMapping : columnMappings) {
            Expression expression = columnMapping.getExpression();
            columnTypes[columnIndex] = expression.getType();
            headerValues[columnIndex++] = columnMapping.getLabel().getText();
            for (Entity entity : entityList)
                values[index++] = entity.evaluate(expression);
        }
        return new DisplayResult(rowCount, values, columnTypes, headerValues, null);
    }
}
