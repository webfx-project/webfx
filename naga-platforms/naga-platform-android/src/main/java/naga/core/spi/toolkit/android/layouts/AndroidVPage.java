package naga.core.spi.toolkit.android.layouts;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.toolkit.android.node.AndroidNode;
import naga.core.spi.toolkit.node.GuiNode;
import naga.core.spi.toolkit.android.AndroidToolkit;
import naga.core.spi.toolkit.layouts.VPage;

/**
 * @author Bruno Salmon
 */
public class AndroidVPage extends AndroidNode<RelativeLayout> implements VPage<RelativeLayout, View> {

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
            GuiNode<View> top = getHeader();
            if (top != null)
                params.addRule(RelativeLayout.BELOW, top.unwrapToNativeNode().getId());
            else
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            GuiNode<View> bottom = getFooter();
            if (bottom != null)
                params.addRule(RelativeLayout.ABOVE, bottom.unwrapToNativeNode().getId());
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


    private final Property<GuiNode<View>> headerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<View>> headerProperty() {
        return headerProperty;
    }

    private final Property<GuiNode<View>> centerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<View>> centerProperty() {
        return centerProperty;
    }

    private final Property<GuiNode<View>> footerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<View>> footerProperty() {
        return footerProperty;
    }
}
