package com.sun.javafx.scene;

import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.TraversalMethod;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.LayoutFlags;

/**
 * @author Bruno Salmon
 */
public class NodeHelper {

    public static void traverse(Node node, Direction dir, TraversalMethod method) {
        node.traverse(dir, method);
    }

    public static boolean isTreeVisible(Node node) {
        return node.impl_isTreeVisible();
    }


    // Additional WebFX utility methods

    public static void webfx_forceWholeSceneGraphLayout(Scene scene) {
        webfx_forceLayoutOnThisNodeAndChildren(scene.getRoot());
    }

    public static void webfx_forceLayoutOnThisNodeAndChildren(Node node) {
        if (node != null) {
            node.clearCache();
            if (node instanceof Parent) {
                Parent parent = (Parent) node;
                parent.setLayoutFlag(LayoutFlags.NEEDS_LAYOUT);
                parent.getChildren().forEach(NodeHelper::webfx_forceLayoutOnThisNodeAndChildren);
            }
            node.onPeerSizeChanged();
            // Also running webfx-onCssOrFontLoadedRunnable (eventually set by HtmlScenePeer)
            Object runnable = node.getProperties().get("webfx-onCssOrFontLoadedRunnable");
            if (runnable instanceof Runnable)
                ((Runnable) runnable).run();
        }
    }
}
