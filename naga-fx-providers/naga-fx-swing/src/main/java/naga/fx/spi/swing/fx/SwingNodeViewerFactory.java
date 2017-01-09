package naga.fx.spi.swing.fx;

import naga.fx.spi.swing.fx.viewer.*;
import naga.fx.scene.Group;
import naga.fx.scene.control.*;
import naga.fx.scene.image.ImageView;
import naga.fx.scene.layout.*;
import naga.fx.scene.shape.Circle;
import naga.fx.scene.shape.Line;
import naga.fx.scene.shape.Rectangle;
import naga.fx.scene.text.Text;
import naga.fx.spi.viewer.NodeViewer;
import naga.fx.spi.viewer.base.NodeViewerFactoryImpl;
import naga.fxdata.chart.LineChart;
import naga.fxdata.control.DataGrid;
import naga.fxdata.control.HtmlText;

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
        registerNodeViewerFactory(Label.class, SwingLabelViewer::new);
        registerNodeViewerFactory(Group.class, SwingGroupViewer::new);
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

    @Override
    protected NodeViewer<Region> createDefaultRegionViewer(Region node) {
        return new SwingLayoutViewer<>();
    }
}
