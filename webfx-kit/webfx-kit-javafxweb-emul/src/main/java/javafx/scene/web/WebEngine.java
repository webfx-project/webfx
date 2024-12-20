package javafx.scene.web;

import dev.webfx.kit.mapper.peers.javafxweb.engine.WebEnginePeer;
import dev.webfx.kit.registry.javafxweb.JavaFxWebRegistry;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;

import java.util.Objects;

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

    public WebEngine() {
        this(null);
    }

    WebEngine(WebView webView) {
        this.webView = webView;
        webEnginePeer = JavaFxWebRegistry.createWebEnginePeer(this);
    }

    public WebView getWebView() {
        return webView;
    }

    public void load(String url) {
        // If the webview is already loaded with this url, we unload it because a call to webEngine.load() should always
        // cause a webview load.
        if (Objects.equals(url, webView.getUrl()))
            webView.setUrl(null);
        webView.setUrl(url);
    }

    public void loadContent(String content) {
        loadContent(content, "text/html");
    }

    public void loadContent(String content, String contentType) {
        // If the webview has already the same content, we unload it because a call to webEngine.loadContent() should
        // always cause to reload its content.
        if (Objects.equals(content, webView.getLoadContent()))
            webView.setLoadContent(null);
        webView.setLoadContent(content);
    }


    public Worker<Void> getLoadWorker() {
        return webEnginePeer.getLoadWorker();
    }


    public Object executeScript(String script) {
        return webEnginePeer.executeScript(script);
    }

    private StringProperty userStyleSheetLocation;

    public final void setUserStyleSheetLocation(String value) {
        userStyleSheetLocationProperty().set(value);
    }

    public final String getUserStyleSheetLocation() {
        return userStyleSheetLocation == null ? null : userStyleSheetLocation.get();
    }

    public final StringProperty userStyleSheetLocationProperty() {
        if (userStyleSheetLocation == null) {
            userStyleSheetLocation = new StringPropertyBase(null) {

                @Override public void invalidated() {
                    webEnginePeer.updateUserStyleSheetLocation(get());
                }

                @Override public Object getBean() {
                    return WebEngine.this;
                }

                @Override public String getName() {
                    return "userStyleSheetLocation";
                }
            };
        }
        return userStyleSheetLocation;
    }

}
