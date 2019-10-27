package webfx.extras.type;

/**
 * @author Bruno Salmon
 */
public final class SpecializedTextType extends DerivedType {

    private final SpecializedText specializedText;

    public final static SpecializedTextType IMAGE_URL = new SpecializedTextType(SpecializedText.IMAGE_URL);
    public final static SpecializedTextType HTML = new SpecializedTextType(SpecializedText.HTML);

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
