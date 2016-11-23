package naga.providers.toolkit.javafx.drawing.view;

import naga.toolkit.drawing.shapes.Group;
import naga.toolkit.drawing.spi.view.GroupView;

/**
 * @author Bruno Salmon
 */
public class FxGroupView extends FxParentView<Group, javafx.scene.Group> implements GroupView {

    @Override
    javafx.scene.Group createFxNode(Group node) {
        return new javafx.scene.Group();
    }
}
