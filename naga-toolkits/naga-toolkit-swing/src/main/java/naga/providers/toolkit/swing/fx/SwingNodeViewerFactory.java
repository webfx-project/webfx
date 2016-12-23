package naga.providers.toolkit.swing.fx;

import naga.providers.toolkit.swing.fx.viewer.*;
import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.scene.control.*;
import naga.toolkit.fx.scene.image.ImageView;
import naga.toolkit.fx.scene.layout.*;
import naga.toolkit.fx.scene.shape.Circle;
import naga.toolkit.fx.scene.shape.Line;
import naga.toolkit.fx.scene.shape.Rectangle;
import naga.toolkit.fx.scene.text.Text;
import naga.toolkit.fx.spi.viewer.base.NodeViewerFactoryImpl;
import naga.toolkit.fxdata.chart.LineChart;
import naga.toolkit.fxdata.control.DataGrid;
import naga.toolkit.fxdata.control.HtmlText;

/**
 * @author Bruno Salmon
 */
public class SwingNodeViewerFactory extends NodeViewerFactoryImpl {

    public final static SwingNodeViewerFactory SINGLETON = new SwingNodeViewerFactory();

    private SwingNodeViewerFactory() {
        registerNodeViewerFactory(Rectangle.class, SwingRectangleViewer::new);
        registerNodeViewerFactory(Circle.class, SwingCircleViewer::new);
        registerNodeViewerFactory(Line.class, SwingLineViewer::new);
        registerNodeViewerFactory(Text.class, SwingTextViewer::new);
        registerNodeViewerFactory(Group.class, SwingGroupViewer::new);
        registerNodeViewerFactory(Region.class, SwingLayoutViewer::new);
        registerNodeViewerFactory(VBox.class, SwingLayoutViewer::new);
        registerNodeViewerFactory(HBox.class, SwingLayoutViewer::new);
        registerNodeViewerFactory(BorderPane.class, SwingLayoutViewer::new);
        registerNodeViewerFactory(FlowPane.class, SwingLayoutViewer::new);
        registerNodeViewerFactory(GridPane.class, SwingLayoutViewer::new);
        registerNodeViewerFactory(Button.class, SwingButtonViewer::new);
        registerNodeViewerFactory(TextField.class, SwingTextFieldViewer::new);
        registerNodeViewerFactory(HtmlText.class, SwingHtmlTextViewer::new);
        registerNodeViewerFactory(CheckBox.class, SwingCheckBoxViewer::new);
        registerNodeViewerFactory(RadioButton.class, SwingRadioButtonViewer::new);
        registerNodeViewerFactory(Slider.class, SwingSliderViewer::new);
        registerNodeViewerFactory(ImageView.class, SwingImageViewViewer::new);
        registerNodeViewerFactory(DataGrid.class, SwingDataGridViewer::new);
        registerNodeViewerFactory(LineChart.class, SwingLineChartViewer::new);
    }
}
