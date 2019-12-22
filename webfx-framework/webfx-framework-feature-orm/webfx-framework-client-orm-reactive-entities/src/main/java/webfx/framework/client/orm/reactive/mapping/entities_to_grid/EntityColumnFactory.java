package webfx.framework.client.orm.reactive.mapping.entities_to_grid;

import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.orm.domainmodel.FieldsGroup;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.ExpressionArray;
import webfx.framework.shared.orm.domainmodel.formatter.ValueFormatter;
import webfx.framework.shared.orm.domainmodel.formatter.FormatterRegistry;
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonArray;
import webfx.platform.shared.services.json.JsonObject;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
public interface EntityColumnFactory {

    EntityColumnFactory DEFAULT = new EntityColumnFactory() {};

    static EntityColumnFactory get() {
        return DEFAULT;
    }

    default <E extends Entity> EntityColumn<E> create(String expressionDefinition, Expression<E> expression, Object label, ValueFormatter displayFormatter, JsonObject json) {
        return new EntityColumnImpl<>(expressionDefinition, expression, label, displayFormatter, json);
    }

    default <E extends Entity> EntityColumn<E> create(String jsonOrExpressionDefinition) {
        if (jsonOrExpressionDefinition.startsWith("{"))
            return create(Json.parseObject(jsonOrExpressionDefinition));
        return create(jsonOrExpressionDefinition, (ValueFormatter) null);
    }

    default <E extends Entity> EntityColumn<E> create(JsonObject json) {
        return create(json.getString("expression"), json);
    }

    default <E extends Entity> EntityColumn<E> create(String expressionDefinition, String jsonOptions) {
        return create(expressionDefinition, Json.parseObject(jsonOptions));
    }

    default <E extends Entity> EntityColumn<E> create(String expressionDefinition, JsonObject options) {
        return create(expressionDefinition, null, options.get("label"), FormatterRegistry.getFormatter(options.getString("format")), options);
    }

    default <E extends Entity> EntityColumn<E> create(String expressionDefinition, ValueFormatter expressionFormatter) {
        return create(expressionDefinition, null, null, expressionFormatter, null);
    }

    default <E extends Entity> EntityColumn<E> create(Expression<E> expression) {
        return create(null, expression, null, null, null);
    }

    default <E extends Entity> EntityColumn<E>[] createArray(int size) {
        return new EntityColumn[size];
    }

    default <E extends Entity>  EntityColumn<E>[] fromJsonArray(String array, DomainModel domainModel, Object domainClassId) {
        EntityColumn<E>[] columns = fromJsonArray(Json.parseArray(array));
        for (EntityColumn<E> column : columns)
            column.parseExpressionDefinitionIfNecessary(domainModel, domainClassId);
        return columns;
    }

    default <E extends Entity>  EntityColumn<E>[] fromJsonArray(String array) {
        return fromJsonArray(Json.parseArray(array));
    }

    default <E extends Entity>  EntityColumn<E>[] fromJsonArray(JsonArray array) {
        int n = array.size();
        EntityColumn<E>[] entityColumns = createArray(n);
        for (int i = 0; i < n; i++) {
            Object element = array.getElement(i);
            entityColumns[i] = element instanceof JsonObject ? create((JsonObject) element) : create(element.toString());
        }
        return entityColumns;
    }

    default <E extends Entity>  EntityColumn<E>[] fromExpressions(Expression<E>[] columnExpressions) {
        columnExpressions = expandFieldsGroups(columnExpressions);
        EntityColumn<E>[] entityColumns = createArray(columnExpressions.length);
        int columnIndex = 0;
        for (Expression<E> columnExpression : columnExpressions)
            entityColumns[columnIndex++] = create(columnExpression);
        return entityColumns;
    }

    default <E extends Entity> ExpressionArray<E> toDisplayExpressionArray(EntityColumn<E>[] columns) {
        return new ExpressionArray<>(Arrays.stream(columns).map(EntityColumn::getDisplayExpression).collect(Collectors.toList()));
    }

    default <E extends Entity> Expression<E>[] expandFieldsGroups(Expression<E>[] columnExpressions) {
        return Arrays.stream(columnExpressions).flatMap(ce -> Arrays.stream(expandFieldsGroup(ce))).toArray(Expression[]::new);
    }

    default <E extends Entity> Expression<E>[] expandFieldsGroup(Expression<E> columnExpression) {
        if (columnExpression instanceof FieldsGroup) {
            columnExpression = ((FieldsGroup) columnExpression).getExpression();
            if (columnExpression instanceof ExpressionArray)
                return expandFieldsGroups(((ExpressionArray) columnExpression).getExpressions());
        }
        return new Expression[]{columnExpression};
    }

    default <E extends Entity> EntityColumn<E>[] fromExpressionsDefinition(String columnExpressionsDefinition, DomainModel domainModel, Object classId) {
        return fromExpressions(domainModel.<E>parseExpressionArray(columnExpressionsDefinition, classId).getExpressions());
    }

    default <E extends Entity> EntityColumn<E>[] fromJsonArrayOrExpressionsDefinition(String jsonOrDef, DomainModel domainModel, Object classId) {
        return (jsonOrDef.startsWith("[")) ?
            fromJsonArray(jsonOrDef, domainModel, classId)
            : fromExpressionsDefinition(jsonOrDef, domainModel, classId);
    }

    // Shortcut method (when class can't be null)
    default <E extends Entity>  EntityColumn<E>[] fromJsonArrayOrExpressionsDefinition(String jsonOrDef, DomainClass domainClass) {
        return fromJsonArrayOrExpressionsDefinition(jsonOrDef, domainClass.getDomainModel(), domainClass);
    }
}
