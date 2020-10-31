package webfx.demo.mandelbrot.webworker;

import webfx.demo.mandelbrot.math.MandelbrotMath;
import webfx.platform.client.services.webassembly.WebAssembly;
import webfx.platform.client.services.webassembly.WebAssemblyInstance;
import webfx.platform.client.services.webassembly.WebAssemblyMemoryBufferReader;
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.webworker.spi.base.JavaCodedWebWorkerBase;
import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class MandelbrotWebWorker extends JavaCodedWebWorkerBase {

    private int width, height, placeIndex, frameIndex;
    private int[] pixelIterations; // Output fields that will hold the result of the computation
    // Fields for WebAssembly management
    private boolean usingWebAssembly;
    private WebAssemblyInstance webAssemblyInstance;
    private WebAssemblyMemoryBufferReader outputBufferReader;
    private boolean webAssemblyInstanceInited;

    @Override
    public void onLoaded() {
        if (WebAssembly.isSupported()) {
            Future<WebAssemblyInstance> future = WebAssembly.loadAndInstantiate("classes.wasm"/*, new WebAssemblyImport("mandelbrot", "setPixelIteration", this::setPixelIteration)*/);
            future.setHandler(ar -> {
                webAssemblyInstance = ar.result();
                outputBufferReader = webAssemblyInstance.getDataReader(0);
            });
        }
        setOnMessageHandler(data -> {
            JsonObject json = Json.createObject(data);
            int cy = json.getInteger("cy", 0); // TODO: fix bug returning null for 0 value in TeaVM implementation
            Integer iWidth = json.getInteger("width"); // TODO: fix json.has() not compiling with TeaVM
            boolean initFrame = iWidth != null;
            if (initFrame) {
                width = json.getInteger("width");
                height = json.getInteger("height");
                placeIndex = json.getInteger("placeIndex", 0);
                frameIndex = json.getInteger("frameIndex", 0);
                usingWebAssembly = json.isTrue("wasm");
            }
            if (usingWebAssembly && webAssemblyInstance != null) { // Delegating computation to WebAssembly instance
                if (initFrame || !webAssemblyInstanceInited) {
                    int outputBufferOffset = ((Number) webAssemblyInstance.call("initAndComputeLinePixelIterations", cy, width, height, placeIndex, frameIndex)).intValue();
                    outputBufferReader.setMemoryBufferOffset(outputBufferOffset);
                    webAssemblyInstanceInited = true;
                } else
                    webAssemblyInstance.call("computeLinePixelIterations", cy);
                outputBufferReader.resetMemoryBufferOffset();
                pixelIterations = outputBufferReader.readIntArray(width);
            } else { // Doing computation in this worker
                if (initFrame)
                    MandelbrotMath.init(width, height, placeIndex, frameIndex);
                pixelIterations = MandelbrotMath.createLinePixelResultStorage(width, null);
                MandelbrotMath.computeLinePixelIterations(cy, pixelIterations);
            }
            postMessage(toNativeJsonArray(pixelIterations));
        });
    }

/*
    private void setPixelIteration(int cx, int count) {
        pixelIterations[cx] = count;
    }
*/
}
