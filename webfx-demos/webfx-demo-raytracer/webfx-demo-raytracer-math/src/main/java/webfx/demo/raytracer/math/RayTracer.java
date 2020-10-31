package webfx.demo.raytracer.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * /EXTERNAL SOURCES/ located at lines 108 and 110
 * 
 * /TODO/:
 * Finish refraction
 *  ray splitting for reflection, and refraction
 *  total internal reflection
 *  Fresnel reflectivity
 * Add normal mapping
 * Add displacement mapping
 * Add Fresnel reflectivity
 * -Add super sampling, adaptive sampling, and stochastic sampling modes
 * Add Depth of Field
 * Add area light sources
 * Add diffuse inter-reflection
 * Add surface and object caustics
 * Add transparency
 * Add soft shadows
 * 
 * @author Rollie
 */

public class RayTracer {

    private final static int amount = 4;

    public static Colour renderPixel(double x, double y, View view, int bmpWidth, int bmpHeight) {
        // WebFx addition: adjusting view port in case it's not square to preserve the ratio (otherwise spheres appear elliptical)
        if (bmpWidth != bmpHeight) {
            if (bmpWidth < bmpHeight) {
                x += 0.5 * (bmpHeight - bmpWidth);
                bmpWidth = bmpHeight;
            } else {
                y += 0.5 * (bmpWidth - bmpHeight);
                bmpHeight = bmpWidth;
            }
        }

        Vector _direction = getLocation(x, y, view.camera, bmpWidth, bmpHeight);

        return trace(
                new Ray() {{
                    begin = view.camera.position;
                    direction = _direction;
                }},
                view,
                0);
    }

    public static Colour trace(Ray ray, View view, int amount)
    {
        Iterable<Intersect> intersections = intersections(ray, view);
        Intersect intersect = null;

        if(intersections.iterator().hasNext()){
            intersect = intersections.iterator().next();
        }

        if (intersect == null){
            return Colour.def;
        }
        else{
            return getShade(intersect, view, amount);
        }
    }

    private static Iterable<Intersect> intersections(Ray ray, View view)
    {
        ArrayList<Intersect> rayIntersections = new ArrayList<>();
        
        for (AbsObject i : view.objects){
            Intersect temp = i.intersect(ray);
            if (temp != null){
                rayIntersections.add(temp);
            }
        }
        
        rayIntersections.sort(Comparator.comparingDouble((Intersect x) -> x.distance));
        
        Iterable<Intersect> temp;
        Intersect[] tempL = new Intersect[rayIntersections.toArray().length];
        
        for (int i = 0; i < tempL.length; i++){
            tempL[i] = (Intersect)rayIntersections.toArray()[i];
        }
        
        temp = Arrays.asList(tempL);
        
        return temp;
    }
    
    private static double shadowTest(Ray ray, View view)
    {
        Iterable<Intersect> intersections = intersections(ray, view);
        Intersect intersect = null;
        
        if(intersections.iterator().hasNext()){
            intersect = intersections.iterator().next();   
        }
        
        if (intersect == null){
            return 0;
        }
        else{
            return intersect.distance;
        }
    }
    
    private static Colour getShade(Intersect intersect, View view, int _amount)
    {
        Vector direction = intersect.ray.direction;
        Vector position = Vector.add(Vector.multiply(intersect.distance, intersect.ray.direction), intersect.ray.begin);
        Vector normal = intersect.object.normal(position);
        //http://graphics.stanford.edu/courses/cs148-10-summer/docs/2006--degreve--reflection_refraction.pdf - SOURCE for reflectDirection vector + equation
        Vector reflectDirection = Vector.subtract(direction, Vector.multiply(2 * Vector.dotProduct(normal, direction), normal));
        //http://web.cse.ohio-state.edu/~hwshen/681/Site/Slides_files/reflection_refraction.pdf - SOURCE for refractDirection vector + equation
        //nr -> object refractive index
        //(nr(Norm . direction) - sqrt(1 - nr^2(1 - (normal . direction)^2)) * Normal) - normal*direction
        Vector refractDirection =
                Vector.subtract(
                        //Vector.multiply(intersect.object.material.refractiveIndex.apply(position), Vector.dotProduct(normal, direction)),
                        Vector.multiply(
                                (
                                    (intersect.object.material.refractiveIndex.apply(position) * Vector.dotProduct(normal, direction))- 
                                    Math.sqrt(1 - Math.pow(intersect.object.material.refractiveIndex.apply(position), 2) * (1 - Math.pow(Vector.dotProduct(normal, direction), 2)))
                                ),
                                normal
                            ),
                        Vector.multiply(intersect.object.material.refractiveIndex.apply(position), direction)
                );
        
        Colour outColor = Colour.def;
        
        outColor = Colour.add(outColor, getColour(intersect.object, position, normal, reflectDirection, view));
        //outColor = Colour.add(outColor, getColour(intersect.object, position, normal, refractDirection, view));
        //outColor = Colour.add(outColor, Colour.average(getColour(intersect.object, position, normal, reflectDirection, view), getColour(intersect.object, position, normal, refractDirection, view)));
        
        if (_amount < amount){
            Colour reflect;// = outColor;
            Colour refract; // = outColor;
            // Vector.add(position, Vector.multiply(0.001, refractDirection)) -> epsilon correction
            //return Colour.add(outColor, getReflectColor(intersect.object, Vector.add(position, Vector.multiply(0.001, reflectDirection)), reflectDirection, view, _amount));
            //return Colour.add(outColor, getRefractColor(intersect.object, Vector.add(position, Vector.multiply(0.001, refractDirection)), refractDirection, view, _amount));
            
            //if (intersect.object.material.refractiveIndex.apply(normal) < 1){
                //Vector refractDir = Vector.normal(refract());
                //refract = getRefractColor(intersect.object, Vector.add(normal, Vector.multiply(0.001, refractDirection)), refractDirection, view, _amount);
            //}
            
            reflect = getReflectColor(intersect.object, Vector.add(position, Vector.multiply(0.001, reflectDirection)), reflectDirection, view, _amount);
            refract = getRefractColor(intersect.object, Vector.add(normal, Vector.multiply(0.001, refractDirection)), refractDirection, view, _amount);

            reflect = Colour.multiply((1 - intersect.object.material.transparency), reflect);
            refract = Colour.multiply((intersect.object.material.transparency), refract);
            
            return Colour.add(outColor, Colour.add(reflect, refract));
        }
        else
        {
            return outColor;
        }
    }
    
    private static Colour getReflectColor(AbsObject object, Vector position, Vector rayDirection, View view, int _amount)
    {
        return Colour.multiply(object.material.reflect.apply(position), trace(
                new Ray() {{ 
                    begin = position;
                    direction = rayDirection;
                }},
                view,
                _amount + 1
        ));
    }
    
    //NOT FINISHED
    private static Colour getRefractColor(AbsObject object, Vector normal, Vector rayDirection, View view, int _amount){
        //double tempFresnel = fresnel(rayDirection, normal, object, 1.5);
        
        return Colour.multiply(object.material.refractiveIndex.apply(normal), trace(
                new Ray() {{ 
                    begin = normal;
                    direction = rayDirection;
                }},
                view,
                _amount + 1
        ));
    }

    private static Colour getColour(AbsObject object, Vector position, Vector normal, Vector rayDirection, View view)
    {
        Colour outColor = Colour.create(0, 0, 0);
        
        for (LightSource light : view.lightSource)
        {
            Vector lightDistance = Vector.subtract(light.position, position);
            Vector normalLD = Vector.normal(lightDistance);
            
            double cleanIntersection = shadowTest(
                    new Ray() {{
                        begin = position;
                        direction = normalLD;
                    }}
                , view);
            
            boolean notInShadow = ((cleanIntersection > Vector.magn(lightDistance)) || (cleanIntersection == 0));
            
            if (notInShadow)
            {
                double illumination = Vector.dotProduct(normalLD, normal);
                Colour lightColor = illumination > 0 ? Colour.multiply(illumination, light.color) : Colour.create(0, 0, 0);
                double specular = Vector.dotProduct(normalLD, Vector.normal(rayDirection));
                Colour specularColor = specular > 0 ? Colour.multiply(Math.pow(specular, object.material.specularWidth), light.color) : Colour.create(0, 0, 0);
                outColor = Colour.add(outColor, Colour.add(Colour.multiply(object.material.diffuse.apply(position), lightColor), Colour.multiply(object.material.specular.apply(position), specularColor)));
            }
        }
        
        return outColor;
    }

    public static Vector getLocation(double x, double y, Camera camera, int bmpWidth, int bmpHeight)
    {
        return Vector.normal(Vector.add(camera.front, Vector.add(Vector.multiply(centerHorizontal(x, bmpWidth), camera.right), Vector.multiply(centerVertical(y, bmpHeight), camera.above))));
    }
    
    private static double centerHorizontal(double horizontal, int bmpWidth)
    {
        return (horizontal - (bmpWidth / 2.0)) / (2.0 * bmpWidth);
    }
    
    private static double centerVertical(double vertical, int bmpHeight)
    {
        return -(vertical - (bmpHeight / 2.0)) / (2.0 * bmpHeight);
    }
}