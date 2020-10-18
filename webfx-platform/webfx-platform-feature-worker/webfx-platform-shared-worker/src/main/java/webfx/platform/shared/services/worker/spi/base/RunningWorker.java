package webfx.platform.shared.services.worker.spi.base;

import webfx.platform.shared.services.worker.Worker;

/**
 * @author Bruno Salmon
 */
public interface RunningWorker extends Worker {

    void log(String message);

    void log(Object object);

    Object toNativeJsonArray(double[] doubleArray);

    Object toNativeJsonArray(int[] intArray);

    int getJsonInt(Object nativeObject, String key);

    double getJsonDouble(Object nativeObject, String key);

}
