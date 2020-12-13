package dev.webfx.platform.shared.datascope;

/**
 * @author Bruno Salmon
 */
final class MultiKeyDataScopeImpl implements MultiKeyDataScope {

    private final KeyDataScope[] keyDataScopes;

    public MultiKeyDataScopeImpl(KeyDataScope... keyDataScopes) {
        this.keyDataScopes = keyDataScopes;
    }

    @Override
    public KeyDataScope[] getKeyDataScopes() {
        return keyDataScopes;
    }
}
