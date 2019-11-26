package mongoose.client.presentationmodel;

import javafx.beans.property.Property;
import webfx.framework.client.orm.entity.filter.DqlStatement;

public interface HasSelectedGroupConditionDqlStatementProperty {

    Property<DqlStatement> selectedGroupConditionDqlStatementProperty();
    default DqlStatement getSelectedGroupConditionDqlStatement() { return selectedGroupConditionDqlStatementProperty().getValue(); }
    default void setSelectedGroupConditionDqlStatement(DqlStatement value) { selectedGroupConditionDqlStatementProperty().setValue(value); }

}
