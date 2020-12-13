package dev.webfx.platform.shared.services.serial.spi.impl;

import dev.webfx.platform.shared.services.json.JsonObject;
import dev.webfx.platform.shared.services.json.WritableJsonObject;
import dev.webfx.platform.shared.services.log.Logger;

/**
 * @author Bruno Salmon
 */
public class ExceptionSerialCodec extends SerialCodecBase<Exception> {

    private static final String CODEC_ID = "exception";
    private static final String MESSAGE_KEY = "message";

    public ExceptionSerialCodec() {
        super(Exception.class, CODEC_ID);
    }

    @Override
    public void encodeToJson(Exception exception, WritableJsonObject json) {
        json.set(MESSAGE_KEY, exception.getClass().getName() + ": " + exception.getMessage() + "\n" + Logger.captureStackTrace(exception));
    }

    @Override
    public Exception decodeFromJson(JsonObject json) {
        return new Exception(json.getString(MESSAGE_KEY));
    }
}
