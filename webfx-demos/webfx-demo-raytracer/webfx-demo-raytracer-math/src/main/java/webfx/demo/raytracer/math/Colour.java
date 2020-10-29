package webfx.demo.raytracer.math;


/**
 *
 * @author Rollie
 */

public class Colour
{
    public double R, G, B;

    public Colour(double r, double g, double b) 
    {
        R = r; G = g; B = b; 
    }

    public static Colour def = create(0, 0, 0);
    
    public static Colour create(double r, double g, double b) 
    { 
        return new Colour(r, g, b); 
    }

    public static Colour multiply(double k, Colour _colour)
    {
        return new Colour((k * _colour.R), (k * _colour.G), (k * _colour.B));
    }
    
    public static Colour multiply(Colour _colourO, Colour _colourT)
    {
        return new Colour((_colourO.R * _colourT.R), (_colourO.G * _colourT.G), (_colourO.B * _colourT.B));
    }

    public static Colour add(Colour _colourO, Colour _colourT)
    {
        return new Colour(_colourO.R + _colourT.R, _colourO.G + _colourT.G, _colourO.B + _colourT.B);
    }
    
    public static Colour average(Colour _colourO, Colour _colourT)
    {
        return new Colour((_colourO.R + _colourT.R) / 2, (_colourO.G + _colourT.G) / 2, (_colourO.B + _colourT.B) / 2);
    }
    
    public static Colour subtract(Colour _colourO, Colour _colourT)
    {
        return new Colour(_colourO.R - _colourT.R, _colourO.G - _colourT.G, _colourO.B - _colourT.B);
    }
    
    public double checkColour(double _colour)
    {
        if (_colour < 0){
           _colour = 0;
        }
        else if (_colour > 1){
            _colour = 1;
        }
        
        return _colour;
    }
}