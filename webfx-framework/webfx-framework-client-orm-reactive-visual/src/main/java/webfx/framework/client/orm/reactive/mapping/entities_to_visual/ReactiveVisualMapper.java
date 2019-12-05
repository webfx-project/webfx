package webfx.framework.client.orm.reactive.mapping.entities_to_visual;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import webfx.extras.type.PrimType;
import webfx.extras.visual.VisualColumnBuilder;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualSelection;
import webfx.framework.client.orm.reactive.dql.query.ReactiveDqlQuery;
import webfx.framework.client.orm.reactive.dql.statement.ReactiveDqlStatement;
import webfx.framework.client.orm.reactive.dql.statement.conventions.HasSelectedGroupProperty;
import webfx.framework.client.orm.reactive.dql.statement.conventions.HasSelectedGroupReferenceResolver;
import webfx.framework.client.orm.reactive.dql.statement.conventions.HasSelectedMasterProperty;
import webfx.framework.client.orm.reactive.mapping.dql_to_entities.ReactiveEntityMapper;
import webfx.framework.client.orm.reactive.mapping.dql_to_entities.ReactiveEntityMapperAPI;
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
    implements ReactiveEntityMapperAPI<E, ReactiveVisualMapper<E>> {

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

    public ReactiveVisualMapper(ReactiveEntityMapper<E> reactiveEntityMapper) {
        super(reactiveEntityMapper);
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
        return getCurrentEntityList().get(row);
    }

    public Property<VisualResult> visualResultProperty() {
        return visualResultProperty;
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

    public static <E extends Entity> ReactiveVisualMapper<E> create(ReactiveEntityMapper<E> reactiveEntityMapper) {
        return new ReactiveVisualMapper<>(reactiveEntityMapper);
    }

    /*==================================================================================================================
      ==================================== Conventional static factory API =============================================
      ================================================================================================================*/

    public static <E extends Entity> ReactiveVisualMapper<E> createMaster(Object pm, ReactiveEntityMapper<E> reactiveEntityMapper) {
        return initializeMaster(create(reactiveEntityMapper), pm);
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

    public static <E extends Entity> ReactiveVisualMapper<E> createGroup(Object pm, ReactiveEntityMapper<E> reactiveEntityMapper) {
        return initializeGroup(create(reactiveEntityMapper), pm);
    }

    protected static <E extends Entity> ReactiveVisualMapper<E> initializeGroup(ReactiveVisualMapper<E> group, Object pm) {
        if (pm instanceof HasGroupVisualResultProperty)
            group.visualizeResultInto(((HasGroupVisualResultProperty) pm).groupVisualResultProperty());
        if (pm instanceof HasGroupVisualSelectionProperty)
            group.setVisualSelectionProperty(((HasGroupVisualSelectionProperty) pm).groupVisualSelectionProperty());
        if (pm instanceof HasSelectedGroupProperty)
            group.setSelectedEntityHandler(((HasSelectedGroupProperty) pm)::setSelectedGroup);
        if (pm instanceof HasSelectedGroupReferenceResolver)
            ((HasSelectedGroupReferenceResolver) pm).setSelectedGroupReferenceResolver(group.getReactiveEntityMapper().getReactiveDqlQuery().getRootAliasReferenceResolver());
        return group;
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createSlave(Object pm, ReactiveEntityMapper<E> reactiveEntityMapper) {
        return initializeSlave(create(reactiveEntityMapper), pm);
    }

    protected static <E extends Entity> ReactiveVisualMapper<E> initializeSlave(ReactiveVisualMapper<E> slave, Object pm) {
        if (pm instanceof HasSlaveVisualResultProperty)
            slave.visualizeResultInto(((HasSlaveVisualResultProperty) pm).slaveVisualResultProperty());
        return slave;
    }

    /*==================================================================================================================
      ============================ Shortcut static factory API (ReactiveDqlQuery) ======================================
      ================================================================================================================*/

    public static <E extends Entity> ReactiveVisualMapper<E> create(ReactiveDqlQuery<E> reactiveDqlQuery) {
        return create(ReactiveEntityMapper.create(reactiveDqlQuery));
    }

    /*==================================================================================================================
      ========================== Shortcut static factory API (ReactiveDqlStatement) ====================================
      ================================================================================================================*/

    public static <E extends Entity> ReactiveVisualMapper<E> createReactiveChain() {
        return create(ReactiveEntityMapper.create(ReactiveDqlStatement.create()));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> create(ReactiveDqlStatement<E> reactiveDqlStatement) {
        return create(ReactiveEntityMapper.create(reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createReactiveChain(Object mixin) {
        return create(ReactiveEntityMapper.create(mixin, ReactiveDqlStatement.create()));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> create(Object mixin, ReactiveDqlStatement<E> reactiveDqlStatement) {
        return create(ReactiveEntityMapper.create(mixin, reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createPush(ReactiveDqlStatement<E> reactiveDqlStatement) {
        return create(ReactiveEntityMapper.createPush(reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createPushReactiveChain(Object mixin) {
        return create(ReactiveEntityMapper.createPush(mixin, ReactiveDqlStatement.create()));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createPush(Object mixin, ReactiveDqlStatement<E> reactiveDqlStatement) {
        return create(ReactiveEntityMapper.createPush(mixin, reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createMaster(Object pm, ReactiveDqlStatement<E> reactiveDqlStatement) {
        return createMaster(pm, ReactiveEntityMapper.create(reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createMasterReactiveChain(Object mixin, Object pm) {
        return createMaster(pm, ReactiveEntityMapper.create(mixin, ReactiveDqlStatement.create()));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createMaster(Object mixin, Object pm, ReactiveDqlStatement<E> reactiveDqlStatement) {
        return createMaster(pm, ReactiveEntityMapper.create(mixin, reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createMasterPush(Object pm, ReactiveDqlStatement<E> reactiveDqlStatement) {
        return createMaster(pm, ReactiveEntityMapper.createPush(reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createMasterPushReactiveChain(Object mixin, Object pm) {
        return createMaster(pm, ReactiveEntityMapper.createPush(mixin, ReactiveDqlStatement.createMaster(pm)));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createMasterPush(Object mixin, Object pm, ReactiveDqlStatement<E> reactiveDqlStatement) {
        return createMaster(pm, ReactiveEntityMapper.createPush(mixin, reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createGroup(Object pm, ReactiveDqlStatement<E> reactiveDqlStatement) {
        return createGroup(pm, ReactiveEntityMapper.create(reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createGroupReactiveChain(Object mixin, Object pm) {
        return createGroup(pm, ReactiveEntityMapper.create(mixin, ReactiveDqlStatement.createGroup(pm)));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createGroup(Object mixin, Object pm, ReactiveDqlStatement<E> reactiveDqlStatement) {
        return createGroup(pm, ReactiveEntityMapper.create(mixin, reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createGroupPush(Object pm, ReactiveDqlStatement<E> reactiveDqlStatement) {
        return createGroup(pm, ReactiveEntityMapper.createPush(reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createGroupPush(Object mixin, Object pm, ReactiveDqlStatement<E> reactiveDqlStatement) {
        return createGroup(pm, ReactiveEntityMapper.createPush(mixin, reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createSlave(Object pm, ReactiveDqlStatement<E> reactiveDqlStatement) {
        return createSlave(pm, ReactiveEntityMapper.create(reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createSlave(Object mixin, Object pm, ReactiveDqlStatement<E> reactiveDqlStatement) {
        return createSlave(pm, ReactiveEntityMapper.create(mixin, reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createSlavePush(Object pm, ReactiveDqlStatement<E> reactiveDqlStatement) {
        return createSlave(pm, ReactiveEntityMapper.createPush(reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createSlavePushReactiveChain(Object mixin, Object pm) {
        return createSlave(pm, ReactiveEntityMapper.createPush(mixin, ReactiveDqlStatement.create()));
    }

    public static <E extends Entity> ReactiveVisualMapper<E> createSlavePush(Object mixin, Object pm, ReactiveDqlStatement<E> reactiveDqlStatement) {
        return createSlave(pm, ReactiveEntityMapper.createPush(mixin, reactiveDqlStatement));
    }
}
