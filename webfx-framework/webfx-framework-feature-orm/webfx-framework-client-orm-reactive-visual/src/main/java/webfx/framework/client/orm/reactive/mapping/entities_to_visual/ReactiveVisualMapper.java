package webfx.framework.client.orm.reactive.mapping.entities_to_visual;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import webfx.extras.type.PrimType;
import webfx.extras.visual.*;
import webfx.framework.client.orm.reactive.dql.statement.conventions.HasSelectedGroupProperty;
import webfx.framework.client.orm.reactive.dql.statement.conventions.HasSelectedGroupReferenceResolver;
import webfx.framework.client.orm.reactive.dql.statement.conventions.HasSelectedMasterProperty;
import webfx.framework.client.orm.reactive.mapping.dql_to_entities.ReactiveEntitiesMapper;
import webfx.framework.client.orm.reactive.mapping.dql_to_entities.ReactiveEntitiesMapperAPI;
import webfx.framework.client.orm.reactive.mapping.entities_to_grid.EntityColumn;
import webfx.framework.client.orm.reactive.mapping.entities_to_grid.ReactiveGridMapper;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.conventions.*;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityList;
import webfx.framework.shared.orm.expression.terms.ExpressionArray;
import webfx.platform.shared.util.async.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class ReactiveVisualMapper<E extends Entity> extends ReactiveGridMapper<E>
    implements ReactiveEntitiesMapperAPI<E, ReactiveVisualMapper<E>> {

    private final ObjectProperty<VisualResult> visualResultProperty = new SimpleObjectProperty<VisualResult/*GWT*/>() {
        @Override
        protected void invalidated() {
            if (autoSelectSingleRow && get() != null && get().getRowCount() == 1)
                visualSelectionProperty.setValue(VisualSelection.createSingleRowSelection(0));
        }
    };

    private final ObjectProperty<VisualSelection> visualSelectionProperty = new SimpleObjectProperty<VisualSelection/*GWT*/>() {
        @Override
        protected void invalidated() {
            VisualSelection visualSelection = get();
            if (selectedEntityHandler != null && visualSelection != null && visualSelection.isSingle())
                selectedEntityHandler.handle(getSelectedEntity());
        }
    };

    public ReactiveVisualMapper(ReactiveEntitiesMapper<E> reactiveEntitiesMapper) {
        super(reactiveEntitiesMapper);
    }

    @Override
    public List<E> getSelectedEntities() {
        return getSelectedEntities(visualSelectionProperty.get());
    }

    private List<E> getSelectedEntities(VisualSelection selection) {
        if (selection == null)
            return null;
        List<E> selectedEntities = new ArrayList<>();
        selection.forEachRow(row -> selectedEntities.add(getEntityAt(row)));
        return selectedEntities;
    }

    @Override
    public E getSelectedEntity() {
        VisualSelection visualSelection = visualSelectionProperty.get();
        return visualSelection == null || visualSelection.isEmpty() ? null : getEntityAt(visualSelection.getSelectedRow());
    }

    private E getEntityAt(int row) {
        return getCurrentEntities().get(row);
    }

    public Property<VisualResult> visualResultProperty() {
        return visualResultProperty;
    }

    public ReactiveVisualMapper<E> visualizeResultInto(HasVisualResultProperty hasVisualResultProperty) {
        if (hasVisualResultProperty instanceof HasVisualSelectionProperty)
            setVisualSelectionProperty(((HasVisualSelectionProperty) hasVisualResultProperty).visualSelectionProperty());
        return visualizeResultInto(hasVisualResultProperty.visualResultProperty());
    }

    public ReactiveVisualMapper<E> visualizeResultInto(Property<VisualResult> visualResultProperty) {
        visualResultProperty.bind(this.visualResultProperty);
        return this;
    }

    public ReactiveVisualMapper<E> setVisualSelectionProperty(Property<VisualSelection> visualSelectionProperty) {
        visualSelectionProperty.bindBidirectional(this.visualSelectionProperty);
        return this;
    }

    public ReactiveVisualMapper<E> applyDomainModelRowStyle() {
        return (ReactiveVisualMapper<E>) super.applyDomainModelRowStyle();
    }

    @Override
    public ReactiveVisualMapper<E> autoSelectSingleRow() {
        return (ReactiveVisualMapper<E>) super.autoSelectSingleRow();
    }

    @Override
    public ReactiveVisualMapper<E> setSelectedEntityHandler(Handler<E> selectedEntityHandler) {
        return (ReactiveVisualMapper<E>) super.setSelectedEntityHandler(selectedEntityHandler);
    }

    @Override
    public ReactiveVisualMapper<E> setEntityColumns(String jsonArrayOrExpressionDefinition) {
        return (ReactiveVisualMapper<E>) super.setEntityColumns(jsonArrayOrExpressionDefinition);
    }

    @Override
    public ReactiveVisualMapper<E> setEntityColumns(EntityColumn<E>... entityColumns) {
        return (ReactiveVisualMapper<E>) super.setEntityColumns(entityColumns);
    }

    @Override
    protected VisualEntityColumnFactory getEntityColumnFactory() {
        return VisualEntityColumnFactory.get();
    }

    @Override
    protected EntityColumn<E> createStyleEntityColumn(ExpressionArray<E> rowStylesExpressionArray) {
        return getEntityColumnFactory().create(rowStylesExpressionArray, VisualColumnBuilder.create("style", PrimType.STRING).setRole("style").build());
    }

    @Override
    protected void onEntityListChanged(EntityList<E> entityList) {
        setVisualResult(entitiesToVisualResult(entityList));
    }

    void setVisualResult(VisualResult rs) {
        //System.out.println("ReactiveVisualMapper.setVisualResult()"); // + " result = " + rs);
        visualResultProperty.setValue(rs);
        if (autoSelectSingleRow && rs.getRowCount() == 1 || selectFirstRowOnFirstDisplay && rs.getRowCount() > 0) {
            selectFirstRowOnFirstDisplay = false;
            visualSelectionProperty.setValue(VisualSelection.createSingleRowSelection(0));
        }
    }

    VisualResult entitiesToVisualResult(List<E> entities) {
        return EntitiesToVisualResultMapper.mapEntitiesToVisualResult(entities, entityColumns);
    }

    /*==================================================================================================================
      ======================================= Classic static factory API ===============================================
      ================================================================================================================*/

    public static <E extends Entity> ReactiveVisualMapper<E> create(ReactiveEntitiesMapper<E> reactiveEntitiesMapper) {
        return new ReactiveVisualMapper<>(reactiveEntitiesMapper);
    }

    /*==================================================================================================================
      ==================================== Conventional static factory API =============================================
      ================================================================================================================*/

    public static <E extends Entity> ReactiveVisualMapper<E> createMaster(Object pm, ReactiveEntitiesMapper<E> reactiveEntitiesMapper) {
        return initializeMaster(create(reactiveEntitiesMapper), pm);
    }

    protected static <E extends Entity> ReactiveVisualMapper<E> initializeMaster(ReactiveVisualMapper<E> master, Object pm) {
        if (pm instanceof HasMasterVisualResultProperty)
            master.visualizeResultInto(((HasMasterVisualResultProperty) pm).masterVisualResultProperty());
        if (pm instanceof HasMasterVisualSelectionProperty)
            master.setVisualSelectionProperty(((HasMasterVisualSelectionProperty) pm).masterVisualSelectionProperty());
        if (pm instanceof HasSelectedMasterProperty)
            master.setSelectedEntityHandler(((HasSelectedMasterProperty<E>) pm)::setSelectedMaster);
        return master;
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createGroup(Object pm, ReactiveEntitiesMapper<E> reactiveEntitiesMapper) {
        return initializeGroup(create(reactiveEntitiesMapper), pm);
    }

    protected static <E extends Entity> ReactiveVisualMapper<E> initializeGroup(ReactiveVisualMapper<E> group, Object pm) {
        if (pm instanceof HasGroupVisualResultProperty)
            group.visualizeResultInto(((HasGroupVisualResultProperty) pm).groupVisualResultProperty());
        if (pm instanceof HasGroupVisualSelectionProperty)
            group.setVisualSelectionProperty(((HasGroupVisualSelectionProperty) pm).groupVisualSelectionProperty());
        if (pm instanceof HasSelectedGroupProperty)
            group.setSelectedEntityHandler(((HasSelectedGroupProperty) pm)::setSelectedGroup);
        if (pm instanceof HasSelectedGroupReferenceResolver)
            ((HasSelectedGroupReferenceResolver) pm).setSelectedGroupReferenceResolver(group.getReactiveEntitiesMapper().getReactiveDqlQuery().getRootAliasReferenceResolver());
        return group;
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createSlave(Object pm, ReactiveEntitiesMapper<E> reactiveEntitiesMapper) {
        return initializeSlave(create(reactiveEntitiesMapper), pm);
    }

    protected static <E extends Entity> ReactiveVisualMapper<E> initializeSlave(ReactiveVisualMapper<E> slave, Object pm) {
        if (pm instanceof HasSlaveVisualResultProperty)
            slave.visualizeResultInto(((HasSlaveVisualResultProperty) pm).slaveVisualResultProperty());
        return slave;
    }

    /*==================================================================================================================
      ===================================== Shortcut static factory API ================================================
      ================================================================================================================*/

    public static <E extends Entity> ReactiveVisualMapper<E> createReactiveChain() {
        return create(ReactiveEntitiesMapper.createReactiveChain());
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createReactiveChain(Object mixin) {
        return create(ReactiveEntitiesMapper.createReactiveChain(mixin));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createPushReactiveChain() {
        return create(ReactiveEntitiesMapper.createPushReactiveChain());
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createPushReactiveChain(Object mixin) {
        return create(ReactiveEntitiesMapper.createPushReactiveChain(mixin));
    }

    // Master
    
    public static <E extends Entity> ReactiveVisualMapper<E> createMasterReactiveChain(Object pm) {
        return createMaster(pm, ReactiveEntitiesMapper.createMasterReactiveChain(pm));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createMasterReactiveChain(Object mixin, Object pm) {
        return createMaster(pm, ReactiveEntitiesMapper.createMasterReactiveChain(mixin, pm));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createMasterPushReactiveChain(Object pm) {
        return createMaster(pm, ReactiveEntitiesMapper.createMasterPushReactiveChain(pm));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createMasterPushReactiveChain(Object mixin, Object pm) {
        return createMaster(pm, ReactiveEntitiesMapper.createMasterPushReactiveChain(mixin, pm));
    }

    // Group
    
    public static <E extends Entity> ReactiveVisualMapper<E> createGroupReactiveChain(Object pm) {
        return createGroup(pm, ReactiveEntitiesMapper.createGroupReactiveChain(pm));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createGroupReactiveChain(Object mixin, Object pm) {
        return createGroup(pm, ReactiveEntitiesMapper.createGroupReactiveChain(mixin, pm));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createGroupPushReactiveChain(Object pm) {
        return createGroup(pm, ReactiveEntitiesMapper.createGroupPushReactiveChain(pm));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createGroupPushReactiveChain(Object mixin, Object pm) {
        return createGroup(pm, ReactiveEntitiesMapper.createGroupPushReactiveChain(mixin, pm));
    }

    // Slave

    public static <E extends Entity> ReactiveVisualMapper<E> createSlaveReactiveChain(Object pm) {
        return createSlave(pm, ReactiveEntitiesMapper.createSlaveReactiveChain(pm));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createSlaveReactiveChain(Object mixin, Object pm) {
        return createSlave(pm, ReactiveEntitiesMapper.createSlaveReactiveChain(mixin, pm));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createSlavePushReactiveChain(Object pm) {
        return createSlave(pm, ReactiveEntitiesMapper.createSlavePushReactiveChain(pm));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createSlavePushReactiveChain(Object mixin, Object pm) {
        return createSlave(pm, ReactiveEntitiesMapper.createSlavePushReactiveChain(mixin, pm));
    }

}
