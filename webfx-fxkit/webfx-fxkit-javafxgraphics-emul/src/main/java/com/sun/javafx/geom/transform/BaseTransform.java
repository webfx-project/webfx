/*
 * Copyright (c) 2009, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;

/**
 *
 */
public abstract class BaseTransform /*implements CanTransformVec3d */{
    public static final BaseTransform IDENTITY_TRANSFORM = new Identity();

/*
    public enum Degree {
        IDENTITY,
        TRANSLATE_2D,
        AFFINE_2D,
        TRANSLATE_3D,
        AFFINE_3D,
    }
*/

    /*
     * This constant is only useful for a cached type field.
     * It indicates that the type has been decached and must be recalculated.
     */
    //protected static final int TYPE_UNKNOWN = -1;

    /**
     * This constant indicates that the transform defined by this object
     * is an identity transform.
     * An identity transform is one in which the output coordinates are
     * always the same as the input coordinates.
     * If this transform is anything other than the identity transform,
     * the type will either be the constant GENERAL_TRANSFORM or a
     * combination of the appropriate flag bits for the various coordinate
     * conversions that this transform performs.
     * @see #TYPE_TRANSLATION
     * @see #TYPE_UNIFORM_SCALE
     * @see #TYPE_GENERAL_SCALE
     * @see #TYPE_FLIP
     * @see #TYPE_QUADRANT_ROTATION
     * @see #TYPE_GENERAL_ROTATION
     * @see #TYPE_GENERAL_TRANSFORM
     * @see #getType
     */
    //public static final int TYPE_IDENTITY = 0;

    /**
     * This flag bit indicates that the transform defined by this object
     * performs a translation in addition to the conversions indicated
     * by other flag bits.
     * A translation moves the coordinates by a constant amount in x
     * and y without changing the length or angle of vectors.
     * @see #TYPE_IDENTITY
     * @see #TYPE_UNIFORM_SCALE
     * @see #TYPE_GENERAL_SCALE
     * @see #TYPE_FLIP
     * @see #TYPE_QUADRANT_ROTATION
     * @see #TYPE_GENERAL_ROTATION
     * @see #TYPE_GENERAL_TRANSFORM
     * @see #getType
     */
    //public static final int TYPE_TRANSLATION = 1;

    /**
     * This flag bit indicates that the transform defined by this object
     * performs a uniform scale in addition to the conversions indicated
     * by other flag bits.
     * A uniform scale multiplies the length of vectors by the same amount
     * in both the x and y directions without changing the angle between
     * vectors.
     * This flag bit is mutually exclusive with the TYPE_GENERAL_SCALE flag.
     * @see #TYPE_IDENTITY
     * @see #TYPE_TRANSLATION
     * @see #TYPE_GENERAL_SCALE
     * @see #TYPE_FLIP
     * @see #TYPE_QUADRANT_ROTATION
     * @see #TYPE_GENERAL_ROTATION
     * @see #TYPE_GENERAL_TRANSFORM
     * @see #getType
     */
    //public static final int TYPE_UNIFORM_SCALE = 2;

    /**
     * This flag bit indicates that the transform defined by this object
     * performs a general scale in addition to the conversions indicated
     * by other flag bits.
     * A general scale multiplies the length of vectors by different
     * amounts in the x and y directions without changing the angle
     * between perpendicular vectors.
     * This flag bit is mutually exclusive with the TYPE_UNIFORM_SCALE flag.
     * @see #TYPE_IDENTITY
     * @see #TYPE_TRANSLATION
     * @see #TYPE_UNIFORM_SCALE
     * @see #TYPE_FLIP
     * @see #TYPE_QUADRANT_ROTATION
     * @see #TYPE_GENERAL_ROTATION
     * @see #TYPE_GENERAL_TRANSFORM
     * @see #getType
     */
    //public static final int TYPE_GENERAL_SCALE = 4;

    /**
     * This constant is a bit mask for any of the scale flag bits.
     * @see #TYPE_UNIFORM_SCALE
     * @see #TYPE_GENERAL_SCALE
     */
    //public static final int TYPE_MASK_SCALE = (TYPE_UNIFORM_SCALE | TYPE_GENERAL_SCALE);

    /**
     * This flag bit indicates that the transform defined by this object
     * performs a mirror image flip about some axis which changes the
     * normally right handed coordinate system into a left handed
     * system in addition to the conversions indicated by other flag bits.
     * A right handed coordinate system is one where the positive X
     * axis rotates counterclockwise to overlay the positive Y axis
     * similar to the direction that the fingers on your right hand
     * curl when you stare end on at your thumb.
     * A left handed coordinate system is one where the positive X
     * axis rotates clockwise to overlay the positive Y axis similar
     * to the direction that the fingers on your left hand curl.
     * There is no mathematical way to determine the angle of the
     * original flipping or mirroring transformation since all angles
     * of flip are identical given an appropriate adjusting rotation.
     * @see #TYPE_IDENTITY
     * @see #TYPE_TRANSLATION
     * @see #TYPE_UNIFORM_SCALE
     * @see #TYPE_GENERAL_SCALE
     * @see #TYPE_QUADRANT_ROTATION
     * @see #TYPE_GENERAL_ROTATION
     * @see #TYPE_GENERAL_TRANSFORM
     * @see #getType
     */
    //public static final int TYPE_FLIP = 64;
    /* NOTE: TYPE_FLIP was added after GENERAL_TRANSFORM was in public
     * circulation and the flag bits could no longer be conveniently
     * renumbered without introducing binary incompatibility in outside
     * code.
     */

    /**
     * This flag bit indicates that the transform defined by this object
     * performs a quadrant rotation by some multiple of 90 degrees in
     * addition to the conversions indicated by other flag bits.
     * A rotation changes the angles of vectors by the same amount
     * regardless of the original direction of the vector and without
     * changing the length of the vector.
     * This flag bit is mutually exclusive with the TYPE_GENERAL_ROTATION flag.
     * @see #TYPE_IDENTITY
     * @see #TYPE_TRANSLATION
     * @see #TYPE_UNIFORM_SCALE
     * @see #TYPE_GENERAL_SCALE
     * @see #TYPE_FLIP
     * @see #TYPE_GENERAL_ROTATION
     * @see #TYPE_GENERAL_TRANSFORM
     * @see #getType
     */
    //public static final int TYPE_QUADRANT_ROTATION = 8;

    /**
     * This flag bit indicates that the transform defined by this object
     * performs a rotation by an arbitrary angle in addition to the
     * conversions indicated by other flag bits.
     * A rotation changes the angles of vectors by the same amount
     * regardless of the original direction of the vector and without
     * changing the length of the vector.
     * This flag bit is mutually exclusive with the
     * TYPE_QUADRANT_ROTATION flag.
     * @see #TYPE_IDENTITY
     * @see #TYPE_TRANSLATION
     * @see #TYPE_UNIFORM_SCALE
     * @see #TYPE_GENERAL_SCALE
     * @see #TYPE_FLIP
     * @see #TYPE_QUADRANT_ROTATION
     * @see #TYPE_GENERAL_TRANSFORM
     * @see #getType
     */
    //public static final int TYPE_GENERAL_ROTATION = 16;

    /**
     * This constant is a bit mask for any of the rotation flag bits.
     * @see #TYPE_QUADRANT_ROTATION
     * @see #TYPE_GENERAL_ROTATION
     */
    //public static final int TYPE_MASK_ROTATION = (TYPE_QUADRANT_ROTATION | TYPE_GENERAL_ROTATION);

    /**
     * This constant indicates that the transform defined by this object
     * performs an arbitrary conversion of the input coordinates.
     * If this transform can be classified by any of the above constants,
     * the type will either be the constant TYPE_IDENTITY or a
     * combination of the appropriate flag bits for the various coordinate
     * conversions that this transform performs.
     * @see #TYPE_IDENTITY
     * @see #TYPE_TRANSLATION
     * @see #TYPE_UNIFORM_SCALE
     * @see #TYPE_GENERAL_SCALE
     * @see #TYPE_FLIP
     * @see #TYPE_QUADRANT_ROTATION
     * @see #TYPE_GENERAL_ROTATION
     * @see #getType
     */
    //public static final int TYPE_GENERAL_TRANSFORM = 32;

/*
    public static final int TYPE_AFFINE2D_MASK =
            (TYPE_TRANSLATION |
             TYPE_UNIFORM_SCALE |
             TYPE_GENERAL_SCALE |
             TYPE_QUADRANT_ROTATION |
             TYPE_GENERAL_ROTATION |
             TYPE_GENERAL_TRANSFORM |
             TYPE_FLIP);

    public static final int TYPE_AFFINE_3D = 128;
*/

    /*
     * Convenience method used internally to throw exceptions when
     * an operation of degree higher than AFFINE_2D is attempted.
     */
/*
    static void degreeError(Degree maxSupported) {
        throw new InternalError("does not support higher than "+
                                maxSupported+" operations");
    }

    public static BaseTransform getInstance(BaseTransform tx) {
        if (tx.isIdentity()) {
            return IDENTITY_TRANSFORM;
        } else if (tx.isTranslateOrIdentity()) {
            throw new UnsupportedOperationException("Translate2D not implemented"); //return new Translate2D(tx);
        } else if (tx.is2D()) {
            throw new UnsupportedOperationException("Affine2D not implemented"); //return new Affine2D(tx);
        }
        throw new UnsupportedOperationException("Affine3D not implemented"); //return new Affine3D(tx);
    }


    public static BaseTransform getInstance(double mxx, double mxy, double mxz, double mxt,
                                            double myx, double myy, double myz, double myt,
                                            double mzx, double mzy, double mzz, double mzt)
    {
        if (mxz == 0.0 && myz == 0.0 &&
            mzx == 0.0 && mzy == 0.0 && mzz == 1.0 && mzt == 0.0)
        {
            return getInstance(mxx, myx, mxy, myy, mxt, myt);
        } else {
            return new Affine3D(mxx, mxy, mxz, mxt,
                                myx, myy, myz, myt,
                                mzx, mzy, mzz, mzt);
        }
    }

    public static BaseTransform getInstance(double mxx, double myx,
                                            double mxy, double myy,
                                            double mxt, double myt)
    {
        if (mxx == 1.0 && myx == 0.0 && mxy == 0.0 && myy == 1.0) {
            return getTranslateInstance(mxt, myt);
        } else {
            return new Affine2D(mxx, myx, mxy, myy, mxt, myt);
        }
    }

    public static BaseTransform getTranslateInstance(double mxt, double myt) {
        if (mxt == 0.0 && myt == 0.0) {
            return IDENTITY_TRANSFORM;
        } else {
            return new Translate2D(mxt, myt);
        }
    }

    public static BaseTransform getScaleInstance(double mxx, double myy) {
        return getInstance(mxx, 0, 0, myy, 0, 0);
    }

    public static BaseTransform getRotateInstance(double theta, double x, double y) {
        Affine2D a = new Affine2D();
        a.setToRotation(theta, x, y);
        return a;
    }

    public abstract Degree getDegree();

    public abstract int getType();
    */


    public abstract boolean isIdentity();
    public abstract boolean isTranslateOrIdentity();
    public abstract boolean is2D();

    /*
    public abstract double getDeterminant();

    public double getMxx() { return 1.0; }
    public double getMxy() { return 0.0; }
    public double getMxz() { return 0.0; }
    public double getMxt() { return 0.0; }
    public double getMyx() { return 0.0; }
    public double getMyy() { return 1.0; }
    public double getMyz() { return 0.0; }
    public double getMyt() { return 0.0; }
    public double getMzx() { return 0.0; }
    public double getMzy() { return 0.0; }
    public double getMzz() { return 1.0; }
    public double getMzt() { return 0.0; }

    public abstract Point2D transform(Point2D src, Point2D dst);
    public abstract Point2D inverseTransform(Point2D src, Point2D dst)
        throws NoninvertibleTransformException;
    public abstract Vec3d transform(Vec3d src, Vec3d dst);
    public abstract Vec3d deltaTransform(Vec3d src, Vec3d dst);
    public abstract Vec3d inverseTransform(Vec3d src, Vec3d dst)
        throws NoninvertibleTransformException;
    public abstract Vec3d inverseDeltaTransform(Vec3d src, Vec3d dst)
        throws NoninvertibleTransformException;
    */
    public abstract void transform(float[] srcPts, int srcOff,
                                   float[] dstPts, int dstOff,
                                   int numPts); /*
    public abstract void transform(double[] srcPts, int srcOff,
                                   double[] dstPts, int dstOff,
                                   int numPts);
    public abstract void transform(float[] srcPts, int srcOff,
                                   double[] dstPts, int dstOff,
                                   int numPts);
    public abstract void transform(double[] srcPts, int srcOff,
                                   float[] dstPts, int dstOff,
                                   int numPts);
    public abstract void deltaTransform(float[] srcPts, int srcOff,
                                        float[] dstPts, int dstOff,
                                        int numPts);
    public abstract void deltaTransform(double[] srcPts, int srcOff,
                                        double[] dstPts, int dstOff,
                                        int numPts);
    public abstract void inverseTransform(float[] srcPts, int srcOff,
                                          float[] dstPts, int dstOff,
                                          int numPts)
        throws NoninvertibleTransformException;
    public abstract void inverseDeltaTransform(float[] srcPts, int srcOff,
                                               float[] dstPts, int dstOff,
                                               int numPts)
        throws NoninvertibleTransformException;
    public abstract void inverseTransform(double[] srcPts, int srcOff,
                                          double[] dstPts, int dstOff,
                                          int numPts)
        throws NoninvertibleTransformException;
*/
    public abstract BaseBounds transform(BaseBounds bounds, BaseBounds result);
/*
    public abstract void transform(Rectangle rect, Rectangle result);
    public abstract BaseBounds inverseTransform(BaseBounds bounds, BaseBounds result)
        throws NoninvertibleTransformException;
    public abstract void inverseTransform(Rectangle rect, Rectangle result)
        throws NoninvertibleTransformException;

    public abstract Shape createTransformedShape(Shape s);

    public abstract void setToIdentity();
    public abstract void setTransform(BaseTransform xform);

    public abstract void invert() throws NoninvertibleTransformException;

    public abstract void restoreTransform(double mxx, double myx,
                                          double mxy, double myy,
                                          double mxt, double myt);

    public abstract void restoreTransform(double mxx, double mxy, double mxz, double mxt,
                                          double myx, double myy, double myz, double myt,
                                          double mzx, double mzy, double mzz, double mzt);

    public abstract BaseTransform deriveWithTranslation(double mxt, double myt);
    public abstract BaseTransform deriveWithTranslation(double mxt, double myt, double mzt);
    public abstract BaseTransform deriveWithScale(double mxx, double myy, double mzz);
    public abstract BaseTransform deriveWithRotation(double theta, double axisX, double axisY, double axisZ);
    public abstract BaseTransform deriveWithPreTranslation(double mxt, double myt);
    public abstract BaseTransform deriveWithConcatenation(double mxx, double myx,
                                                          double mxy, double myy,
                                                          double mxt, double myt);
    public abstract BaseTransform deriveWithConcatenation(
            double mxx, double mxy, double mxz, double mxt,
            double myx, double myy, double myz, double myt,
            double mzx, double mzy, double mzz, double mzt);
    public abstract BaseTransform deriveWithPreConcatenation(BaseTransform transform);
    public abstract BaseTransform deriveWithConcatenation(BaseTransform tx);
    public abstract BaseTransform deriveWithNewTransform(BaseTransform tx);

    public abstract BaseTransform createInverse()
        throws NoninvertibleTransformException;

    public abstract BaseTransform copy();

    @Override
    public int hashCode() {
        if (isIdentity()) return 0;
        long bits = 0;
        bits = bits * 31 + Double.doubleToLongBits(getMzz());
        bits = bits * 31 + Double.doubleToLongBits(getMzy());
        bits = bits * 31 + Double.doubleToLongBits(getMzx());
        bits = bits * 31 + Double.doubleToLongBits(getMyz());
        bits = bits * 31 + Double.doubleToLongBits(getMxz());
        bits = bits * 31 + Double.doubleToLongBits(getMyy());
        bits = bits * 31 + Double.doubleToLongBits(getMyx());
        bits = bits * 31 + Double.doubleToLongBits(getMxy());
        bits = bits * 31 + Double.doubleToLongBits(getMxx());
        bits = bits * 31 + Double.doubleToLongBits(getMzt());
        bits = bits * 31 + Double.doubleToLongBits(getMyt());
        bits = bits * 31 + Double.doubleToLongBits(getMxt());
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BaseTransform)) {
            return false;
        }

        BaseTransform a = (BaseTransform) obj;

        return (getMxx() == a.getMxx() &&
                getMxy() == a.getMxy() &&
                getMxz() == a.getMxz() &&
                getMxt() == a.getMxt() &&
                getMyx() == a.getMyx() &&
                getMyy() == a.getMyy() &&
                getMyz() == a.getMyz() &&
                getMyt() == a.getMyt() &&
                getMzx() == a.getMzx() &&
                getMzy() == a.getMzy() &&
                getMzz() == a.getMzz() &&
                getMzt() == a.getMzt());
    }

    static Point2D makePoint(Point2D src, Point2D dst) {
        if (dst == null) {
            dst = Point2D.create(0, 0);
        }
        return dst;
    }

    static final double EPSILON_ABSOLUTE = 1.0e-5;

    public static boolean almostZero(double a) {
        return ((a < EPSILON_ABSOLUTE) && (a > -EPSILON_ABSOLUTE));
    }

    @Override
    public String toString() {
        return "Matrix: degree " + getDegree() + "\n" +
                getMxx() + ", " + getMxy() + ", " + getMxz() + ", " + getMxt() + "\n" +
                getMyx() + ", " + getMyy() + ", " + getMyz() + ", " + getMyt() + "\n" +
                getMzx() + ", " + getMzy() + ", " + getMzz() + ", " + getMzt() + "\n";
    }
    */
}
