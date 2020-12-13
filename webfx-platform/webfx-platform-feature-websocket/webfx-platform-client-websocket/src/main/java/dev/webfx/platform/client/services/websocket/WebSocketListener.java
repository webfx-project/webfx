package dev.webfx.platform.client.services.websocket;

import dev.webfx.platform.shared.services.json.JsonObject;

/**
 * Listens for events on a {@link WebSocket}.
 */
public interface WebSocketListener {
    /**
     * Called when the socket is ready to receive messages.
     */
    void onOpen();

    /**
     * Called when the socket receives a message.
     */
    void onMessage(String message);

    /**
     * Called when an error occurs on the socket.
     */
    void onError(String error);

    /**
     * Called when the socket is closed. When the socket is closed, it cannot be reopened.
     */
    void onClose(JsonObject reason);
}
