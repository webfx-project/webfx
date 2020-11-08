/*
 * Copyright (c) 2015 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Credits to Digital-7 font
 * Sizenko Alexander
 * Style-7
 * http://www.styleseven.com
 */

package eu.hansolo.enzo.fonts;

import javafx.scene.text.Font;


/**
 * User: hansolo
 * Date: 22.10.13
 * Time: 10:27
 */
public final class Fonts {
    private static final String BEBAS_NEUE_NAME;
    private static final String DIGITAL_NAME;
    private static final String DIGITAL_READOUT_NAME;
    private static final String DIGITAL_READOUT_BOLD_NAME;
    private static final String DIN_FUN_NAME;
    private static final String ELEKTRA_NAME;        
    private static final String ROBOTO_THIN_NAME;
    private static final String ROBOTO_LIGHT_NAME;
    private static final String ROBOTO_REGULAR_NAME;
    private static final String ROBOTO_MEDIUM_NAME;
    private static final String ROBOTO_BOLD_NAME;
    private static final String LATO_HAIRLINE_NAME;
    private static final String LATO_LIGHT_NAME;
    private static final String LATO_REGULAR_NAME;
    private static final String LATO_BOLD_NAME;
    private static final String LATO_BLACK_NAME;

    private static String bebasNeueName;
    private static String digitalName;
    private static String digitalReadoutName;
    private static String digitalReadoutBoldName;
    private static String dinFunName;
    private static String elektraName;    
    
    private static String robotoThinName;
    private static String robotoLightName;
    private static String robotoRegularName;
    private static String robotoMediumName;
    private static String robotoBoldName;

    private static String latoHairlineName;
    private static String latoLightName;
    private static String latoRegularName;
    private static String latoBoldName;
    private static String latoBlackName;
    

    static {
/*
        try {
            bebasNeueName          = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/enzo/fonts/bebasneue.otf"), 10).getName();
            digitalName            = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/enzo/fonts/digital.ttf"), 10).getName();
            digitalReadoutName     = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/enzo/fonts/digitalreadout.ttf"), 10).getName();
            digitalReadoutBoldName = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/enzo/fonts/digitalreadoutb.ttf"), 10).getName();
            dinFunName             = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/enzo/fonts/din.otf"), 10).getName();
            elektraName            = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/enzo/fonts/elektra.ttf"), 10).getName();
            robotoThinName         = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/enzo/fonts/Roboto-Thin.ttf"), 10).getName();
            robotoLightName        = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/enzo/fonts/Roboto-Light.ttf"), 10).getName();
            robotoRegularName      = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/enzo/fonts/Roboto-Regular.ttf"), 10).getName();
            robotoMediumName       = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/enzo/fonts/Roboto-Medium.ttf"), 10).getName();
            robotoBoldName         = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/enzo/fonts/Roboto-Bold.ttf"), 10).getName();
            latoHairlineName       = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/enzo/fonts/Lato-Hai.ttf"), 10).getName();
            latoLightName          = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/enzo/fonts/Lato-Lig.ttf"), 10).getName();
            latoRegularName        = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/enzo/fonts/Lato-Reg.ttf"), 10).getName();
            latoBoldName           = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/enzo/fonts/Lato-Bol.ttf"), 10).getName();
            latoBlackName          = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/enzo/fonts/Lato-Bla.ttf"), 10).getName();
        } catch (Exception exception) { }
*/
        BEBAS_NEUE_NAME           = bebasNeueName;
        DIGITAL_NAME              = digitalName;
        DIGITAL_READOUT_NAME      = digitalReadoutName;
        DIGITAL_READOUT_BOLD_NAME = digitalReadoutBoldName;
        DIN_FUN_NAME              = dinFunName;
        ELEKTRA_NAME              = elektraName;                
        ROBOTO_THIN_NAME          = robotoThinName;
        ROBOTO_LIGHT_NAME         = robotoLightName;
        ROBOTO_REGULAR_NAME       = robotoRegularName;
        ROBOTO_MEDIUM_NAME        = robotoMediumName;
        ROBOTO_BOLD_NAME          = robotoBoldName;
        LATO_HAIRLINE_NAME        = latoHairlineName;
        LATO_LIGHT_NAME           = latoLightName;
        LATO_REGULAR_NAME         = latoRegularName;
        LATO_BOLD_NAME            = latoBoldName;
        LATO_BLACK_NAME           = latoBlackName;
    }

    
    // ******************** Methods *******************************************
    public static Font bebasNeue(final double SIZE) {
        return new Font(BEBAS_NEUE_NAME, SIZE);
    }

    public static Font digital(final double SIZE) {
        return new Font(DIGITAL_NAME, SIZE);
    }

    public static Font digitalReadout(final double SIZE) {
        return new Font(DIGITAL_READOUT_NAME, SIZE);
    }
    public static Font digitalReadoutBold(final double SIZE) {
        return new Font(DIGITAL_READOUT_BOLD_NAME, SIZE);
    }

    public static Font dinFun(final double SIZE) {
        return new Font(DIN_FUN_NAME, SIZE);
    }

    public static Font elektra(final double SIZE) {
        return new Font(ELEKTRA_NAME, SIZE);
    }

    public static Font robotoThin(final double SIZE) {
        return new Font(ROBOTO_THIN_NAME, SIZE);
    }
    public static Font robotoLight(final double SIZE) {
        return new Font(ROBOTO_LIGHT_NAME, SIZE);
    }
    public static Font robotoRegular(final double SIZE) {
        return new Font(ROBOTO_REGULAR_NAME, SIZE);
    }
    public static Font robotoMedium(final double SIZE) {
        return new Font(ROBOTO_MEDIUM_NAME, SIZE);
    }
    public static Font robotoBold(final double SIZE) {
        return new Font(ROBOTO_BOLD_NAME, SIZE);
    }

    public static Font latoHairline(final double SIZE) {
        return new Font(LATO_HAIRLINE_NAME, SIZE);
    }
    public static Font latoLight(final double SIZE) {
        return new Font(LATO_LIGHT_NAME, SIZE);
    }
    public static Font latoRegular(final double SIZE) {
        return new Font(LATO_REGULAR_NAME, SIZE);
    }
    public static Font latoBold(final double SIZE) {
        return new Font(LATO_BOLD_NAME, SIZE);
    }
    public static Font latoBlack(final double SIZE) {
        return new Font(LATO_BLACK_NAME, SIZE);
    }
}
