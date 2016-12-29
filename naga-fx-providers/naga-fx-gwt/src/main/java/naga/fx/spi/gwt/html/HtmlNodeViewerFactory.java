package naga.fx.spi.gwt.html;

import naga.fx.scene.Group;
import naga.fx.scene.control.*;
import naga.fx.scene.image.ImageView;
import naga.fx.scene.layout.*;
import naga.fx.scene.shape.Circle;
import naga.fx.scene.shape.Line;
import naga.fx.scene.shape.Rectangle;
import naga.fx.scene.text.Text;
import naga.fx.spi.gwt.html.viewer.*;
import naga.fx.spi.viewer.base.NodeViewerFactoryImpl;
import naga.fxdata.control.DataGrid;
import naga.fxdata.control.HtmlText;

/**
 * @author Bruno Salmon
 */
class HtmlNodeViewerFactory extends NodeViewerFactoryImpl {

    final static HtmlNodeViewerFactory SINGLETON = new HtmlNodeViewerFactory();

    private HtmlNodeViewerFactory() {
        registerNodeViewerFactory(Rectangle.class, HtmlRectangleViewer::new);
        registerNodeViewerFactory(Circle.class, HtmlCircleViewer::new);
        registerNodeViewerFactory(Line.class, HtmlLineViewer::new);
        registerNodeViewerFactory(Text.class, HtmlTextViewer::new);
        registerNodeViewerFactory(Label.class, HtmlLabelViewer::new);
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
