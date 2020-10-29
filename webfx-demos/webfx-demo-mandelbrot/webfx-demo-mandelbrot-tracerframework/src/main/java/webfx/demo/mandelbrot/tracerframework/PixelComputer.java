package webfx.demo.mandelbrot.tracerframework;

import javafx.scene.paint.Color;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.webworker.spi.base.JavaCodedWebWorkerBase;

/**
 * @author Bruno Salmon
 */
public interface PixelComputer {

    int getPlacesCount();

    int getFramesCount(int placeIndex);

    TracerThumbnail getPlaceThumbnail(int placeIndex);

    void setUsingWebAssembly(boolean usingWebAssembly);

    boolean isUsingWebAssembly();

    long getLastFrameIterations(); // TODO Find a way to remove this Mandelbrot specific method

    void initFrame(int canvasWidth, int canvasHeight, int placeIndex, int frameIndex);

    void endFrame();

    // Pixel color extractor from the line storage (once it has been computed)
    Color getPixelResultColor(int x, int y, Object linePixelResultStorage);

    // API when working in UI thread or standard threads (not workers)

    Object createLinePixelResultStorage(); // Empty storage for the line

    void computeAndStorePixelResult(int x, int y, Object linePixelResultStorage);

    // API when working with workers

    Class<? extends JavaCodedWebWorkerBase> getWorkerClass();

    JsonObject getLineWorkerParameters(int y, boolean firstWorkerCall);

    Object getLinePixelResultStorage(Object workerResult);
}
