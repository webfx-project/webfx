package webfx.platforms.core.services.serial;

import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.serial.spi.SerialCodec;
import webfx.platforms.core.util.collection.Collections;

import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class SerialCodecModuleInitializer implements ApplicationModuleInitializer {

    @Override
    public String getModuleName() {
        return "webfx-platforms-core-serial";
    }

    @Override
    public int getInitLevel() {
        return SERIAL_CODEC_INIT_LEVEL;
    }

    @Override
    public void initModule() {
        StringBuilder sb = new StringBuilder();
        List<SerialCodec> serialCodecs = Collections.listOf(ServiceLoader.load(SerialCodec.class));
        for (SerialCodec serialCodec : serialCodecs) {
            SerialCodecManager.registerSerialCodec(serialCodec);
            sb.append(sb.length() == 0 ? serialCodecs.size() + " serial codecs provided for: " : ", ").append(serialCodec.getJavaClass().getSimpleName());
        }
        Logger.log(sb);
    }
}
