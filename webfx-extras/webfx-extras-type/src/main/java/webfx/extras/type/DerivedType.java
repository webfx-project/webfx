package webfx.extras.type;

/**
 * @author Bruno Salmon
 */

public class DerivedType implements Type {

    private final String name;
    private final PrimType primType;

    public DerivedType(String name, PrimType primType) {
        this.name = name;
        this.primType = primType;
    }

    public DerivedType(String name, Type type) {
        this(name, Types.getPrimType(type));
    }

    public String getName() {
        return name;
    }

    public PrimType getPrimType() {
        return primType;
    }

    public static DerivedType create(String name, Type type) {
        if (Types.isStringType(type) && name.equalsIgnoreCase("html"))
            return SpecializedTextType.HTML;
        return new DerivedType(name, type);
    }

}
