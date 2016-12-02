package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.spi.view.GroupView;

/**
 * @author Bruno Salmon
 */
public class FxGroupView extends FxParentView<Group, javafx.scene.Group> implements GroupView {

    @Override
    javafx.scene.Group createFxNode(Group node) {
        return new javafx.scene.Group();
    }
}
