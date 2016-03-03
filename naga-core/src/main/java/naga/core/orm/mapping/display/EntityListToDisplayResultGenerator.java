package naga.core.orm.mapping.display;

import naga.core.orm.domainmodel.DomainModel;
import naga.core.orm.domainmodel.Label;
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
        DisplayColumnMapping[] columnMappings = new DisplayColumnMapping[columnExpressions.length];
        int columnIndex = 0;
        for (Expression columnExpression : columnExpressions)
            columnMappings[columnIndex++] = new DisplayColumnMapping(columnExpression, Label.from(columnExpression));
        return createDisplayResult(entityList, columnMappings);
    }

    public static DisplayResult createDisplayResult(EntityList entityList, DisplayColumnMapping[] columnMappings) {
        int rowCount = entityList.size();
        int columnCount = columnMappings.length;
        Type[] columnTypes = new Type[columnCount];
        Object[] headerValues = new Object[columnCount];
        int columnIndex = 0;
        for (DisplayColumnMapping columnMapping : columnMappings) {
            columnTypes[columnIndex] = columnMapping.getExpression().getType();
            headerValues[columnIndex++] = columnMapping.getLabel().getText();
        }
        Object[] values = new Object[rowCount * columnCount];
        int index = 0;
        for (DisplayColumnMapping columnMapping : columnMappings)
            for (Entity entity : entityList)
                values[index++] = entity.evaluate(columnMapping.getExpression());
        return new DisplayResult(rowCount, values, columnTypes, headerValues, null);
    }
}
