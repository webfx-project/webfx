package naga.fx.spi.gwt.html;

import emul.javafx.scene.Group;
import emul.javafx.scene.control.*;
import emul.javafx.scene.image.ImageView;
import emul.javafx.scene.layout.Region;
import emul.javafx.scene.shape.Circle;
import emul.javafx.scene.shape.Line;
import emul.javafx.scene.shape.Rectangle;
import emul.javafx.scene.text.Text;
import naga.fx.spi.gwt.html.peer.*;
import naga.fx.spi.peer.NodePeer;
import naga.fx.spi.peer.base.NodePeerFactoryImpl;
import naga.fxdata.chart.*;
import naga.fxdata.control.DataGrid;
import naga.fxdata.control.HtmlText;
import naga.fxdata.control.HtmlTextEditor;

/**
 * @author Bruno Salmon
 */
class HtmlNodePeerFactory extends NodePeerFactoryImpl {

    final static HtmlNodePeerFactory SINGLETON = new HtmlNodePeerFactory();

    private HtmlNodePeerFactory() {
        registerNodePeerFactory(ScrollPane.class, HtmlScrollPanePeer::new);
        registerNodePeerFactory(Rectangle.class, HtmlRectanglePeer::new);
        registerNodePeerFactory(Circle.class, HtmlCirclePeer::new);
        registerNodePeerFactory(Line.class, HtmlLinePeer::new);
        registerNodePeerFactory(Text.class, HtmlTextPeer::new);
        registerNodePeerFactory(Label.class, HtmlLabelPeer::new);
        registerNodePeerFactory(Hyperlink.class, HtmlHyperlinkPeer::new);
        registerNodePeerFactory(Group.class, HtmlGroupPeer::new);
        registerNodePeerFactory(Button.class, HtmlButtonPeer::new);
        registerNodePeerFactory(ToggleButton.class, HtmlToggleButtonPeer::new);
        registerNodePeerFactory(CheckBox.class, HtmlCheckBoxPeer::new);
        registerNodePeerFactory(RadioButton.class, HtmlRadioButtonPeer::new);
        registerNodePeerFactory(Slider.class, HtmlSliderPeer::new);
        registerNodePeerFactory(TextField.class, HtmlTextFieldPeer::new);
        registerNodePeerFactory(TextArea.class, HtmlTextAreaPeer::new);
        registerNodePeerFactory(HtmlText.class, HtmlHtmlTextPeer::new);
        registerNodePeerFactory(HtmlTextEditor.class, HtmlHtmlTextEditorPeer::new);
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
