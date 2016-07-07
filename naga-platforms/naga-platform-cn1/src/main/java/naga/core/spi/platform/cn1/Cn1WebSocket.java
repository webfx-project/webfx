package naga.core.spi.platform.cn1;

import naga.core.bus.client.WebSocketListener;
import naga.core.json.Json;
import naga.core.bus.client.WebSocket;

import java.io.UnsupportedEncodingException;

final class Cn1WebSocket implements WebSocket {

    private final com.codename1.io.websocket.WebSocket socket;
    private WebSocketListener listener;

    public Cn1WebSocket(String uri) {
        socket = new com.codename1.io.websocket.WebSocket(uri) {

            @Override
            protected void onOpen() {
                if (listener != null)
                    listener.onOpen();
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
                if (listener != null)
                    listener.onMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                if (listener != null) {
                    String message = e.getMessage();
                    listener.onError(message == null ? e.getClass().getSimpleName() : message);
                }
            }

            @Override
            protected void onClose(int code, String reason) {
                if (listener != null)
                    listener.onClose(Json.createObject().set("code", code).set("reason", reason));
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
    public void setListener(WebSocketListener listener) {
        this.listener = listener;
    }


    @Override
    public void close() {
        socket.close();
    }
}