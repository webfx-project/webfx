package dev.webfx.kit.mapper.peers.javafxweb.engine;

import javafx.concurrent.Worker;

/**
 * @author Bruno Salmon
 */
public interface WebEnginePeer {

    Worker<Void> getLoadWorker();

    Object executeScript(String script);

}
