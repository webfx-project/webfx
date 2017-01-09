package naga.fx.spi.swing;

import naga.fx.spi.swing.peer.*;
import naga.fx.scene.Group;
import naga.fx.scene.control.*;
import naga.fx.scene.image.ImageView;
import naga.fx.scene.layout.*;
import naga.fx.scene.shape.Circle;
import naga.fx.scene.shape.Line;
import naga.fx.scene.shape.Rectangle;
import naga.fx.scene.text.Text;
import naga.fx.spi.peer.NodePeer;
import naga.fx.spi.peer.base.NodePeerFactoryImpl;
import naga.fxdata.chart.LineChart;
import naga.fxdata.control.DataGrid;
import naga.fxdata.control.HtmlText;

/**
 * @author Bruno Salmon
 */
public class SwingNodePeerFactory extends NodePeerFactoryImpl {

    public final static SwingNodePeerFactory SINGLETON = new SwingNodePeerFactory();

    private SwingNodePeerFactory() {
        registerNodePeerFactory(Rectangle.class, SwingRectanglePeer::new);
        registerNodePeerFactory(Circle.class, SwingCirclePeer::new);
        registerNodePeerFactory(Line.class, SwingLinePeer::new);
        registerNodePeerFactory(Text.class, SwingTextPeer::new);
        registerNodePeerFactory(Label.class, SwingLabelPeer::new);
        registerNodePeerFactory(Group.class, SwingGroupPeer::new);
        registerNodePeerFactory(Button.class, SwingButtonPeer::new);
        registerNodePeerFactory(TextField.class, SwingTextFieldPeer::new);
        registerNodePeerFactory(HtmlText.class, SwingHtmlTextPeer::new);
        registerNodePeerFactory(CheckBox.class, SwingCheckBoxPeer::new);
        registerNodePeerFactory(RadioButton.class, SwingRadioButtonPeer::new);
        registerNodePeerFactory(Slider.class, SwingSliderPeer::new);
        registerNodePeerFactory(ImageView.class, SwingImageViewPeer::new);
        registerNodePeerFactory(DataGrid.class, SwingDataGridPeer::new);
        registerNodePeerFactory(LineChart.class, SwingLineChartPeer::new);
    }

    @Override
    protected NodePeer<Region> createDefaultRegionPeer(Region node) {
        return new SwingLayoutPeer<>();
    }
}
