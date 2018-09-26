package webfx.platforms.core.services.serial;

import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.serial.spi.SerialCodec;

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
        for (SerialCodec serialCodec : ServiceLoader.load(SerialCodec.class)) {
            SerialCodecManager.registerSerialCodec(serialCodec);
            sb.append(sb.length() == 0 ? "Serial codecs registered for classes: " : ", ").append(serialCodec.getJavaClass().getSimpleName());
        }
        Logger.log(sb);
    }
}
