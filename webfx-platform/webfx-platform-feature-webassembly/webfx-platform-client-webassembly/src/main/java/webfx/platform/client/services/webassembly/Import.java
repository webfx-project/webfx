package webfx.platform.client.services.webassembly;

/**
 * @author Bruno Salmon
 */
public class Import {

    private final String moduleName;
    private final String functionName;
    private final Fn method;

    public Import(String moduleName, String functionName, Fn method) {
        this.moduleName = moduleName;
        this.functionName = functionName;
        this.method = method;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public Fn getMethod() {
        return method;
    }

    public interface Fn {
        void handle(int x, int count);
    }
}
