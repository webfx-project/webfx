package javafx.scene.web;

import dev.webfx.kit.registry.javafxweb.JavaFxWebRegistry;
import dev.webfx.kit.mapper.peers.javafxweb.engine.WebEnginePeer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;

/**
 * @author Bruno Salmon
 */
public class WebEngine {

    private final WebView webView;
    private final WebEnginePeer webEnginePeer;

    private final ObjectProperty<EventHandler<WebErrorEvent>> onError =
            new SimpleObjectProperty<>(this, "onError");

    public final EventHandler<WebErrorEvent> getOnError() {
        return onError.get();
    }

    public final void setOnError(EventHandler<WebErrorEvent> handler) {
        onError.set(handler);
    }

    public final ObjectProperty<EventHandler<WebErrorEvent>> onErrorProperty() {
        return onError;
    }


    WebEngine(WebView webView) {
        this.webView = webView;
        webEnginePeer = JavaFxWebRegistry.createWebEnginePeer(this);
    }

    public WebView getWebView() {
        return webView;
    }

    public void load(String url) {
        webView.setUrl(url);
    }

    public void loadContent(String content) {
        loadContent(content, "text/html");
    }

    public void loadContent(String content, String contentType) {
        webView.setLoadContent(content);
    }


    public Worker<Void> getLoadWorker() {
        return webEnginePeer.getLoadWorker();
    }


    public Object executeScript(String script) {
        return webEnginePeer.executeScript(script);
    }
}
