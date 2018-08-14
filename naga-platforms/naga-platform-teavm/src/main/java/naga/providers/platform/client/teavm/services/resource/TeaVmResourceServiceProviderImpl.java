package naga.providers.platform.client.teavm.services.resource;

import naga.platform.services.resource.spi.ResourceServiceProvider;
import naga.util.async.Future;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Bruno Salmon
 */
public final class TeaVmResourceServiceProviderImpl implements ResourceServiceProvider {

    @Override
    public Future<String> getText(String resourcePath) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = bufferedReader.readLine();
            while(line != null){
                sb.append(line);sb.append('\n');
                line = bufferedReader.readLine();
            }
            return Future.succeededFuture(sb.toString());
        } catch (Exception e) {
            return Future.failedFuture(e);
        }
    }
}
