package mongoose.entities;

/**
 * @author Bruno Salmon
 */
public enum ItemFamilyType {
    ACCOMMODATION,
    MEALS,
    TEACHING,
    TRANSPORT,
    DIET,
    TRANSLATION,
    UNKNOWN;

    public static ItemFamilyType fromCode(String code) {
        if (code != null) {
            switch (code) {
                case "acco" : return ACCOMMODATION;
                case "meals" : return MEALS;
                case "teach" : return TEACHING;
                case "transp" : return TRANSPORT;
                case "diet" : return DIET;
                case "transl" : return TRANSLATION;
            }
        }
        return UNKNOWN;
    }
}
