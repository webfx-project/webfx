package naga.fx.spi.gwt.html;

import naga.fx.scene.Group;
import naga.fx.scene.control.*;
import naga.fx.scene.image.ImageView;
import naga.fx.scene.layout.Region;
import naga.fx.scene.shape.Circle;
import naga.fx.scene.shape.Line;
import naga.fx.scene.shape.Rectangle;
import naga.fx.scene.text.Text;
import naga.fx.spi.gwt.html.peer.*;
import naga.fx.spi.peer.NodePeer;
import naga.fx.spi.peer.base.NodePeerFactoryImpl;
import naga.fxdata.chart.*;
import naga.fxdata.control.DataGrid;
import naga.fxdata.control.HtmlText;

/**
 * @author Bruno Salmon
 */
class HtmlNodePeerFactory extends NodePeerFactoryImpl {

    final static HtmlNodePeerFactory SINGLETON = new HtmlNodePeerFactory();

    private HtmlNodePeerFactory() {
        registerNodePeerFactory(Rectangle.class, HtmlRectanglePeer::new);
        registerNodePeerFactory(Circle.class, HtmlCirclePeer::new);
        registerNodePeerFactory(Line.class, HtmlLinePeer::new);
        registerNodePeerFactory(Text.class, HtmlTextPeer::new);
        registerNodePeerFactory(Label.class, HtmlLabelPeer::new);
        registerNodePeerFactory(Group.class, HtmlGroupPeer::new);
        registerNodePeerFactory(Button.class, HtmlButtonPeer::new);
        registerNodePeerFactory(CheckBox.class, HtmlCheckBoxPeer::new);
        registerNodePeerFactory(RadioButton.class, HtmlRadioButtonPeer::new);
        registerNodePeerFactory(Slider.class, HtmlSliderPeer::new);
        registerNodePeerFactory(TextField.class, HtmlTextFieldPeer::new);
        registerNodePeerFactory(HtmlText.class, HtmlHtmlTextPeer::new);
        registerNodePeerFactory(ImageView.class, HtmlImageViewPeer::new);
        registerNodePeerFactory(DataGrid.class, HtmlDataGridPeer::new);
        registerNodePeerFactory(LineChart.class, HtmlLineChartPeer::new);
        registerNodePeerFactory(AreaChart.class, HtmlAreaChartPeer::new);
        registerNodePeerFactory(BarChart.class, HtmlBarChartPeer::new);
        registerNodePeerFactory(PieChart.class, HtmlPieChartPeer::new);
        registerNodePeerFactory(ScatterChart.class, HtmlScatterChartPeer::new);
    }

    @Override
    protected NodePeer<Region> createDefaultRegionPeer(Region node) {
        return new HtmlLayoutPeer<>();
    }
}
