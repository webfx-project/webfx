package dev.webfx.platform.shared.services.webworker.spi.base;

import dev.webfx.platform.shared.services.webworker.WebWorker;

/**
 * @author Bruno Salmon
 */
public abstract class JavaCodedWebWorkerBase extends DelegatingWebWorker implements RunningWebWorker {

    private RunningWebWorker platformWorker;

    public abstract void onLoaded();

    @Override
    public void setDelegate(WebWorker delegate) {
        platformWorker = (RunningWebWorker) delegate;
        super.setDelegate(delegate);
    }

    public final void setPlatformWorker(RunningWebWorker platformWorker) {
        setDelegate(platformWorker);
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
    public Object toNativeJsonArray(byte[] byteArray) {
        return platformWorker.toNativeJsonArray(byteArray);
    }

    @Override
    public Object toNativeJsonArray(double[] doubleArray) {
        return platformWorker.toNativeJsonArray(doubleArray);
    }

    @Override
    public Object toNativeJsonArray(int[] intArray) {
        return platformWorker.toNativeJsonArray(intArray);
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
