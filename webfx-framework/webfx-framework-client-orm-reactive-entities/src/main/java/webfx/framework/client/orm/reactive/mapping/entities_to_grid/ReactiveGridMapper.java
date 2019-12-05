package webfx.framework.client.orm.reactive.mapping.entities_to_grid;

import javafx.application.Platform;
import webfx.framework.client.orm.dql.DqlStatement;
import webfx.framework.client.orm.dql.DqlStatementBuilder;
import webfx.framework.client.orm.reactive.dql.query.ReactiveDqlQuery;
import webfx.framework.client.orm.reactive.dql.statement.ReactiveDqlStatement;
import webfx.framework.client.orm.reactive.mapping.dql_to_entities.ReactiveEntityMapper;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityList;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.ExpressionArray;
import webfx.platform.shared.util.async.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class ReactiveGridMapper<E extends Entity> {

    protected final ReactiveEntityMapper<E> reactiveEntityMapper;

    protected EntityColumn<E>[] entityColumns;
    protected boolean autoSelectSingleRow;
    protected boolean selectFirstRowOnFirstDisplay;
    private boolean applyDomainModelRowStyle;
    protected boolean startsWithEmptyTable = true;
    protected Handler<E> selectedEntityHandler;
    private String columnsPersistentFields;

    public ReactiveGridMapper(ReactiveEntityMapper<E> reactiveEntityMapper) {
        this.reactiveEntityMapper = reactiveEntityMapper;
        reactiveEntityMapper.addEntitiesHandler(this::onEntityListChanged);
        ReactiveDqlStatement<E> reactiveDqlStatement = reactiveEntityMapper.getReactiveDqlQuery().getReactiveDqlStatement();
        reactiveDqlStatement.addResultTransformer(dqlStatement -> {
            // If the resulting dql statement defines columns (ex: from a fields filter), we take them as the columns to display
            if (dqlStatement.getColumns() != null)
                setEntityColumnsPrivate(parseEntityColumnsDefinition(dqlStatement.getColumns()));
            // If no columns have been defined so far (neither from explicit setEntityColumns() call nor from resulting dql statement)
            if (entityColumns == null)
                return DqlStatement.EMPTY_STATEMENT; // We prevent the server call as there is nothing to show
            updateColumnsPersistentFields();
            // If as a result of applying the columns we have additional persistent fields to load, we include them in the final dql statement
            if (columnsPersistentFields != null)
                dqlStatement = new DqlStatementBuilder(dqlStatement).mergeFields(columnsPersistentFields).build();
            if (startsWithEmptyTable && reactiveEntityMapper.getEntityList() == null)
                Platform.runLater(() -> {
                    if (reactiveEntityMapper.getEntityList() == null)
                        onEntityListChanged(null);
                });

            return dqlStatement;
        });
        //reactiveDqlStatement.combine(persistentFieldsDqlStatementProperty);
    }

    public ReactiveEntityMapper<E> getReactiveEntityMapper() {
        return reactiveEntityMapper;
    }

    public EntityColumn<E>[] getEntityColumns() {
        return entityColumns;
    }

    public EntityList<E> getCurrentEntityList() {
        return reactiveEntityMapper.getCurrentEntityList();
    }

    public abstract List<E> getSelectedEntities();

    public abstract E getSelectedEntity();

    public ReactiveGridMapper<E> autoSelectSingleRow() {
        autoSelectSingleRow = true;
        return this;
    }

    protected ReactiveGridMapper<E> selectFirstRowOnFirstDisplay() {
        selectFirstRowOnFirstDisplay = true;
        return this;
    }

    public ReactiveGridMapper<E> setSelectedEntityHandler(Handler<E> selectedEntityHandler) {
        this.selectedEntityHandler = selectedEntityHandler;
        return this;
    }

    protected EntityColumnFactory getEntityColumnFactory() {
        return EntityColumnFactory.get();
    }

    public ReactiveGridMapper<E> setEntityColumns(String jsonArrayOrExpressionDefinition) {
        setEntityColumns(parseEntityColumnsDefinition(jsonArrayOrExpressionDefinition));
        return this;
    }

    @SuppressWarnings("unchecked")
    private EntityColumn<E>[] parseEntityColumnsDefinition(String jsonArrayOrExpressionDefinition) {
        ReactiveDqlQuery<E> reactiveDqlQuery = reactiveEntityMapper.getReactiveDqlQuery();
        Object[] holder = new Object[1];
        reactiveDqlQuery.executeParsingCode(() -> holder[0] = getEntityColumnFactory().fromJsonArrayOrExpressionsDefinition(jsonArrayOrExpressionDefinition, reactiveDqlQuery.getDomainModel(), reactiveDqlQuery.getReactiveDqlStatement().getDomainClassId()));
        return (EntityColumn<E>[]) holder[0];
    }

    public ReactiveGridMapper<E> setEntityColumns(EntityColumn<E>... entityColumns) {
        setEntityColumnsPrivate(entityColumns);
        // Asking a dql refresh
        // reactiveDqlStatement.refreshResultTransform();
        return this;
    }

    private void setEntityColumnsPrivate(EntityColumn<E>[] entityColumns) {
        this.entityColumns = entityColumns;
        markColumnsPersistentFieldsAsOutOfDate();
        if (startsWithEmptyTable && reactiveEntityMapper.getEntityList() == null)
            scheduleEmptyTable();
    }

    private boolean emptyTableScheduled;

    private void scheduleEmptyTable() {
        if (!emptyTableScheduled) {
            emptyTableScheduled = true;
            Platform.runLater(() -> {
                //emptyTableScheduled = false; // Commented as doing it only once
                if (startsWithEmptyTable && reactiveEntityMapper.getEntityList() == null)
                    onEntityListChanged(null);
            });
        }
    }

    public ReactiveGridMapper<E> applyDomainModelRowStyle() {
        applyDomainModelRowStyle = true;
        markColumnsPersistentFieldsAsOutOfDate();
        return this;
    }

    private void markColumnsPersistentFieldsAsOutOfDate() {
        columnsPersistentFields = null;
    }

    private void updateColumnsPersistentFields() {
        if (columnsPersistentFields == null) {
            if (applyDomainModelRowStyle)
                applyDomainModelRowStyleNow();
            collectColumnsPersistentFields();
        }
    }

    private void applyDomainModelRowStyleNow() {
        DomainClass domainClass = getDomainClass();
        ExpressionArray<E> rowStylesExpressionArray = domainClass.getStyleClassesExpressionArray();
        if (rowStylesExpressionArray != null && entityColumns != null) {
            EntityColumn<E>[] includingRowStyleColumns = new EntityColumn[entityColumns.length + 1];
            includingRowStyleColumns[0] = createStyleEntityColumn(rowStylesExpressionArray);
            System.arraycopy(entityColumns, 0, includingRowStyleColumns, 1, entityColumns.length);
            setEntityColumnsPrivate(includingRowStyleColumns);
        }
    }

    private void collectColumnsPersistentFields() {
        if (entityColumns == null)
            columnsPersistentFields = null;
        else {
            List<Expression<E>> columnsPersistentTerms = new ArrayList<>();
            ReactiveDqlQuery<E> reactiveDqlQuery = reactiveEntityMapper.getReactiveDqlQuery();
            reactiveDqlQuery.executeParsingCode(() -> {
                DomainModel domainModel = reactiveDqlQuery.getDomainModel();
                Object domainClassId = reactiveDqlQuery.getDomainClassId();
                for (EntityColumn<E> entityColumn : entityColumns) {
                    entityColumn.parseExpressionDefinitionIfNecessary(domainModel, domainClassId);
                    entityColumn.getDisplayExpression().collectPersistentTerms(columnsPersistentTerms);
                }
            });
            columnsPersistentFields = new ExpressionArray<>(columnsPersistentTerms).toString();
        }
    }

    protected abstract EntityColumn<E> createStyleEntityColumn(ExpressionArray<E> rowStylesExpressionArray);

    private DomainClass getDomainClass() {
        return reactiveEntityMapper.getReactiveDqlQuery().getDomainClass();
    }

    protected abstract void onEntityListChanged(EntityList<E> entityList);
}
