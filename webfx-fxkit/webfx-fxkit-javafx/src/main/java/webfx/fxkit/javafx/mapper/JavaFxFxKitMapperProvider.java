package webfx.fxkit.javafx.mapper;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;
import webfx.fxkit.javafx.mapper.peer.*;
import webfx.fxkits.core.mapper.spi.NodePeer;
import webfx.fxkits.core.mapper.spi.ScenePeer;
import webfx.fxkits.core.mapper.spi.StagePeer;
import webfx.fxkits.core.mapper.spi.WindowPeer;
import webfx.fxkits.core.mapper.spi.impl.FxKitMapperProviderBase;
import webfx.fxkits.extra.cell.collator.GridCollator;
import webfx.fxkits.extra.chart.*;
import webfx.fxkits.extra.control.DataGrid;
import webfx.fxkits.extra.control.HtmlText;
import webfx.fxkits.extra.control.HtmlTextEditor;

/**
 * @author Bruno Salmon
 */
public class JavaFxFxKitMapperProvider extends FxKitMapperProviderBase {

    public JavaFxFxKitMapperProvider() {
        registerNodePeerFactory(HtmlText.class, FxHtmlTextPeer::new);
        registerNodePeerFactory(HtmlTextEditor.class, FxHtmlTextEditorPeer::new);
        registerNodePeerFactory(DataGrid.class, FxDataGridPeer::new);
        registerNodePeerFactory(AreaChart.class, FxAreaChartPeer::new);
        registerNodePeerFactory(BarChart.class, FxBarChartPeer::new);
        registerNodePeerFactory(LineChart.class, FxLineChartPeer::new);
        registerNodePeerFactory(PieChart.class, FxPieChartPeer::new);
        registerNodePeerFactory(ScatterChart.class, FxScatterChartPeer::new);
        registerNodePeerFactory(GridCollator.class, GridCollator.GridCollatorPeer::new);
    }

    @Override
    protected NodePeer<Region> createDefaultRegionPeer(Region node) {
        return null;
    }

    @Override
    public StagePeer createStagePeer(Stage stage) {
        return null;
    }

    @Override
    public WindowPeer createWindowPeer(Window window) {
        return null;
    }

    @Override
    public ScenePeer createScenePeer(Scene scene) {
        return null;
    }
}
