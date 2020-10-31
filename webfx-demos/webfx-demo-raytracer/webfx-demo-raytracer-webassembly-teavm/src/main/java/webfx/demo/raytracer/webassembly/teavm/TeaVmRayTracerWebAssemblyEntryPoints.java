package webfx.demo.raytracer.webassembly.teavm;

import org.teavm.interop.Address;
import org.teavm.interop.Export;
import webfx.demo.raytracer.math.*;


/**
 * @author Bruno Salmon
 */
public class TeaVmRayTracerWebAssemblyEntryPoints {

    public static void main(String[] args) {} // Not used but required to declare the main class on TeaVM

    private static byte[] rgbs;

    @Export(name = "initAndComputeLinePixelRGBs")
    public static int initAndComputeLinePixelRGBs(int cy, int width, int height, int placeIndex, int frameIndex) {
        System.out.println("cy = " + cy + ", width = " + width + ", height = " + height);
        rgbs = RayTracerMath.createLinePixelResultStorage(width, rgbs);
        RayTracerMath.init(width, height, placeIndex, frameIndex);
        return computeLinePixelRGBs(cy);
    }

    @Export(name = "computeLinePixelRGBs")
    public static int computeLinePixelRGBs(int cy) {
        RayTracerMath.computeLinePixelRGBs(cy, rgbs);
        return Address.ofData(rgbs).toInt();
    }
}
