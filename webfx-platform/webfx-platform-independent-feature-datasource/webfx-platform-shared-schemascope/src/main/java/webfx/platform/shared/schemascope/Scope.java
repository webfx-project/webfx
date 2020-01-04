package webfx.platform.shared.schemascope;

/**
 * @author Bruno Salmon
 */
public interface Scope {

    boolean intersects(Scope scope);

}
