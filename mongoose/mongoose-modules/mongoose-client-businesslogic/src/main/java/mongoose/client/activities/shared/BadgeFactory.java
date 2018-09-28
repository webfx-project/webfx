package mongoose.client.activities.shared;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

/**
 * @author Bruno Salmon
 */
public final class BadgeFactory {

    public static Node createBadge(Node... badgeNodes) {
        return new HBox(badgeNodes);
    }

}
