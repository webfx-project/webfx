package webfx.platform.shared.services.worker.spi.abstrimpl;

import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public abstract class JavaApplicationWorker implements RunningWorker {

    private RunningWorker platformWorker;

    public abstract void onLoaded();

    public final void setPlatformWorker(RunningWorker platformWorker) {
        this.platformWorker = platformWorker;
    }

    @Override
    public final void postMessage(Object msg) {
        platformWorker.postMessage(msg);
    }

    @Override
    public final void setOnMessageHandler(Consumer<Object> onMessageHandler) {
        platformWorker.setOnMessageHandler(onMessageHandler);
    }

    @Override
    public final void terminate() {
        platformWorker.terminate();
    }

    @Override
    public final void log(String message) {
        platformWorker.log(message);
    }

    @Override
    public final void log(Object object) {
        platformWorker.log(object);
    }

    @Override
    public Object toNativeJsonArray(double[] doubleArray) {
        return platformWorker.toNativeJsonArray(doubleArray);
    }

    @Override
    public int getJsonInt(Object nativeObject, String key) {
        return platformWorker.getJsonInt(nativeObject, key);
    }

    @Override
    public double getJsonDouble(Object nativeObject, String key) {
        return platformWorker.getJsonDouble(nativeObject, key);
    }
}
