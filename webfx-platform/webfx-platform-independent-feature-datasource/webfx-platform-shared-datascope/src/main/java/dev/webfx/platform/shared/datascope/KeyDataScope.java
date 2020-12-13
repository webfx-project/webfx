package dev.webfx.platform.shared.datascope;

/**
 * @author Bruno Salmon
 */
public interface KeyDataScope extends DataScope {

    Object getKey();

    @Override
    default boolean intersects(DataScope otherScope) {
        return otherScope instanceof MultiKeyDataScope && ((MultiKeyDataScope) otherScope).intersects(this)
                || otherScope instanceof KeyDataScope && intersects((KeyDataScope) otherScope)
                ;
    }

    boolean intersects(KeyDataScope otherScope);

}
