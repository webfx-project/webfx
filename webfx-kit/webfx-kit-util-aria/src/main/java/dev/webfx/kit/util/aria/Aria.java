package dev.webfx.kit.util.aria;

import javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public final class Aria {
    /**
     * Sets the ARIA role for a node.
     *
     * @param node the node to set the ARIA role for
     * @param ariaRole the ARIA role to set
     */
    public static void setAriaRole(Node node, AriaRole ariaRole) {
        node.getProperties().put("aria-role", ariaRole);
    }

    /**
     * Sets the ARIA label for a node.
     *
     * @param node the node to set the ARIA label for
     * @param ariaLabel the ARIA label to set
     */
    public static void setAriaLabel(Node node, String ariaLabel) {
        node.getProperties().put("aria-label", ariaLabel);
    }

    /**
     * Sets the ARIA selected state for a node.
     *
     * @param node the node to set the ARIA selected state for
     * @param ariaSelected the ARIA checked state to set
     */
    public static void setAriaSelected(Node node, boolean ariaSelected) {
        node.getProperties().put("aria-selected", ariaSelected);
    }

    /**
     * Sets the ARIA selected attribute to use to reflect the selected state for a node.
     *
     * @param node the node to set the ARIA selected attribute for
     * @param stateAttribute the ARIA selected attribute to set
     */
    public static void setAriaSelectedAttribute(Node node, AriaSelectedAttribute stateAttribute) {
        node.getProperties().put("aria-state-attribute", stateAttribute);
    }

    /**
     * Sets the ARIA expanded state for a node.
     *
     * @param node the node to set the ARIA expanded state for
     * @param ariaExpanded the ARIA expanded state to set
     */
    public static void setAriaExpanded(Node node, boolean ariaExpanded) {
        node.getProperties().put("aria-expanded", ariaExpanded);
    }

    /**
     * Sets the ARIA hidden state for a node.
     *
     * @param node the node to set the ARIA hidden state for
     * @param ariaHidden the ARIA hidden state to set
     */
    public static void setAriaHidden(Node node, boolean ariaHidden) {
        node.getProperties().put("aria-hidden", ariaHidden);
    }

    /**
     * Sets the ARIA disabled state for a node.
     *
     * @param node the node to set the ARIA disabled state for
     * @param ariaDisabled the ARIA disabled state to set
     */
    public static void setAriaDisabled(Node node, boolean ariaDisabled) {
        node.getProperties().put("aria-disabled", ariaDisabled);
    }

    /**
     * Sets the ARIA read-only state for a node.
     *
     * @param node the node to set the ARIA read-only state for
     * @param ariaReadOnly the ARIA read-only state to set
     */
    public static void setAriaReadOnly(Node node, boolean ariaReadOnly) {
        node.getProperties().put("aria-readonly", ariaReadOnly);
    }

    /**
     * Sets the ARIA required state for a node.
     *
     * @param node the node to set the ARIA required state for
     * @param ariaRequired the ARIA required state to set
     */
    public static void setAriaRequired(Node node, boolean ariaRequired) {
        node.getProperties().put("aria-required", ariaRequired);
    }

    /**
     * Sets the ARIA invalid state for a node.
     *
     * @param node the node to set the ARIA invalid state for
     * @param ariaInvalid the ARIA invalid state to set
     */
    public static void setAriaInvalid(Node node, boolean ariaInvalid) {
        node.getProperties().put("aria-invalid", ariaInvalid);
    }
}
