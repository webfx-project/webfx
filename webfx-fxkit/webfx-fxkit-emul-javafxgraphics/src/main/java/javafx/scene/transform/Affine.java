package javafx.scene.transform;

/**
 * @author Bruno Salmon
 */
public class Affine {

    private double xx;
    private double xy;
    //private double xz;
    private double yx;
    private double yy;
    //private double yz;
    //private double zx;
    //private double zy;
    //private double zz;
    private double xt;
    private double yt;
    //private double zt;


    public Affine(double xx, double xy, double yx, double yy, double xt, double yt) {
        this.xx = xx;
        this.xy = xy;
        this.yx = yx;
        this.yy = yy;
        this.xt = xt;
        this.yt = yt;
    }

    public double getMxx() {
        return xx;
    }

    public double getMxy() {
        return xy;
    }

    public double getMyx() {
        return yx;
    }

    public double getMyy() {
        return yy;
    }

    public double getTx() {
        return xt;
    }

    public double getTy() {
        return yt;
    }

    public void append(Affine a) {
        append(a.getMxx(), a.getMxy(), a.getTx(), a.getMyx(), a.getMyy(), a.getTy());
    }

    public void append(double mxx, double mxy, double tx, double myx, double myy, double ty) {
        double m_xx = getMxx();
        double m_xy = getMxy();
        double m_yx = getMyx();
        double m_yy = getMyy();

        xx = (m_xx * mxx + m_xy * myx);
        xy = (m_xx * mxy + m_xy * myy);
        xt = (m_xx * tx + m_xy * ty + getTx());
        yx = (m_yx * mxx + m_yy * myx);
        yy = (m_yx * mxy + m_yy * myy);
        yt = (m_yx * tx + m_yy * ty + getTy());
    }
}
