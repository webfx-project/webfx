package naga.providers.platform.client.teavm.websocket;

import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

/**
 * @author Bruno Salmon
 */
interface SockJS extends JSObject {

    @JSProperty("readyState")
    JSObject getReadyState();

    void close();

    void send(String data);

    @JSProperty()
    void setOnopen(VoidHandler handler);

    @JSProperty()
    void setOnclose(JSObjectHandler handler);

    @JSProperty()
    void setOnmessage(JSObjectHandler handler);

    @JSProperty()
    void setOnerror(JSObjectHandler handler);

    @JSFunctor
    interface VoidHandler extends JSObject {
        void handle();
    }

    @JSFunctor
    interface JSObjectHandler extends JSObject {
        void handle(JSObject jso);
    }

}
