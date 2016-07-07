package naga.commons.type;

/*
 * @author Bruno Salmon
 */

public class DerivedType implements Type {

    private final String name;
    private final PrimType primType;
    private final boolean displayAsImage;

    public DerivedType(String name, PrimType primType, boolean displayAsImage) {
        this.name = name;
        this.primType = primType;
        this.displayAsImage = displayAsImage;
    }

    public DerivedType(String name, Type type, boolean displayAsImage) {
        this(name, Types.getPrimType(type), displayAsImage);
    }

    public String getName() {
        return name;
    }

    public PrimType getPrimType() {
        return primType;
    }

    public boolean isDisplayAsImage() {
        return displayAsImage;
    }
}
