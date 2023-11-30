package dev.webfx.kit.mapper.peers.javafxweb.engine;

import javafx.concurrent.Worker;

/**
 * @author Bruno Salmon
 */
public abstract class WebEnginePeerBase implements WebEnginePeer {

    protected WorkerImpl<Void> worker = new WorkerImpl<>();

    @Override
    public Worker<Void> getLoadWorker() {
        return worker;
    }
}
