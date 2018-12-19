package webfx.platform.shared.services.log;

import webfx.platform.shared.util.serviceloader.SingleServiceLoader;
import webfx.platform.shared.services.log.spi.LoggerProvider;

/**
 * @author Bruno Salmon
 */
public final class Logger {

    public static LoggerProvider getProvider() {
        return SingleServiceLoader.loadService(LoggerProvider.class);
    }

    public static void log(Object message) {
        getProvider().log(message);
    }

    public static void log(String message) {
        getProvider().log(message);
    }

    public static void log(String message, Throwable error) {
        getProvider().log(message, error);
    }

    public static String captureStackTrace(Throwable exception) {
/* Not GWT compatible
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        return sw.toString();
*/
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : exception.getStackTrace())
            sb.append(sb.length() == 0 ? "" : "\n").append("    ").append(element);
        return sb.toString();
    }
}
