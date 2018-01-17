package naga.framework.ui.mapping;

import naga.util.Arrays;
import naga.util.Booleans;
import naga.util.collection.Collections;
import naga.framework.expression.Expression;
import naga.framework.expression.terms.ExpressionArray;
import naga.framework.expression.terms.Select;
import naga.framework.expression.terms.function.AggregateKey;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.domainmodel.DomainModel;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityList;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;
import naga.framework.ui.filter.ExpressionColumn;
import naga.framework.ui.format.Formatter;
import naga.framework.ui.i18n.I18n;
import naga.fxdata.displaydata.DisplayColumn;
import naga.fxdata.displaydata.DisplayResultSet;
import naga.fxdata.displaydata.DisplayResultSetBuilder;
import naga.fxdata.displaydata.Label;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class EntityListToDisplayResultSetGenerator {

    public static DisplayResultSet select(EntityList<? extends Entity> entityList, String select, I18n i18n) {
        int fromIndex = select.indexOf(" from ");
        String columnsDefinition = select.substring(select.indexOf("select") + 6, fromIndex).trim();
        select = "select " + select.substring(fromIndex + 6);
        return select(entityList, select, columnsDefinition, i18n);
    }

    public static DisplayResultSet select(EntityList<? extends Entity> entityList, String select, String columnsDefinition, I18n i18n) {
        return select(entityList, entityList.getStore().getDomainModel().parseSelect(select), columnsDefinition, i18n);
    }

    public static DisplayResultSet select(EntityList<? extends Entity> entityList, Select select, String columnsDefinition, I18n i18n) {
        EntityStore store = entityList.getStore();
        Expression where = select.getWhere();
        if (where != null)
            entityList = EntityList.create(entityList.getListId() + "-filtered", store, Collections.filter(entityList, e -> Booleans.isTrue(e.evaluate(where))));
        ExpressionArray groupBy = select.getGroupBy();
        if (groupBy != null) {
            DomainClass domainClass = (DomainClass) select.getDomainClass();
            Map<GroupValue, DynamicEntity> groupEntities = new HashMap<>();
            for (Entity e : entityList) {
                GroupValue groupValue = new GroupValue(e.evaluate(groupBy));
                DynamicEntity groupEntity = groupEntities.get(groupValue);
                AggregateKey aggregateKey;
                if (groupEntity == null) {
                    aggregateKey = new AggregateKey(groupEntities.size());
                    groupEntity = store.getOrCreateEntity(EntityId.create(domainClass, aggregateKey));
                    groupEntity.copyAllFieldsFrom(e);
                    groupEntities.put(groupValue, groupEntity);
                    aggregateKey = (AggregateKey) groupEntity.getId().getPrimaryKey();
                    aggregateKey.getAggregates().clear();
                } else {
                    aggregateKey = (AggregateKey) groupEntity.getPrimaryKey();
                    //store.copyEntity(e);
                }
                aggregateKey.getAggregates().add(e);
            }
            entityList = EntityList.create(entityList.getListId() + "-grouped", store, groupEntities.values());
        }
        ExpressionArray orderBy = select.getOrderBy();
        if (orderBy != null)
            entityList.orderBy(orderBy);
        return createDisplayResultSet(entityList, columnsDefinition, select.getDomainClass(), i18n);
    }

    public static DisplayResultSet createDisplayResultSet(EntityList<? extends Entity> entityList, String columnsDefinition, Object classId, I18n i18n) {
        return createDisplayResultSet(entityList, columnsDefinition, entityList.getStore().getDomainModel(), classId, i18n);
    }

    public static DisplayResultSet createDisplayResultSet(List<? extends Entity> entityList, String columnsDefinition, DomainModel domainModel, Object classId, I18n i18n) {
        ExpressionColumn[] columns = ExpressionColumn.fromJsonArray(columnsDefinition, domainModel, classId);
        return createDisplayResultSet(entityList, columns, i18n);
    }

    public static DisplayResultSet createDisplayResultSet(List<? extends Entity> entityList, ExpressionColumn[] expressionColumns, I18n i18n) {
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

    private static class GroupValue {
        private final Object[] values;

        public GroupValue(Object value) {
            this.values = (Object[]) value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GroupValue that = (GroupValue) o;

            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            return java.util.Arrays.deepEquals(values, that.values);
        }

        @Override
        public int hashCode() {
            return java.util.Arrays.hashCode(values);
        }
    }

}
