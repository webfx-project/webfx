package com.sun.javafx.scene;

import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import javafx.scene.Parent;

/**
 * @author Bruno Salmon
 */
public class ParentHelper extends NodeHelper {

    public static ParentTraversalEngine getTraversalEngine(Parent parent) {
        return parent.getTraversalEngine();
    }

    public static void setTraversalEngine(Parent parent, ParentTraversalEngine parentTraversalEngine) {
        parent.setTraversalEngine(parentTraversalEngine);
    }
}
