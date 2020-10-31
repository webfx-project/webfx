package webfx.demo.raytracer.math;

/**
 *
 * @author Rollie
 */

public class Material{
    public java.util.function.Function<Vector, Colour> specular;
    public java.util.function.Function<Vector, Double> reflect;
    public java.util.function.Function<Vector, Colour> diffuse;
    public java.util.function.Function<Vector, Double> refractiveIndex;
    public double transparency;
    public double specularWidth;
    public String name;
}

