package naga.providers.toolkit.html;

import naga.commons.scheduler.UiScheduler;
import naga.platform.spi.Platform;
import naga.providers.toolkit.html.fx.html.HtmlDrawingNode;
import naga.providers.toolkit.html.nodes.layouts.HtmlWindow;
import naga.toolkit.fx.spi.DrawingNode;
import naga.toolkit.spi.Toolkit;

/**
 * @author Bruno Salmon
 */
public class HtmlToolkit extends Toolkit {

    public HtmlToolkit() {
        super(/* TODO: remove this dependency to Platform */(UiScheduler) Platform.get().scheduler(), HtmlWindow::new);
        registerNodeFactory(DrawingNode.class, HtmlDrawingNode::new);
    }
}