package emul.javafx.scene.control;

import emul.javafx.beans.property.ObjectProperty;

/**
 * <p>
 * The Skinnable interface is implemented by the {@link Control} class,
 * and therefore is implemented by all Control implementations.
 * <p>
 * @since JavaFX 2.0
 */
public interface Skinnable {
    /**
     * Skin is responsible for rendering this {@code Control}. From the
     * perspective of the {@code Control}, the {@code Skin} is a black box.
     * It listens and responds to changes in state in a {@code Control}.
     * <p>
     * There is a one-to-one relationship between a {@code Control} and its
     * {@code Skin}. Every {@code Skin} maintains a back reference to the
     * {@code Control}.
     * <p>
     * A skin may be null.
     *
     */
    ObjectProperty<Skin<?>> skinProperty();

    /**
     * Sets the skin that will render this {@link Control}
     */
    void setSkin(Skin<?> value);

    /**
     * Returns the skin that renders this {@link Control}
     */
    Skin<?> getSkin();
}

