package naga.providers.toolkit.android.nodes.layouts;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.android.nodes.AndroidNode;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.VPage;
import naga.providers.toolkit.android.AndroidToolkit;

/**
 * @author Bruno Salmon
 */
public class AndroidVPage extends AndroidNode<RelativeLayout> implements VPage {

    public AndroidVPage() {
        this(AndroidToolkit.currentActivity);
    }

    public AndroidVPage(Context context) {
        this(new RelativeLayout(context));
    }

    public AndroidVPage(RelativeLayout node) {
        super(node);
        headerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.removeView(oldValue.unwrapToNativeNode());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            node.addView(newValue.unwrapToNativeNode(), params);
        });
        centerProperty.addListener((observable, oldValue, newValue) -> AndroidToolkit.get().scheduler().scheduleDeferred(() -> {
            if (oldValue != null)
                node.removeView(oldValue.unwrapToNativeNode());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            GuiNode top = getHeader();
            if (top != null)
                params.addRule(RelativeLayout.BELOW, ((View) top.unwrapToNativeNode()).getId());
            else
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            GuiNode bottom = getFooter();
            if (bottom != null)
                params.addRule(RelativeLayout.ABOVE, ((View) bottom.unwrapToNativeNode()).getId());
            else
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            node.addView(newValue.unwrapToNativeNode(), params);
        }));
        footerProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.removeView(oldValue.unwrapToNativeNode());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            node.addView(newValue.unwrapToNativeNode(), params);
        });
    }


    private final Property<GuiNode> headerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode> headerProperty() {
        return headerProperty;
    }

    private final Property<GuiNode> centerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode> centerProperty() {
        return centerProperty;
    }

    private final Property<GuiNode> footerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode> footerProperty() {
        return footerProperty;
    }
}
