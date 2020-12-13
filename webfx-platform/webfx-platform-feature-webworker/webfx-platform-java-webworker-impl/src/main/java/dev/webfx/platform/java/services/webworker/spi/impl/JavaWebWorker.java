package dev.webfx.platform.java.services.webworker.spi.impl;

import dev.webfx.platform.client.services.uischeduler.UiScheduler;
import dev.webfx.platform.shared.services.json.JsonObject;
import dev.webfx.platform.shared.services.log.Logger;
import dev.webfx.platform.shared.services.webworker.spi.base.JavaCodedWebWorkerBase;
import dev.webfx.platform.shared.services.webworker.spi.base.PlatformWebWorkerBase;
import dev.webfx.platform.shared.services.webworker.spi.base.WebWorkerBase;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public class JavaWebWorker extends WebWorkerBase { // this instance represents the proxy the application interacts with

    private ScheduledExecutorService workerExecutor = Executors.newSingleThreadScheduledExecutor();

    private final JavaCodedWebWorkerBase applicationWorker; // This instance represents the actual running worker coded in Java (instantiated through the passed class)
    private final PlatformWebWorkerBase javaPlatformWorker = new PlatformWebWorkerBase() { // This instance represents the java platform layer the application worker interacts with
        @Override
        public void log(String message) {
            Logger.log(message);
        }

        @Override
        public void log(Object object) {
            Logger.log(object);
        }

        @Override
        public Object toNativeJsonArray(byte[] byteArray) {
            return byteArray;
        }

        @Override
        public Object toNativeJsonArray(double[] doubleArray) {
            return doubleArray;
        }

        @Override
        public Object toNativeJsonArray(int[] intArray) {
            return intArray;
        }

        @Override
        public int getJsonInt(Object nativeObject, String key) {
            return ((JsonObject) nativeObject).getInteger(key);
        }

        @Override
        public double getJsonDouble(Object nativeObject, String key) {
            return ((JsonObject) nativeObject).getDouble(key);
        }

        @Override
        public void postMessage(Object msg) {
            // When the application worker call this method, we need to pass this to the onMessageHandler of the proxy
            UiScheduler.runInUiThread(() -> JavaWebWorker.this.getOnMessageHandler().accept(msg));
        }

        @Override
        public void terminate() {
            workerExecutor.shutdown();
        }
    };

    public JavaWebWorker(String scriptUrl) {
        throw new UnsupportedOperationException();
    }

    public JavaWebWorker(Class<? extends JavaCodedWebWorkerBase> javaCodedWorkerClass) {
        JavaCodedWebWorkerBase javaApplicationWorker;
        try {
            javaApplicationWorker = javaCodedWorkerClass.getDeclaredConstructor().newInstance();
            javaApplicationWorker.setPlatformWorker(javaPlatformWorker);
        } catch (Exception e) {
            e.printStackTrace();
            javaApplicationWorker = null;
        }
        this.applicationWorker = javaApplicationWorker;
        javaApplicationWorker.onLoaded();
    }

    @Override
    public void postMessage(Object msg) {
        // When the application calls this method, we need to pass this to the onMessageHandler of the application
        workerExecutor.schedule(() -> javaPlatformWorker.getOnMessageHandler().accept(msg), 0, TimeUnit.SECONDS);
    }

    @Override
    public void terminate() {
        applicationWorker.terminate();
    }
}
