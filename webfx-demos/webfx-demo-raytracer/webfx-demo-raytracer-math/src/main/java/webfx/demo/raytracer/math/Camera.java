package webfx.demo.raytracer.math;

/**
 *
 * @author Rollie
 */

public class Camera {
    public Vector position;
    public Vector above;
    public Vector front;
    public Vector right;
    
    public static Camera create(Vector target, Vector _position){
        Vector tempM, _front, _bottom, tempCFD, tempNormCFD, _right, tempCFR, tempNormCFR, _above;
        tempM = Vector.subtract(target, _position);
        _front = Vector.normal(tempM);
        
        _bottom = new Vector(0, -1, 0);
        
        tempCFD = Vector.crossP(_front, _bottom);
        tempNormCFD = Vector.normal(tempCFD);
        _right = Vector.multiply(2.0, tempNormCFD);
        
        tempCFR = Vector.crossP(_front, _right);
        tempNormCFR = Vector.normal(tempCFR);
        _above = Vector.multiply(2.0, tempNormCFR);
        
        return new Camera() {{
            this.position = _position; 
            this.front = _front;
            this.above = _above;
            this.right = _right;
        }};
    }
    
    public static Camera create(Vector target, Vector _position, double x, double y){
        Vector tempM, _front, _bottom, tempCFD, tempNormCFD, _right, tempCFR, tempNormCFR, _above;
        double ratio = y / x;
        
        tempM = Vector.subtract(target, _position);
        _front = Vector.normal(tempM);
        
        _bottom = new Vector(0, -1, 0);
        
        tempCFD = Vector.crossP(_front, _bottom);
        tempNormCFD = Vector.normal(tempCFD);
        _right = Vector.multiply(2.0, tempNormCFD);
        
        tempCFR = Vector.crossP(_front, _right);
        tempNormCFR = Vector.normal(tempCFR);
        _above = Vector.multiply(2.0 * ratio, tempNormCFR);
        
        return new Camera() {{
            this.position = _position; 
            this.front = _front;
            this.above = _above;
            this.right = _right;
        }};
    }
}
