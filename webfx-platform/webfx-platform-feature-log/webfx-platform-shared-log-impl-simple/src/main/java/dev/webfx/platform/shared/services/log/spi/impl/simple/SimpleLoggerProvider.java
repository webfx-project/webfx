package dev.webfx.platform.shared.services.log.spi.impl.simple;

import dev.webfx.platform.shared.services.log.spi.LoggerProvider;

/**
 * @author Bruno Salmon
 */
public class SimpleLoggerProvider implements LoggerProvider {

/*
    private static ExecutorService threadPool = Executors.newSingleThreadExecutor();

    @Override
    public void log(String message, Throwable error) {
        threadPool.submit(() -> {
            if (message != null)
                System.out.println(message);
            if (error != null)
                error.printStackTrace(System.err);
        });
    }
*/
}
