package mongoose.application.backend.teavm;

import org.teavm.classlib.ResourceSupplier;
import org.teavm.classlib.ResourceSupplierContext;

/**
 * This class is called by the TeaVM compiler (through the ServiceLoader) and is actually not used at runtime.
 * It lists all resource files that needs to be embed in the javascript client code.
 *
 * @author Bruno Salmon
 */
public class TeaVmCompilerResourceSupplier implements ResourceSupplier {

    @Override
    public String[] supplyResources(ResourceSupplierContext context) {
        return new String[]{"mongoose/domainmodel/DomainModelSnapshot.lzb64json"};
    }
}
