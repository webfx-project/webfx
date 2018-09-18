package webfx.platforms.core.services.json.codec;

import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platforms.core.services.log.Logger;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public class JsonCodecModuleInitializer implements ApplicationModuleInitializer {

    @Override
    public String getModuleName() {
        return "webfx-platforms-core-json";
    }

    @Override
    public int getInitLevel() {
        return JSON_CODEC_INIT_LEVEL;
    }

    @Override
    public void initModule() {
        StringBuilder sb = new StringBuilder();
        for (JsonCodec jsonCodec : ServiceLoader.load(JsonCodec.class)) {
            JsonCodecManager.registerJsonCodec(jsonCodec.getJavaClass(), jsonCodec);
            sb.append(sb.length() == 0 ? "Json codecs registered for classes: " : ", ").append(jsonCodec.getJavaClass().getSimpleName());
        }
        Logger.log(sb);
    }
}
