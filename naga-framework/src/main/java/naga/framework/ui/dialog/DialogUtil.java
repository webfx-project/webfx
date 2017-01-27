package naga.framework.ui.dialog;

import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import naga.fx.properties.Properties;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

/**
 * @author Bruno Salmon
 */
public class DialogUtil {

    public static GridPane createGoldLayout(Region child) {
        GridPane goldPane = new GridPane();
        goldPane.setAlignment(Pos.TOP_CENTER);
        RowConstraints rc = new RowConstraints();
        rc.prefHeightProperty().bind(Properties.combine(goldPane.heightProperty(), child.heightProperty(),
                (gpHeight, bpHeight) -> (gpHeight.doubleValue() - bpHeight.doubleValue()) / 2.61));
        Properties.runOnceOnPropertiesChange((p) -> goldPane.layout(), goldPane.heightProperty());
        goldPane.getRowConstraints().add(rc);
        child.setMaxWidth(USE_PREF_SIZE);
        child.setMaxHeight(USE_PREF_SIZE);
        goldPane.add(child, 0, 1);
        return goldPane;
    }

    public static void showModalNodeInGoldLayout(Region modalNode, Pane parent) {
        showModalNode(createGoldLayout(modalNode), parent);
    }

    public static void showModalNode(Region modalNode, Pane parent) {
        modalNode.setManaged(false);
        modalNode.setMaxWidth(Double.MAX_VALUE);
        modalNode.setMaxHeight(Double.MAX_VALUE);
        Properties.runNowAndOnPropertiesChange(p ->
                        modalNode.resizeRelocate(0, 0, parent.getWidth(), parent.getHeight()),
                parent.widthProperty(), parent.heightProperty());
        parent.getChildren().add(modalNode);
    }
}
