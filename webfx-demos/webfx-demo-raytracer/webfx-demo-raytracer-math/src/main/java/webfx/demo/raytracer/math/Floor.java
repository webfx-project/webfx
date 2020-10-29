package webfx.demo.raytracer.math;

/**
 *
 * @author Rollie
 */

public class Floor extends AbsObject
{
    public double displacement;
    public Vector objectPosition;

    @Override
    public Vector normal(Vector position)
    {
        return objectPosition;
    }

    @Override
    public Intersect intersect(Ray _ray)
    {
        double _vectorD = Vector.dotProduct(objectPosition, _ray.direction);
        double _distance;

        if (_vectorD <= 0)
        {
            _distance = (displacement + Vector.dotProduct(objectPosition, _ray.begin)) / (-_vectorD);
            Floor floor = this;
            
            return new Intersect(){{
                object = floor;
                ray = _ray;
                distance = _distance;
            }};
        }
        else
        {
            return null;
        }
    }
}
