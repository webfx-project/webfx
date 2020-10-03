package webfx.demos.mandelbrot.mandelbrotmodel;

import javafx.scene.paint.Color;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Bruno Salmon
 */
public class MandelbrotPlaces {

    public static MandelbrotModel[] PLACES = {
            createPlace9(),
            createPlace4(),
            createPlace6(),
            createPlace1(),
            createPlace2(),
            createPlace3(),
            createPlace12(),
            //createPlace0(),
            //createPlace5(),
            //createPlace7(),
            //createPlace8(),
            createPlace10(),
            //createPlace11(),
            createPlace13(),
            //createPlace14(),
    };

    public static MandelbrotModel createPlace(double xmin, double xmax, double ymin, double ymax, Color rgbForMandelbrot, int paletteColorType, Double[] divisionPoints, float[][] colorComponents, int paletteMappingLength, int paletteMappingOffset, int maxIterations, String thumbnailUrl) {
        MandelbrotModel model = new MandelbrotModel();
        model.xmin = BigDecimal.valueOf(xmin);
        model.xmax = BigDecimal.valueOf(xmax);
        model.ymin = BigDecimal.valueOf(ymin);
        model.ymax = BigDecimal.valueOf(ymax);
        model.mandelbrotColor = rgbForMandelbrot;
        model.palette = new Palette(paletteColorType, true, new ArrayList<>(Arrays.asList(divisionPoints)), new ArrayList<>(Arrays.asList(colorComponents)));
        model.paletteMapping = new PaletteMapping(paletteMappingLength, paletteMappingOffset);
        model.maxIterations = maxIterations;
        model.thumbnailUrl = thumbnailUrl == null ? "place1.png" : thumbnailUrl;
        return model;
    }

    public static MandelbrotModel createPlace0() {
        return createPlace(
                -2.33316666666666657415,
                1.00016666666666657415,
                -1.25,
                1.25,
                Color.BLACK,
                Palette.COLOR_TYPE_HSB,
                new Double[]{0d, 1.0},
                new float[][]{
                        {0, 1, 1},
                        {1, 1, 1}
                },
                0,
                0,
                100,
                null);
    }

    public static MandelbrotModel createPlace1() { // 330 frames
        return createPlace(
                0.32559696638634449700,
                0.38386410145977916300,
                0.07356094433491353404,
                0.11726129563998953596,
                Color.BLACK,
                Palette.COLOR_TYPE_HSB,
                new Double[]{0d, 1.0},
                new float[][]{
                        {0, 1, 1},
                        {1, 1, 1}
                },
                0,
                12,
                3000,
                "place1.png");
    }

    public static MandelbrotModel createPlace2() { // 252 frames
        return createPlace(
                -1.90735282229322523912,
                -1.90735149248092120328,
                0.00198475588464815965,
                0.00198575324387618653,
                Color.BLACK,
                Palette.COLOR_TYPE_RGB,
                new Double[]{0d, 0.18604651162790697, 0.5, 0.8462, 1.0},
                new float[][]{
                        {1, 1, 1},
                        {0, 0.4f, 1},
                        {0.2f, 0.2f, 0.2f},
                        {0.9f, 0, 0.7f},
                        {1, 1, 1}
                },
                1157,
                565,
                5000,
                "place2.png");
    }

    public static MandelbrotModel createPlace3() { // 233 frames
        return createPlace(
                -1.94102269174208500814,
                -1.94094306644316698850,
                0.00061826443327335312,
                0.00067798340746186786,
                Color.BLACK,
                Palette.COLOR_TYPE_HSB,
                new Double[]{0d, 1.0},
                new float[][]{
                        {0, 1, 1},
                        {1, 1, 1}
                },
                250,
                1,
                5000,
                "place3.png");
    }

    public static MandelbrotModel createPlace4() { // 183 frames
        return createPlace(
                -1.61761763190675491000,
                2.08212433045193209000,
                -0.83790581376914740202,
                1.93690065799986800202,
                Color.BLACK,
                Palette.COLOR_TYPE_RGB,
                new Double[]{0d, 1.0},
                new float[][]{
                        {0, 1, 1},
                        {0, 0, 0}
                },
                0,
                0,
                250,
                "place4.png");
    }

    public static MandelbrotModel createPlace5() {
        return createPlace(
                -0.7515104166666667,
                -0.7384895833333335,
                -0.1179174885797342,
                -0.1081518635797342,
                Color.BLACK,
                Palette.COLOR_TYPE_RGB,
                new Double[]{0d, 1.0},
                new float[][]{
                        {1, 0, 0},
                        {0, 1, 1}
                },
                250,
                0,
                2500,
                null);
    }

    public static MandelbrotModel createPlace6() {  // 298 frames
        return createPlace(
                -0.39236041546562448760,
                -0.35385390657205185240,
                0.63374498262526876100,
                0.66262486429544823900,
                Color.BLACK,
                Palette.COLOR_TYPE_HSB,
                new Double[]{0.0, 1.0},
                new float[][]{
                        {0, 1, 1},
                        {1, 1, 1}
                },
                2486,
                1360,
                5000,
                "place6.png");
    }

    public static MandelbrotModel createPlace7() {
        return createPlace(
                -1.7878063835085548723667371150,
                -1.7878063835085548715923738750,
                0.0000568907551128812097350920,
                0.0000568907551128817905075220,
                Color.BLACK,
                Palette.COLOR_TYPE_RGB,
                new Double[]{0.0, 0.15, 0.33, 0.67, 0.85, 1.0},
                new float[][]{
                        {1, 1, 1},
                        {1, 0.8f, 0},
                        {0.53f, 0.12f, 0.075f},
                        {0, 0, 0.6f},
                        {0, 0.4f, 1},
                        {1, 1, 1}
                },
                136,
                344,
                500,
                null);
    }

    public static MandelbrotModel createPlace8() {
        return createPlace(
                0.2860151227315265,
                0.2860183016459146,
                0.011536302756629499,
                0.011538686942420542,
                Color.BLACK,
                Palette.COLOR_TYPE_HSB,
                new Double[]{0.0, 1.0},
                new float[][]{
                        {0, 1, 1},
                        {1, 1, 1}
                },
                250,
                0,
                2500,
                null);
    }

    public static MandelbrotModel createPlace9() { // 371 frames
        return createPlace(
                -3.901275720262803480000005,
                0.363215535788634519999995,
                -1.596029975905122196803487,
                1.602338458831756603196513,
                Color.BLACK,
                Palette.COLOR_TYPE_RGB,
                new Double[]{0.0, 0.15, 0.33, 0.67, 0.85, 1.0},
                new float[][]{
                        {1, 1, 1},
                        {1, 0.8f, 0},
                        {0.53f, 0.12f, 0.075f},
                        {0, 0, 0.6f},
                        {0, 0.4f, 1},
                        {1, 1, 1}
                },
                321,
                44,
                1000,
                "place9.png");
    }

    public static MandelbrotModel createPlace10() {
        return createPlace(
                -1.59601390496026451259,
                0.49205851715564928741,
                -0.30991904708784934612,
                1.25613526949908609080,
                Color.BLACK,
                Palette.COLOR_TYPE_RGB,
                new Double[]{0.0, 0.17, 0.83, 1.0},
                new float[][]{
                        {0, 0, 0},
                        {1, 0, 0},
                        {1, 1, 0},
                        {1, 1, 1}
                },
                0,
                0,
                10000,
                "place10.png");
    }

    public static MandelbrotModel createPlace11() {
        return createPlace(
                0.25989953330036834367,
                0.25989963871014069873,
                0.00161257537296835110,
                0.00161265443029761740,
                Color.BLACK,
                Palette.COLOR_TYPE_HSB,
                new Double[]{0.0, 1.0},
                new float[][]{
                        {0, 1, 1},
                        {1, 1, 1}
                },
                250,
                50,
                1500,
                null);
    }

    public static MandelbrotModel createPlace12() {
        return createPlace(
                -1.46119936451573935232,
                -1.46119693935171824768,
                -0.00000269122434052013,
                -8.7235132469165E-7,
                Color.BLACK,
                Palette.COLOR_TYPE_HSB,
                new Double[]{0.0, 0.18604651162790697, 0.295169946332737, 0.44364937388193204, 1.0},
                new float[][]{
                        {0, 1, 1},
                        {0.18604651f, 1, 1},
                        {0.33333334f, 1, 1},
                        {0.6010734f, 1, 1},
                        {1, 1, 1}
                },
                1092,
                539,
                5000,
                "place12.png");
    }

    public static MandelbrotModel createPlace13() {
        return createPlace(
                0.25211825446288318944,
                0.25249702635864023520,
                -0.00031927191194277593,
                -0.00003519299012499159,
                Color.BLACK,
                Palette.COLOR_TYPE_RGB,
                new Double[]{0.0, 0.2, 0.4, 0.6, 0.8, 1.0},
                new float[][]{
                        {1, 1, 1},
                        {0, 0, 1},
                        {0, 0, 0},
                        {1, 80f/255f, 0},
                        {1, 1, 0},
                        {1, 1, 1},
                },
                1300,
                0,
                50000,
                "place13.png");
    }

    public static MandelbrotModel createPlace14() {
        return createPlace(
                -1.76858217260546868,
                -1.76856842422656246,
                0.000731570617187499964,
                0.000741881901367187494,
                Color.BLACK,
                Palette.COLOR_TYPE_RGB,
                new Double[]{0.0, 0.1429, 0.2587, 0.4286, 0.5714, 0.7143, 0.8571, 1.0},
                new float[][]{
                        {0, 0, 0},
                        {1, 0, 0},
                        {1, 1, 0},
                        {0, 1, 0},
                        {0, 1, 1},
                        {0, 0, 1},
                        {1, 0, 1},
                        {0, 0, 0},
                },
                896,
                860,
                50000,
                null);
    }
}
