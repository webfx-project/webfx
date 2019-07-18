package mongoose.client.icons;

import javafx.scene.image.ImageView;
import mongoose.shared.entities.Option;
import webfx.fxkit.extra.util.ImageStore;

/**
 * @author Bruno Salmon
 */
public final class MongooseIcons {

    public final static String addIcon16Url = "images/s16/actions/add.png";
    public final static String copyIcon16Url = "images/s16/actions/copy.png";
    public final static String removeIcon16Url = "images/s16/actions/remove.png";
    public final static String editIcon16Url = "images/s16/actions/edit.png";
    public final static String cancelIcon16Url = "images/s16/actions/cancel.png";
    public final static String confirmIcon16Url = "images/s16/actions/confirm.png";
    public final static String deleteIcon16Url = "images/s16/actions/delete.png";
    public final static String markAsReadIcon16Url = "images/s16/actions/markAsRead.png";
    public final static String willPayIcon16Url = "images/s16/actions/willPay.png";
    public final static String flagIcon16Url = "images/s16/actions/flag.png";
    public final static String passReadyIcon16Url = "images/s16/actions/passReady.png";
    public final static String passUpdatedIcon16Url = "images/s16/actions/passUpdated.png";
    public final static String markAsArrivedIcon16Url = "images/s16/actions/markAsArrived.png";
    public final static String security_knownIcon16Url = "images/s16/security/known.png";
    public final static String security_uncheckedIcon16Url = "images/s16/security/unchecked.png";
    public final static String security_unknownIcon16Url = "images/s16/security/unknown.png";
    public final static String security_verifiedIcon16Url = "images/s16/security/verified.png";
    public final static String mergeIcon16Url = "images/s16/actions/merge.png";
    public final static String letterIcon16Url = "images/s16/letter.png";
    public final static String contraIcon16Url = "images/s16/methods/contra.png";
    public final static String seedMailIcon16Url = "images/s16/actions/seeMail.png";
    public final static String sendMailIcon16Url = "images/s16/actions/sendMail.png";
    public final static String multipleBookingIcon16Url = "images/s16/multipleBookings/redCross.png";
    public final static String cartIcon16Url = "images/s16/cart.png";
    public final static String attendanceIcon16Url = "images/s16/itemFamilies/attendance.png";
    public final static String checkedIcon16Url = "images/s16/checked.png";
    public final static String uncheckedIcon16Url = "images/s16/unchecked.png";
    public final static String spinnerIcon16Url = "images/s16/spinner.gif";
    public final static String lang_englishIcon16Url = "images/s16/system/lang_en.png";
    public final static String lang_frenchIcon16Url = "images/s16/system/lang_fr.png";

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
        return ImageStore.createImageView("images/s32/system/lang_" + language + ".png", 32, 32);
    }

    public static ImageView getItemFamilyIcon16(Option option) {
        return ImageStore.createImageView("images/s16/itemFamilies/" + option.getItemFamilyCode() + ".png", 16, 16);
    }
}
