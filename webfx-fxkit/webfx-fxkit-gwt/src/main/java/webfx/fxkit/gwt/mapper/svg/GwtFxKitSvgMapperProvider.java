package webfx.fxkit.gwt.mapper.svg;

import emul.javafx.scene.Group;
import emul.javafx.scene.Scene;
import emul.javafx.scene.control.Button;
import emul.javafx.scene.control.CheckBox;
import emul.javafx.scene.control.TextField;
import emul.javafx.scene.layout.Region;
import emul.javafx.scene.shape.Circle;
import emul.javafx.scene.shape.Rectangle;
import emul.javafx.scene.text.Text;
import emul.javafx.stage.Stage;
import emul.javafx.stage.Window;
import webfx.fxkit.gwt.mapper.shared.GwtPrimaryStagePeer;
import webfx.fxkit.gwt.mapper.shared.GwtSecondaryStagePeer;
import webfx.fxkit.gwt.mapper.html.HtmlScenePeer;
import webfx.fxkit.gwt.mapper.html.peer.HtmlButtonPeer;
import webfx.fxkit.gwt.mapper.html.peer.HtmlCheckBoxPeer;
import webfx.fxkit.gwt.mapper.html.peer.HtmlTextFieldPeer;
import webfx.fxkit.gwt.mapper.svg.peer.*;
import webfx.fxkits.core.launcher.FxKitLauncher;
import webfx.fxkits.core.mapper.spi.NodePeer;
import webfx.fxkits.core.mapper.spi.ScenePeer;
import webfx.fxkits.core.mapper.spi.StagePeer;
import webfx.fxkits.core.mapper.spi.WindowPeer;
import webfx.fxkits.core.mapper.spi.impl.FxKitMapperProviderBase;

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

    @Override
    public double getVerticalScrollbarExtraWidth() {
        return 0; // Perfect scrollbar library is used and the transparent scrollbar overlays the view port (so no extra width)
    }

}
