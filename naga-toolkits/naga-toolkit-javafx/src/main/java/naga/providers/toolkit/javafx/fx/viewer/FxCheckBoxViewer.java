package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.spi.viewer.base.CheckBoxViewerBase;
import naga.toolkit.fx.spi.viewer.base.CheckBoxViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxCheckBoxViewer
        extends FxButtonBaseViewer<javafx.scene.control.CheckBox, CheckBox, CheckBoxViewerBase, CheckBoxViewerMixin>
        implements CheckBoxViewerMixin, FxLayoutMeasurable {

    public FxCheckBoxViewer() {
        super(new CheckBoxViewerBase());
    }

    @Override
    protected  javafx.scene.control.CheckBox createFxNode() {
        javafx.scene.control.CheckBox checkBox = new javafx.scene.control.CheckBox();
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> getNode().setSelected(newValue));
        return checkBox;
    }

    @Override
    public void updateSelected(Boolean selected) {
        getFxNode().setSelected(selected);
    }
}
