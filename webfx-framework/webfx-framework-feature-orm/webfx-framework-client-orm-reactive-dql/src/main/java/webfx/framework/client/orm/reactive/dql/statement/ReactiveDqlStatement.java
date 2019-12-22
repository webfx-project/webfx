package webfx.framework.client.orm.reactive.dql.statement;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import webfx.framework.client.orm.reactive.dql.statement.conventions.*;
import webfx.framework.shared.orm.dql.DqlStatement;
import webfx.framework.shared.orm.dql.DqlStatementBuilder;
import webfx.kit.util.properties.Properties;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.util.Numbers;
import webfx.platform.shared.util.Strings;
import webfx.platform.shared.util.function.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static webfx.framework.shared.orm.dql.DqlStatement.limit;

/**
 * @author Bruno Salmon
 */
public final class ReactiveDqlStatement<E> implements ReactiveDqlStatementAPI<E, ReactiveDqlStatement<E>> {

    private Class<E> domainJavaClass;
    private Object domainClassId;
    private final List<ObservableValue<DqlStatement>> dqlStatementProperties = new ArrayList<>();
    private DqlStatement baseStatement;
    private boolean markDqlStatementsAsChanged;
    private final ObjectProperty<DqlStatement> resultingDqlStatementProperty = new SimpleObjectProperty<>();
    private final List<Function<DqlStatement, DqlStatement>> resultTransformers = new ArrayList<>();

    /*==================================================================================================================
      ============================================== Constructors ======================================================
      ================================================================================================================*/

    public ReactiveDqlStatement() {}

    public ReactiveDqlStatement(Class<E> domainJavaClass) {
        this.domainJavaClass = domainJavaClass;
    }

    @Override
    public ReactiveDqlStatement<E> getReactiveDqlStatement() {
        return this;
    }

    @Override
    public Object getDomainClassId() {
        return domainClassId;
    }

    @Override
    public DqlStatement getBaseStatement() {
        return baseStatement;
    }

    @Override
    public DqlStatement getResultingDqlStatement() {
        return resultingDqlStatementProperty.get();
    }

    @Override
    public ObservableValue<DqlStatement> resultingDqlStatementProperty() {
        return resultingDqlStatementProperty;
    }

    @Override
    public void addResultTransformer(Function<DqlStatement, DqlStatement> resultTransformer) {
        resultTransformers.add(resultTransformer);
    }

    private void markDqlStatementsAsChanged() {
        if (!markDqlStatementsAsChanged) {
            markDqlStatementsAsChanged = true;
            UiScheduler.scheduleDeferred(() -> {
                markDqlStatementsAsChanged = false;
                DqlStatement result = mergeDqlStatements();
                resultingDqlStatementProperty.setValue(result);
            });
        }
    }

    private DqlStatement mergeDqlStatements() {
        DqlStatementBuilder mergeBuilder = new DqlStatementBuilder(getDomainClassId());
        for (ObservableValue<DqlStatement> dqlStatementProperty : dqlStatementProperties)
            mergeBuilder.merge(dqlStatementProperty.getValue());
        DqlStatement result = mergeBuilder.build();
        for (Function<DqlStatement, DqlStatement> resultTransformer : resultTransformers)
            if (!result.isInherentlyEmpty())
                result = resultTransformer.apply(result);
        return result;
    }


    /*==================================================================================================================
      ============================================== Fluent API ========================================================
      ================================================================================================================*/

    public ReactiveDqlStatement<E> always(ObservableValue<DqlStatement> dqlStatementProperty) {
        Properties.runOnPropertiesChange(this::markDqlStatementsAsChanged, dqlStatementProperty);
        return addWithoutListening(dqlStatementProperty);
    }

    private ReactiveDqlStatement<E> addWithoutListening(ObservableValue<DqlStatement> dqlStatementProperty) {
        dqlStatementProperties.add(dqlStatementProperty);
        markDqlStatementsAsChanged();
        return this;
    }

    @Override
    public ReactiveDqlStatement<E> always(DqlStatement dqlStatement) {
        if (domainClassId == null) {
            domainClassId = dqlStatement.getDomainClassId();
            baseStatement = dqlStatement;
        }
        return addWithoutListening(new SimpleObjectProperty<>(dqlStatement));
    }

    @Override
    public ReactiveDqlStatement<E> always(String dqlStatementString) {
        return always(DqlStatement.parse(dqlStatementString));
    }

    @Override
    public ReactiveDqlStatement<E> always(JsonObject json) {
        return always(new DqlStatement(json));
    }

    @Override
    public ReactiveDqlStatement<E> always(Object jsonOrClass) {
        return always(new DqlStatementBuilder(jsonOrClass).build());
    }

    @Override
    public <T> ReactiveDqlStatement<E> always(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter) {
        return addWithoutListening(Properties.compute(property, t -> {
            // Calling the converter to get the dql statement
            DqlStatement dqlStatement = toDqlStatementConverter.convert(t);
            // If different from last value, this will trigger a global change check
            // However it's possible that the DqlStatement hasn't changed but contains parameters that have changed (ex: name like ?search)
            // In that case (DqlStatement with parameter), we always schedule a global change check (which will consider parameters)
            //if (dqlStatementString != null && dqlStatementString.contains("?")) // Simple parameter test with ?
            markDqlStatementsAsChanged();
            return dqlStatement;
        }));
    }

    private <T> Converter<T, DqlStatement> stringToDqlStatementConverter(Converter<T, String> toDqlStatementStringConverter) {
        return t -> DqlStatement.parse(toDqlStatementStringConverter.convert(t));
    }

    public ReactiveDqlStatement<E> ifTrue(ObservableValue<Boolean> ifProperty, DqlStatement dqlStatement) {
        return addWithoutListening(Properties.compute(ifProperty, value -> {
            markDqlStatementsAsChanged();
            return value ? dqlStatement : null;
        }));
    }

    @Override
    public ReactiveDqlStatement<E> ifTrue(ObservableValue<Boolean> ifProperty, String dqlStatementString) {
        return ifTrue(ifProperty, DqlStatement.parse(dqlStatementString));
    }

    @Override
    public ReactiveDqlStatement<E> ifFalse(ObservableValue<Boolean> ifProperty, DqlStatement dqlStatement) {
        return addWithoutListening(Properties.compute(ifProperty, value -> {
            markDqlStatementsAsChanged();
            return value ? null : dqlStatement;
        }));
    }

    public ReactiveDqlStatement<E> ifFalse(ObservableValue<Boolean> ifProperty, String dqlStatementString) {
        return ifFalse(ifProperty, DqlStatement.parse(dqlStatementString));
    }


    @Override
    public <T extends Number> ReactiveDqlStatement<E> ifPositive(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter) {
        return always(property, value -> Numbers.isPositive(value) ? toDqlStatementConverter.convert(value) : null);
    }

    @Override
    public ReactiveDqlStatement<E> ifNotEmpty(ObservableValue<String> property, Converter<String, DqlStatement> toDqlStatementConverter) {
        return always(property, s -> Strings.isEmpty(s) ? null : toDqlStatementConverter.convert(s));
    }

    @Override
    public ReactiveDqlStatement<E> ifTrimNotEmpty(ObservableValue<String> property, Converter<String, DqlStatement> toDqlStatementConverter) {
        return ifNotEmpty(property, s -> toDqlStatementConverter.convert(Strings.trim(s)));
    }

    @Override
    public <T> ReactiveDqlStatement<E> ifNotNull(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter) {
        return ifNotNullOtherwise(property, toDqlStatementConverter, null);
    }

    @Override
    public <T> ReactiveDqlStatement<E> ifNotNullOtherwise(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter, DqlStatement otherwiseDqlStatement) {
        return always(property, value -> value == null ? otherwiseDqlStatement : toDqlStatementConverter.convert(value));
    }

    @Override
    public <T> ReactiveDqlStatement<E> ifNotNullOtherwiseEmpty(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter) {
        return ifNotNullOtherwise(property, toDqlStatementConverter, DqlStatement.EMPTY_STATEMENT);
    }

    @Override
    public <T> ReactiveDqlStatement<E> ifNotNullOtherwiseEmptyString(ObservableValue<T> property, Converter<T, String> toDqlStatementStringConverter) {
        return ifNotNullOtherwiseEmpty(property, stringToDqlStatementConverter(toDqlStatementStringConverter));
    }

    /*==================================================================================================================
      ======================================= Classic static factory API ===============================================
      ================================================================================================================*/

    public static <E> ReactiveDqlStatement<E> create() {
        return create(null);
    }

    public static <E> ReactiveDqlStatement<E> create(Class<E> domainJavaClass) {
        return new ReactiveDqlStatement<>(domainJavaClass);
    }

    /*==================================================================================================================
      ==================================== Conventional static factory API =============================================
      ================================================================================================================*/

    public static <E> ReactiveDqlStatement<E> createMaster(Object pm) {
        return createMaster(null, pm);
    }

    public static <E> ReactiveDqlStatement<E> createMaster(Class<E> domainJavaClass, Object pm) {
        return initializeMaster(create(domainJavaClass), pm);
    }

    protected static <E> ReactiveDqlStatement<E> initializeMaster(ReactiveDqlStatement<E> master, Object pm) {
        // Applying the condition and columns selected by the user
        if (pm instanceof HasConditionDqlStatementProperty)
            master.ifNotNullOtherwiseEmpty(((HasConditionDqlStatementProperty) pm).conditionDqlStatementProperty(), conditionDqlStatement -> conditionDqlStatement);
        if (pm instanceof HasColumnsDqlStatementProperty)
            master.ifNotNullOtherwiseEmpty(((HasColumnsDqlStatementProperty) pm).columnsDqlStatementProperty(), columnsDqlStatement -> columnsDqlStatement);
        // Also, in case groups are showing and a group is selected, applying the condition associated with that group
        if (pm instanceof HasSelectedGroupConditionDqlStatementProperty)
            master.ifNotNull(((HasSelectedGroupConditionDqlStatementProperty) pm).selectedGroupConditionDqlStatementProperty(), selectedGroupConditionDqlStatement -> selectedGroupConditionDqlStatement);
        // Limit clause
        if (pm instanceof HasLimitProperty)
            master.ifPositive(((HasLimitProperty) pm).limitProperty(), limit -> limit("?", limit));
        return master;
    }

    public static <E> ReactiveDqlStatement<E> createGroup(Object pm) {
        return createGroup(null, pm);
    }

    public static <E> ReactiveDqlStatement<E> createGroup(Class<E> domainJavaClass, Object pm) {
        return initializeGroup(create(domainJavaClass), pm);
    }

    protected static <E> ReactiveDqlStatement<E> initializeGroup(ReactiveDqlStatement<E> group, Object pm) {
        // Applying the condition and group selected by the user
        if (pm instanceof HasConditionDqlStatementProperty)
            group.ifNotNullOtherwiseEmpty(((HasConditionDqlStatementProperty) pm).conditionDqlStatementProperty(), conditionDqlStatement -> conditionDqlStatement);
        if (pm instanceof HasGroupDqlStatementProperty)
            group.ifNotNullOtherwiseEmpty(((HasGroupDqlStatementProperty) pm).groupDqlStatementProperty(), groupDqlStatement -> groupDqlStatement.getGroupBy() != null ? groupDqlStatement : DqlStatement.EMPTY_STATEMENT);
        return group;
    }

    public static <E> ReactiveDqlStatement<E> createSlave(Object pm) {
        return createSlave(null, pm);
    }

    public static <E> ReactiveDqlStatement<E> createSlave(Class<E> domainJavaClass, Object pm) {
        return initializeSlave(create(domainJavaClass), pm);
    }

    protected static <E> ReactiveDqlStatement<E> initializeSlave(ReactiveDqlStatement<E> slave, Object pm) {
        if (pm instanceof HasSelectedMasterProperty)
            slave.ifTrue(Properties.compute(((HasSelectedMasterProperty) pm).selectedMasterProperty(), selectedMaster ->
                    selectedMaster == null || pm instanceof HasSlaveVisibilityCondition && !((HasSlaveVisibilityCondition) pm).isSlaveVisible(selectedMaster)
            ), DqlStatement.EMPTY_STATEMENT);
        return slave;
    }
}
