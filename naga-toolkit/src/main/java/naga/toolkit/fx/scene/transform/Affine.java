package naga.toolkit.fx.scene.transform;

/**
 * @author Bruno Salmon
 */
public interface Affine {

    double getMxx();

    double getMxy();

    double getMyx();

    double getMyy();

    //double getMyz();

    //double getMzx();

    //double getMzy();

    //double getMzz();

    double getTx();

    double getTy();

    //double getTz();

    default void append(Affine a) {
        append(a.getMxx(), a.getMxy(), a.getTx(), a.getMyx(), a.getMyy(), a.getTy());
    }

    void append(double mxx, double mxy, double tx,
                double myx, double myy, double ty);

/*
    void append(double mxx, double mxy, double mxz, double tx,
                double myx, double myy, double myz, double ty,
                double mzx, double mzy, double mzz, double tz);
*/
}
