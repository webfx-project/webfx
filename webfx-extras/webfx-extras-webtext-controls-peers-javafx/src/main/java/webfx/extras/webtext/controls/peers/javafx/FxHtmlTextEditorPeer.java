package webfx.extras.webtext.controls.peers.javafx;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import webfx.extras.webtext.controls.HtmlTextEditor;
import webfx.extras.webtext.controls.peers.base.HtmlTextEditorPeerBase;
import webfx.extras.webtext.controls.peers.base.HtmlTextEditorPeerMixin;
import webfx.platform.shared.util.Objects;
import netscape.javascript.JSObject;

/**
 * @author Bruno Salmon
 */
public final class FxHtmlTextEditorPeer
        <FxN extends WebView, N extends HtmlTextEditor, NB extends HtmlTextEditorPeerBase<N, NB, NM>, NM extends HtmlTextEditorPeerMixin<N, NB, NM>>
        extends FxHtmlTextPeer<FxN, N, NB, NM>
        implements HtmlTextEditorPeerMixin<N, NB, NM> {

    private static final String ckEditorUrl = "http://cdn.ckeditor.com/4.7.2/full/ckeditor.js";
    private JSObject ckEditor;

    public FxHtmlTextEditorPeer() {
        this((NB) new HtmlTextEditorPeerBase());
    }

    public FxHtmlTextEditorPeer(NB base) {
        super(base);
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent("<html><head><script src='" + ckEditorUrl + "'></script></head><body><div id='ckEditorDiv'></div></body></html>");
        webEngine.getLoadWorker().stateProperty().addListener(
                (ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED && ckEditor == null) {
                        N node = getNode();
                        JSObject window = (JSObject) executeScript("window");
                        if (window != null) {
                            window.setMember("javaThis", this);
                            ckEditor = (JSObject) executeScript("CKEDITOR.replace('ckEditorDiv', {resize_enabled: false, on: {'instanceReady': function(e) {e.editor.execCommand('maximize'); e.editor.on('change', function() {javaThis.onEditorDataChanged();});}}});");
                            updateText(node.getText());
                        }
                    }
                } );
    }

    @Override
    public void updateText(String text) {
        if (ckEditor != null && !Objects.areEquals(text, getEditorData()))
            ckEditor.call("setData", text);
    }

/*
    @Override
    protected void resizeWebView(double width, double height) {
        super.resizeWebView(width, height);
        if (ckEditor != null)
            ckEditor.call("resize", width - 20, height - 20);
    }
*/
    public void onEditorDataChanged() {
        getNode().setText(getEditorData());
    }

    private String getEditorData() {
        return ckEditor.call("getData").toString();
    }
}