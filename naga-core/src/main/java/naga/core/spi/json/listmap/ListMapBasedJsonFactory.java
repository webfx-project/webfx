package naga.core.spi.json.listmap;

import naga.core.spi.json.JsonElement;
import naga.core.spi.json.JsonFactory;

/**
 * @author Bruno Salmon
 */
public abstract class ListMapBasedJsonFactory<NA, NO> extends JsonFactory<NA, NO> {

    @Override
    protected <T extends JsonElement> T nativeToJsonElement(Object nativeElement) {
        return ListMapUtil.wrap(nativeElement);
    }

}