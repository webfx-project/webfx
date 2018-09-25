package webfx.platforms.core.services.serial;

import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platforms.core.services.log.Logger;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class SerialCodecModuleInitializer implements ApplicationModuleInitializer {

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
        for (SerialCodec serialCodec : ServiceLoader.load(SerialCodec.class)) {
            SerialCodecManager.registerJsonCodec(serialCodec.getJavaClass(), serialCodec);
            sb.append(sb.length() == 0 ? "Json codecs registered for classes: " : ", ").append(serialCodec.getJavaClass().getSimpleName());
        }
        Logger.log(sb);
    }
}
