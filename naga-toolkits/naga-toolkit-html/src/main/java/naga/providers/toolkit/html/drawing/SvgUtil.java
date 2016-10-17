package naga.providers.toolkit.html.drawing;

import elemental2.Element;

import static elemental2.Global.document;

/**
 * @author Bruno Salmon
 */
public class SvgUtil {

    private final static String svgNS = "http://www.w3.org/2000/svg";

    public static Element createSvgElement(String tag) {
        return document.createElementNS(svgNS, tag);
    }

    public static /*SVGElement Elemental2 compilation error */ Element createSvgElement() {
        return /*SVGElement*/ createSvgElement("svg");
    }

    public static Element createSvgRectangle() {
        return createSvgElement("rect");
    }

}
