package naga.providers.toolkit.javafx.drawing.view;

import naga.toolkit.drawing.shapes.Group;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.GroupView;
import naga.toolkit.util.ObservableLists;

/**
 * @author Bruno Salmon
 */
public class FxGroupView extends FxDrawableViewImpl<Group, javafx.scene.Group> implements GroupView {

    @Override
    public void bind(Group g, DrawingRequester drawingRequester) {
        setAndBindDrawableProperties(g, new javafx.scene.Group());
        ObservableLists.runNowAndOnListChange(() -> drawingRequester.requestDrawableParentAndChildrenViewsUpdate(g), g.getDrawableChildren());
    }
}
