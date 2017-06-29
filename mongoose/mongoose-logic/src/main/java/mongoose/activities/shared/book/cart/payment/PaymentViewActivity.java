package mongoose.activities.shared.book.cart.payment;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import mongoose.activities.shared.book.cart.CartBasedViewActivity;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.domainmodel.format.PriceFormatter;
import mongoose.entities.*;
import mongoose.entities.markers.EntityHasName;
import mongoose.util.Labels;
import naga.commons.util.Dates;
import naga.commons.util.Strings;
import naga.commons.util.async.Batch;
import naga.commons.util.collection.Collections;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainModel;
import naga.framework.orm.entity.EntityList;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.UpdateStore;
import naga.framework.orm.mapping.QueryResultSetToEntityListGenerator;
import naga.framework.ui.controls.DialogUtil;
import naga.framework.ui.controls.LayoutUtil;
import naga.fx.spi.Toolkit;
import naga.fxdata.control.HtmlText;
import naga.platform.client.bus.WebSocketBus;
import naga.platform.client.url.location.WindowLocation;
import naga.platform.services.query.QueryArgument;
import naga.platform.spi.Platform;
import naga.platform.spi.client.ClientPlatform;

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
    private Label totalLabel;

    @Override
    public Node buildUi() {
        paymentsVBox = new VBox(20);

        HBox buttonBar = new HBox(20,
                newButton("Back", () -> getHistory().goBack()),
                LayoutUtil.createHGrowable(),
                newButton("MakePayment", this::submitPayment));

        BorderPane totalSection = HighLevelComponents.createSectionPanel(null, newLabel("TotalAmount:"), LayoutUtil.createHGrowable(), totalLabel = new Label());
        VBox vBox = new VBox(20, newLabel("PaymentPrompt:"), paymentsVBox, totalSection, buttonBar);
        BorderPane container = new BorderPane(LayoutUtil.createVerticalScrollPaneWithPadding(vBox));

        displayDocumentPaymentsIfReady();

        return container;
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
            Toolkit.get().scheduler().runInUiThread(() -> {
                paymentsVBox.getChildren().setAll(Collections.map(documentPayments, DocumentPayment::getNode));
                updateTotal();
            });
        }
    }

    private void updateTotal() {
        total = Collections.sum(documentPayments, DocumentPayment::getAmount);
        totalLabel.setText(formatCurrency(total));
    }

    private String formatPrice(int amount) {
        return PriceFormatter.formatWithoutCurrency(amount);
    }

    private String formatCurrency(int amount) {
        return PriceFormatter.formatWithCurrency(amount, getEvent());
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
                String title = document.getFullName() + " - " + instantTranslate("Booking") + " " + document.getRef() + "   " + instantTranslate("Fee:") + " " + formatCurrency(document.getPriceNet()) + "   " + instantTranslate("Deposit:") + " " + formatCurrency(document.getPriceDeposit()) + "   " + instantTranslate("MinDeposit:") + " " + formatCurrency(document.getPriceMinDeposit());
                BorderPane bp = HighLevelComponents.createSectionPanel(null, new Label(title), LayoutUtil.createHGrowable(), newLabel("PaymentAmount"));
                hBox = new HBox(20);
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setPadding(new Insets(10));
                radioGroup = new ToggleGroup();
                amountTextField = new TextField();
                amountTextField.setAlignment(Pos.BASELINE_RIGHT);
                amountTextField.textProperty().addListener((observable, oldValue, text) -> {
                    try {
                        updateAmount((int) (Float.parseFloat(text) * 100), true);
                    } catch (NumberFormatException e) {
                    }
                });
                amountTextField.setPrefWidth(100d);
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
            RadioButton rb = new RadioButton(formatCurrency(price) + (translationKey == null ? "" : " (" + instantTranslate(translationKey) + ")"));
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
            slider.setValue((double) amount);
            radioGroup.selectToggle(amount == maxAmount ? maxRadioButton : amount == minAmount ? minRadioButton : amount == 0 ? zeroRadioButton : null);
            updating = false;
            if (updateTotal)
                updateTotal();
        }

        int getAmount() {
            return amount;
        }
    }

    private MoneyTransfer lastPayment;
    private Document doc;
    private String paymentRef;

    private void submitPayment() {
        List<DocumentPayment> payments = Collections.filter(documentPayments, p -> p.getAmount() > 0);
        if (payments.isEmpty())
            return;
        EntityStore loadStore = getEvent().getStore();
        UpdateStore updateStore = UpdateStore.createAbove(loadStore);
        MoneyTransfer moneyTransfer = updateStore.insertEntity(MoneyTransfer.class);
        moneyTransfer.setPending(true);
        moneyTransfer.setMethod(Method.ONLINE_METHOD_ID);
        paymentRef = null;
        if (payments.size() == 1) {
            DocumentPayment payment = Collections.first(payments);
            moneyTransfer.setDocument(doc = payment.document);
            moneyTransfer.setAmount(payment.getAmount());
            paymentRef = payment.document.getRef().toString();
        } else {
            moneyTransfer.setSpread(true);
            doc = null;
            for (DocumentPayment payment : payments) {
                MoneyTransfer childTransfer = updateStore.insertEntity(MoneyTransfer.class);
                childTransfer.setDocument(payment.document);
                childTransfer.setAmount(payment.getAmount());
                childTransfer.setParent(moneyTransfer);
                if (doc == null)
                    doc = payment.document;
                paymentRef = (paymentRef == null ? "" : paymentRef + "-") + payment.document.getRef().toString();;
            }
        }
        updateStore.executeUpdate().setHandler(ar -> {
            if (ar.failed())
                Platform.log("Error submitting payment", ar.cause());
            else {
                cartService().unload();
                DataSourceModel dataSourceModel = loadStore.getDataSourceModel();
                Object dataSourceId = dataSourceModel.getId();
                Object[] paymentIdParameter = {ar.result().getArray()[0].getGeneratedKeys()[0]};
                DomainModel domainModel = dataSourceModel.getDomainModel();
                SqlCompiled sqlCompiled1 = domainModel.compileSelect("select <frontend_loadEvent> from GatewayParameter gp where exists(select MoneyTransfer mt where mt=? and (gp.account=mt.toMoneyAccount or gp.account=null and gp.company=mt.toMoneyAccount.gatewayCompany)) order by company");
                SqlCompiled sqlCompiled2 = domainModel.compileSelect("select <frontend_cart> from MoneyTransfer where id=?");
                Platform.getQueryService().executeQueryBatch(
                        new Batch<>(new QueryArgument[]{
                                new QueryArgument(sqlCompiled1.getSql(), paymentIdParameter, dataSourceId),
                                new QueryArgument(sqlCompiled2.getSql(), paymentIdParameter, dataSourceId)
                        })
                ).setHandler(ar2 -> {
                    if (ar.failed())
                        Platform.log("Error submitting payment", ar.cause());
                    else {
                        EntityList<GatewayParameter> gatewayParameters = QueryResultSetToEntityListGenerator.createEntityList(ar2.result().getArray()[0], sqlCompiled1.getQueryMapping(), loadStore, "gatewayParameters");
                        lastPayment = (MoneyTransfer) QueryResultSetToEntityListGenerator.createEntityList(ar2.result().getArray()[1], sqlCompiled2.getQueryMapping(), loadStore, "lastPayment").get(0);
                        Toolkit.get().scheduler().runInUiThread(() -> {
                            String innerHtml = generateHtmlForm(gatewayParameters);
                            Platform.log(innerHtml);
                            HtmlText htmlText = LayoutUtil.setPrefSizeToInfinite(new HtmlText(innerHtml));
                            DialogUtil.showModalNodeInGoldLayout(htmlText, (Pane) getNode(), 0.9, 0.8);
                        });
                    }
                });
            }
        });
    }

    private String paymentUrl;

    private String generateHtmlForm(List<GatewayParameter> gatewayParameters) {
        boolean live = getEvent().isLive();
        boolean test = !live;
        String certificate = paymentUrl = null;
        gatewayParameters = Collections.filter(gatewayParameters, gp -> live && gp.isLive() || test && gp.isTest());
        StringBuilder sb = new StringBuilder();
        for (GatewayParameter gp : gatewayParameters) {
            switch (gp.getName()) {
                case "paymentUrl": paymentUrl = gp.getValue(); break;
                case "certificate": certificate = gp.getValue(); break;
                default: sb.append("\n<input type='hidden' name=").append(htmlQuote(gp.getName())).append(" value=").append(htmlQuote(replaceBrackets(gp.getValue()))).append("/>");
            }
        }
        if (certificate != null) { // certificate => computing vads signature
            String signature = "";
            Collections.sort(gatewayParameters, Collections.comparing(EntityHasName::getName));
            for (GatewayParameter gp : gatewayParameters) {
                if (gp.getName().startsWith("vads_"))
                    signature += replaceBrackets(gp.getValue()) + '+';
            }
            signature += certificate;
            signature = Sha1.hash(signature);
            sb.append("\n<input type='hidden' name='signature' value=").append(htmlQuote(signature)).append("/>");
        }
        return replaceBrackets("<html><body><form id='gatewayForm' action='[paymentUrl]' method='POST'>") + sb + "\n</form><script type='text/javascript'>document.getElementById('gatewayForm').submit();</script></body></html>";
    }

    private String replaceBrackets(String value) {
        Event event = getEvent();
        value = Strings.replaceAllSafe(value, "[paymentUrl]", paymentUrl);
        value = Strings.replaceAllSafe(value, "[ref]", paymentRef);
        value = Strings.replaceAllSafe(value, "[bref]", doc.getRef().toString());
        value = Strings.replaceAllSafe(value, "[amount]", PriceFormatter.INSTANCE.format(lastPayment.getAmount(), true).toString());
        value = Strings.replaceAllSafe(value, "[amount_int]", lastPayment.getAmount().toString());
        value = Strings.replaceAllSafe(value, "[event]", Labels.instantTranslate(event, getI18n()));
        value = Strings.replaceAllSafe(value, "[eventid]", event.getPrimaryKey().toString());
        value = Strings.replaceAllSafe(value, "[eventid5]", digits(event.getPrimaryKey().toString(), 5, true));
        value = Strings.replaceAllSafe(value, "[firstName]", doc.getFirstName());
        value = Strings.replaceAllSafe(value, "[lastName]", doc.getLastName());
        value = Strings.replaceAllSafe(value, "[name]", doc.isOrdained() ? doc.getLayName() : doc.getFullName());
        value = Strings.replaceAllSafe(value, "[street]", doc.getStreet());
        value = Strings.replaceAllSafe(value, "[city]", doc.getCityName());
        value = Strings.replaceAllSafe(value, "[admin1]", doc.getAdmin1Name());
        value = Strings.replaceAllSafe(value, "[admin2]", doc.getAdmin2Name());
        ////"[state_2c]": firstDocument.person_state == null ? null : firstDocument.person_state.substr(0, 2),
        value = Strings.replaceAllSafe(value, "[postCode]", doc.getPostCode());
        value = Strings.replaceAllSafe(value, "[country]", doc.getCountry() != null ? doc.getCountry().getName() : doc.getCountryName());
        value = Strings.replaceAllSafe(value, "[countryCode]", doc.getCountry() != null ? doc.getCountry().getIsoAlpha2() : null);
        ////"[postcode_int]": keepDigitsOnly(firstDocument.person_postCode),
        value = Strings.replaceAllSafe(value, "[phone]", doc.getPhone());
        ////"[phone_int]": keepDigitsOnly(firstDocument.person_phone),
        value = Strings.replaceAllSafe(value, "[email]", doc.getEmail());
        WindowLocation currentLocation = ClientPlatform.get().getCurrentLocation();
        value = Strings.replaceAllSafe(value, "[frontendUrl]", currentLocation.getOrigin());
        String cartUrl = Strings.removeSuffix(currentLocation.getHref(), "/payment");
        value = Strings.replaceAllSafe(value, "[cartUrl]", cartUrl);
        value = Strings.replaceAllSafe(value, "[session]", ((WebSocketBus) Platform.bus()).getSessionId());
        value = Strings.replaceAllSafe(value, "[lang]", getLanguage().toString());
        value = Strings.replaceAllSafe(value, "[paymentId]", lastPayment.getPrimaryKey().toString());
        value = Strings.replaceAllSafe(value, "[paymentId6]", digits(lastPayment.getPrimaryKey().toString(), 6, false));
        value = Strings.replaceAllSafe(value, "[date]", Dates.format(lastPayment.getDate(), "yyyyMMddHHmmss"));
        return value;
    }

    private static String htmlQuote(String s) {
        return "'" + escapeHtml(s) + "'";
    }

    private static String escapeHtml(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = s.length(); i < n; i++) {
            char c = s.charAt(i);
            if (c > 127 || c == '"' || c == '<' || c == '>' || c == '&' || c == '\'') {
                sb.append("&#");
                sb.append((int) c);
                sb.append(';');
            } else
                sb.append(c);
        }
        return sb.toString();
    }

    private static String digits(String s, int n, boolean right) {
        s = "" + s;
        while (s.length() < n)
            s = right ? s + '0' : '0' + s;
        return s.substring(s.length() - n, s.length());
    }
}
