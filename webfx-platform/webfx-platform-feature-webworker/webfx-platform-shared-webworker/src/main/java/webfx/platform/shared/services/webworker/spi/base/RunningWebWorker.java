package webfx.platform.shared.services.webworker.spi.base;

import webfx.platform.shared.services.webworker.WebWorker;

/**
 * @author Bruno Salmon
 */
public interface RunningWebWorker extends WebWorker {

    void log(String message);

    void log(Object object);

    Object toNativeJsonArray(byte[] byteArray);

    Object toNativeJsonArray(double[] doubleArray);

    Object toNativeJsonArray(int[] intArray);

    int getJsonInt(Object nativeObject, String key);

    double getJsonDouble(Object nativeObject, String key);

}
