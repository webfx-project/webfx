package dev.webfx.platform.shared.datascope;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Bruno Salmon
 */
public interface MultiKeyDataScope extends DataScope {

    KeyDataScope[] getKeyDataScopes();

    default boolean intersects(DataScope otherScope) {
        if (otherScope instanceof MultiKeyDataScope)
            return intersects((MultiKeyDataScope) otherScope);
        if (otherScope instanceof KeyDataScope)
            return intersects((KeyDataScope) otherScope);
        return false;
    }

    default boolean intersects(MultiKeyDataScope multiKeyDataScope) {
        for (KeyDataScope kds : multiKeyDataScope.getKeyDataScopes())
            if (!intersects(kds))
                return false;
        return true;
    }

    default boolean intersects(KeyDataScope keyDataScope) {
        Object key = keyDataScope.getKey();
        for (KeyDataScope kds : getKeyDataScopes())
            if (key.equals(kds.getKey()))
                return kds.intersects(keyDataScope);
        return true;
    }

    static ArrayList<KeyDataScope> collectKeyDataScopes(ArrayList<KeyDataScope> keyDataScopes, DataScope... dataScopes) {
        for (DataScope dataScope : dataScopes) {
            if (dataScope instanceof KeyDataScope)
                keyDataScopes.add((KeyDataScope) dataScope);
            else if (dataScope instanceof MultiKeyDataScope)
                keyDataScopes.addAll(Arrays.asList(((MultiKeyDataScope) dataScope).getKeyDataScopes()));
            else if (dataScope != null)
                throw new IllegalArgumentException("Cant concat this data scope type " + dataScope.getClass());
        }
        return keyDataScopes;
    }

}
