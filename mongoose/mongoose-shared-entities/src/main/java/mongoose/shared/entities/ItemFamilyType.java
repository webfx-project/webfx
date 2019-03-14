package mongoose.shared.entities;

/**
 * @author Bruno Salmon
 */
public enum ItemFamilyType {
    TEACHING,
    TRANSLATION,
    ACCOMMODATION,
    MEALS,
    DIET,
    PARKING,
    TRANSPORT,
    TAX,
    UNKNOWN;

    public static ItemFamilyType fromCode(String code) {
        if (code != null) {
            switch (code) {
                case "teach" : return TEACHING;
                case "transl" : return TRANSLATION;
                case "acco" : return ACCOMMODATION;
                case "meals" : return MEALS;
                case "diet" : return DIET;
                case "park" : return PARKING;
                case "transp" : return TRANSPORT;
                case "tax" : return TAX;
            }
        }
        return UNKNOWN;
    }
}
