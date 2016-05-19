package naga.core.spi.toolkit.gwt.nodes;

import com.google.gwt.user.client.ui.*;
import naga.core.spi.toolkit.nodes.VBox;

/**
 * @author Bruno Salmon
 */
public class GwtVBox extends GwtParent<ScrollPanel> implements VBox<ScrollPanel, Widget> {

    public GwtVBox() {
        this(createScrollPanel());
    }

    public GwtVBox(ScrollPanel node) {
        super(node);
    }

    @Override
    protected HasWidgets getGwtChildContainer() {
        return (node.getWidget() instanceof HasWidgets) ? (HasWidgets) node.getWidget() : super.getGwtChildContainer();
    }

    @Override
    protected Widget prepareChildWidget(Widget child) {
        child.setWidth("100%");
        return child;
    }

    private static ScrollPanel createScrollPanel() {
        VerticalPanel vp = new VerticalPanel();
        vp.setWidth("100%");
        vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_JUSTIFY);
        return new ScrollPanel(vp);
    }
}
