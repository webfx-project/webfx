package mongoose.activities.shared.book.event.shared;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.ui.action.ButtonFactoryMixin;
import naga.framework.ui.controls.DialogCallback;
import naga.framework.ui.controls.DialogUtil;
import naga.framework.ui.controls.LayoutUtil;
import naga.framework.ui.filter.ReactiveExpressionFilter;
import naga.framework.ui.i18n.I18n;
import naga.fxdata.cell.collator.GridCollator;

import static naga.framework.ui.controls.LayoutUtil.createHGrowable;

/**
 * @author Bruno Salmon
 */
public class TermsDialog implements ButtonFactoryMixin {

    private final Object eventId;
    private final DataSourceModel dataSourceModel;
    private final I18n i18n;
    private final Pane parent;
    private Runnable onClose;
    private DialogCallback termsDialogCallback;

    public TermsDialog(Object eventId, DataSourceModel dataSourceModel, I18n i18n, Pane parent) {
        this.eventId = eventId;
        this.dataSourceModel = dataSourceModel;
        this.i18n = i18n;
        this.parent = parent;
    }

    public TermsDialog setOnClose(Runnable onClose) {
        this.onClose = onClose;
        return this;
    }

    @Override
    public I18n getI18n() {
        return i18n;
    }

    public void show() {
        GridCollator termsLetterCollator = new GridCollator("first", "first");
        BorderPane entityDialogPane = new BorderPane(LayoutUtil.setMaxPrefSizeToInfinite(LayoutUtil.createVerticalScrollPaneWithPadding(termsLetterCollator)));
        createReactiveExpressionFilter("{class: 'Letter', where: 'type.terms', limit: '1'}")
                .combine("{where: 'event=" + eventId + "'}")
                .combine(i18n.languageProperty(), lang -> "{columns: '[`html(" + lang + ")`]'}")
                .displayResultSetInto(termsLetterCollator.displayResultSetProperty())
                .start();
        HBox hBox = new HBox(20, createHGrowable(), newOkButton(this::closeTermsDialog), createHGrowable());
        hBox.setPadding(new Insets(20, 0, 0, 0));
        entityDialogPane.setBottom(hBox);
        termsDialogCallback = DialogUtil.showModalNodeInGoldLayout(entityDialogPane, parent, 0.9, 0.8);
    }

    private void closeTermsDialog() {
        termsDialogCallback.closeDialog();
        if (onClose != null)
            onClose.run();
    }

    private ReactiveExpressionFilter createReactiveExpressionFilter(Object jsonOrClass) {
        return initializeReactiveExpressionFilter(new ReactiveExpressionFilter(jsonOrClass));
    }

    private ReactiveExpressionFilter initializeReactiveExpressionFilter(ReactiveExpressionFilter reactiveExpressionFilter) {
        return reactiveExpressionFilter
                .setDataSourceModel(dataSourceModel)
                .setI18n(i18n)
                //.bindActivePropertyTo(activeProperty())
                ;
    }

}
