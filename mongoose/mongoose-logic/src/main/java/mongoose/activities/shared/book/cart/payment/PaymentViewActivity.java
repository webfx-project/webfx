package mongoose.activities.shared.book.cart.payment;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mongoose.activities.shared.book.cart.CartBasedViewActivity;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.domainmodel.format.PriceFormatter;
import mongoose.entities.Document;
import naga.commons.util.collection.Collections;
import naga.framework.ui.controls.LayoutUtil;
import naga.framework.ui.i18n.I18n;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class PaymentViewActivity extends CartBasedViewActivity {

    private VBox paymentsVBox;
    private List<DocumentPayment> documentPayments;
    private int notPaidEnoughCount;
    private int notPaidFullCount;
    private int total;
    private Text totalTextField;

    @Override
    public Node buildUi() {
        paymentsVBox = new VBox(20);

        I18n i18n = getI18n();
        Button backButton = i18n.translateText(new Button(), "Back");
        Button paymentButton = i18n.translateText(new Button(), "MakePayment");
        HBox buttonBar = new HBox(20, backButton, LayoutUtil.createHGrowable(), paymentButton);

        backButton.setOnAction(e -> getHistory().goBack());

        BorderPane totalSection = HighLevelComponents.createSectionPanel(null, i18n.translateText(new Label(), "TotalAmount:"), LayoutUtil.createHGrowable(), totalTextField = new Text());
        VBox vBox = new VBox(20, i18n.translateText(new Label(), "PaymentPrompt"), paymentsVBox, totalSection, buttonBar);
        displayDocumentPaymentsIfReady();
        return LayoutUtil.createVerticalScrollPane(vBox);
    }

    @Override
    protected void onCartLoaded() {
        displayDocumentPaymentsIfReady();
    }

    private void displayDocumentPaymentsIfReady() {
        List<Document> cartDocuments = cartService().getCartDocuments();
        if (cartDocuments != null && paymentsVBox != null) {
            notPaidEnoughCount = notPaidFullCount = 0;
            documentPayments = Collections.mapFilter(cartDocuments, DocumentPayment::new, DocumentPayment::hasBalance);
            paymentsVBox.getChildren().setAll(Collections.map(documentPayments, DocumentPayment::getNode));
            updateTotal();
        }
    }

    private void updateTotal() {
        total = Collections.sum(documentPayments, DocumentPayment::getAmount);
        totalTextField.setText(formatCurrency(total));
    }

    private String formatPrice(int amount) {
        return PriceFormatter.formatWithoutCurrency(amount);
    }

    private String formatCurrency(int amount) {
        return PriceFormatter.formatWithCurrency(amount, cartService().getEventService().getEvent());
    }

    private static int CENTS = 100;
    private static int MIN_AMOUNT_TO_PAY = 10 * CENTS;

    private class DocumentPayment {
        final Document document;
        final int minAmount, maxAmount;
        int amount;
        Node node;
        HBox hBox;
        ToggleGroup radioGroup;
        RadioButton zeroRadioButton, minRadioButton, maxRadioButton;
        Slider slider;
        TextField amountTextField;

        DocumentPayment(Document document) {
            this.document = document;
            int priceDeposit = document.getPriceDeposit();
            int priceMinDeposit = document.getPriceMinDeposit();
            maxAmount = document.getPriceNet() - priceDeposit;
            int min = priceMinDeposit - priceDeposit;
            if (min <= MIN_AMOUNT_TO_PAY) // 10.00 as minimum amount to pay
                min = Math.min(MIN_AMOUNT_TO_PAY, maxAmount);
            minAmount = min;
            if (hasBalance())
                notPaidFullCount++;
            if (!document.isCancelled() && priceDeposit < priceMinDeposit)
                notPaidEnoughCount++;
        }

        boolean hasBalance() {
            return maxAmount > 0;
        }

        Node getNode() {
            if (node == null) {
                I18n i18n = getI18n();
                BorderPane bp = HighLevelComponents.createSectionPanel(null, new Text(document.getFullName() + " - " + i18n.instantTranslate("Booking") + " " + document.getRef() + "   " + i18n.instantTranslate("Fee:") + " " + formatCurrency(document.getPriceNet()) + "   " + i18n.instantTranslate("Deposit:") + " " + formatCurrency(document.getPriceDeposit()) + "   " + i18n.instantTranslate("MinDeposit:") + " " + formatCurrency(document.getPriceMinDeposit())));
                hBox = new HBox(20);
                radioGroup = new ToggleGroup();
                amountTextField = new TextField();
                amountTextField.setAlignment(Pos.BASELINE_RIGHT);
                amountTextField.textProperty().addListener((observable, oldValue, text) -> {
                    try {
                        updateAmount((int) (Float.parseFloat(text) * 100), true);
                    } catch (NumberFormatException e) {
                    }
                });
                amountTextField.setPrefWidth(100);
                //LayoutUtil.setPrefMaxWidthToMin(amountTextField);
                amountTextField.focusedProperty().addListener((observable, oldValue, focused) -> updateAmount(amount, false));
                slider = new Slider(minAmount, maxAmount, maxAmount);
                slider.valueProperty().addListener((observable, oldValue, newValue) -> updateAmount(newValue.intValue(), true));
                if (notPaidFullCount > 1)
                    zeroRadioButton = addRadioButton(0, "notNow");
                if (minAmount > 0 && minAmount < maxAmount)
                    minRadioButton = addRadioButton(minAmount, document.getPriceDeposit() < document.getPriceMinDeposit() ? "minDeposit" : null);
                addNode(slider);
                maxRadioButton = addRadioButton(maxAmount, "payInFull");
                updateAmount(minAmount, false);
                addNode(LayoutUtil.createHGrowable());
                addNode(amountTextField);
                bp.setCenter(hBox);
                node = bp;
            }
            return node;
        }

        private RadioButton addRadioButton(int price, Object translationKey) {
            RadioButton rb = new RadioButton(formatCurrency(price) + (translationKey == null ? "" : " (" + getI18n().instantTranslate(translationKey) + ")"));
            rb.setToggleGroup(radioGroup);
            rb.setOnAction(e -> updateAmount(price, true));
            addNode(rb);
            return rb;
        }

        private void addNode(Node node) {
            hBox.getChildren().add(node);
        }

        private boolean updating;

        private void updateAmount(int amount, boolean updateTotal) {
            if (updating)
                return;
            updating = true;
            if (amount < minAmount)
                amount = 0;
            else if (amount > minAmount && amount < maxAmount)
                amount = Math.max(minAmount, (amount / CENTS) * CENTS);
            else if (amount > maxAmount)
                amount = maxAmount;
            this.amount = amount;
            if (!amountTextField.isFocused())
                amountTextField.setText(formatPrice(amount));
            slider.setValue(amount);
            radioGroup.selectToggle(amount == maxAmount ? maxRadioButton : amount == minAmount ? minRadioButton : amount == 0 ? zeroRadioButton : null);
            updating = false;
            if (updateTotal)
                updateTotal();
        }

        int getAmount() {
            return amount;
        }
    }
}
