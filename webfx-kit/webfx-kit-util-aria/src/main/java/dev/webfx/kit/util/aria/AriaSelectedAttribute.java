package dev.webfx.kit.util.aria;

/**
 * @author Bruno Salmon
 */
public enum AriaSelectedAttribute {
    CHECKED("aria-checked"),
    SELECTED("aria-selected"),
    PRESSED("aria-pressed");

    private final String attributeName;

    AriaSelectedAttribute(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }
}
