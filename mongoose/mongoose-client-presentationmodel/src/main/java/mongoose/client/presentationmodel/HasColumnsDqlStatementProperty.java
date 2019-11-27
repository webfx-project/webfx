package mongoose.client.presentationmodel;

import javafx.beans.property.Property;
import webfx.framework.client.orm.dql.DqlStatement;

public interface HasColumnsDqlStatementProperty {

    Property<DqlStatement> columnsDqlStatementProperty();
    default DqlStatement getColumnsDqlStatement() { return columnsDqlStatementProperty().getValue(); }
    default void setColumnsDqlStatement(DqlStatement value) { columnsDqlStatementProperty().setValue(value); }

}
