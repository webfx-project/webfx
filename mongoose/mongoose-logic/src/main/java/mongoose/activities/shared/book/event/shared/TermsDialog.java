package mongoose.activities.shared.book.event.shared;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.ui.action.ButtonFactoryMixin;
import naga.framework.ui.controls.BackgroundUtil;
import naga.framework.ui.controls.DialogCallback;
import naga.framework.ui.controls.DialogUtil;
import naga.framework.ui.filter.ReactiveExpressionFilterFactoryMixin;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.layouts.LayoutUtil;
import naga.fxdata.cell.collator.GridCollator;

import static naga.framework.ui.layouts.LayoutUtil.createHGrowable;

/**
 * @author Bruno Salmon
 */
public class TermsDialog implements ButtonFactoryMixin, ReactiveExpressionFilterFactoryMixin {

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

    @Override
    public DataSourceModel getDataSourceModel() {
        return dataSourceModel;
    }

    @Override
    public ObservableValue<Boolean> activeProperty() {
        return null;
    }

    public void show() {
        GridCollator termsLetterCollator = new GridCollator("first", "first");
        termsLetterCollator.setBackground(BackgroundUtil.WHITE_BACKGROUND);
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

}
