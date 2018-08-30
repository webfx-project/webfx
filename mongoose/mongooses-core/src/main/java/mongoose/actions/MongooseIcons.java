package mongoose.actions;

import javafx.scene.image.ImageView;
import mongoose.entities.Option;
import naga.fx.util.ImageStore;

/**
 * @author Bruno Salmon
 */
public class MongooseIcons {

    public final static String addIcon16Url = "images/16/actions/add.png";
    public final static String removeIcon16Url = "images/16/actions/remove.png";
    public final static String attendanceIcon16Url = "images/16/itemFamilies/attendance.png";
    public final static String checkedIcon16Url = "images/16/checked.png";
    public final static String uncheckedIcon16Url = "images/16/unchecked.png";
    public final static String spinnerIcon16Url = "images/16/spinner.gif";
    public final static String validationErrorIcon16Url = "images/16/validation/decoration-error.png";
    public final static String validationRequiredIcon16Url = "images/16/validation/required-indicator.png";

    public final static String certificateMonoSvgUrl = "images/svg/mono/certificate.svg";
    public final static String calendarMonoSvgUrl = "images/svg/mono/calendar.svg";
    public final static String priceTagMonoSvgUrl = "images/svg/mono/price-tag.svg";
    public final static String priceTagColorSvgUrl = "images/svg/color/price-tag.svg";

    public final static String addIcon16JsonUrl = getJsonUrl16(addIcon16Url);
    public final static String removeIcon16JsonUrl = getJsonUrl16(removeIcon16Url);
    public final static String attendanceIcon16JsonUrl = getJsonUrl16(attendanceIcon16Url);

    public final static String certificateMonoSvg16JsonUrl = getJsonUrl16(certificateMonoSvgUrl);
    public final static String calendarMonoSvg16JsonUrl = getJsonUrl16(calendarMonoSvgUrl);
    public final static String priceTagMonoSvg16JsonUrl = getJsonUrl16(priceTagMonoSvgUrl);
    public final static String priceTagColorSvg16JsonUrl = getJsonUrl16(priceTagColorSvgUrl);

    public static String getJsonUrl16(String url) {
        return "{url: '" + url + "', width: 16, height: 16}";
    }

    public static ImageView getLanguageIcon32(Object language) {
        return ImageStore.createImageView("images/32/system/lang_" + language + ".png", 32, 32);
    }

    public static ImageView getItemFamilyIcon16(Option option) {
        return ImageStore.createImageView("images/16/itemFamilies/" + option.getItemFamilyCode() + ".png", 16, 16);
    }
}
