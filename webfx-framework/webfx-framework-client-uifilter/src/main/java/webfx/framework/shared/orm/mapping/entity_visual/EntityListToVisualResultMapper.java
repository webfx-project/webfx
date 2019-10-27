package webfx.framework.shared.orm.mapping.entity_visual;

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
import webfx.framework.shared.util.formatter.Formatter;
import webfx.extras.visual.VisualColumn;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualResultBuilder;
import webfx.extras.label.Label;
import webfx.platform.shared.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class EntityListToVisualResultMapper {

    public static VisualResult select(EntityList<? extends Entity> entityList, String select) {
        int fromIndex = select.indexOf(" from ");
        String columnsDefinition = select.substring(select.indexOf("select") + 6, fromIndex).trim();
        select = "select " + select.substring(fromIndex + 6);
        return select(entityList, select, columnsDefinition);
    }

    public static VisualResult select(EntityList<? extends Entity> entityList, String select, String columnsDefinition) {
        return select(entityList, entityList.getDomainModel().parseSelect(select), columnsDefinition);
    }

    public static <E extends Entity> VisualResult select(EntityList<E> entityList, Select<E> select, String columnsDefinition) {
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
        return createVisualResult(entityList, columnsDefinition, select.getDomainClass());
    }

    public static VisualResult createVisualResult(EntityList<? extends Entity> entityList, String columnsDefinition, Object classId) {
        return createVisualResult(entityList, columnsDefinition, entityList.getDomainModel(), classId);
    }

    public static VisualResult createVisualResult(List<? extends Entity> entityList, String columnsDefinition, DomainModel domainModel, Object classId) {
        ExpressionColumn[] columns = ExpressionColumn.fromJsonArray(columnsDefinition, domainModel, classId);
        return createVisualResult(entityList, columns);
    }

    public static <E extends Entity> VisualResult createVisualResult(List<E> entityList, ExpressionColumn[] expressionColumns) {
        int rowCount = entityList == null ? 0 : entityList.size();
        int columnCount = Arrays.length(expressionColumns);
        VisualResultBuilder rsb = VisualResultBuilder.create(rowCount, columnCount);
        if (expressionColumns != null) {
            int columnIndex = 0;
            int inlineIndex = 0;
            for (ExpressionColumn expressionColumn : expressionColumns) {
                // First setting the display column
                VisualColumn visualColumn = expressionColumn.getVisualColumn();
                // Translating the label if i18n is provided
                Label label = visualColumn.getLabel();
                Object i18nKey = label.getCode(); // the code used as translation key for i18n
                if (i18nKey != null)
                    label.setText(I18n.getI18nText(i18nKey));
                rsb.setVisualColumn(columnIndex++, visualColumn);
                // Then setting the column values (including possible formatting)
                Expression<E> expression = expressionColumn.getVisualExpression();
                Formatter formatter = expressionColumn.getVisualFormatter();
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
