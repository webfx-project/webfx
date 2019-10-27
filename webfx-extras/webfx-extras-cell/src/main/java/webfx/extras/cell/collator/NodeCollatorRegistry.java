package webfx.extras.cell.collator;

import webfx.platform.shared.util.Arrays;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class NodeCollatorRegistry {

    private static final Map<String, NodeCollator> collators = new HashMap<>();

    private static final NodeCollator hBoxCollator = nodes -> {
        HBox hBox = new HBox(5);
        if (nodes != null)
            hBox.getChildren().addAll(nodes);
        hBox.setAlignment(Pos.CENTER_LEFT);
        return hBox;
    };
    private static final NodeCollator vBoxCollator = VBox::new;
    private static final NodeCollator flowPaneCollator = FlowPane::new;
    private static final NodeCollator firstCollator = nodes -> Arrays.getValue(nodes, 0);

    static {
        registerCollator("hBox", hBoxCollator);
        registerCollator("vBox", vBoxCollator);
        registerCollator("flowPane", flowPaneCollator);
        registerCollator("first", firstCollator);
    }

    private static void registerCollator(String name, NodeCollator collator) {
        collators.put(name.toLowerCase(), collator);
    }

    public static NodeCollator getCollator(String name) {
        return name == null ? null : collators.get(name.toLowerCase());
    }

    public static NodeCollator hBoxCollator() {
        return hBoxCollator;
    }

    public static NodeCollator vBoxCollator() {
        return vBoxCollator;
    }

    public static NodeCollator flowPaneCollator() {
        return flowPaneCollator;
    }

    public static NodeCollator firstCollator() {
        return firstCollator;
    }
}
