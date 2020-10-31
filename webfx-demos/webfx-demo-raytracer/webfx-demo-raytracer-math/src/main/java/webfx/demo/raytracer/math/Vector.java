package webfx.demo.raytracer.math;

/**
 *
 * @author Rollie
 */

public class Vector {
    public double X;
    public double Y;
    public double Z;

    public Vector(double x, double y, double z) 
    {
        X = x; Y = y; Z = z;
    }
    
    public static Vector create(double x, double y, double z) 
    { 
        return new Vector(x, y, z); 
    }
    
    public static Vector normal(Vector _vector)
    {
        double magn = magn(_vector);
        double mult = magn == 0 ? Double.POSITIVE_INFINITY : 1 / magn;
        
        return multiply(mult, _vector);
    }
    
    public static boolean isEqual(Vector _vectorO, Vector _vectorT)
    {
        return (_vectorO.X == _vectorT.X) && (_vectorO.Y == _vectorT.Y) && (_vectorO.Z == _vectorT.Z);
    }
    
    public static Vector multiply(double k, Vector _vector)
    {
        return new Vector((_vector.X * k), (_vector.Y * k), (_vector.Z * k));
    }
    
    public static Vector multiply(Vector _vectorO, Vector _vectorT)
    {
        return new Vector((_vectorO.X * _vectorT.Y), (_vectorO.Y * _vectorT.Y), (_vectorO.Z * _vectorT.Z));
    }
    
    public static Vector multiply(double k, double i)
    {
        return new Vector((k * i), (k * i), (k * i));
    }
    
    public static Vector subtract(Vector _vectorO, Vector _vectorT)
    {
        return new Vector(_vectorO.X - _vectorT.X, _vectorO.Y - _vectorT.Y, _vectorO.Z - _vectorT.Z);
    }
    
    public static Vector add(Vector _vectorO, Vector _vectorT)
    {
        return new Vector(_vectorO.X + _vectorT.X, _vectorO.Y + _vectorT.Y, _vectorO.Z + _vectorT.Z);
    }
    
    public static double dotProduct(Vector _vectorO, Vector _vectorT)
    {
        return (_vectorO.X * _vectorT.X) + (_vectorO.Y * _vectorT.Y) + (_vectorO.Z * _vectorT.Z);
    }
    
    public static Vector crossP(Vector _vectorO, Vector _vectorT)
    {
        return new Vector(((_vectorO.Y * _vectorT.Z) - (_vectorO.Z * _vectorT.Y)), ((_vectorO.Z * _vectorT.X) - (_vectorO.X * _vectorT.Z)), ((_vectorO.X * _vectorT.Y) - (_vectorO.Y * _vectorT.X)));
    }
    
    public static double magn(Vector _vector)
    {
        return Math.sqrt(dotProduct(_vector, _vector)); 
    }
}
