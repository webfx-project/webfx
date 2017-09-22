package emul.javafx.scene.control;

import emul.javafx.geometry.HPos;
import emul.javafx.geometry.Insets;
import emul.javafx.geometry.VPos;
import emul.javafx.scene.Node;
import emul.javafx.scene.layout.Pane;

/**
 * @author Bruno Salmon
 */
public class ScrollPane extends Pane {

    private Runnable onChildrenLayout;

    public ScrollPane() {
    }

    public ScrollPane(Node content) {
        super(content);
    }

    public final void setHbarPolicy(ScrollBarPolicy value) {
        //hbarPolicyProperty().set(value);
    }

    public Skin getSkin() {
        return null;
    }

    @Override
    protected void layoutChildren() {
        layoutInArea(getChildren().get(0), 0, 0, Double.MAX_VALUE, Double.MAX_VALUE, 0, Insets.EMPTY, false, false, HPos.LEFT, VPos.TOP, true);
        if (onChildrenLayout != null)
            onChildrenLayout.run();
    }

    public void setOnChildrenLayout(Runnable onChildrenLayout) {
        this.onChildrenLayout = onChildrenLayout;
    }

    /***************************************************************************
     *                                                                         *
     * Support classes                                                         *
     *                                                                         *
     **************************************************************************/

    /**
     * An enumeration denoting the policy to be used by a scrollable
     * Control in deciding whether to show a scroll bar.
     * @since JavaFX 2.0
     */
    public enum ScrollBarPolicy {
        /**
         * Indicates that a scroll bar should never be shown.
         */
        NEVER,
        /**
         * Indicates that a scroll bar should always be shown.
         */
        ALWAYS,
        /**
         * Indicates that a scroll bar should be shown when required.
         */
        AS_NEEDED
    }

}
