package naga.commons.type;

/**
 * @author Bruno Salmon
 */
public class SpecializedTextType extends DerivedType {

    private final SpecializedText specializedText;

    public SpecializedTextType(SpecializedText specializedText) {
        this(specializedText.name(), specializedText);
    }

    public SpecializedTextType(String name, SpecializedText specializedText) {
        super(name, PrimType.STRING);
        this.specializedText = specializedText;
    }

    public SpecializedText getSpecializedText() {
        return specializedText;
    }
}
