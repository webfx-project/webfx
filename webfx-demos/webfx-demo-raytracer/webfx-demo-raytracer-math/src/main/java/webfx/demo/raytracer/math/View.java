package webfx.demo.raytracer.math;

import java.util.Arrays;

/**
 *
 * @author Rollie
 */

public abstract class View {
    public AbsObject[] objects;
    public Camera camera;
    public LightSource[] lightSource;
    
    public Iterable<Intersect> intersect(Ray ray){
        Iterable<Intersect> temp;
        Intersect[] tempL = new Intersect[objects.length];
        
        for (int i = 0; i < tempL.length; i++){
            tempL[i] = objects[i].intersect(ray);
        }
        
        temp = Arrays.asList(tempL);
        
        return temp;
    }
}
