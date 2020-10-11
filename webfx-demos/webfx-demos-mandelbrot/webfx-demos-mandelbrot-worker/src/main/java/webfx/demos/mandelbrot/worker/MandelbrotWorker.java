package webfx.demos.mandelbrot.worker;

import webfx.demos.mandelbrot.computation.MandelbrotComputation;
import webfx.demos.mandelbrot.computation.MandelbrotPoint;
import webfx.demos.mandelbrot.computation.MandelbrotViewport;
import webfx.platform.client.services.webassembly.Import;
import webfx.platform.client.services.webassembly.WebAssembly;
import webfx.platform.client.services.webassembly.WebAssemblyInstance;
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.worker.spi.abstrimpl.JavaApplicationWorker;
import webfx.platform.shared.util.async.Future;

import java.math.BigDecimal;

/**
 * @author Bruno Salmon
 */
public class MandelbrotWorker extends JavaApplicationWorker {

    private MandelbrotViewport viewport;
    private double width, height;
    private int maxIterations;
    private int[] pixelIterations;
    private WebAssemblyInstance webAssemblyInstance;

    @Override
    public void onLoaded() {
        if (WebAssembly.isSupported()) {
            Future<WebAssemblyInstance> future = WebAssembly.load("classes.wasm", new Import("mandelbrot", "setPixelIteration", this::setPixelIteration));
            future.setHandler(ar -> webAssemblyInstance = ar.result());
        }
        setOnMessageHandler(data -> {
            MandelbrotComputation.init();
            JsonObject json = Json.createObject(data);
            double cy = json.getDouble("cy");
            if (viewport == null) {
                viewport = new MandelbrotViewport();
                viewport.xmin = BigDecimal.valueOf(json.getDouble("xmin"));
                viewport.xmax = BigDecimal.valueOf(json.getDouble("xmax"));
                viewport.ymin = BigDecimal.valueOf(json.getDouble("ymin"));
                viewport.ymax = BigDecimal.valueOf(json.getDouble("ymax"));
                width = json.getDouble("width");
                height = json.getDouble("height");
                maxIterations = json.getInteger("maxIterations");
            }
            if (webAssemblyInstance != null) {
                pixelIterations = new int[(int) width];
                webAssemblyInstance.call("computeLinePixelIterations", cy, width, height, viewport.xmin.doubleValue(), viewport.xmax.doubleValue(), viewport.ymin.doubleValue(), viewport.ymax.doubleValue(), maxIterations);
            } else {
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
