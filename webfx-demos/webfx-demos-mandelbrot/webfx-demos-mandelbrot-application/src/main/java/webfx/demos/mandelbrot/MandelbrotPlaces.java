package webfx.demos.mandelbrot;

import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;

import java.math.BigDecimal;

/**
 * @author Bruno Salmon
 */
final class MandelbrotPlaces {

    public static MandelbrotModel[] PLACES = {
            createPlace9(),  createPlace4(),  createPlace6(),
            createPlace1(),  createPlace2(),  createPlace3(),
            createPlace12(), createPlace10(), createPlace13(),
    };

    public static MandelbrotModel createPlace(double xmin, double xmax, double ymin, double ymax, Color rgbForMandelbrot, LinearGradient linearGradient, int colorType, int paletteMappingLength, int paletteMappingOffset, int maxIterations, int thumbnailFrame, int lastFrame, long totalIterations) {
        MandelbrotModel model = new MandelbrotModel();
        model.xmin = BigDecimal.valueOf(xmin);
        model.xmax = BigDecimal.valueOf(xmax);
        model.ymin = BigDecimal.valueOf(ymin);
        model.ymax = BigDecimal.valueOf(ymax);
        model.mandelbrotColor = rgbForMandelbrot;
        model.palette = new Palette(linearGradient, colorType, true);
        model.paletteMapping = new PaletteMapping(paletteMappingLength, paletteMappingOffset);
        model.maxIterations = maxIterations;
        model.thumbnailFrame = thumbnailFrame;
        model.lastFrame = lastFrame;
        model.totalIterations = totalIterations;
        return model;
    }

    public static MandelbrotModel createPlace1() {
        return createPlace(
                0.32559696638634449700,
                0.38386410145977916300,
                0.07356094433491353404,
                0.11726129563998953596,
                Color.BLACK,
                LinearGradient.valueOf("red, red"),
                Palette.COLOR_TYPE_HSB,
                0,
                12,
                3000,
                73, 330, 114422252224L);
    }

    public static MandelbrotModel createPlace2() {
        return createPlace(
                -1.90735282229322523912,
                -1.90735149248092120328,
                0.00198475588464815965,
                0.00198575324387618653,
                Color.BLACK,
                LinearGradient.valueOf("white, rgb(0, 102, 255) 18% , rgb(51, 51, 51) 50%, rgb(229, 0, 178) 85%, white"),
                Palette.COLOR_TYPE_RGB,
                1157,
                565,
                5000,
                126, 252, 76945411145L);
    }

    public static MandelbrotModel createPlace3() {
        return createPlace(
                -1.94102269174208500814,
                -1.94094306644316698850,
                0.00061826443327335312,
                0.00067798340746186786,
                Color.BLACK,
                LinearGradient.valueOf("red, red"),
                Palette.COLOR_TYPE_HSB,
                250,
                1,
                5000,
                118, 253, 148766555135L);
    }

    public static MandelbrotModel createPlace4() {
        return createPlace(
                -1.61761763190675491000,
                2.08212433045193209000,
                -0.83790581376914740202,
                1.93690065799986800202,
                Color.BLACK,
                LinearGradient.valueOf("cyan, black"),
                Palette.COLOR_TYPE_RGB,
                0,
                0,
                250,
                123, 183, 7803669148L);
    }

    public static MandelbrotModel createPlace6() {
        return createPlace(
                -0.39236041546562448760,
                -0.35385390657205185240,
                0.63374498262526876100,
                0.66262486429544823900,
                Color.BLACK,
                LinearGradient.valueOf("red, red"),
                Palette.COLOR_TYPE_HSB,
                2486,
                1360,
                5000,
                100, 298, 62297329304L);
    }

    public static MandelbrotModel createPlace9() {
        return createPlace(
                -3.901275720262803480000005,
                0.363215535788634519999995,
                -1.596029975905122196803487,
                1.602338458831756603196513,
                Color.BLACK,
                LinearGradient.valueOf("white, rgb(255, 204, 0) 15%, rgb(135, 31, 19) 33%, rgb(0, 0, 153) 67%, rgb(0, 102, 255) 85%, white"),
                Palette.COLOR_TYPE_RGB,
                321,
                44,
                1000,
                145, 371, 41588766216L);
    }

    public static MandelbrotModel createPlace10() {
        return createPlace(
                -1.59601390496026451259,
                0.49205851715564928741,
                -0.30991904708784934612,
                1.25613526949908609080,
                Color.BLACK,
                LinearGradient.valueOf("black, red 17%, yellow 83%, white"),
                Palette.COLOR_TYPE_RGB,
                0,
                0,
                10000,
                54, 207, 280002108456L);
    }

    public static MandelbrotModel createPlace12() {
        return createPlace(
                -1.46119936451573935232,
                -1.46119693935171824768,
                -0.00000269122434052013,
                -8.7235132469165E-7,
                Color.BLACK,
                LinearGradient.valueOf("red, yellow 18%, green 29%, #1E90FF 44%, red"),
                Palette.COLOR_TYPE_HSB,
                1092,
                539,
                5000,
                144, 181, 39840341628L);
    }

    public static MandelbrotModel createPlace13() {
        return createPlace(
                0.25211825446288318944,
                0.25249702635864023520,
                -0.00031927191194277593,
                -0.00003519299012499159,
                Color.BLACK,
                LinearGradient.valueOf("white, blue, black, rgb(255, 80, 0), yellow, white"),
                Palette.COLOR_TYPE_RGB,
                1300,
                0,
                50000,
                125, 288, 338885034418L);
    }
}
