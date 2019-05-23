package webfx.fxkit.gwt.mapper.html;

import com.sun.javafx.scene.control.skin.LabeledText;
import com.sun.javafx.scene.control.skin.ToolkitTextBox;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import webfx.fxkit.extra.controls.displaydata.chart.*;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;
import webfx.fxkit.extra.controls.html.HtmlText;
import webfx.fxkit.extra.controls.html.HtmlTextEditor;
import webfx.fxkit.extra.controls.svg.SvgText;
import webfx.fxkit.gwt.mapper.html.peer.extra.*;
import webfx.fxkit.gwt.mapper.html.peer.javafxcontrols.*;
import webfx.fxkit.gwt.mapper.html.peer.javafxgraphics.*;
import webfx.fxkit.gwt.mapper.shared.GwtPrimaryStagePeer;
import webfx.fxkit.gwt.mapper.shared.GwtSecondaryStagePeer;
import webfx.fxkit.launcher.FxKitLauncher;
import webfx.fxkit.javafxgraphics.mapper.spi.NodePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.ScenePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.StagePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.WindowPeer;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.FxKitMapperProviderBase;

/**
 * @author Bruno Salmon
 */
public final class GwtFxKitHtmlMapperProvider extends FxKitMapperProviderBase {

    public GwtFxKitHtmlMapperProvider() {
        // Graphics
        registerNodePeerFactory(Group.class, HtmlGroupPeer::new);
        registerNodePeerFactory(Rectangle.class, HtmlRectanglePeer::new);
        registerNodePeerFactory(Circle.class, HtmlCirclePeer::new);
        registerNodePeerFactory(Line.class, HtmlLinePeer::new);
        registerNodePeerFactory(Text.class, HtmlTextPeer::new);
        registerNodePeerFactory(ImageView.class, HtmlImageViewPeer::new);
        registerNodePeerFactory(Canvas.class, HtmlCanvasPeer::new);
        registerNodePeerFactory(Path.class, HtmlPathPeer::new);

        // Controls
        registerNodePeerFactory(LabeledText.class, HtmlTextPeer::new);
        registerNodePeerFactory(Label.class, HtmlLabelPeer::new);
        registerNodePeerFactory(Hyperlink.class, HtmlHyperlinkPeer::new);
        registerNodePeerFactory(Button.class, HtmlButtonPeer::new);
        registerNodePeerFactory(ToggleButton.class, HtmlToggleButtonPeer::new);
        registerNodePeerFactory(CheckBox.class, HtmlCheckBoxPeer::new);
        registerNodePeerFactory(RadioButton.class, HtmlRadioButtonPeer::new);
        registerNodePeerFactory(Slider.class, HtmlSliderPeer::new);
        registerNodePeerFactory(TextField.class, HtmlTextFieldPeer::new);
        registerNodePeerFactory(PasswordField.class, HtmlTextFieldPeer::new);
        registerNodePeerFactory(ToolkitTextBox.class, HtmlTextFieldPeer::createHtmlTextBoxPeer);
        registerNodePeerFactory(TextArea.class, HtmlTextAreaPeer::new);
        registerNodePeerFactory(ChoiceBox.class, HtmlChoiceBoxPeer::new);
        registerNodePeerFactory(DatePicker.class, HtmlDatePickerPeer::new);
        registerNodePeerFactory(ScrollPane.class, HtmlScrollPanePeer::new);

        // Extra
        registerNodePeerFactory(SvgText.class, HtmlSvgTextPeer::new);
        registerNodePeerFactory(HtmlText.class, HtmlHtmlTextPeer::new);
        registerNodePeerFactory(HtmlTextEditor.class, HtmlHtmlTextEditorPeer::new);
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
    public GraphicsContext getGraphicsContext2D(Canvas canvas) {
        return new HtmlGraphicsContext(canvas);
    }
}
