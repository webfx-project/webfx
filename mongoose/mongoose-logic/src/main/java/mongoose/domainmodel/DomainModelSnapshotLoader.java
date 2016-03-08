package mongoose.domainmodel;

import naga.core.jsoncodec.JsonCodecManager;
import naga.core.orm.domainmodel.DomainModel;
import naga.core.orm.domainmodelloader.DomainModelLoader;
import naga.core.spi.json.Json;
import naga.core.spi.json.JsonElement;
import naga.core.spi.platform.client.ClientPlatform;
import naga.core.spi.sql.SqlReadResult;
import naga.core.util.async.Batch;
import naga.core.util.async.Future;
import naga.core.util.compression.string.LZString;

/**
 * @author Bruno Salmon
 */
public class DomainModelSnapshotLoader {

    public static DomainModel loadDomainModelFromSnapshot() {
        try {
            Future<String> text = ClientPlatform.res().getText("mongoose/domainmodel/DomainModelSnapshot.lzb64json");
            String jsonString = LZString.decompressFromBase64(text.result());
            JsonElement json = Json.parse(jsonString);
            Batch<SqlReadResult> snapshotBatch = JsonCodecManager.decodeFromJson(json);
            return new DomainModelLoader(1).generateDomainModel(snapshotBatch);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
