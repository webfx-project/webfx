package naga.fx.spi.gwt.html;

import naga.fx.scene.Group;
import naga.fx.scene.control.*;
import naga.fx.scene.image.ImageView;
import naga.fx.scene.layout.Region;
import naga.fx.scene.shape.Circle;
import naga.fx.scene.shape.Line;
import naga.fx.scene.shape.Rectangle;
import naga.fx.scene.text.Text;
import naga.fx.spi.gwt.html.viewer.*;
import naga.fx.spi.viewer.NodeViewer;
import naga.fx.spi.viewer.base.NodeViewerFactoryImpl;
import naga.fxdata.chart.*;
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
        registerNodeViewerFactory(Button.class, HtmlButtonViewer::new);
        registerNodeViewerFactory(CheckBox.class, HtmlCheckBoxViewer::new);
        registerNodeViewerFactory(RadioButton.class, HtmlRadioButtonViewer::new);
        registerNodeViewerFactory(Slider.class, HtmlSliderViewer::new);
        registerNodeViewerFactory(TextField.class, HtmlTextFieldViewer::new);
        registerNodeViewerFactory(HtmlText.class, HtmlHtmlTextViewer::new);
        registerNodeViewerFactory(ImageView.class, HtmlImageViewViewer::new);
        registerNodeViewerFactory(DataGrid.class, HtmlDataGridViewer::new);
        registerNodeViewerFactory(LineChart.class, HtmlLineChartViewer::new);
        registerNodeViewerFactory(AreaChart.class, HtmlAreaChartViewer::new);
        registerNodeViewerFactory(BarChart.class, HtmlBarChartViewer::new);
        registerNodeViewerFactory(PieChart.class, HtmlPieChartViewer::new);
        registerNodeViewerFactory(ScatterChart.class, HtmlScatterChartViewer::new);
    }

    @Override
    protected NodeViewer<Region> createDefaultRegionViewer(Region node) {
        return new HtmlLayoutViewer<>();
    }
}
