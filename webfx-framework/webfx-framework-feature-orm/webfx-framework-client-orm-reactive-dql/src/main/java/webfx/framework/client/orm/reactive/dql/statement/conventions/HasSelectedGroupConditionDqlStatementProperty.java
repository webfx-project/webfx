package webfx.framework.client.orm.reactive.dql.statement.conventions;

import javafx.beans.property.Property;
import webfx.framework.shared.orm.dql.DqlStatement;

public interface HasSelectedGroupConditionDqlStatementProperty {

    Property<DqlStatement> selectedGroupConditionDqlStatementProperty();
    default DqlStatement getSelectedGroupConditionDqlStatement() { return selectedGroupConditionDqlStatementProperty().getValue(); }
    default void setSelectedGroupConditionDqlStatement(DqlStatement value) { selectedGroupConditionDqlStatementProperty().setValue(value); }

}
