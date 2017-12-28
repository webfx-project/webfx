package mongoose.actions;

import javafx.scene.image.ImageView;
import mongoose.entities.Option;
import naga.framework.ui.controls.ImageViewUtil;
import naga.fx.util.ImageStore;

/**
 * @author Bruno Salmon
 */
public class MongooseIcons {

    public final static String addIcon16Url = "images/16/actions/add.png";
    public final static String removeIcon16Url = "images/16/actions/remove.png";
    public final static String certificateIcon16Url = "images/certificate.svg";
    public final static String calendarIcon16Url = "images/calendar.svg";
    public final static String checkedIcon16Url = "images/16/checked.png";
    public final static String uncheckedIcon16Url = "images/16/unchecked.png";
    public final static String spinnerIcon16Url = "images/16/spinner.gif";
    public final static String attendanceIcon16Url = "images/16/itemFamilies/attendance.png";
    public final static String validationErrorIcon16Url = "images/16/validation/decoration-error.png";
    public final static String validationRequiredIcon16Url = "images/16/validation/required-indicator.png";

    public final static String addIcon16JsonUrl = getJsonUrl16(addIcon16Url);
    public final static String removeIcon16JsonUrl = getJsonUrl16(removeIcon16Url);
    public final static String certificateIcon16JsonUrl = getJsonUrl16(certificateIcon16Url);
    public final static String calendarIcon16JsonUrl = getJsonUrl16(calendarIcon16Url);
    public final static String attendanceIcon16JsonUrl = getJsonUrl16(attendanceIcon16Url);

    public static String getJsonUrl16(String url) {
        return "{url: '" + url + "', width: 16, height: 16}";
    }

    public static ImageView getLanguageIcon32(Object language) {
        return ImageStore.createImageView("images/32/system/lang_" + language + ".png");
    }

    public static ImageView getItemFamilyIcon16(Option option) {
        return ImageViewUtil.createImageView("images/16/itemFamilies/" + option.getItemFamilyCode() + ".png");
    }
}
