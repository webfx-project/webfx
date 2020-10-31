package webfx.demo.mandelbrot.math;

/**
 * @author Bruno Salmon
 */
public final class MandelbrotPlaces {

    public static MandelbrotPlace[] PLACES = {
            createPlace1(), createPlace2(), createPlace3(),
            createPlace4(), createPlace5(), createPlace6(),
            createPlace7(), createPlace8(), createPlace9(),
    };

    private static MandelbrotPlace createPlace1() {
        return new MandelbrotPlace(
                -3.901275720262803480000005,
                0.363215535788634519999995,
                -1.596029975905122196803487,
                1.602338458831756603196513,
                "black",
                "white, rgb(255, 204, 0) 15%, rgb(135, 31, 19) 33%, rgb(0, 0, 153) 67%, rgb(0, 102, 255) 85%, white",
                false,
                321,
                44,
                1000,
                240, 371, 41588766216L);
    }

    private static MandelbrotPlace createPlace2() {
        return new MandelbrotPlace(
                -1.61761763190675491000,
                2.08212433045193209000,
                -0.83790581376914740202,
                1.93690065799986800202,
                "black",
                "blueviolet, orange, brown, black",
                false,
                0,
                0,
                250,
                123, 183, 7803669148L);
    }

    private static MandelbrotPlace createPlace3() {
        return new MandelbrotPlace(
                -0.39236041546562448760,
                -0.35385390657205185240,
                0.63374498262526876100,
                0.66262486429544823900,
                "black",
                "red, red",
                true,
                2486,
                1200,
                5000,
                100, 298, 62297329304L);
    }

    private static MandelbrotPlace createPlace4() {
        return new MandelbrotPlace(
                0.32559696638634449700,
                0.38386410145977916300,
                0.07356094433491353404,
                0.11726129563998953596,
                "black",
                "red, red",
                true,
                0,
                12,
                3000,
                73, 330, 114422252224L);
    }

    private static MandelbrotPlace createPlace5() {
        return new MandelbrotPlace(
                -1.90735282229322523912,
                -1.90735149248092120328,
                0.00198475588464815965,
                0.00198575324387618653,
                "black",
                "white, rgb(0, 102, 255) 18% , rgb(51, 51, 51) 50%, rgb(229, 0, 178) 85%, white",
                false,
                1157,
                565,
                5000,
                126, 252, 76945411145L);
    }

    private static MandelbrotPlace createPlace6() {
        return new MandelbrotPlace(
                -1.46119936451573935232,
                -1.46119693935171824768,
                -0.00000269122434052013,
                -8.7235132469165E-7,
                "black",
                "red, yellow 18%, green 29%, #1E90FF 44%, red",
                true,
                1092,
                539,
                5000,
                144, 181, 39840341628L);
    }

    private static MandelbrotPlace createPlace7() {
        return new MandelbrotPlace(
                -1.59601390496026451259,
                0.49205851715564928741,
                -0.30991904708784934612,
                1.25613526949908609080,
                "black",
                "black, red 17%, yellow 83%, white",
                false,
                0,
                0,
                10000,
                54, 207, 280002108456L);
    }

    private static MandelbrotPlace createPlace8() {
        return new MandelbrotPlace(
                -1.94102269174208500814,
                -1.94094306644316698850,
                0.00061826443327335312,
                0.00067798340746186786,
                "black",
                "red, red",
                true,
                250,
                1,
                5000,
                118, 253, 148766555135L);
    }

    private static MandelbrotPlace createPlace9() {
        return new MandelbrotPlace(
                0.25211825446288318944,
                0.25249702635864023520,
                -0.00031927191194277593,
                -0.00003519299012499159,
                "black",
                "red, orange, green, blue, yellow, white",
                false,
                1300,
                0,
                50000,
                125, 288, 338885034418L);
    }
}
