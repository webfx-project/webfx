package webfx.demos.mandelbrot.worker;

import webfx.demos.mandelbrot.computation.MandelbrotComputation;
import webfx.demos.mandelbrot.computation.MandelbrotPoint;
import webfx.demos.mandelbrot.computation.MandelbrotViewport;
import webfx.platform.client.services.webassembly.*;
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.worker.spi.base.JavaCodedWorkerBase;
import webfx.platform.shared.util.async.Future;

import java.math.BigDecimal;

/**
 * @author Bruno Salmon
 */
public class MandelbrotWorker extends JavaCodedWorkerBase {

    // These fields will store the input parameters passed by the JS UI through JSON
    private String sXmin, sXmax, sYmin, sYmax;
    private int width, height, maxIterations;
    private boolean usingWebAssembly;
    // Mandelbrot view port (actually used only when computing here in JS, not when calling WebAssembly)
    private final MandelbrotViewport viewport = new MandelbrotViewport();
    private int[] pixelIterations; // Output fields that will hold the result of the computation
    // Fields for WebAssembly management
    private WebAssemblyInstance webAssemblyInstance;
    private WebAssemblyMemoryBufferWriter inputBufferWriter;
    private WebAssemblyMemoryBufferReader outputBufferReader;
    private boolean webAssemblyInstanceInited;

    @Override
    public void onLoaded() {
        if (WebAssembly.isSupported()) {
            Future<WebAssemblyInstance> future = WebAssembly.loadAndInstantiate("classes.wasm", new WebAssemblyImport("mandelbrot", "setPixelIteration", this::setPixelIteration));
            future.setHandler(ar -> {
                webAssemblyInstance = ar.result();
                inputBufferWriter = webAssemblyInstance.getDataWriter(((Number) webAssemblyInstance.call("getInputBufferAddress")).intValue());
                outputBufferReader = webAssemblyInstance.getDataReader(0);
            });
        }
        setOnMessageHandler(data -> {
            JsonObject json = Json.createObject(data);
            int cy = json.getInteger("cy", 0); // TODO: fix bug returning null for 0 value in TeaVM implementation
            String sxmin = json.get("sxmin");
            boolean initViewPort = sxmin != null;
            if (initViewPort) {
                sXmin = sxmin;
                sXmax = json.getString("sxmax");
                sYmin = json.getString("symin");
                sYmax = json.getString("symax");
                width = json.getInteger("width");
                height = json.getInteger("height");
                maxIterations = json.getInteger("maxIterations");
                usingWebAssembly = json.isTrue("wasm");
            }
            if (usingWebAssembly && webAssemblyInstance != null) {
                //pixelIterations = new int[(int) width];
                if (initViewPort || !webAssemblyInstanceInited) {
                    inputBufferWriter.resetMemoryBufferOffset();
                    inputBufferWriter.writeString(sXmin);
                    inputBufferWriter.writeString(sXmax);
                    inputBufferWriter.writeString(sYmin);
                    inputBufferWriter.writeString(sYmax);
                    int outputBufferOffset = ((Number) webAssemblyInstance.call("initAndComputeLinePixelIterations", cy, width, height, maxIterations)).intValue();
                    outputBufferReader.setMemoryBufferOffset(outputBufferOffset);
                    webAssemblyInstanceInited = true;
                } else
                    webAssemblyInstance.call("computeLinePixelIterations", cy);
                outputBufferReader.resetMemoryBufferOffset();
                pixelIterations = outputBufferReader.readIntArray(width);
            } else {
                if (initViewPort) {
                    MandelbrotComputation.init();
                    viewport.xmin = new BigDecimal(sXmin);
                    viewport.xmax = new BigDecimal(sXmax);
                    viewport.ymin = new BigDecimal(sYmin);
                    viewport.ymax = new BigDecimal(sYmax);
                }
                pixelIterations = computeLinePixelIterations(cy, width, height, viewport, maxIterations);
            }
            postMessage(toNativeJsonArray(pixelIterations));
        });
    }

    private void setPixelIteration(int cx, int count) {
        pixelIterations[cx] = count;
    }

    private static int[] computeLinePixelIterations(int cy, int width, int height, MandelbrotViewport viewport, int maxIterations) {
        int[] pixelIterations = new int[width];
        int cx = 0;
        while (cx < width) {
            // Passing the canvas pixel for the pixel color computation
            MandelbrotPoint mbp = MandelbrotComputation.convertCanvasPixelToModelPoint(cx, cy, width, height, viewport);
            int count = MandelbrotComputation.computeModelPointValue(mbp.x, mbp.y, maxIterations);
            pixelIterations[cx++] = count;
        }
        return pixelIterations;
    }
}
