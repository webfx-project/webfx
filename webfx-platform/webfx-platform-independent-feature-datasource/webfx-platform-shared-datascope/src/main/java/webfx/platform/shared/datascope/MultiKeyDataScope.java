package webfx.platform.shared.datascope;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Bruno Salmon
 */
public final class MultiKeyDataScope implements DataScope {

    private final KeyDataScope[] keyDataScopes;

    public MultiKeyDataScope(KeyDataScope... keyDataScopes) {
        this.keyDataScopes = keyDataScopes;
    }

    @Override
    public boolean intersects(DataScope dataScope) {
        if (dataScope instanceof MultiKeyDataScope)
            return intersects((MultiKeyDataScope) dataScope);
        if (dataScope instanceof KeyDataScope)
            return intersects((KeyDataScope) dataScope);
        return false;
    }

    public boolean intersects(MultiKeyDataScope multiKeyDataScope) {
        for (KeyDataScope kds : multiKeyDataScope.keyDataScopes)
            if (!intersects(kds))
                return false;
        return true;
    }

    public boolean intersects(KeyDataScope keyDataScope) {
        Object key = keyDataScope.getKey();
        for (KeyDataScope kds : keyDataScopes)
            if (key.equals(kds.getKey()))
                return kds.intersects(keyDataScope);
        return true;
    }

    static ArrayList<KeyDataScope> collectKeyDataScopes(ArrayList<KeyDataScope> keyDataScopes, DataScope... dataScopes) {
        for (DataScope dataScope : dataScopes) {
            if (dataScope instanceof KeyDataScope)
                keyDataScopes.add((KeyDataScope) dataScope);
            else if (dataScope instanceof MultiKeyDataScope)
                keyDataScopes.addAll(Arrays.asList(((MultiKeyDataScope) dataScope).keyDataScopes));
            else if (dataScope != null)
                throw new IllegalArgumentException("Cant concat this data scope type " + dataScope.getClass());
        }
        return keyDataScopes;
    }

}
