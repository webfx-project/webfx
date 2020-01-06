package webfx.framework.client.orm.reactive.dql.statement;

import javafx.beans.value.ObservableValue;
import webfx.framework.shared.orm.dql.DqlStatement;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.util.function.Converter;

import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public interface ReactiveDqlStatementAPI<E, THIS> {

    ReactiveDqlStatement<E> getReactiveDqlStatement();

    default Object getDomainClassId() {
        return getReactiveDqlStatement().getDomainClassId();
    }

    default DqlStatement getBaseStatement() {
        return getReactiveDqlStatement().getBaseStatement();
    }

    default DqlStatement getResultingDqlStatement() {
        return getReactiveDqlStatement().getResultingDqlStatement();
    }

    default ObservableValue<DqlStatement> resultingDqlStatementProperty() {
        return getReactiveDqlStatement().resultingDqlStatementProperty();
    }

    default void addResultTransformer(Function<DqlStatement, DqlStatement> resultTransformer) {
        getReactiveDqlStatement().addResultTransformer((resultTransformer));
    }

    /*==================================================================================================================
      ============================================== Fluent API ========================================================
      ================================================================================================================*/

    default THIS always(ObservableValue<DqlStatement> dqlStatementProperty) {
        getReactiveDqlStatement().always(dqlStatementProperty);
        return (THIS) this;
    }

    default THIS always(DqlStatement dqlStatement) {
        getReactiveDqlStatement().always(dqlStatement);
        return (THIS) this;
    }

    default THIS always(String dqlStatementString) {
        getReactiveDqlStatement().always(dqlStatementString);
        return (THIS) this;
    }

    default THIS always(JsonObject json) {
        getReactiveDqlStatement().always(json);
        return (THIS) this;
    }

    default THIS always(Object jsonOrClass) {
        getReactiveDqlStatement().always(jsonOrClass);
        return (THIS) this;
    }

    default <T> THIS always(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter) {
        getReactiveDqlStatement().always(property, toDqlStatementConverter);
        return (THIS) this;
    }

    default THIS ifTrue(ObservableValue<Boolean> ifProperty, DqlStatement dqlStatement) {
        getReactiveDqlStatement().ifTrue(ifProperty, dqlStatement);
        return (THIS) this;
    }

    default THIS ifTrue(ObservableValue<Boolean> ifProperty, String dqlStatementString) {
        getReactiveDqlStatement().ifTrue(ifProperty, dqlStatementString);
        return (THIS) this;
    }

    default THIS ifFalse(ObservableValue<Boolean> ifProperty, DqlStatement dqlStatement) {
        getReactiveDqlStatement().ifFalse(ifProperty, dqlStatement);
        return (THIS) this;
    }

    default THIS ifFalse(ObservableValue<Boolean> ifProperty, String dqlStatementString) {
        getReactiveDqlStatement().ifTrue(ifProperty, dqlStatementString);
        return (THIS) this;
    }


    default <T extends Number> THIS ifPositive(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter) {
        getReactiveDqlStatement().ifPositive(property, toDqlStatementConverter);
        return (THIS) this;
    }

    default THIS ifNotEmpty(ObservableValue<String> property, Converter<String, DqlStatement> toDqlStatementConverter) {
        getReactiveDqlStatement().ifNotEmpty(property, toDqlStatementConverter);
        return (THIS) this;
    }

    default THIS ifTrimNotEmpty(ObservableValue<String> property, Converter<String, DqlStatement> toDqlStatementConverter) {
        getReactiveDqlStatement().ifTrimNotEmpty(property, toDqlStatementConverter);
        return (THIS) this;
    }

    default <T> THIS ifNotNull(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter) {
        getReactiveDqlStatement().ifNotNull(property, toDqlStatementConverter);
        return (THIS) this;
    }

    default <T> THIS ifNotNullOtherwise(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter, DqlStatement otherwiseDqlStatement) {
        getReactiveDqlStatement().ifNotNullOtherwise(property, toDqlStatementConverter, otherwiseDqlStatement);
        return (THIS) this;
    }

    default <T> THIS ifNotNullOtherwiseEmpty(ObservableValue<T> property, Converter<T, DqlStatement> toDqlStatementConverter) {
        getReactiveDqlStatement().ifNotNullOtherwiseEmpty(property, toDqlStatementConverter);
        return (THIS) this;
    }

    default <T> THIS ifNotNullOtherwiseEmptyString(ObservableValue<T> property, Converter<T, String> toDqlStatementStringConverter) {
        getReactiveDqlStatement().ifNotNullOtherwiseEmptyString(property, toDqlStatementStringConverter);
        return (THIS) this;
    }
}
