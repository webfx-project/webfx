package webfx.demo.mandelbrot.webassembly.teavm;

import org.teavm.interop.Address;
import org.teavm.interop.Export;
import webfx.demo.mandelbrot.math.MandelbrotMath;

/**
 * @author Bruno Salmon
 */
public final class TeaVmMandelbrotWebAssemblyEntryPoints {

    public static void main(String[] args) {} // Not used but required to declare the main class on TeaVM

    private static int[] outputBuffer;

    @Export(name = "initAndComputeLinePixelIterations")
    public static int initAndComputeLinePixelIterations(int cy, int width, int height, int placeIndex, int frameIndex) {
        outputBuffer = MandelbrotMath.createLinePixelResultStorage(width, outputBuffer);
        MandelbrotMath.init(width, height, placeIndex, frameIndex);
        computeLinePixelIterations(cy);
        return Address.ofData(outputBuffer).toInt();
    }

    @Export(name = "computeLinePixelIterations")
    public static void computeLinePixelIterations(int cy) {
        MandelbrotMath.computeLinePixelIterations(cy, outputBuffer);
    }

}
