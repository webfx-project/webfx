package naga.providers.toolkit.html.fx.html;

import naga.providers.toolkit.html.fx.html.viewer.*;
import naga.toolkit.fxdata.control.DataGrid;
import naga.toolkit.fxdata.control.HtmlText;
import naga.toolkit.fx.scene.control.*;
import naga.toolkit.fx.scene.image.ImageView;
import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.scene.layout.*;
import naga.toolkit.fx.scene.shape.Circle;
import naga.toolkit.fx.scene.shape.Rectangle;
import naga.toolkit.fx.scene.text.Text;
import naga.toolkit.fx.spi.viewer.base.NodeViewerFactoryImpl;

/**
 * @author Bruno Salmon
 */
class HtmlNodeViewerFactory extends NodeViewerFactoryImpl {

    final static HtmlNodeViewerFactory SINGLETON = new HtmlNodeViewerFactory();

    private HtmlNodeViewerFactory() {
        registerNodeViewerFactory(Rectangle.class, HtmlRectangleViewer::new);
        registerNodeViewerFactory(Circle.class, HtmlCircleViewer::new);
        registerNodeViewerFactory(Text.class, HtmlTextViewer::new);
        registerNodeViewerFactory(Group.class, HtmlGroupViewer::new);
        registerNodeViewerFactory(Region.class, HtmlLayoutViewer::new);
        registerNodeViewerFactory(VBox.class, HtmlLayoutViewer::new);
        registerNodeViewerFactory(HBox.class, HtmlLayoutViewer::new);
        registerNodeViewerFactory(BorderPane.class, HtmlLayoutViewer::new);
        registerNodeViewerFactory(FlowPane.class, HtmlLayoutViewer::new);
        registerNodeViewerFactory(GridPane.class, HtmlLayoutViewer::new);
        registerNodeViewerFactory(Button.class, HtmlButtonViewer::new);
        registerNodeViewerFactory(CheckBox.class, HtmlCheckBoxViewer::new);
        registerNodeViewerFactory(RadioButton.class, HtmlRadioButtonViewer::new);
        registerNodeViewerFactory(Slider.class, HtmlSliderViewer::new);
        registerNodeViewerFactory(TextField.class, HtmlTextFieldViewer::new);
        registerNodeViewerFactory(HtmlText.class, HtmlHtmlTextViewer::new);
        registerNodeViewerFactory(ImageView.class, HtmlImageViewViewer::new);
        registerNodeViewerFactory(DataGrid.class, HtmlDataGridViewer::new);
    }
}
