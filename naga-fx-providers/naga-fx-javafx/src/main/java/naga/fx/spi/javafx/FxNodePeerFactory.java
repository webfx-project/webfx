package naga.fx.spi.javafx;

import naga.fx.scene.Group;
import naga.fx.scene.control.*;
import naga.fx.scene.image.ImageView;
import naga.fx.scene.layout.Region;
import naga.fx.scene.shape.Circle;
import naga.fx.scene.shape.Line;
import naga.fx.scene.shape.Rectangle;
import naga.fx.scene.text.Text;
import naga.fx.spi.javafx.peer.*;
import naga.fx.spi.peer.NodePeer;
import naga.fx.spi.peer.base.NodePeerFactoryImpl;
import naga.fxdata.chart.*;
import naga.fxdata.control.DataGrid;
import naga.fxdata.control.HtmlText;

/**
 * @author Bruno Salmon
 */
public class FxNodePeerFactory extends NodePeerFactoryImpl {

    public final static FxNodePeerFactory SINGLETON = new FxNodePeerFactory();

    protected FxNodePeerFactory() {
        registerNodePeerFactory(Rectangle.class, FxRectanglePeer::new);
        registerNodePeerFactory(Circle.class, FxCirclePeer::new);
        registerNodePeerFactory(Line.class, FxLinePeer::new);
        registerNodePeerFactory(Text.class, FxTextPeer::new);
        registerNodePeerFactory(Label.class, FxLabelPeer::new);
        registerNodePeerFactory(Group.class, FxGroupPeer::new);
        registerNodePeerFactory(Button.class, FxButtonPeer::new);
        registerNodePeerFactory(CheckBox.class, FxCheckBoxPeer::new);
        registerNodePeerFactory(RadioButton.class, FxRadioButtonPeer::new);
        registerNodePeerFactory(TextField.class, FxTextFieldPeer::new);
        registerNodePeerFactory(ImageView.class, FxImageViewPeer::new);
        registerNodePeerFactory(HtmlText.class, FxHtmlTextPeer::new);
        registerNodePeerFactory(Slider.class, FxSliderPeer::new);
        registerNodePeerFactory(DataGrid.class, FxDataGridPeer::new);
        registerNodePeerFactory(AreaChart.class, FxAreaChartPeer::new);
        registerNodePeerFactory(BarChart.class, FxBarChartPeer::new);
        registerNodePeerFactory(LineChart.class, FxLineChartPeer::new);
        registerNodePeerFactory(PieChart.class, FxPieChartPeer::new);
        registerNodePeerFactory(ScatterChart.class, FxScatterChartPeer::new);
    }

    @Override
    protected NodePeer<Region> createDefaultRegionPeer(Region node) {
        return new FxLayoutPeer<>();
    }
}
