package mongoose.client.presentationmodel;

import javafx.beans.property.Property;
import webfx.framework.client.orm.dql.DqlStatement;

public interface HasConditionDqlStatementProperty {

    Property<DqlStatement> conditionDqlStatementProperty();
    default DqlStatement getConditionDqlStatement() { return conditionDqlStatementProperty().getValue();}
    default void setConditionDqlStatement(DqlStatement value) { conditionDqlStatementProperty().setValue(value);}

}
