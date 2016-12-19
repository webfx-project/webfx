package naga.providers.toolkit.html.fx.html;

import naga.providers.toolkit.html.fx.html.viewer.*;
import naga.toolkit.fx.ext.impl.DataGridImpl;
import naga.toolkit.fx.ext.impl.HtmlTextImpl;
import naga.toolkit.fx.scene.control.impl.*;
import naga.toolkit.fx.scene.image.impl.ImageViewImpl;
import naga.toolkit.fx.scene.impl.GroupImpl;
import naga.toolkit.fx.scene.layout.impl.*;
import naga.toolkit.fx.scene.shape.impl.CircleImpl;
import naga.toolkit.fx.scene.shape.impl.RectangleImpl;
import naga.toolkit.fx.scene.text.impl.TextImpl;
import naga.toolkit.fx.spi.impl.NodeViewerFactoryImpl;

/**
 * @author Bruno Salmon
 */
class HtmlNodeViewerFactory extends NodeViewerFactoryImpl {

    final static HtmlNodeViewerFactory SINGLETON = new HtmlNodeViewerFactory();

    private HtmlNodeViewerFactory() {
        registerNodeViewerFactory(RectangleImpl.class, HtmlRectangleViewer::new);
        registerNodeViewerFactory(CircleImpl.class, HtmlCircleViewer::new);
        registerNodeViewerFactory(TextImpl.class, HtmlTextViewer::new);
        registerNodeViewerFactory(GroupImpl.class, HtmlGroupViewer::new);
        registerNodeViewerFactory(RegionImpl.class, HtmlLayoutViewer::new);
        registerNodeViewerFactory(VBoxImpl.class, HtmlLayoutViewer::new);
        registerNodeViewerFactory(HBoxImpl.class, HtmlLayoutViewer::new);
        registerNodeViewerFactory(BorderPaneImpl.class, HtmlLayoutViewer::new);
        registerNodeViewerFactory(FlowPaneImpl.class, HtmlLayoutViewer::new);
        registerNodeViewerFactory(ButtonImpl.class, HtmlButtonViewer::new);
        registerNodeViewerFactory(CheckBoxImpl.class, HtmlCheckBoxViewer::new);
        registerNodeViewerFactory(RadioButtonImpl.class, HtmlRadioButtonViewer::new);
        registerNodeViewerFactory(SliderImpl.class, HtmlSliderViewer::new);
        registerNodeViewerFactory(TextFieldImpl.class, HtmlTextFieldViewer::new);
        registerNodeViewerFactory(HtmlTextImpl.class, HtmlHtmlTextViewer::new);
        registerNodeViewerFactory(ImageViewImpl.class, HtmlImageViewViewer::new);
        registerNodeViewerFactory(DataGridImpl.class, HtmlDataGridViewer::new);
    }
}
