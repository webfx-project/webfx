package webfx.fxkits.extra.type;

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

}
