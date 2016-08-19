package naga.providers.toolkit.javafx.nodes.layouts;


import javafx.scene.Node;
import naga.providers.toolkit.javafx.nodes.FxParent;
import naga.toolkit.spi.nodes.layouts.FlowPane;

/**
 * @author Bruno Salmon
 */
public class FxFlowPane extends FxParent<javafx.scene.layout.FlowPane> implements FlowPane<javafx.scene.layout.FlowPane, Node> {

    public FxFlowPane() {
        this(createFlowPane());
    }

    public FxFlowPane(javafx.scene.layout.FlowPane flowPane) {
        super(flowPane);
    }

    private static javafx.scene.layout.FlowPane createFlowPane() {
        javafx.scene.layout.FlowPane flowPane = new javafx.scene.layout.FlowPane();
        flowPane.setPrefWrapLength(5);
        return flowPane;
    }

}
