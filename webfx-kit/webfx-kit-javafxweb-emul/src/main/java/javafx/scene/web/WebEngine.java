package javafx.scene.web;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;

/**
 * @author Bruno Salmon
 */
public class WebEngine {

    private final WebView webView;

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
}
