package mongoose.backend.operations.entities.moneytransfer;

import javafx.scene.layout.Pane;
import mongoose.shared.entities.MoneyTransfer;
import webfx.framework.client.ui.controls.entity.sheet.EntityPropertiesSheet;
import webfx.platform.shared.util.async.Future;

final class EditPaymentExecutor {

    static Future<Void> executeRequest(EditPaymentRequest rq) {
        return execute(rq.getPayment(), rq.getParentContainer());
    }

    private static Future<Void> execute(MoneyTransfer payment, Pane parentContainer) {
        //payment.getStore().setParameterValue("thisMoneyTransfer", payment);
        EntityPropertiesSheet.editEntity(payment, "[" +
                "'date'," +
                "'method'," +
                "'parentMethod'," +
                "'amount'," +
                "'readOnly(currency)'," +
//                "{expression: 'currency', foreignOrderBy: `code in ('GBP', 'EUR', 'USD') ? 0 : 1,name`, readOnly: true}," +
                "'readOnly(fromMoneyAccount)'," +
                "'readOnly(parentFromMoneyAccount)'," +
                "{expression: 'toMoneyAccount', foreignAlias: 'ma', foreignWhere: 'exists(select MoneyTransfer mt where id=" + payment.getPrimaryKey() + " and ma.type.internal=mt.(!payment or refund=document.expenditure) and ma.type.customer=mt.(payment and !document.expenditure and refund) and ma.type.supplier=mt.(payment and document.expenditure and !refund) and exists(select MethodSupport ms where ms.moneyAccountType=ma.type and ms.method=mt.method) and exists(select MoneyFlow mf where mf.fromMoneyAccount=mt.fromMoneyAccount and mf.toMoneyAccount=ma and (mf.method=null or mf.method=mt.method)))'}," +
                "'parentToMoneyAccount'," +
                "'readOnly(status)'," +
                "'transactionRef'," +
                "'comment'," +
                "'read'," +
                "'pending'," +
                "'successful'," +
                "'readOnly(verifier)'" +
                "]", parentContainer);
        return Future.succeededFuture();
    }
}
