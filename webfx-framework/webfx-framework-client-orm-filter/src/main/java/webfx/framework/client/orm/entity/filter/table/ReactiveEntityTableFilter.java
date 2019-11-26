package webfx.framework.client.orm.entity.filter.table;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import webfx.framework.client.orm.entity.filter.DqlStatement;
import webfx.framework.client.orm.entity.filter.DqlStatementBuilder;
import webfx.framework.client.orm.entity.filter.ReactiveEntityFilter;
import webfx.framework.client.services.i18n.I18n;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityList;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.ExpressionArray;
import webfx.kit.util.properties.Properties;
import webfx.platform.shared.services.json.JsonArray;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.util.async.Handler;
import webfx.platform.shared.util.collection.Collections;
import webfx.platform.shared.util.function.Callable;
import webfx.platform.shared.util.function.Converter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public abstract class ReactiveEntityTableFilter<E extends Entity> extends ReactiveEntityFilter<E> {

    protected final List<TableDisplay> tableDisplays = new ArrayList<>();
    private TableDisplay currentTableDisplay;
    private boolean startsWithEmptyResult = true;

    /*=================================
      ========= Constructors ==========
      =================================*/

    public ReactiveEntityTableFilter() {}

    public ReactiveEntityTableFilter(Object jsonOrClass) {
        super(jsonOrClass);
    }

    public ReactiveEntityTableFilter(ReactiveEntityFilter<?> parent) {
        super(parent);
    }

    public ReactiveEntityTableFilter(ReactiveEntityFilter<?> parent, Object jsonOrClass) {
        super(parent, jsonOrClass);
    }


    /*=================================
      ======== New fluent API =========
      =================================*/

    public ReactiveEntityTableFilter<E> setStartsWithEmptyResult(boolean startsWithEmptyResult) {
        this.startsWithEmptyResult = startsWithEmptyResult;
        return this;
    }

    public ReactiveEntityTableFilter<E> nextDisplay() {
        goToNextTableDisplayIfSet();
        return this;
    }

    public ReactiveEntityTableFilter<E> setSelectedEntityHandler(Handler<E> entityHandler) {
        getCurrentTableDisplay().setSelectedEntityHandler(entityHandler);
        return this;
    }

    public ReactiveEntityTableFilter<E> selectFirstRowOnFirstDisplay() {
        getCurrentTableDisplay().selectFirstRowOnFirstDisplay();
        return this;
    }

    public ReactiveEntityTableFilter<E> autoSelectSingleRow() {
        getCurrentTableDisplay().autoSelectSingleRow();
        return this;
    }

    public ReactiveEntityTableFilter<E> setEntityColumns(String jsonArrayOrExpressionDefinition) {
        getCurrentTableDisplay().setEntityColumns(jsonArrayOrExpressionDefinition);
        return this;
    }

    public ReactiveEntityTableFilter<E> setEntityColumns(JsonArray array) {
        getCurrentTableDisplay().setEntityColumns(array);
        return this;
    }

    public ReactiveEntityTableFilter<E> setEntityColumns(EntityColumn<E>... entityColumns) {
        getCurrentTableDisplay().setEntityColumns(entityColumns);
        return this;
    }

    public ReactiveEntityTableFilter<E> applyDomainModelRowStyle() {
        getCurrentTableDisplay().applyDomainModelRowStyle();
        return this;
    }


    /*=============================
      === New other public API  ===
      =============================*/

    public E getSelectedEntity() {
        return getSelectedEntity(0);
    }

    public E getSelectedEntity(int displayIndex) {
        return getTableDisplay(displayIndex).getSelectedEntity();
    }

    public List<E> getSelectedEntities() {
        return getCurrentTableDisplay().getSelectedEntities();
    }

    public EntityList<E> getCurrentEntityList() {
        return getCurrentEntityList(0);
    }

    public EntityList<E> getCurrentEntityList(int displayIndex) {
        return getTableDisplay(displayIndex).getCurrentEntityList();
    }

    public EntityColumn<E>[] getEntityColumns() {
        return getCurrentTableDisplay().getEntityColumns();
    }


    /*===============================
      === Internal implementation ===
      ===============================*/

    @Override
    protected QueryArgument queryArgumentFetcher() {
        QueryArgument queryArgument = super.queryArgumentFetcher();
        // Initializing the display with empty results (no rows but columns) so the component (probably a table) display the columns before calling the server
        if (queryArgument != null && startsWithEmptyResult && Collections.isEmpty(getCurrentEntityList()))
            refreshAllTableDisplays(true);
        return queryArgument;
    }

    @Override
    protected void onSkippingQueryCallAsSameArgument() {
        super.onSkippingQueryCallAsSameArgument(); // Logging
        // Even if the query argument hasn't changed, the columns may have changed (ex: bookings prices and contact columns result into same query argument)
        refreshAllTableDisplays(false); // Forcing a refresh
    }

    @Override
    protected void onBeforeQueryCall() {
        // Initializing the display with empty results (no rows but columns) so the component (probably a table) display the columns before calling the server
        if (startsWithEmptyResult && Collections.isEmpty(getCurrentEntityList()))
            Platform.runLater(() -> refreshAllTableDisplays(true)); // Running this later to not slow the call
    }

    @Override
    protected DqlStatement mergeDqlStatements() {
        DqlStatementBuilder mergeBuilder = new DqlStatementBuilder(getDomainClassId());
        Iterator<TableDisplay> it = tableDisplays.iterator();
        currentTableDisplay = Collections.next(it);
        for (int i = 0; i < dqlStatementProperties.size(); i++) {
            mergeBuilder.merge(dqlStatementProperties.get(i).getValue());
            if (currentTableDisplay != null && currentTableDisplay.dqlStatementPropertiesLastIndex == i) {
                String columns = mergeBuilder.getColumns();
                if (columns != null) {
                    currentTableDisplay.setEntityColumns(columns);
                    mergeBuilder.setColumns(null);
                }
                currentTableDisplay = Collections.next(it);
            }
        }
        // If expression columns have persistent fields, we include them in the fields to load
        for (TableDisplay tableDisplay : tableDisplays) {
            List<Expression<E>> columnsPersistentTerms = tableDisplay.collectColumnsPersistentTerms();
            if (!columnsPersistentTerms.isEmpty())
                mergeBuilder.mergeFields(new ExpressionArray<>(columnsPersistentTerms).toString());
        }
        return mergeBuilder.build();
    }

    private void goToNextTableDisplayIfSet() {
        if (currentTableDisplay != null && currentTableDisplay.isTableSet()) {
            currentTableDisplay.dqlStatementPropertiesLastIndex = dqlStatementProperties.size() - 1;
            currentTableDisplay = null;
        }
    }

    protected TableDisplay getCurrentTableDisplay() {
        if (isStarted())
            currentTableDisplay = getTableDisplay(0);
        else if (currentTableDisplay == null)
            tableDisplays.add(currentTableDisplay = createTableDisplay());
        return currentTableDisplay;
    }

    protected TableDisplay getTableDisplay(int displayIndex) {
        if (tableDisplays.isEmpty())
            tableDisplays.add(currentTableDisplay = createTableDisplay());
        return tableDisplays.get(displayIndex);
    }


    protected void onEntityListChanged() {
        if (!tableDisplays.isEmpty() && tableDisplays.get(0).isTableSet())
            updateEntitiesToTableMapping();
        else
            super.onEntityListChanged();
    }

    protected EntityColumnFactory getEntityColumnFactory() {
        return EntityColumnFactory.get();
    }

    private void refreshAllTableDisplays(boolean empty) {
        for (TableDisplay tableDisplay : tableDisplays)
            tableDisplay.refreshTable(empty);
    }


    /*=================================
      ========== Abstract API =========
      =================================*/

    protected abstract void updateEntitiesToTableMapping();

    protected abstract void clearCacheForNextTableDisplays();

    protected abstract TableDisplay createTableDisplay();

    protected abstract class TableDisplay {

        protected EntityColumn<E>[] entityColumns;
        protected boolean selectFirstRowOnFirstDisplay;
        protected boolean autoSelectSingleRow;
        int dqlStatementPropertiesLastIndex = -1;
        boolean appliedDomainModelRowStyle;
        List<Expression<E>> columnsPersistentTerms;

        protected abstract boolean isTableSet();

        protected abstract EntityColumn<E> createStyleEntityColumn(ExpressionArray<E> rowStylesExpressionArray);

        protected abstract E getSelectedEntity();

        protected abstract void setSelectedEntityHandler(Handler<E> entityHandler);

        protected abstract List<E> getSelectedEntities();

        protected abstract void refreshTable(boolean empty);

        protected void selectFirstRowOnFirstDisplay() {
            selectFirstRowOnFirstDisplay = true;
        }

        void autoSelectSingleRow() {
            autoSelectSingleRow = true;
        }

        protected E getEntityAt(int row) {
            if (row >= 0)
                return getCurrentEntityList().get(row);
            return null;
        }

        EntityList<E> getCurrentEntityList() {
            return getStore().getOrCreateEntityList(listId);
        }

        void setEntityColumns(JsonArray array) {
            setEntityColumns(getEntityColumnFactory().fromJsonArray(array));
        }

        void setEntityColumns(EntityColumn<E>[] entityColumns) {
            setEntityColumnsPrivate(entityColumns);
            if (appliedDomainModelRowStyle)
                applyDomainModelRowStyle();
            refreshWhenActive();
        }

        void setEntityColumns(String jsonOrDefColumns) {
            executeParsingCode(() -> setEntityColumns(getEntityColumnFactory().fromJsonArrayOrExpressionsDefinition(jsonOrDefColumns, getDomainModel(), getDomainClassId())));
        }

        void setEntityColumnsPrivate(EntityColumn<E>[] entityColumns) {
            this.entityColumns = entityColumns;
            columnsPersistentTerms = null; // forcing re-computation on next collectColumnsPersistentTerms() call since the columns have changed
        }

        public EntityColumn<E>[] getEntityColumns() {
            return entityColumns;
        }

        void applyDomainModelRowStyle() {
            DomainClass domainClass = getDomainClass();
            ExpressionArray<E> rowStylesExpressionArray = domainClass.getStyleClassesExpressionArray();
            if (rowStylesExpressionArray != null && entityColumns != null) {
                EntityColumn<E>[] includingRowStyleColumns = new EntityColumn[entityColumns.length + 1];
                includingRowStyleColumns[0] = createStyleEntityColumn(rowStylesExpressionArray);
                System.arraycopy(entityColumns, 0, includingRowStyleColumns, 1, entityColumns.length);
                setEntityColumnsPrivate(includingRowStyleColumns);
            }
            appliedDomainModelRowStyle = true;
        }

        protected List<Expression<E>> collectColumnsPersistentTerms() {
            if (columnsPersistentTerms == null) {
                columnsPersistentTerms = new ArrayList<>();
                if (entityColumns != null)
                    executeParsingCode(() -> {
                        DomainModel domainModel = getDomainModel();
                        for (EntityColumn<E> entityColumn : entityColumns) {
                            entityColumn.parseExpressionDefinitionIfNecessary(domainModel, getDomainClassId());
                            entityColumn.getDisplayExpression().collectPersistentTerms(columnsPersistentTerms);
                        }
                    });
            }
            return columnsPersistentTerms;
        }
    }


    /*=======================================
      ====== Fluent API (class upgrade) =====
      ======================================*/

    public ReactiveEntityTableFilter<E> start() {
        // The following call is to set dqlStatementPropertiesLastIndex on the latest tableDisplay
        goToNextTableDisplayIfSet();
        // Also adding a listener reacting to a language change by updating the columns translations immediately (without making a new server request)
        Properties.runOnPropertiesChange(new Consumer<ObservableValue/*GWT*/>() {
            private boolean dictionaryChanged;

            @Override
            public void accept(ObservableValue p) {
                dictionaryChanged |= p == I18n.dictionaryProperty();
                if (dictionaryChanged) {
                    clearCacheForNextTableDisplays(); // Clearing the cache to have a fresh display result set next time it is active
                    if (isActive()) {
                        refreshAllTableDisplays(false);
                        dictionaryChanged = false;
                    }
                } else
                    refreshWhenActive();
            }
        }, I18n.dictionaryProperty(), activeProperty());
        return (ReactiveEntityTableFilter<E>) super.start();
    }

    public ReactiveEntityTableFilter<E> stop() {
        return (ReactiveEntityTableFilter<E>) super.stop();
    }

    public ReactiveEntityTableFilter<E> setDataSourceModel(DataSourceModel dataSourceModel) {
        return (ReactiveEntityTableFilter<E>) super.setDataSourceModel(dataSourceModel);
    }

    public ReactiveEntityTableFilter<E> setStore(EntityStore store) {
        return (ReactiveEntityTableFilter<E>) super.setStore(store);
    }

    public ReactiveEntityTableFilter<E> setListId(Object listId) {
        return (ReactiveEntityTableFilter<E>) super.setListId(listId);
    }

    public ReactiveEntityTableFilter<E> setRestrictedFilterList(List<E> restrictedFilterList) {
        return (ReactiveEntityTableFilter<E>) super.setRestrictedFilterList(restrictedFilterList);
    }

    public ReactiveEntityTableFilter<E> setEntitiesHandler(Handler<EntityList<E>> entitiesHandler) {
        return (ReactiveEntityTableFilter<E>) super.setEntitiesHandler(entitiesHandler);
    }

    public ReactiveEntityTableFilter<E> bindActivePropertyTo(ObservableValue<Boolean> activeProperty) {
        return (ReactiveEntityTableFilter<E>) super.bindActivePropertyTo(activeProperty);
    }

    public ReactiveEntityTableFilter<E> setActive(boolean active) {
        return (ReactiveEntityTableFilter<E>) super.setActive(active);
    }

    public ReactiveEntityTableFilter<E> setPush(boolean push) {
        return (ReactiveEntityTableFilter<E>) super.setPush(push);
    }

    public ReactiveEntityTableFilter<E> combine(ObservableValue<DqlStatement> dqlStatementProperty) {
        return (ReactiveEntityTableFilter<E>) super.combine(dqlStatementProperty);
    }

    public ReactiveEntityTableFilter<E> combine(DqlStatement dqlStatement) {
        return (ReactiveEntityTableFilter<E>) super.combine(dqlStatement);
    }

    public ReactiveEntityTableFilter<E> combine(String dqlStatementString) {
        return (ReactiveEntityTableFilter<E>) super.combine(dqlStatementString);
    }

    public ReactiveEntityTableFilter<E> combine(JsonObject json) {
        return (ReactiveEntityTableFilter<E>) super.combine(json);
    }

    public <T> ReactiveEntityTableFilter<E> combine(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter) {
        return (ReactiveEntityTableFilter<E>) super.combine(property, toDqlStatementConverter);
    }

    public <T> ReactiveEntityTableFilter<E> combineString(ObservableValue<T> property, Converter<T, String> toDqlStatementStringConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineString(property, toDqlStatementStringConverter);
    }

    public ReactiveEntityTableFilter<E> combineIfTrue(ObservableValue<Boolean> ifProperty, DqlStatement dqlStatement) {
        return (ReactiveEntityTableFilter<E>) super.combineIfTrue(ifProperty, dqlStatement);
    }

    public ReactiveEntityTableFilter<E> combineIfTrue(ObservableValue<Boolean> ifProperty, String dqlStatementString) {
        return (ReactiveEntityTableFilter<E>) super.combineIfTrue(ifProperty, dqlStatementString);
    }

    public ReactiveEntityTableFilter<E> combineIfTrue(ObservableValue<Boolean> ifProperty, Callable<DqlStatement> toDqlStatementCallable) {
        return (ReactiveEntityTableFilter<E>) super.combineIfTrue(ifProperty, toDqlStatementCallable);
    }

    public ReactiveEntityTableFilter<E> combineStringIfTrue(ObservableValue<Boolean> ifProperty, Callable<String> toDqlStatementStringCallable) {
        return (ReactiveEntityTableFilter<E>) super.combineStringIfTrue(ifProperty, toDqlStatementStringCallable);
    }

    public ReactiveEntityTableFilter<E> combineIfFalse(ObservableValue<Boolean> property, Callable<DqlStatement> toDqlStatementCallable) {
        return (ReactiveEntityTableFilter<E>) super.combineIfFalse(property, toDqlStatementCallable);
    }

    public ReactiveEntityTableFilter<E> combineStringIfFalse(ObservableValue<Boolean> ifProperty, Callable<String> toDqlStatementStringCallable) {
        return (ReactiveEntityTableFilter<E>) super.combineStringIfFalse(ifProperty, toDqlStatementStringCallable);
    }

    public <T extends Number> ReactiveEntityTableFilter<E> combineIfPositive(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineIfPositive(property, toDqlStatementConverter);
    }

    public <T extends Number> ReactiveEntityTableFilter<E> combineStringIfPositive(ObservableValue<T> property, Converter<T, String> toDqlStatementStringConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineStringIfPositive(property, toDqlStatementStringConverter);
    }

    public ReactiveEntityTableFilter<E> combineIfNotEmpty(ObservableValue<String> property, Converter<String, DqlStatement> toDqlStatementConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineIfNotEmpty(property, toDqlStatementConverter);
    }

    public ReactiveEntityTableFilter<E> combineStringIfNotEmpty(ObservableValue<String> property, Converter<String, String> toDqlStatementStringConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineStringIfNotEmpty(property, toDqlStatementStringConverter);
    }

    public ReactiveEntityTableFilter<E> combineIfNotEmptyTrim(ObservableValue<String> property, Converter<String, DqlStatement> toDqlStatementConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineIfNotEmptyTrim(property, toDqlStatementConverter);
    }

    public ReactiveEntityTableFilter<E> combineStringIfNotEmptyTrim(ObservableValue<String> property, Converter<String, String> toDqlStatementStringConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineStringIfNotEmptyTrim(property, toDqlStatementStringConverter);
    }

    public <T> ReactiveEntityTableFilter<E> combineIfNotNull(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineIfNotNull(property, toDqlStatementConverter);
    }

    public <T> ReactiveEntityTableFilter<E> combineStringIfNotNull(ObservableValue<T> property, Converter<T, String> toDqlStatementStringConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineStringIfNotNull(property, toDqlStatementStringConverter);
    }

    public <T> ReactiveEntityTableFilter<E> combineIfNotNullOtherwise(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter, String otherwiseDqlStatementString, Object... parameterValues) {
        return (ReactiveEntityTableFilter<E>) super.combineIfNotNullOtherwise(property, toDqlStatementConverter, otherwiseDqlStatementString);
    }

    public <T> ReactiveEntityTableFilter<E> combineIfNotNullOtherwise(ObservableValue<T> property, Converter<T, DqlStatement> toJsonFilterConverter, DqlStatement otherwiseDqlStatement) {
        return (ReactiveEntityTableFilter<E>) super.combineIfNotNullOtherwise(property, toJsonFilterConverter, otherwiseDqlStatement);
    }

    public <T> ReactiveEntityTableFilter<E> combineStringIfNotNullOtherwise(ObservableValue<T> property, Converter<T, String> toDqlStatementStringConverter, String otherwiseDqlStatementString, Object... parameterValues) {
        return (ReactiveEntityTableFilter<E>) super.combineStringIfNotNullOtherwise(property, toDqlStatementStringConverter, otherwiseDqlStatementString);
    }

    public <T> ReactiveEntityTableFilter<E> combineIfNotNullOtherwiseForceEmptyResult(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineIfNotNullOtherwiseForceEmptyResult(property, toDqlStatementConverter);
    }

    public <T> ReactiveEntityTableFilter<E> combineStringIfNotNullOtherwiseForceEmptyResult(ObservableValue<T> property, Converter<T, String> toDqlStatementStringConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineStringIfNotNullOtherwiseForceEmptyResult(property, toDqlStatementStringConverter);
    }
}
