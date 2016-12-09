package naga.providers.toolkit.javafx.fx;

import naga.providers.toolkit.javafx.fx.view.*;
import naga.toolkit.fx.ext.impl.DataGridImpl;
import naga.toolkit.fx.scene.control.impl.ButtonImpl;
import naga.toolkit.fx.scene.control.impl.CheckBoxImpl;
import naga.toolkit.fx.scene.control.impl.TextFieldImpl;
import naga.toolkit.fx.scene.image.impl.ImageViewImpl;
import naga.toolkit.fx.scene.impl.EmbedGuiNodeImpl;
import naga.toolkit.fx.scene.impl.GroupImpl;
import naga.toolkit.fx.scene.layout.impl.BorderPaneImpl;
import naga.toolkit.fx.scene.layout.impl.FlowPaneImpl;
import naga.toolkit.fx.scene.layout.impl.HBoxImpl;
import naga.toolkit.fx.scene.layout.impl.VBoxImpl;
import naga.toolkit.fx.scene.shape.impl.CircleImpl;
import naga.toolkit.fx.scene.shape.impl.RectangleImpl;
import naga.toolkit.fx.scene.text.impl.TextImpl;
import naga.toolkit.fx.spi.impl.NodeViewFactoryImpl;

/**
 * @author Bruno Salmon
 */
class FxNodeViewFactory extends NodeViewFactoryImpl {

    final static FxNodeViewFactory SINGLETON = new FxNodeViewFactory();

    private FxNodeViewFactory() {
        registerNodeViewFactory(RectangleImpl.class, FxRectangleView::new);
        registerNodeViewFactory(CircleImpl.class, FxCircleView::new);
        registerNodeViewFactory(TextImpl.class, FxTextView::new);
        registerNodeViewFactory(EmbedGuiNodeImpl.class, FxEmbedGuiNodeView::new);
        registerNodeViewFactory(GroupImpl.class, FxGroupView::new);
        registerNodeViewFactory(VBoxImpl.class, FxLayoutView::new);
        registerNodeViewFactory(HBoxImpl.class, FxLayoutView::new);
        registerNodeViewFactory(BorderPaneImpl.class, FxLayoutView::new);
        registerNodeViewFactory(FlowPaneImpl.class, FxLayoutView::new);
        registerNodeViewFactory(ButtonImpl.class, FxButtonView::new);
        registerNodeViewFactory(CheckBoxImpl.class, FxCheckBoxView::new);
        registerNodeViewFactory(TextFieldImpl.class, FxTextFieldView::new);
        registerNodeViewFactory(ImageViewImpl.class, FxImageViewView::new);
        registerNodeViewFactory(DataGridImpl.class, FxDataGridView::new);
    }
}
