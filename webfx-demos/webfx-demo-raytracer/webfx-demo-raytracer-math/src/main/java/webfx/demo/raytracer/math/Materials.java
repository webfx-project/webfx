package webfx.demo.raytracer.math;

/**
 *
 * @author Rollie
 */

public class Materials {
    public static Material[] materials;
    
    public static Material Default = 
        new Material(){{
            name = "Default";
            refractiveIndex = (Vector position) -> 0.0;
            reflect = (Vector position)  -> 0.25;
            diffuse = (Vector position)  -> Colour.create(0.25, 0.25, 0.25);
            specular = (Vector position) -> Colour.create(0.25, 0.25, 0.25);
            specularWidth = 0;
            transparency = 0.1;
        }};
    
    public static Material Floor = 
        new Material(){{
            name = "Floor";
            //reflect = position -> 0.15;
            refractiveIndex = (Vector position) -> 0.0;
            reflect = (Vector position)  -> 0.65;
            diffuse = (Vector position)  -> Colour.create(0.25, 0.25, 0.25);
            specular = (Vector position) -> Colour.create(0.5, 0.5, 0.5);
            specularWidth = 15;
            transparency = 0.0;
        }};
    
    public static Material Mirror = 
        new Material(){{
            name = "Mirror";
            refractiveIndex = (Vector position) -> 0.0;
            reflect = (Vector position)  -> 1.0;
            diffuse = (Vector position)  -> Colour.create(1.0, 1.0, 1.0);
            specular = (Vector position) -> Colour.create(0.75, 0.75, 0.75);
            specularWidth = 25;
            transparency = 0.1;
        }};
    
    public static Material Glass = 
        new Material(){{
            name = "Glass";
            refractiveIndex = (Vector position) -> 1.0;
            reflect = (Vector position)  -> 0.0;
            diffuse = (Vector position)  -> Colour.create(0.0, 0.0, 0.0);
            specular = (Vector position) -> Colour.create(0.5, 0.5, 0.5);
            specularWidth = 100;
            transparency = 0.95;
        }};
}