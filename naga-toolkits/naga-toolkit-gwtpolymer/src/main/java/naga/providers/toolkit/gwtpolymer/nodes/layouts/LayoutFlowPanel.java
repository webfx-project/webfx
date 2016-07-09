package naga.providers.toolkit.gwtpolymer.nodes.layouts;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

/**
 * A FlowLayout that implements RequiresResize and ProvidesResize so that children are automatically resized
 */
class LayoutFlowPanel extends FlowPanel implements RequiresResize, ProvidesResize {

    @Override
    public void onResize() {
        for (Widget child : getChildren()) {
            if (child instanceof RequiresResize) {
                ((RequiresResize) child).onResize();
            }
        }
    }
}
