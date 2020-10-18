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

    private String sXmin, sXmax, sYmin, sYmax;
    private final MandelbrotViewport viewport = new MandelbrotViewport();
    private double width, height;
    private int maxIterations;
    private int[] pixelIterations;
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
            double cy = json.getDouble("cy");
            String sxmin = json.get("sxmin");
            boolean initViewPort = sxmin != null;
            if (initViewPort) {
                sXmin = sxmin;
                sXmax = json.getString("sxmax");
                sYmin = json.getString("symin");
                sYmax = json.getString("symax");
                width = json.getDouble("width");
                height = json.getDouble("height");
                maxIterations = json.getInteger("maxIterations");
            }
            if (webAssemblyInstance != null) {
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
                pixelIterations = outputBufferReader.readIntArray((int) width);
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

    private void setPixelIteration(int x, int count) {
        pixelIterations[x] = count;
    }

    private static int[] computeLinePixelIterations(double cy, double width, double height, MandelbrotViewport viewport, int maxIterations) {
        int[] pixelIterations = new int[(int) width];
        double cx = 0;
        while (cx < width) {
            // Passing the canvas pixel for the pixel color computation
            MandelbrotPoint mbp = MandelbrotComputation.convertCanvasPixelToModelPoint(cx, cy, width, height, viewport);
            int count = MandelbrotComputation.computeModelPointValue(mbp.x, mbp.y, maxIterations);
            pixelIterations[(int) cx++] = count;
        }
        return pixelIterations;
    }
}
