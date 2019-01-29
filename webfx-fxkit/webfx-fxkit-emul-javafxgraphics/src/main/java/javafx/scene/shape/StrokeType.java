package javafx.scene.shape;

/**
 * Defines where to draw the stroke around the boundary of a Shape node.
 *
 * @see javafx.scene.shape.Shape
 * @since JavaFX 2.0
 */
public enum StrokeType {

    /**
     * The stroke is applied by extending the boundary of a closed {@link javafx.scene.shape.Shape}
     * node into its interior by a distance specified by the
     * {@link javafx.scene.shape.Shape#strokeWidthProperty strokeWidth}.
     * Since the definition of {@code INSIDE} depends on the filled interior
     * of the shape, this {@code StrokeType} is undefined for unclosed shapes.
     *
     * <p>
     * The image shows a shape without stroke and the same shape with a thick
     * inside stroke applied.
     * </p><p>
     * <img src="doc-files/stroketype-inside.png"/>
     * </p>
     */
    INSIDE,

    /**
     * The stroke is applied by extending the boundary of a closed {@link javafx.scene.shape.Shape}
     * node outside of its interior by a distance specified by the
     * {@link javafx.scene.shape.Shape#strokeWidthProperty strokeWidth}.
     * Since the definition of {@code OUTSIDE} depends on the filled interior
     * of the shape, this {@code StrokeType} is undefined for unclosed shapes.
     *
     * <p>
     * The image shows a shape without stroke and the same shape with a thick
     * outside stroke applied.
     * </p><p>
     * <img src="doc-files/stroketype-outside.png"/>
     * </p>
     */
    OUTSIDE,

    /**
     * The stroke is applied by thickening the boundary of the {@link javafx.scene.shape.Shape}
     * node by a distance of half of the {@link javafx.scene.shape.Shape#strokeWidthProperty strokeWidth}
     * on either side of the boundary.
     * Since the definition of {@code CENETERED} is symmetric and agnostic as
     * to the location of any interior of a shape, it can be used for either
     * closed or unclosed shapes.
     *
     * <p>
     * The image shows a shape without stroke and the same shape with a thick
     * centered stroke applied.
     * </p><p>
     * <img src="doc-files/stroketype-centered.png"/>
     * </p>
     */
    CENTERED
}
