package webfx.framework.client.orm.entity.filter.table;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import webfx.framework.client.orm.entity.filter.EqlFilter;
import webfx.framework.client.orm.entity.filter.EqlFilterBuilder;
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
    protected EqlFilter mergeEqlFilters() {
        EqlFilterBuilder mergeBuilder = new EqlFilterBuilder(getDomainClassId());
        Iterator<TableDisplay> it = tableDisplays.iterator();
        currentTableDisplay = Collections.next(it);
        for (int i = 0; i < eqlFilterProperties.size(); i++) {
            mergeBuilder.merge(eqlFilterProperties.get(i).getValue());
            if (currentTableDisplay != null && currentTableDisplay.eqlFilterPropertiesLastIndex == i) {
                if (mergeBuilder.getColumns() != null) {
                    currentTableDisplay.setEntityColumns(mergeBuilder.getColumns());
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
            currentTableDisplay.eqlFilterPropertiesLastIndex = eqlFilterProperties.size() - 1;
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
        int eqlFilterPropertiesLastIndex = -1;
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


    /*=================================
      ====== Fluent API (upgrade) =====
      =================================*/

    public ReactiveEntityTableFilter<E> start() {
        // The following call is to set eqlFilterPropertiesLastIndex on the latest tableDisplay
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

    public ReactiveEntityTableFilter<E> combine(String eqlFilterString) {
        return (ReactiveEntityTableFilter<E>) super.combine(eqlFilterString);
    }

    public ReactiveEntityTableFilter<E> combine(JsonObject json) {
        return (ReactiveEntityTableFilter<E>) super.combine(json);
    }

    public ReactiveEntityTableFilter<E> combine(EqlFilterBuilder eqlFilterBuilder) {
        return (ReactiveEntityTableFilter<E>) super.combine(eqlFilterBuilder);
    }

    public ReactiveEntityTableFilter<E> combine(EqlFilter eqlFilter) {
        return (ReactiveEntityTableFilter<E>) super.combine(eqlFilter);
    }

    public ReactiveEntityTableFilter<E> combine(ObservableValue<EqlFilter> eqlFilterProperty) {
        return (ReactiveEntityTableFilter<E>) super.combine(eqlFilterProperty);
    }

    public <T> ReactiveEntityTableFilter<E> combine(ObservableValue<T> property, Converter<T, String> toEqlFilterStringConverter) {
        return (ReactiveEntityTableFilter<E>) super.combine(property, toEqlFilterStringConverter);
    }

    public <T> ReactiveEntityTableFilter<E> combineIfNotNull(ObservableValue<T> property, Converter<T, String> toJsonFilterConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineIfNotNull(property, toJsonFilterConverter);
    }

    public <T> ReactiveEntityTableFilter<E> combineIfNotNullOtherwiseForceEmptyResult(ObservableValue<T> property, Converter<T, String> toJsonFilterConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineIfNotNullOtherwiseForceEmptyResult(property, toJsonFilterConverter);
    }

    public <T> ReactiveEntityTableFilter<E> combineIfNotNullOtherwise(ObservableValue<T> property, Converter<T, String> toJsonFilterConverter, String otherwiseEqlFilterString) {
        return (ReactiveEntityTableFilter<E>) super.combineIfNotNullOtherwise(property, toJsonFilterConverter, otherwiseEqlFilterString);
    }

    public ReactiveEntityTableFilter<E> combineIfNotEmpty(ObservableValue<String> property, Converter<String, String> toJsonFilterConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineIfNotEmpty(property, toJsonFilterConverter);
    }

    public ReactiveEntityTableFilter<E> combineIfNotEmptyTrim(ObservableValue<String> property, Converter<String, String> toJsonFilterConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineIfNotEmptyTrim(property, toJsonFilterConverter);
    }

    public <T extends Number> ReactiveEntityTableFilter<E> combineIfPositive(ObservableValue<T> property, Converter<T, String> toJsonFilterConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineIfPositive(property, toJsonFilterConverter);
    }

    public ReactiveEntityTableFilter<E> combineIfTrue(ObservableValue<Boolean> property, Callable<String> toJsonFilterConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineIfTrue(property, toJsonFilterConverter);
    }

    public ReactiveEntityTableFilter<E> combineIfFalse(ObservableValue<Boolean> property, Callable<String> toJsonFilterConverter) {
        return (ReactiveEntityTableFilter<E>) super.combineIfFalse(property, toJsonFilterConverter);
    }

    public ReactiveEntityTableFilter<E> combine(Property<Boolean> ifProperty, EqlFilterBuilder eqlFilterBuilder) {
        return (ReactiveEntityTableFilter<E>) super.combine(ifProperty, eqlFilterBuilder);
    }

    public ReactiveEntityTableFilter<E> combine(Property<Boolean> ifProperty, String json) {
        return (ReactiveEntityTableFilter<E>) super.combine(ifProperty, json);
    }

    public ReactiveEntityTableFilter<E> combine(Property<Boolean> ifProperty, EqlFilter eqlFilter) {
        return (ReactiveEntityTableFilter<E>) super.combine(ifProperty, eqlFilter);
    }
}
