package webfx.framework.client.orm.entity.filter.visual;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import webfx.extras.type.PrimType;
import webfx.extras.visual.VisualColumnBuilder;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualSelection;
import webfx.framework.client.orm.dql.DqlStatement;
import webfx.framework.client.orm.entity.filter.ReactiveEntityFilter;
import webfx.framework.client.orm.entity.filter.table.EntityColumn;
import webfx.framework.client.orm.entity.filter.table.ReactiveEntityTableFilter;
import webfx.framework.client.orm.mapping.entity_to_visual.EntitiesToVisualResultMapper;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityList;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.expression.terms.ExpressionArray;
import webfx.kit.util.properties.Properties;
import webfx.platform.shared.services.json.JsonArray;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.util.async.Handler;
import webfx.platform.shared.util.function.Callable;
import webfx.platform.shared.util.function.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class ReactiveVisualFilter<E extends Entity> extends ReactiveEntityTableFilter<E> {

    /*=================================
      ========= Constructors ==========
      =================================*/

    public ReactiveVisualFilter() {}

    public ReactiveVisualFilter(Object jsonOrClass) {
        super(jsonOrClass);
    }

    public ReactiveVisualFilter(ReactiveEntityFilter<?> parent) {
        super(parent);
    }

    public ReactiveVisualFilter(ReactiveEntityFilter<?> parent, Object jsonOrClass) {
        super(parent, jsonOrClass);
    }


    /*=================================
      ======== New fluent API =========
      =================================*/

    public ReactiveVisualFilter<E> setVisualSelectionProperty(Property<VisualSelection> visualSelectionProperty) {
        getCurrentTableDisplay().setVisualSelectionProperty(visualSelectionProperty);
        return this;
    }

    public ReactiveVisualFilter<E> setSelectedEntityHandler(Property<VisualSelection> visualSelectionProperty, Handler<E> entityHandler) {
        getCurrentTableDisplay().setSelectedEntityHandler(visualSelectionProperty, entityHandler);
        return this;
    }

    public ReactiveVisualFilter<E> selectFirstRowOnFirstDisplay(Property<VisualSelection> visualSelectionProperty, Property<?> onEachChangeProperty) {
        getCurrentTableDisplay().selectFirstRowOnFirstDisplay(visualSelectionProperty, onEachChangeProperty);
        return this;
    }

    public ReactiveVisualFilter<E> selectFirstRowOnFirstDisplay(Property<VisualSelection> visualSelectionProperty) {
        getCurrentTableDisplay().selectFirstRowOnFirstDisplay(visualSelectionProperty);
        return this;
    }

    public ReactiveVisualFilter<E> visualizeResultInto(Property<VisualResult> visualResultProperty) {
        getCurrentTableDisplay().setVisualResultProperty(visualResultProperty);
        return this;
    }


    /*=============================
      === New other public API  ===
      =============================*/

    public Property<VisualSelection> getVisualSelectionProperty() {
        return getVisualSelectionProperty(0);
    }

    public Property<VisualSelection> getVisualSelectionProperty(int displayIndex) {
        return getTableDisplay(displayIndex).getVisualSelectionProperty();
    }

    public E getSelectedEntity(VisualSelection selection) {
        return getSelectedEntity(0, selection);
    }

    public E getSelectedEntity(int displayIndex, VisualSelection selection) {
        return getTableDisplay(displayIndex).getSelectedEntity(selection);
    }


    /*===============================
      === Internal implementation ===
      ===============================*/

    // Cache fields used in entitiesToVisualResults() method
    private EntityList<E> lastEntitiesInput;
    private VisualResult[] lastVisualResultsOutput;

    @Override
    protected void clearCacheForNextTableDisplays() {
        lastEntitiesInput = null;
    }

    @Override
    protected void updateEntitiesToTableMapping() {
        applyVisualResults(entitiesToVisualResults(entityListProperty.get()));
    }

    private void applyVisualResults(VisualResult[] visualResults) {
        for (int i = 0, n = visualResults.length; i < n; i++)
            getTableDisplay(i).setVisualResult(visualResults[i]);
    }

    private VisualResult[] entitiesToVisualResults(EntityList<E> entities) {
        //log("Converting entities into VisualResult: " + entities);
        // Returning the cached output if input didn't change (ex: the same entity list instance is emitted again on active property change)
        if (entities == lastEntitiesInput)
            return lastVisualResultsOutput; // Returning the same instance will avoid triggering the results changeListeners (high cpu consuming in UI)
        // Calling the entities handler now we are sure there is a real change
        if (entitiesHandler != null)
            entitiesHandler.handle(entities);
        // Transforming the entities into visualResults (entity to display mapping)
        int n = tableDisplays.size();
        VisualResult[] results = new VisualResult[n];
        for (int i = 0; i < n; i++)
            results[i] = getTableDisplay(i).entitiesToVisualResult(entities);
        // Caching and returning the result
        lastEntitiesInput = entities;
        return lastVisualResultsOutput = results;
    }

    @Override
    protected VisualEntityColumnFactory getEntityColumnFactory() {
        return VisualEntityColumnFactory.get();
    }

    protected VisualTableDisplay getCurrentTableDisplay() {
        return (VisualTableDisplay) super.getCurrentTableDisplay();
    }

    protected VisualTableDisplay createTableDisplay() {
        return new VisualTableDisplay();
    }

    protected VisualTableDisplay getTableDisplay(int displayIndex) {
        return (VisualTableDisplay) super.getTableDisplay(displayIndex);
    }

    protected final class VisualTableDisplay extends TableDisplay {

        private Property<VisualResult> visualResultProperty;
        private Property<VisualSelection> visualSelectionProperty;

        void selectFirstRowOnFirstDisplay(Property<VisualSelection> visualSelectionProperty) {
            setVisualSelectionProperty(visualSelectionProperty);
            selectFirstRowOnFirstDisplay();
        }

        @Override
        protected boolean isTableSet() {
            return visualResultProperty != null;
        }

        @Override
        protected EntityColumn<E> createStyleEntityColumn(ExpressionArray<E> rowStylesExpressionArray) {
            return getEntityColumnFactory().create(rowStylesExpressionArray, VisualColumnBuilder.create("style", PrimType.STRING).setRole("style").build());
        }

        @Override
        protected E getSelectedEntity() {
            return getSelectedEntity(visualSelectionProperty == null ? null : visualSelectionProperty.getValue());
        }

        @Override
        protected void setSelectedEntityHandler(Handler<E> entityHandler) {
            visualSelectionProperty.addListener((observable, oldValue, newValue) -> entityHandler.handle(getSelectedEntity()));
        }

        @Override
        protected List<E> getSelectedEntities() {
            return getSelectedEntities(visualSelectionProperty == null ? null :visualSelectionProperty.getValue());
        }

        @Override
        protected void refreshTable(boolean empty) {
            if (empty)
                setEmptyVisualResult();
            else if (visualResultProperty != null)
                visualResultProperty.setValue(entitiesToVisualResult(getCurrentEntityList()));
        }

        Property<VisualSelection> getVisualSelectionProperty() {
            return visualSelectionProperty;
        }

        void setVisualSelectionProperty(Property<VisualSelection> visualSelectionProperty) {
            this.visualSelectionProperty = visualSelectionProperty;
        }

        Property<VisualResult> getVisualResultProperty() {
            return visualResultProperty;
        }

        void setSelectedEntityHandler(Property<VisualSelection> visualSelectionProperty, Handler<E> entityHandler) {
            setVisualSelectionProperty(visualSelectionProperty);
            setSelectedEntityHandler(entityHandler);
        }

        void selectFirstRowOnFirstDisplay(Property<VisualSelection> visualSelectionProperty, Property<?> onEachChangeProperty) {
            // Each time the property change, we clear the selection and reset the selectFirstRowOnFirstDisplay to true to arm the mechanism again
            Properties.runOnPropertiesChange(() -> {
                if (isActive()) {
                    visualSelectionProperty.setValue(null);
                    selectFirstRowOnFirstDisplay();
                }
            }, onEachChangeProperty, visualResultProperty);
            selectFirstRowOnFirstDisplay(visualSelectionProperty);
        }

        E getSelectedEntity(VisualSelection selection) {
            return getEntityAt(selection == null ? -1 : selection.getSelectedRow());
        }

        List<E> getSelectedEntities(VisualSelection selection) {
            if (selection == null)
                return null;
            List<E> selectedEntities = new ArrayList<>();
            selection.forEachRow(row -> selectedEntities.add(getEntityAt(row)));
            return selectedEntities;
        }

        void setVisualResultProperty(Property<VisualResult> visualResultProperty) {
            this.visualResultProperty = visualResultProperty;
        }

        void setVisualResult(VisualResult rs) {
            if (visualResultProperty != null)
                visualResultProperty.setValue(rs);
            if (autoSelectSingleRow && rs.getRowCount() == 1 || selectFirstRowOnFirstDisplay && rs.getRowCount() > 0) {
                selectFirstRowOnFirstDisplay = false;
                visualSelectionProperty.setValue(VisualSelection.createSingleRowSelection(0));
            }
        }

        void setEmptyVisualResult() {
            if (visualResultProperty != null)
                visualResultProperty.setValue(emptyVisualResult());
        }

        VisualResult entitiesToVisualResult(List<E> entities) {
            collectColumnsPersistentTerms();
            return EntitiesToVisualResultMapper.mapEntitiesToVisualResult(entities, entityColumns);
        }

        VisualResult emptyVisualResult() {
            return entitiesToVisualResult(emptyFutureList());
        }
    }


    /*=================================
      ====== Fluent API (upgrade) =====
      =================================*/

    public ReactiveVisualFilter<E> setStartsWithEmptyResult(boolean startsWithEmptyResult) {
        return (ReactiveVisualFilter<E>) super.setStartsWithEmptyResult(startsWithEmptyResult);
    }

    public ReactiveVisualFilter<E> setSelectedEntityHandler(Handler<E> entityHandler) {
        return (ReactiveVisualFilter<E>) super.setSelectedEntityHandler(entityHandler);
    }

    public ReactiveVisualFilter<E> selectFirstRowOnFirstDisplay() {
        return (ReactiveVisualFilter<E>) super.selectFirstRowOnFirstDisplay();
    }

    public ReactiveVisualFilter<E> nextDisplay() {
        return (ReactiveVisualFilter<E>) super.nextDisplay();
    }

    public ReactiveVisualFilter<E> autoSelectSingleRow() {
        return (ReactiveVisualFilter<E>) super.autoSelectSingleRow();
    }

    public ReactiveVisualFilter<E> setEntityColumns(String jsonArrayOrExpressionDefinition) {
        return (ReactiveVisualFilter<E>) super.setEntityColumns(jsonArrayOrExpressionDefinition);
    }

    public ReactiveVisualFilter<E> setEntityColumns(JsonArray array) {
        return (ReactiveVisualFilter<E>) super.setEntityColumns(array);
    }

    public ReactiveVisualFilter<E> setEntityColumns(EntityColumn... entityColumns) {
        return (ReactiveVisualFilter<E>) super.setEntityColumns(entityColumns);
    }

    public ReactiveVisualFilter<E> applyDomainModelRowStyle() {
        return (ReactiveVisualFilter<E>)  super.applyDomainModelRowStyle();
    }


    public ReactiveVisualFilter<E> setDataSourceModel(DataSourceModel dataSourceModel) {
        return (ReactiveVisualFilter<E>) super.setDataSourceModel(dataSourceModel);
    }

    public ReactiveVisualFilter<E> setStore(EntityStore store) {
        return (ReactiveVisualFilter<E>) super.setStore(store);
    }

    public ReactiveVisualFilter<E> setListId(Object listId) {
        return (ReactiveVisualFilter<E>) super.setListId(listId);
    }

    public ReactiveVisualFilter<E> setRestrictedFilterList(List<E> restrictedFilterList) {
        return (ReactiveVisualFilter<E>) super.setRestrictedFilterList(restrictedFilterList);
    }

    public ReactiveVisualFilter<E> setEntitiesHandler(Handler<EntityList<E>> entitiesHandler) {
        return (ReactiveVisualFilter<E>) super.setEntitiesHandler(entitiesHandler);
    }

    public ReactiveVisualFilter<E> bindActivePropertyTo(ObservableValue<Boolean> activeProperty) {
        return (ReactiveVisualFilter<E>) super.bindActivePropertyTo(activeProperty);
    }

    public ReactiveVisualFilter<E> setActive(boolean active) {
        return (ReactiveVisualFilter<E>) super.setActive(active);
    }

    public ReactiveVisualFilter<E> setPush(boolean push) {
        return (ReactiveVisualFilter<E>) super.setPush(push);
    }

    public ReactiveVisualFilter<E> combine(ObservableValue<DqlStatement> dqlStatementProperty) {
        return (ReactiveVisualFilter<E>) super.combine(dqlStatementProperty);
    }

    public ReactiveVisualFilter<E> combine(DqlStatement dqlStatement) {
        return (ReactiveVisualFilter<E>) super.combine(dqlStatement);
    }

    public ReactiveVisualFilter<E> combine(String dqlStatementString) {
        return (ReactiveVisualFilter<E>) super.combine(dqlStatementString);
    }

    public ReactiveVisualFilter<E> combine(JsonObject json) {
        return (ReactiveVisualFilter<E>) super.combine(json);
    }

    public <T> ReactiveVisualFilter<E> combine(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter) {
        return (ReactiveVisualFilter<E>) super.combine(property, toDqlStatementConverter);
    }

    public <T> ReactiveVisualFilter<E> combineString(ObservableValue<T> property, Converter<T, String> toDqlStatementStringConverter) {
        return (ReactiveVisualFilter<E>) super.combineString(property, toDqlStatementStringConverter);
    }

    public ReactiveVisualFilter<E> combineIfTrue(ObservableValue<Boolean> ifProperty, DqlStatement dqlStatement) {
        return (ReactiveVisualFilter<E>) super.combineIfTrue(ifProperty, dqlStatement);
    }

    public ReactiveVisualFilter<E> combineIfTrue(ObservableValue<Boolean> ifProperty, String dqlStatementString) {
        return (ReactiveVisualFilter<E>) super.combineIfTrue(ifProperty, dqlStatementString);
    }

    public ReactiveVisualFilter<E> combineIfTrue(ObservableValue<Boolean> ifProperty, Callable<DqlStatement> toDqlStatementCallable) {
        return (ReactiveVisualFilter<E>) super.combineIfTrue(ifProperty, toDqlStatementCallable);
    }

    public ReactiveVisualFilter<E> combineStringIfTrue(ObservableValue<Boolean> ifProperty, Callable<String> toDqlStatementStringCallable) {
        return (ReactiveVisualFilter<E>) super.combineStringIfTrue(ifProperty, toDqlStatementStringCallable);
    }

    public ReactiveVisualFilter<E> combineIfFalse(ObservableValue<Boolean> property, Callable<DqlStatement> toDqlStatementCallable) {
        return (ReactiveVisualFilter<E>) super.combineIfFalse(property, toDqlStatementCallable);
    }

    public ReactiveVisualFilter<E> combineStringIfFalse(ObservableValue<Boolean> ifProperty, Callable<String> toDqlStatementStringCallable) {
        return (ReactiveVisualFilter<E>) super.combineStringIfFalse(ifProperty, toDqlStatementStringCallable);
    }

    public <T extends Number> ReactiveVisualFilter<E> combineIfPositive(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter) {
        return (ReactiveVisualFilter<E>) super.combineIfPositive(property, toDqlStatementConverter);
    }

    public <T extends Number> ReactiveVisualFilter<E> combineStringIfPositive(ObservableValue<T> property, Converter<T, String> toDqlStatementStringConverter) {
        return (ReactiveVisualFilter<E>) super.combineStringIfPositive(property, toDqlStatementStringConverter);
    }

    public ReactiveVisualFilter<E> combineIfNotEmpty(ObservableValue<String> property, Converter<String, DqlStatement> toDqlStatementConverter) {
        return (ReactiveVisualFilter<E>) super.combineIfNotEmpty(property, toDqlStatementConverter);
    }

    public ReactiveVisualFilter<E> combineStringIfNotEmpty(ObservableValue<String> property, Converter<String, String> toDqlStatementStringConverter) {
        return (ReactiveVisualFilter<E>) super.combineStringIfNotEmpty(property, toDqlStatementStringConverter);
    }

    public ReactiveVisualFilter<E> combineIfNotEmptyTrim(ObservableValue<String> property, Converter<String, DqlStatement> toDqlStatementConverter) {
        return (ReactiveVisualFilter<E>) super.combineIfNotEmptyTrim(property, toDqlStatementConverter);
    }

    public ReactiveVisualFilter<E> combineStringIfNotEmptyTrim(ObservableValue<String> property, Converter<String, String> toDqlStatementStringConverter) {
        return (ReactiveVisualFilter<E>) super.combineStringIfNotEmptyTrim(property, toDqlStatementStringConverter);
    }

    public <T> ReactiveVisualFilter<E> combineIfNotNull(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter) {
        return (ReactiveVisualFilter<E>) super.combineIfNotNull(property, toDqlStatementConverter);
    }

    public <T> ReactiveVisualFilter<E> combineStringIfNotNull(ObservableValue<T> property, Converter<T, String> toDqlStatementStringConverter) {
        return (ReactiveVisualFilter<E>) super.combineStringIfNotNull(property, toDqlStatementStringConverter);
    }

    public <T> ReactiveVisualFilter<E> combineIfNotNullOtherwise(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter, String otherwiseDqlStatementString, Object... parameterValues) {
        return (ReactiveVisualFilter<E>) super.combineIfNotNullOtherwise(property, toDqlStatementConverter, otherwiseDqlStatementString);
    }

    public <T> ReactiveVisualFilter<E> combineIfNotNullOtherwise(ObservableValue<T> property, Converter<T, DqlStatement> toJsonFilterConverter, DqlStatement otherwiseDqlStatement) {
        return (ReactiveVisualFilter<E>) super.combineIfNotNullOtherwise(property, toJsonFilterConverter, otherwiseDqlStatement);
    }

    public <T> ReactiveVisualFilter<E> combineStringIfNotNullOtherwise(ObservableValue<T> property, Converter<T, String> toDqlStatementStringConverter, String otherwiseDqlStatementString, Object... parameterValues) {
        return (ReactiveVisualFilter<E>) super.combineStringIfNotNullOtherwise(property, toDqlStatementStringConverter, otherwiseDqlStatementString);
    }

    public <T> ReactiveVisualFilter<E> combineIfNotNullOtherwiseForceEmptyResult(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter) {
        return (ReactiveVisualFilter<E>) super.combineIfNotNullOtherwiseForceEmptyResult(property, toDqlStatementConverter);
    }

    public <T> ReactiveVisualFilter<E> combineStringIfNotNullOtherwiseForceEmptyResult(ObservableValue<T> property, Converter<T, String> toDqlStatementStringConverter) {
        return (ReactiveVisualFilter<E>) super.combineStringIfNotNullOtherwiseForceEmptyResult(property, toDqlStatementStringConverter);
    }

    public ReactiveVisualFilter<E> start() {
        return (ReactiveVisualFilter<E>) super.start();
    }

    public ReactiveVisualFilter<E> stop() {
        return (ReactiveVisualFilter<E>) super.stop();
    }
}
