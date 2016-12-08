package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.spi.view.base.CheckBoxViewBase;
import naga.toolkit.fx.spi.view.base.CheckBoxViewMixin;

/**
 * @author Bruno Salmon
 */
public class FxCheckBoxView
        extends FxButtonBaseView<javafx.scene.control.CheckBox, CheckBox, CheckBoxViewBase, CheckBoxViewMixin>
        implements CheckBoxViewMixin, FxLayoutMeasurable {

    public FxCheckBoxView() {
        super(new CheckBoxViewBase());
    }

    @Override
    javafx.scene.control.CheckBox createFxNode() {
        javafx.scene.control.CheckBox checkBox = new javafx.scene.control.CheckBox();
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> getNode().setSelected(newValue));
        return checkBox;
    }

    @Override
    public void updateSelected(Boolean selected) {
        getFxNode().setSelected(selected);
    }
}
