package naga.fx.scene.paint;

/**
 * @author Bruno Salmon
 */
public interface Paint {

    boolean isOpaque();

    /**
     * Creates a paint value from a string representation. Recognizes strings
     * representing {@code Color}, {@code RadialGradient} or {@code LinearGradient}.
     * String specifying LinearGradient must begin with linear-gradient keyword
     * and string specifying RadialGradient must begin with radial-gradient.
     *
     * @param value the string to convert
     * @throws NullPointerException if {@code value} is {@code null}
     * @throws IllegalArgumentException if {@code value} cannot be parsed
     * @return a {@code Color}, {@code RadialGradient} or {@code LinearGradient}
     * object holding the value represented by the string argument.
     *
     * @see Color#valueOf(String)
     * @see LinearGradient#valueOf(String)
     * @see RadialGradient#valueOf(String)
     * @since JavaFX 2.1
     */
    static Paint valueOf(String value) {
        if (value == null) {
            throw new NullPointerException("paint must be specified");
        }

        if (value.startsWith("linear-gradient(")) {
            return LinearGradient.valueOf(value);
/*
        } else if (value.startsWith("radial-gradient(")) {
            return RadialGradient.valueOf(value);
*/
        } else {
            return Color.valueOf(value);
        }
    }

}
