package mongoose.client.presentationmodel;

import javafx.beans.property.ObjectProperty;
import mongoose.shared.entities.MoneyTransfer;

public interface HasSelectedPaymentProperty {

    ObjectProperty<MoneyTransfer> selectedPaymentProperty();

    default MoneyTransfer getSelectedPayment() { return selectedPaymentProperty().getValue(); }

    default void setSelectedPayment(MoneyTransfer value) { selectedPaymentProperty().setValue(value); }

}
