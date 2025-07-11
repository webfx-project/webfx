package javafx.scene.control;

import javafx.scene.Node;

/**
 * Base class for defining the visual representation of user interface controls
 * by defining a scene graph of nodes to represent the skin.
 * A user interface control is abstracted behind the {@link Skinnable} interface.
 *
 * @param <C> A subtype of Skinnable that the Skin represents. This allows for
 *      Skin implementation to access the {@link Skinnable} implementation,
 *      which is usually a {@link Control} implementation.
 * @since JavaFX 2.0
 */
public interface Skin<C extends Skinnable> {
    /**
     * Gets the Skinnable to which this Skin is assigned. A Skin must be created
     * for one and only one Skinnable. This value will only ever go from a
     * non-null to null value when the Skin is removed from the Skinnable, and
     * only as a consequence of a call to {@link #dispose()}.
     * <p>
     * The caller who constructs a Skinnable must also construct a Skin and
     * properly establish the relationship between the Control and its Skin.
     *
     * @return A non-null Skinnable, or null value if disposed.
     */
    C getSkinnable();

    /**
     * Gets the Node which represents this Skin. This must never be null, except
     * after a call to {@link #dispose()}, and must never change except when
     * changing to null.
     *
     * @return A non-null Node, except when the Skin has been disposed.
     */
    Node getNode();

    /**
     * Called once when {@code Skin} is set. This method is called after the previous skin,
     * if any, has been uninstalled via its {@link #dispose()} method.
     * The skin can now safely make changes to its associated control, like registering listeners,
     * adding child nodes, and modifying properties and event handlers.
     * <p>
     * Application code must not call this method.
     * <p>
     * The default implementation of this method does nothing.
     *
     * @implNote
     * Skins only need to implement {@code install} if they need to make direct changes to the control
     * like overwriting properties or event handlers. Such skins should ensure these changes are undone in
     * their {@link #dispose()} method.
     *
     * @since 20
     */
    default /*public*/ void install() { }

    /**
     * Called by a Skinnable when the Skin is replaced on the Skinnable. This method
     * allows a Skin to implement any logic necessary to clean up itself after
     * the Skin is no longer needed. It may be used to release native resources.
     * The methods {@link #getSkinnable()} and {@link #getNode()}
     * should return null following a call to dispose. Calling dispose twice
     * has no effect.
     */
    void dispose();
}
