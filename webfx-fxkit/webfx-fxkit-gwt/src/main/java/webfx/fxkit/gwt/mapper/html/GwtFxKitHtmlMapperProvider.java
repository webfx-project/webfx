package webfx.fxkit.gwt.mapper.html;

import emul.com.sun.javafx.scene.control.skin.LabeledText;
import emul.com.sun.javafx.scene.control.skin.ToolkitTextBox;
import emul.javafx.scene.Group;
import emul.javafx.scene.Scene;
import emul.javafx.scene.control.*;
import emul.javafx.scene.image.ImageView;
import emul.javafx.scene.layout.Region;
import emul.javafx.scene.shape.Circle;
import emul.javafx.scene.shape.Line;
import emul.javafx.scene.shape.Rectangle;
import emul.javafx.scene.text.Text;
import emul.javafx.stage.Stage;
import emul.javafx.stage.Window;
import webfx.fxkit.extra.chart.*;
import webfx.fxkit.gwt.mapper.shared.GwtPrimaryStagePeer;
import webfx.fxkit.gwt.mapper.shared.GwtSecondaryStagePeer;
import webfx.fxkit.gwt.mapper.html.peer.*;
import webfx.fxkit.launcher.FxKitLauncher;
import webfx.fxkit.mapper.spi.NodePeer;
import webfx.fxkit.mapper.spi.ScenePeer;
import webfx.fxkit.mapper.spi.StagePeer;
import webfx.fxkit.mapper.spi.WindowPeer;
import webfx.fxkit.mapper.spi.impl.FxKitMapperProviderBase;
import webfx.fxkit.extra.control.DataGrid;
import webfx.fxkit.extra.control.HtmlText;
import webfx.fxkit.extra.control.HtmlTextEditor;

/**
 * @author Bruno Salmon
 */
public final class GwtFxKitHtmlMapperProvider extends FxKitMapperProviderBase {

    public GwtFxKitHtmlMapperProvider() {
        registerNodePeerFactory(ScrollPane.class, HtmlScrollPanePeer::new);
        registerNodePeerFactory(Rectangle.class, HtmlRectanglePeer::new);
        registerNodePeerFactory(Circle.class, HtmlCirclePeer::new);
        registerNodePeerFactory(Line.class, HtmlLinePeer::new);
        registerNodePeerFactory(Text.class, HtmlTextPeer::new);
        registerNodePeerFactory(LabeledText.class, HtmlTextPeer::new);
        registerNodePeerFactory(Label.class, HtmlLabelPeer::new);
        registerNodePeerFactory(Hyperlink.class, HtmlHyperlinkPeer::new);
        registerNodePeerFactory(Group.class, HtmlGroupPeer::new);
        registerNodePeerFactory(Button.class, HtmlButtonPeer::new);
        registerNodePeerFactory(ToggleButton.class, HtmlToggleButtonPeer::new);
        registerNodePeerFactory(CheckBox.class, HtmlCheckBoxPeer::new);
        registerNodePeerFactory(RadioButton.class, HtmlRadioButtonPeer::new);
        registerNodePeerFactory(Slider.class, HtmlSliderPeer::new);
        registerNodePeerFactory(TextField.class, HtmlTextFieldPeer::new);
        registerNodePeerFactory(PasswordField.class, HtmlTextFieldPeer::new);
        registerNodePeerFactory(ToolkitTextBox.class, HtmlTextFieldPeer::createHtmlTextBoxPeer);
        registerNodePeerFactory(TextArea.class, HtmlTextAreaPeer::new);
        registerNodePeerFactory(HtmlText.class, HtmlHtmlTextPeer::new);
        registerNodePeerFactory(HtmlTextEditor.class, HtmlHtmlTextEditorPeer::new);
        registerNodePeerFactory(ChoiceBox.class, HtmlChoiceBoxPeer::new);
        registerNodePeerFactory(DatePicker.class, HtmlDatePickerPeer::new);
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

    @Override
    public StagePeer createStagePeer(Stage stage) {
        if (stage == FxKitLauncher.getPrimaryStage())
            return new GwtPrimaryStagePeer(stage);
        return new GwtSecondaryStagePeer(stage);
    }

    @Override
    public WindowPeer createWindowPeer(Window window) {
        return null;
    }

    @Override
    public ScenePeer createScenePeer(Scene scene) {
        return new HtmlScenePeer(scene);
    }

    @Override
    public double getVerticalScrollbarExtraWidth() {
        return 0; // Perfect scrollbar library is used and the transparent scrollbar overlays the view port (so no extra width)
    }

}
