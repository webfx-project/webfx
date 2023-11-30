package dev.webfx.kit.registry.javafxweb;

import dev.webfx.kit.mapper.peers.javafxweb.engine.WebEnginePeer;

/**
 * @author Bruno Salmon
 */
public class JavaFxWebRegistry {

    public static native void registerWebView();

    public static native WebEnginePeer createWebEnginePeer(Object webEngine);

}
