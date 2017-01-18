package emul.javafx.css;

import emul.javafx.collections.ObservableList;

/**
 * Styleable comprises the minimal interface required for an object to be styled by CSS.
 * @see <a href="../scene/doc-files/cssref.html">CSS Reference Guide</a>.
 * @since JavaFX 8.0
 */
public interface Styleable {

    /**
     * The type of this {@code Styleable} that is to be used in selector matching.
     * This is analogous to an "element" in HTML.
     * (<a href="http://www.w3.org/TR/CSS2/selector.html#type-selectors">CSS Type Selector</a>).
     */
    //String getTypeSelector();

    /**
     * The id of this {@code Styleable}. This simple string identifier is useful for
     * finding a specific Node within the scene graph. While the id of a Node
     * should be unique within the scene graph, this uniqueness is not enforced.
     * This is analogous to the "id" attribute on an HTML element
     * (<a href="http://www.w3.org/TR/CSS21/syndata.html#value-def-identifier">CSS ID Specification</a>).
     * <p>
     * For example, if a Node is given the id of "myId", then the lookup method can
     * be used to find this node as follows: <code>scene.lookup("#myId");</code>.
     * </p>
     */
    String getId();

    /**
     * A list of String identifiers which can be used to logically group
     * Nodes, specifically for an external style engine. This variable is
     * analogous to the "class" attribute on an HTML element and, as such,
     * each element of the list is a style class to which this Node belongs.
     *
     * @see <a href="http://www.w3.org/TR/css3-selectors/#class-html">CSS3 class selectors</a>
     */
    ObservableList<String> getStyleClass();

    /**
     * A string representation of the CSS style associated with this
     * specific {@code Node}. This is analogous to the "style" attribute of an
     * HTML element. Note that, like the HTML style attribute, this
     * variable contains style properties and values and not the
     * selector portion of a style rule.
     */
    String getStyle();

    /**
     * The CssMetaData of this Styleable. This may be returned as
     * an unmodifiable list.
     *
     */
    //List<CssMetaData<? extends Styleable, ?>> getCssMetaData();

    /**
     * Return the parent of this Styleable, or null if there is no parent.
     */
    //Styleable getStyleableParent();

    /**
     * Return the pseudo-class state of this Styleable. CSS assumes this set is read-only.
     */
    //ObservableSet<PseudoClass> getPseudoClassStates();

}
