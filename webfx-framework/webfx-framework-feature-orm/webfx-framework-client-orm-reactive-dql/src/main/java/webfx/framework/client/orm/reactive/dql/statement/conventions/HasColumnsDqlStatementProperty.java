package webfx.framework.client.orm.reactive.dql.statement.conventions;

import javafx.beans.property.Property;
import webfx.framework.shared.orm.dql.DqlStatement;

public interface HasColumnsDqlStatementProperty {

    Property<DqlStatement> columnsDqlStatementProperty();
    default DqlStatement getColumnsDqlStatement() { return columnsDqlStatementProperty().getValue(); }
    default void setColumnsDqlStatement(DqlStatement value) { columnsDqlStatementProperty().setValue(value); }

}
