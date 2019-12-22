package webfx.framework.client.orm.reactive.mapping.entities_to_visual;

import webfx.extras.label.Label;
import webfx.extras.visual.VisualColumn;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualResultBuilder;
import webfx.framework.client.orm.reactive.mapping.entities_to_grid.EntityColumn;
import webfx.framework.client.services.i18n.I18n;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityList;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.ExpressionArray;
import webfx.framework.shared.orm.expression.terms.Select;
import webfx.framework.shared.orm.expression.terms.function.AggregateKey;
import webfx.framework.shared.orm.domainmodel.formatter.ValueFormatter;
import webfx.platform.shared.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class EntitiesToVisualResultMapper {

    public static VisualResult mapEntitiesToVisualResult(EntityList<? extends Entity> entities, String columnsDefinition, Object classId) {
        return mapEntitiesToVisualResult(entities, columnsDefinition, entities.getDomainModel(), classId);
    }

    public static <E extends Entity> VisualResult mapEntitiesToVisualResult(List<E> entities, String columnsDefinition, DomainModel domainModel, Object classId) {
        EntityColumn<E>[] columns = VisualEntityColumnFactory.get().fromJsonArray(columnsDefinition, domainModel, classId);
        return mapEntitiesToVisualResult(entities, columns);
    }

    public static <E extends Entity> VisualResult mapEntitiesToVisualResult(List<E> entities, EntityColumn<E>[] entityColumns) {
        int rowCount = entities == null ? 0 : entities.size();
        int columnCount = Arrays.length(entityColumns);
        VisualResultBuilder rsb = VisualResultBuilder.create(rowCount, columnCount);
        if (entityColumns != null) {
            int columnIndex = 0;
            int inlineIndex = 0;
            for (EntityColumn<E> entityColumn : entityColumns) {
                // First setting the display column
                VisualColumn visualColumn = ((VisualEntityColumn) entityColumn).getVisualColumn();
                // Translating the label if i18n is provided
                Label label = visualColumn.getLabel();
                Object i18nKey = label.getCode(); // the code used as translation key for i18n
                if (i18nKey != null)
                    label.setText(I18n.getI18nText(i18nKey));
                rsb.setVisualColumn(columnIndex++, visualColumn);
                // Then setting the column values (including possible formatting)
                Expression<E> expression = entityColumn.getDisplayExpression();
                ValueFormatter formatter = entityColumn.getDisplayFormatter();
                if (entities != null)
                    for (Entity entity : entities) {
                        Object value = entity.evaluate(expression);
                        if (formatter != null)
                            value = formatter.formatValue(value);
                        rsb.setInlineValue(inlineIndex++, value);
                    }
            }
        }
        return rsb.build();
    }

    public static VisualResult selectAndMapEntitiesToVisualResult(EntityList<? extends Entity> entities, String select) {
        int fromIndex = select.indexOf(" from ");
        String columnsDefinition = select.substring(select.indexOf("select") + 6, fromIndex).trim();
        select = "select " + select.substring(fromIndex + 6);
        return selectAndMapEntitiesToVisualResult(entities, select, columnsDefinition);
    }

    public static VisualResult selectAndMapEntitiesToVisualResult(EntityList<? extends Entity> entities, String select, String columnsDefinition) {
        return selectAndMapEntitiesToVisualResult(entities, entities.getDomainModel().parseSelect(select), columnsDefinition);
    }

    public static <E extends Entity> VisualResult selectAndMapEntitiesToVisualResult(EntityList<E> entities, Select<E> select, String columnsDefinition) {
        EntityStore store = entities.getStore();
        Expression<E> where = select.getWhere();
        if (where != null)
            entities = EntityList.create(entities.getListId() + "-filtered", store, entities.filter(where));
        ExpressionArray<E> groupBy = select.getGroupBy();
        if (groupBy != null) {
            DomainClass domainClass = (DomainClass) select.getDomainClass();
            Map<GroupValue, E> groupEntities = new HashMap<>();
            for (E e : entities) {
                GroupValue groupValue = new GroupValue(e.evaluate(groupBy));
                E groupEntity = groupEntities.get(groupValue);
                AggregateKey<E> aggregateKey;
                if (groupEntity == null) {
                    aggregateKey = new AggregateKey<>(groupEntities.size());
                    groupEntity = store.getOrCreateEntity(EntityId.create(domainClass, aggregateKey));
                    ((DynamicEntity) groupEntity).copyAllFieldsFrom(e);
                    groupEntities.put(groupValue, groupEntity);
                    aggregateKey = (AggregateKey<E>) groupEntity.getPrimaryKey();
                    aggregateKey.getAggregates().clear();
                } else {
                    aggregateKey = (AggregateKey<E>) groupEntity.getPrimaryKey();
                    //store.copyEntity(e);
                }
                aggregateKey.getAggregates().add(e);
            }
            entities = EntityList.create(entities.getListId() + "-grouped", store, groupEntities.values());
        }
        ExpressionArray<E> orderBy = select.getOrderBy();
        if (orderBy != null)
            entities.orderBy(orderBy.getExpressions());
        return mapEntitiesToVisualResult(entities, columnsDefinition, select.getDomainClass());
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
