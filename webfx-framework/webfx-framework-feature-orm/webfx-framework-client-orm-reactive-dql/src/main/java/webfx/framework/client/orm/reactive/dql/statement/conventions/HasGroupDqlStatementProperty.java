package webfx.framework.client.orm.reactive.dql.statement.conventions;

import javafx.beans.property.Property;
import webfx.framework.shared.orm.dql.DqlStatement;

public interface HasGroupDqlStatementProperty {

    Property<DqlStatement> groupDqlStatementProperty();
    default DqlStatement getGroupDqlStatement() { return groupDqlStatementProperty().getValue();}
    default void setGroupDqlStatement(DqlStatement value) { groupDqlStatementProperty().setValue(value);}

}
