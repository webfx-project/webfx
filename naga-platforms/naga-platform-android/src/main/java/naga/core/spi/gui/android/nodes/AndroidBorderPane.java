package naga.core.spi.gui.android.nodes;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.spi.gui.GuiNode;
import naga.core.spi.gui.android.AndroidToolkit;
import naga.core.spi.gui.nodes.BorderPane;
import naga.core.spi.gui.android.AndroidNode;

/**
 * @author Bruno Salmon
 */
public class AndroidBorderPane extends AndroidNode<RelativeLayout> implements BorderPane<RelativeLayout, View> {

    public AndroidBorderPane() {
        this(AndroidToolkit.currentActivity);
    }

    public AndroidBorderPane(Context context) {
        this(new RelativeLayout(context));
    }

    public AndroidBorderPane(RelativeLayout node) {
        super(node);
        topProperty.addListener((observable, oldValue, newValue) -> {
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
            GuiNode<View> top = getTop();
            if (top != null)
                params.addRule(RelativeLayout.BELOW, top.unwrapToNativeNode().getId());
            else
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            GuiNode<View> bottom = getBottom();
            if (bottom != null)
                params.addRule(RelativeLayout.ABOVE, bottom.unwrapToNativeNode().getId());
            else
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            node.addView(newValue.unwrapToNativeNode(), params);
        }));
        bottomProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null)
                node.removeView(oldValue.unwrapToNativeNode());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            node.addView(newValue.unwrapToNativeNode(), params);
        });
    }


    private final Property<GuiNode<View>> topProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<View>> topProperty() {
        return topProperty;
    }

    private final Property<GuiNode<View>> centerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<View>> centerProperty() {
        return centerProperty;
    }

    private final Property<GuiNode<View>> bottomProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<View>> bottomProperty() {
        return bottomProperty;
    }
}
