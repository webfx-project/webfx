package webfx.framework.shared.orm.mapping;

import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.expression.terms.ExpressionArray;
import webfx.framework.shared.expression.terms.Select;
import webfx.framework.shared.expression.terms.function.AggregateKey;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityList;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.client.services.i18n.I18n;
import webfx.framework.client.ui.filter.ExpressionColumn;
import webfx.framework.client.ui.util.formatter.Formatter;
import webfx.fxkits.extra.displaydata.DisplayColumn;
import webfx.fxkits.extra.displaydata.DisplayResult;
import webfx.fxkits.extra.displaydata.DisplayResultBuilder;
import webfx.fxkits.extra.displaydata.Label;
import webfx.platform.shared.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class EntityListToDisplayResultMapper {

    public static DisplayResult select(EntityList<? extends Entity> entityList, String select) {
        int fromIndex = select.indexOf(" from ");
        String columnsDefinition = select.substring(select.indexOf("select") + 6, fromIndex).trim();
        select = "select " + select.substring(fromIndex + 6);
        return select(entityList, select, columnsDefinition);
    }

    public static DisplayResult select(EntityList<? extends Entity> entityList, String select, String columnsDefinition) {
        return select(entityList, entityList.getDomainModel().parseSelect(select), columnsDefinition);
    }

    public static <E extends Entity> DisplayResult select(EntityList<E> entityList, Select<E> select, String columnsDefinition) {
        EntityStore store = entityList.getStore();
        Expression<E> where = select.getWhere();
        if (where != null)
            entityList = EntityList.create(entityList.getListId() + "-filtered", store, entityList.filter(where));
        ExpressionArray<E> groupBy = select.getGroupBy();
        if (groupBy != null) {
            DomainClass domainClass = (DomainClass) select.getDomainClass();
            Map<GroupValue, E> groupEntities = new HashMap<>();
            for (E e : entityList) {
                GroupValue groupValue = new GroupValue(e.evaluate(groupBy));
                E groupEntity = groupEntities.get(groupValue);
                AggregateKey<E> aggregateKey;
                if (groupEntity == null) {
                    aggregateKey = new AggregateKey<>(groupEntities.size());
                    groupEntity = store.getOrCreateEntity(EntityId.create(domainClass, aggregateKey));
                    ((DynamicEntity) groupEntity).copyAllFieldsFrom(e);
                    groupEntities.put(groupValue, groupEntity);
                    aggregateKey = (AggregateKey) groupEntity.getPrimaryKey();
                    aggregateKey.getAggregates().clear();
                } else {
                    aggregateKey = (AggregateKey) groupEntity.getPrimaryKey();
                    //store.copyEntity(e);
                }
                aggregateKey.getAggregates().add(e);
            }
            entityList = EntityList.create(entityList.getListId() + "-grouped", store, groupEntities.values());
        }
        ExpressionArray<E> orderBy = select.getOrderBy();
        if (orderBy != null)
            entityList.orderBy(orderBy.getExpressions());
        return createDisplayResult(entityList, columnsDefinition, select.getDomainClass());
    }

    public static DisplayResult createDisplayResult(EntityList<? extends Entity> entityList, String columnsDefinition, Object classId) {
        return createDisplayResult(entityList, columnsDefinition, entityList.getDomainModel(), classId);
    }

    public static DisplayResult createDisplayResult(List<? extends Entity> entityList, String columnsDefinition, DomainModel domainModel, Object classId) {
        ExpressionColumn[] columns = ExpressionColumn.fromJsonArray(columnsDefinition, domainModel, classId);
        return createDisplayResult(entityList, columns);
    }

    public static <E extends Entity> DisplayResult createDisplayResult(List<E> entityList, ExpressionColumn[] expressionColumns) {
        int rowCount = entityList == null ? 0 : entityList.size();
        int columnCount = Arrays.length(expressionColumns);
        DisplayResultBuilder rsb = DisplayResultBuilder.create(rowCount, columnCount);
        if (expressionColumns != null) {
            int columnIndex = 0;
            int inlineIndex = 0;
            for (ExpressionColumn expressionColumn : expressionColumns) {
                // First setting the display column
                DisplayColumn displayColumn = expressionColumn.getDisplayColumn();
                // Translating the label if i18n is provided
                Label label = displayColumn.getLabel();
                String translationKey = label.getCode(); // the code used as translation key for i18n
                if (translationKey != null)
                    label.setText(I18n.instantTranslate(translationKey));
                rsb.setDisplayColumn(columnIndex++, displayColumn);
                // Then setting the column values (including possible formatting)
                Expression<E> expression = expressionColumn.getDisplayExpression();
                Formatter formatter = expressionColumn.getDisplayFormatter();
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

    private static final class GroupValue {
        private final Object[] values;

        GroupValue(Object value) {
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
