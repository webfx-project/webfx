package naga.core.spi.platform.client.cn1;

import naga.core.composite.Composites;
import naga.core.composite.WritableCompositeObject;
import naga.core.spi.platform.client.WebSocket;

import java.io.UnsupportedEncodingException;

final class Cn1WebSocket implements WebSocket {

    private final com.codename1.io.websocket.WebSocket socket;
    private WebSocketHandler eventHandler;

    public Cn1WebSocket(String uri) {
        socket = new com.codename1.io.websocket.WebSocket(uri) {

            @Override
            protected void onOpen() {
                if (eventHandler != null)
                    eventHandler.onOpen();
            }

            @Override
            protected void onMessage(byte[] bytes) {
                try {
                    onMessage(new String(bytes, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(String msg) {
                if (eventHandler != null)
                    eventHandler.onMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                if (eventHandler != null) {
                    String message = e.getMessage();
                    eventHandler.onError(message == null ? e.getClass().getSimpleName() : message);
                }
            }

            @Override
            protected void onClose(int code, String reason) {
                if (eventHandler != null) {
                    WritableCompositeObject msg = Composites.createObject();
                    msg.set("code", code);
                    msg.set("reason", reason);
                    eventHandler.onClose(msg);
                }
            }
        };

        socket.connect();
    }

    @Override
    public State getReadyState() {
        return State.values[socket.getReadyState().ordinal()];
    }

    @Override
    public void send(String data) {
        try {
            socket.send(data);
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void setListen(WebSocketHandler handler) {
        this.eventHandler = handler;
    }


    @Override
    public void close() {
        socket.close();
    }
}