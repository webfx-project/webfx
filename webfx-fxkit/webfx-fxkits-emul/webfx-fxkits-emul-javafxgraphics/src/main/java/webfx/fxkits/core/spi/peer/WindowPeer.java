package webfx.fxkits.core.spi.peer;

import emul.javafx.stage.Window;
import emul.com.sun.javafx.tk.TKStage;

/**
 * @author Bruno Salmon
 */
public interface WindowPeer extends TKStage {

    Window getWindow();

    //void setScene(ScenePeer scenePeer);

    void onSceneRootChanged();

    void setVisible(boolean visible);

    /**
     * Sets the window bounds to the specified values.
     *
     * Gravity values specify how to correct window location if only its size
     * changes (for example when stage decorations are added). User initiated
     * resizing should be ignored and must not influence window location through
     * this mechanism.
     *
     * The corresponding correction formulas are:
     *
     * {@code x -= xGravity * deltaW}
     * {@code y -= yGravity * deltaH}
     *
     * @param x the new window horizontal position, ignored if xSet is set to
     *          false
     * @param y the new window vertical position, ignored if ySet is set to
     *          false
     * @param xSet indicates whether the x parameter is valid
     * @param ySet indicates whether the y parameter is valid
     * @param w the new window width, ignored if set to -1
     * @param h the new window height, ignored if set to -1
     * @param cw the new window content width, ignored if set to -1
     * @param ch the new window content height, ignored if set to -1
     * @param xGravity the xGravity coefficient
     * @param yGravity the yGravity coefficient
     */
    void setBounds(float x, float y, boolean xSet, boolean ySet,
                           float w, float h, float cw, float ch,
                           float xGravity, float yGravity);

}
