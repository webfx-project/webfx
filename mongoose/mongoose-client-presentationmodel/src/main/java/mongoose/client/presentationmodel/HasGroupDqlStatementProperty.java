package mongoose.client.presentationmodel;

import javafx.beans.property.Property;
import webfx.framework.client.orm.entity.filter.DqlStatement;

public interface HasGroupDqlStatementProperty {

    Property<DqlStatement> groupDqlStatementProperty();
    default DqlStatement getGroupDqlStatement() { return groupDqlStatementProperty().getValue();}
    default void setGroupDqlStatement(DqlStatement value) { groupDqlStatementProperty().setValue(value);}

}
