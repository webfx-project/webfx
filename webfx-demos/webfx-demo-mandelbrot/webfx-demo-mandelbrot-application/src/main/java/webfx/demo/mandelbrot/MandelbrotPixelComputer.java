package webfx.demo.mandelbrot;

import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import webfx.demo.mandelbrot.math.MandelbrotMath;
import webfx.demo.mandelbrot.math.MandelbrotPlace;
import webfx.demo.mandelbrot.math.MandelbrotPlaces;
import webfx.demo.mandelbrot.tracerframework.PixelComputer;
import webfx.demo.mandelbrot.tracerframework.TracerThumbnail;
import webfx.demo.mandelbrot.tracerframework.TracerEngine;
import webfx.demo.mandelbrot.webworker.MandelbrotWebWorker;
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.json.WritableJsonArray;
import webfx.platform.shared.services.json.WritableJsonObject;
import webfx.platform.shared.services.webworker.spi.base.JavaCodedWebWorkerBase;

/**
 * @author Bruno Salmon
 */
final class MandelbrotPixelComputer implements PixelComputer {

    private int canvasWidth, canvasHeight;
    private int placeIndex = -1, frameIndex, thumbnailFrameIndex = -1;
    private boolean usingWebAssembly = true;
    private long currentFrameIterations, lastFrameIterations;

    private int maxIterations;
    private Color mandelbrotColor;
    private Color[] paletteColors;

    MandelbrotPixelComputer() {
    }

    private MandelbrotPixelComputer(int placeIndex, int thumbnailFrameIndex) {
        this.placeIndex = placeIndex;
        this.thumbnailFrameIndex = thumbnailFrameIndex;
    }

    @Override
    public int getPlacesCount() {
        return MandelbrotPlaces.PLACES.length;
    }

    @Override
    public int getFramesCount(int placeIndex) {
        return MandelbrotPlaces.PLACES[placeIndex].lastFrame;
    }

    @Override
    public TracerThumbnail getPlaceThumbnail(int placeIndex) {
        TracerThumbnail placeThumbnail = new TracerThumbnail();
        placeThumbnail.setThumbnailTracer(new TracerEngine(placeThumbnail.getCanvas(), new MandelbrotPixelComputer(placeIndex, MandelbrotPlaces.PLACES[placeIndex].thumbnailFrame)));
        return placeThumbnail;
    }

    public long getLastFrameIterations() {
        return lastFrameIterations;
    }

    public void setUsingWebAssembly(boolean usingWebAssembly) {
        this.usingWebAssembly = usingWebAssembly;
    }

    public boolean isUsingWebAssembly() {
        return usingWebAssembly;
    }

    @Override
    public void initFrame(int canvasWidth, int canvasHeight, int placeIndex, int frameIndex) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        currentFrameIterations = 0;
        if (thumbnailFrameIndex >= 0) {
            placeIndex = this.placeIndex;
            frameIndex = thumbnailFrameIndex;
        }
        MandelbrotMath.init(canvasWidth, canvasHeight, placeIndex, frameIndex);
        if (placeIndex != this.placeIndex || thumbnailFrameIndex >= 0) {
            this.placeIndex = placeIndex;
            MandelbrotPlace place = MandelbrotPlaces.PLACES[placeIndex];
            maxIterations = place.maxIterations;
            mandelbrotColor = Color.valueOf(place.rgbForMandelbrot);
            Palette palette = new Palette(LinearGradient.valueOf(place.linearGradient), place.hsbInterpolation, true);
            int offset = place.paletteMappingOffset;
            int length = place.paletteMappingLength;
            if (length == 0)
                length = maxIterations;
            paletteColors = palette.computePaletteColors(length, offset);
        }
        this.frameIndex = frameIndex;
    }

    @Override
    public void endFrame() {
        lastFrameIterations = currentFrameIterations;
    }

    @Override
    public Color getPixelResultColor(int x, int y, Object linePixelResultStorage) {
        int[] pixelIterations = (int[]) linePixelResultStorage;
        int count = pixelIterations[x];

        Color color;
        if (count == maxIterations)
            color = mandelbrotColor;
        else
            color = paletteColors[count % paletteColors.length];

        return color;
    }

    @Override
    public Object createLinePixelResultStorage() {
        return MandelbrotMath.createLinePixelResultStorage(canvasWidth, null);
    }

    @Override
    public void computeAndStorePixelResult(int x, int y, Object linePixelResultStorage) {
        int[] pixelIterations = (int[]) linePixelResultStorage;
        int count = MandelbrotMath.computeAndStorePixelResult(x, y, pixelIterations);
        currentFrameIterations += count;
    }

    @Override
    public Class<? extends JavaCodedWebWorkerBase> getWorkerClass() {
        return MandelbrotWebWorker.class;
    }

    @Override
    public JsonObject getLineWorkerParameters(int y, boolean firstWorkerCall) {
        WritableJsonObject json = Json.createObject().set("cy", y);
        if (firstWorkerCall) {
            json.set("width", canvasWidth);
            json.set("height", canvasHeight);
            json.set("wasm", usingWebAssembly);
            json.set("placeIndex", placeIndex);
            json.set("frameIndex", frameIndex);
        }
        return json;
    }

    @Override
    public Object getLinePixelResultStorage(Object workerResult) {
        int[] values;
        if (workerResult instanceof int[]) {
            values = (int[]) workerResult;
            for (int value : values) currentFrameIterations += value;
        } else {
            WritableJsonArray array = Json.createArray(workerResult);
            int n = array.size();
            values = new int[n];
            for (int i = 0; i < n; i++) {
                int count = array.getInteger(i);
                values[i] = count;
                currentFrameIterations += count;
            }
        }
        return values;
    }
}
