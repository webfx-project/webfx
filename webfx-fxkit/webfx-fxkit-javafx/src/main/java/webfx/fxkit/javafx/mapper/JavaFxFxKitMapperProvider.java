package webfx.fxkit.javafx.mapper;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;
import webfx.fxkit.extra.chart.*;
import webfx.fxkit.javafx.mapper.peer.*;
import webfx.fxkit.mapper.spi.NodePeer;
import webfx.fxkit.mapper.spi.ScenePeer;
import webfx.fxkit.mapper.spi.StagePeer;
import webfx.fxkit.mapper.spi.WindowPeer;
import webfx.fxkit.mapper.spi.impl.FxKitMapperProviderBase;
import webfx.fxkit.extra.cell.collator.GridCollator;
import webfx.fxkit.extra.control.DataGrid;
import webfx.fxkit.extra.control.HtmlText;
import webfx.fxkit.extra.control.HtmlTextEditor;

/**
 * @author Bruno Salmon
 */
public final class JavaFxFxKitMapperProvider extends FxKitMapperProviderBase {

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
