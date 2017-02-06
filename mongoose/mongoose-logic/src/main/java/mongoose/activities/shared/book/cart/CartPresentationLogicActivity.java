package mongoose.activities.shared.book.cart;

import naga.framework.activity.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityImpl;
import naga.framework.orm.entity.Entity;
import naga.framework.ui.filter.ReactiveExpressionFilter;

/**
 * @author Bruno Salmon
 */
public class CartPresentationLogicActivity extends DomainPresentationLogicActivityImpl<CartPresentationModel> {

    public CartPresentationLogicActivity() {
        super(CartPresentationModel::new);
    }

    @Override
    protected void updatePresentationModelFromRouteParameters(CartPresentationModel pm) {
        pm.cartUuidProperty().setValue(getParameter("cartUuid"));
    }

    @Override
    protected void startLogic(CartPresentationModel pm) {
        // Setting up the documents filter
        ReactiveExpressionFilter documentFilter = createReactiveExpressionFilter("{class: 'Document', orderBy: 'creationDate desc'}")
                // Condition
                .combine(pm.cartUuidProperty(), s -> "{where: 'cart.uuid=`" + s + "`'}")
                //.registerParameter(new Parameter("cartUuid", "constant"))
                //.registerParameter(new Parameter("cartUuid", pm.cartUuidProperty()))
                //.combine("{where: 'cart.uuid=?cartUuid'}")
                .setExpressionColumns("[" +
                        "'ref'," +
                        "'person_firstName'," +
                        "'person_lastName'," +
                        "{expression: 'price_net', format: 'price'}," +
                        "{expression: 'price_deposit', format: 'price'}," +
                        "{expression: 'price_balance', format: 'price'}" +
                        "]")
                .applyDomainModelRowStyle()
                .displayResultSetInto(pm.documentDisplayResultSetProperty())
                .selectFirstRowOnFirstDisplay(pm.documentDisplaySelectionProperty(), pm.cartUuidProperty())
                .start();

        // Setting up the document lines filter
        createReactiveExpressionFilter("{class: 'DocumentLine', where: 'item.family.code!=`round`', orderBy: 'item.family.ord,item.ord'}")
                // Condition
                .combine(pm.cartUuidProperty(), s -> "{where: 'document.cart.uuid=`" + s + "`'}")
                .combine(documentFilter.getDisplaySelectionProperty(), displaySelection -> {
                    Entity selectedEntity = documentFilter.getSelectedEntity();
                    return selectedEntity == null ? "{where: 'false'}" : "{where: 'document=" + selectedEntity.getPrimaryKey() + "'}";
                })
                //.combine("{where: 'document=?documentDisplaySelection'}")
                .setExpressionColumns("[" +
                        "{expression: 'site.name', label: 'Site'}," +
                        "{expression: 'item.name', label: 'Item'}," +
                        "'dates'," +
                        "{expression: 'price_net', label: 'Fees', format: 'price'}" +
                        "]")
                .applyDomainModelRowStyle()
                .displayResultSetInto(pm.documentLineDisplayResultSetProperty())
                .start();

        // Setting up the payments filter
        createReactiveExpressionFilter("{class: 'MoneyTransfer', orderBy: 'date'}")
                // Condition
                .combine(pm.cartUuidProperty(), s -> "{where: 'document.cart.uuid=`" + s + "`'}")
                //.combine("{where: 'document.cart.uuid=?cartUuid'}")
                .setExpressionColumns("[" +
                        "{expression: 'date', format: 'dateTime'}," +
                        "{expression: 'document.ref', label: 'Booking ref'}," +
                        "{expression: 'method.name', label: 'Method'}," +
                        "{expression: 'amount', format: 'price'}," +
                        "{expression: 'pending ? `Pending` : successful ? `Success` : `Failed`', label: 'Status'}" +
                        "]")
                .applyDomainModelRowStyle()
                .displayResultSetInto(pm.paymentDisplayResultSetProperty())
                .start();
    }
}
