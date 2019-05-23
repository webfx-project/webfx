package webfx.fxkit.gwt.mapper.svg;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import webfx.fxkit.gwt.mapper.shared.GwtPrimaryStagePeer;
import webfx.fxkit.gwt.mapper.shared.GwtSecondaryStagePeer;
import webfx.fxkit.gwt.mapper.html.HtmlScenePeer;
import webfx.fxkit.gwt.mapper.html.peer.javafxcontrols.HtmlButtonPeer;
import webfx.fxkit.gwt.mapper.html.peer.javafxcontrols.HtmlCheckBoxPeer;
import webfx.fxkit.gwt.mapper.html.peer.javafxcontrols.HtmlTextFieldPeer;
import webfx.fxkit.gwt.mapper.svg.peer.javafxgraphics.*;
import webfx.fxkit.launcher.FxKitLauncher;
import webfx.fxkit.javafxgraphics.mapper.spi.NodePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.ScenePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.StagePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.WindowPeer;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.FxKitMapperProviderBase;

/**
 * @author Bruno Salmon
 */
public final class GwtFxKitSvgMapperProvider extends FxKitMapperProviderBase {

    public GwtFxKitSvgMapperProvider() {
        registerNodePeerFactory(Rectangle.class, SvgRectanglePeer::new);
        registerNodePeerFactory(Circle.class, SvgCirclePeer::new);
        registerNodePeerFactory(Text.class, SvgTextPeer::new);
        registerNodePeerFactory(Group.class, SvgGroupPeer::new);
        registerNodePeerFactory(Button.class, HtmlButtonPeer::new); // Will be embed in a foreignObject
        registerNodePeerFactory(CheckBox.class, HtmlCheckBoxPeer::new); // Will be embed in a foreignObject
        registerNodePeerFactory(TextField.class, HtmlTextFieldPeer::new); // Will be embed in a foreignObject
    }

    @Override
    protected NodePeer<Region> createDefaultRegionPeer(Region node) {
        return new SvgLayoutPeer<>();
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
}
