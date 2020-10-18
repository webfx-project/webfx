package webfx.demos.mandelbrot.worker.teavm;

import org.teavm.interop.Address;
import org.teavm.interop.Export;
import org.teavm.interop.Import;
import webfx.demos.mandelbrot.computation.MandelbrotComputation;
import webfx.demos.mandelbrot.computation.MandelbrotPoint;
import webfx.demos.mandelbrot.computation.MandelbrotViewport;
import webfx.platform.teavm.wasm.TeaVmWasmMemoryBufferReader;
import webfx.platform.teavm.wasm.WasmUtil;

import java.math.BigDecimal;

/**
 * @author Bruno Salmon
 */
public class TeaVmMandelbrotWebAssembly {

    public static void main(String[] args) {} // Not used but required to declare the main class on TeaVM

    private static final byte[] inputBuffer = new byte[1024];
    private static int[] outputBuffer;

    private static final MandelbrotViewport viewport = new MandelbrotViewport();
    private static int width, height;
    private static int maxIterations;

    @Export(name = "getInputBufferAddress")
    public static int getInputBufferAddress() {
        return Address.ofData(inputBuffer).toInt();
    }

    @Export(name = "initAndComputeLinePixelIterations")
    public static int initAndComputeLinePixelIterations(int cy, int width, int height, int maxIterations) {
        TeaVmWasmMemoryBufferReader inputBufferReader = WasmUtil.getMemoryBufferReader(Address.ofData(inputBuffer));
        String sxmin = inputBufferReader.readString();
        String sxmax = inputBufferReader.readString();
        String symin = inputBufferReader.readString();
        String symax = inputBufferReader.readString();
        //System.out.println("cy = " + cy + ", width = " + width + ", height = " + height + ", sxmin = " + sxmin + ", sxmax = " + sxmax + ", symin = " + symin + ", symax = " + symax + ", maxIterations = " + maxIterations);
        viewport.xmin = new BigDecimal(sxmin);
        viewport.xmax = new BigDecimal(sxmax);
        viewport.ymin = new BigDecimal(symin);
        viewport.ymax = new BigDecimal(symax);
        TeaVmMandelbrotWebAssembly.width = width;
        TeaVmMandelbrotWebAssembly.height = height;
        TeaVmMandelbrotWebAssembly.maxIterations = maxIterations;
        if (outputBuffer == null || outputBuffer.length != width)
            outputBuffer = new int[width];
        MandelbrotComputation.init();
        computeLinePixelIterations(cy);
        return Address.ofData(outputBuffer).toInt();
    }

    @Export(name = "computeLinePixelIterations")
    public static void computeLinePixelIterations(int cy) {
        int cx = 0;
        while (cx < width) {
            // Passing the canvas pixel for the pixel color computation
            MandelbrotPoint mbp = MandelbrotComputation.convertCanvasPixelToModelPoint(cx, cy, width, height, viewport);
            int count = MandelbrotComputation.computeModelPointValue(mbp.x, mbp.y, maxIterations);
            outputBuffer[cx++] = count; // First method: storing the count in the output buffer
            //setPixelIteration(cx++, count); // Second method: passing each count with a JS callback
        }
    }

    @Import(module = "mandelbrot", name = "setPixelIteration")
    private static native void setPixelIteration(int cx, int count);

}
