package naga.providers.toolkit.gwtpolymer.nodes.layouts;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import naga.providers.toolkit.gwt.nodes.GwtParent;
import naga.toolkit.spi.nodes.layouts.FlowPane;

/**
 * @author Bruno Salmon
 */
public class GwtPolymerFlowPane extends GwtParent<Panel> implements FlowPane<Panel, Widget> {

    public GwtPolymerFlowPane() {
        this(new FlowPanel());
    }

    public GwtPolymerFlowPane(Panel node) {
        super(node);
        node.getElement().addClassName("wrap");
        //node.getElement().setAttribute("style", "height: 100%;");
    }

}
