package naga.toolkit.providers.gwt.nodes.layouts;

import com.google.gwt.user.client.ui.*;
import naga.toolkit.providers.gwt.nodes.GwtParent;
import naga.toolkit.spi.nodes.layouts.VBox;

/**
 * @author Bruno Salmon
 */
public class GwtVBox extends GwtParent<Panel> implements VBox<Panel, Widget> {

    public GwtVBox() {
        this(createPanel());
    }

    public GwtVBox(Panel node) {
        super(node);
    }

    @Override
    protected HasWidgets getGwtChildContainer() {
        Widget containerWidget = null;
        if (node instanceof SimplePanel)
            containerWidget = ((SimplePanel) node).getWidget();
        return (containerWidget instanceof HasWidgets) ? (HasWidgets) containerWidget : super.getGwtChildContainer();
    }

    @Override
    protected Widget prepareChildWidget(Widget child) {
        child.setWidth("100%");
        return child;
    }

    private static Panel createPanel() {
        VerticalPanel vp = new VerticalPanel();
        vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_JUSTIFY);
        vp.setWidth("100%");
        vp.addAttachHandler(event -> vp.setWidth("100%")); // Otherwise it can be removed by GWT when going back to the page
        return vp;
    }
}
