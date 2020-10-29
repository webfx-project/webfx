package webfx.demo.raytracer.math;

/**
 *
 * @author Rollie
 */

public abstract class AbsObject {
    public Material material;
    public String name;
    
    public abstract Vector normal(Vector _vector);
    public abstract Intersect intersect(Ray ray);
}