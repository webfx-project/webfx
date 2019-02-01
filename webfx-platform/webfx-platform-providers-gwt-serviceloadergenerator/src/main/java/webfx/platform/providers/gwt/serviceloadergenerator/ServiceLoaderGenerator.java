package webfx.platform.providers.gwt.serviceloadergenerator;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Bruno Salmon
 */
public final class ServiceLoaderGenerator {

    private final static String SERVICE_LOADER_JAVA_TEMPLATE = "package java.util;\n" +
            "\n" +
            "import java.util.Iterator;\n" +
            "import java.util.logging.Logger;\n" +
            "import webfx.platform.shared.util.function.Factory;\n" +
            "\n" +
            "public class ServiceLoader<S> implements Iterable<S> {\n" +
            "\n" +
            "    public static <S> ServiceLoader<S> load(Class<S> serviceClass) {\n" +
            "        switch (serviceClass.getName()) {\n" +
            "${generatedCasesCode}" +
            "            // SPI NOT FOUND\n" +
            "            default:\n" +
            "               Logger.getLogger(ServiceLoader.class.getName()).warning(\"SPI not found for \" + serviceClass);\n" +
            "               return new ServiceLoader<S>();\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    private final Factory[] factories;\n" +
            "\n" +
            "    public ServiceLoader(Factory... factories) {\n" +
            "        this.factories = factories;\n" +
            "    }\n" +
            "\n" +
            "    public Iterator<S> iterator() {\n" +
            "        return new Iterator<S>() {\n" +
            "            int index = 0;\n" +
            "            @Override\n" +
            "            public boolean hasNext() {\n" +
            "                return index < factories.length;\n" +
            "            }\n" +
            "\n" +
            "            @Override\n" +
            "            public S next() {\n" +
            "                return (S) factories[index++].create();\n" +
            "            }\n" +
            "        };\n" +
            "    }\n" +
            "}";

    private final static String ARRAY_JAVA_TEMPLATE = "package java.lang.reflect;\n" +
            "\n" +
            "import webfx.platform.shared.services.log.Logger;\n" +
            "\n" +
            "public final class Array {\n" +
            "\n" +
            "    public static Object newInstance(Class<?> componentType, int length) throws NegativeArraySizeException {\n" +
            "        switch (componentType.getName()) {\n" +
            "${generatedCasesCode}" +
            "            // TYPE NOT FOUND\n" +
            "            default:\n" +
            "               Logger.log(\"GWT super source Array.newInstance() has no case for type \" + componentType + \", so new Object[] is returned but this may cause a ClassCastException.\");\n" +
            "               return new Object[length];\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "}";

    public static void main(String[] args) {
        generateServiceLoaderEmulationCode();
        log("");
        generateArrayEmulationCode();
    }

    private static void generateArrayEmulationCode() {
        StringBuilder sb = new StringBuilder();
        appendArrayCasesCode(sb);
        writeJavaTemplateSuperSourceFile(ARRAY_JAVA_TEMPLATE, sb, "java/lang/reflect/Array.java");
    }

    private static void appendArrayCasesCode(StringBuilder sb) {
        log("************************************************************************");
        log("***** Generating java.lang.reflect.Array.java super source for GWT *****");
        log("************************************************************************");
        for (String className: loadProviderClassNames("Array.newInstance", true)) {
            sb.append("            case \"").append(className).append("\": return new ").append(className).append("[length];\n");
        }
    }

    private static void generateServiceLoaderEmulationCode() {
        log("************************************************************************");
        log("***** Generating java.util.ServiceLoader.java super source for GWT *****");
        log("************************************************************************");
        StringBuilder sb = new StringBuilder();
        sb.append("            // Single SPI providers\n");
        appendServiceLoaderCasesCode(sb, "exports.spi.single", true);
        sb.append("            // Multiple SPI providers\n");
        appendServiceLoaderCasesCode(sb, "exports.spi.multiple", false);
        writeJavaTemplateSuperSourceFile(SERVICE_LOADER_JAVA_TEMPLATE, sb, "java/util/ServiceLoader.java");
    }

    private static void writeJavaTemplateSuperSourceFile(String javaTemplate, CharSequence generatedCasesCode, String javaFileRelativePath) {
        writeJavaSuperSourceFile(javaTemplate.replace("${generatedCasesCode}", generatedCasesCode), javaFileRelativePath);
    }

    private static void writeJavaSuperSourceFile(String javaFileContent, String javaFileRelativePath) {
        String javaFileAbsolutePath = System.getProperty("user.dir") + "/src/main/resources/super/" + javaFileRelativePath;
        try {
            log(">>> Writing " + javaFileAbsolutePath);
            writeTextFile(javaFileAbsolutePath, javaFileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void appendServiceLoaderCasesCode(StringBuilder sb, String spiPath, boolean single) {
        for (String spiClassName: loadProviderClassNames(spiPath, true)) {
            List<String> providerClassNames = loadProviderClassNames(spiClassName, false);
            if (providerClassNames.isEmpty())
                log("WARNING: No provider found for " + spiClassName);
            else {
                sb.append("            case \"").append(spiClassName).append("\": return new ServiceLoader<S>(");
                for (String providerClassName : providerClassNames) {
                    String firstProvider = providerClassNames.get(0);
                    if (providerClassName != firstProvider)
                        if (single) {
                            log("INFO: Keeping single provider " + firstProvider + ", skipping provider " + spiClassName);
                            continue;
                        } else
                            sb.append(", ");
                    sb.append(providerClassName.replace('$', '.')).append(providerClassName.equals("webfx.platform.shared.services.json.spi.impl.gwt.GwtJsonObject") ? "::create" : "::new");
                }
                sb.append(");\n");
            }
        }
    }

    private static List<String> loadProviderClassNames(String service, boolean webfx) {
        List<String> providers = new ArrayList<>();
        try {
            String prefix = webfx ? "META-INF/webfx/" : "META-INF/services/";
            String fullName = prefix + service;
            Enumeration<URL> configs = ClassLoader.getSystemResources(fullName);
            while (configs.hasMoreElements()) {
                providers.addAll(parse(service, configs.nextElement()));
            }
        } catch (IOException x) {
            fail(service, "Error locating configuration files", x);
        }
        return providers;
    }

    private static List<String> parse(String service, URL u)
            throws ServiceConfigurationError
    {
        InputStream in = null;
        BufferedReader r = null;
        ArrayList<String> names = new ArrayList<>();
        try {
            in = u.openStream();
            r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            int lc = 1;
            while ((lc = parseLine(service, u, r, lc, names)) >= 0);
        } catch (IOException x) {
            fail(service, "Error reading configuration file", x);
        } finally {
            try {
                if (r != null) r.close();
                if (in != null) in.close();
            } catch (IOException y) {
                fail(service, "Error closing configuration file", y);
            }
        }
        return names;
    }


    // Parse a single line from the given configuration file, adding the name
    // on the line to the names list.
    //
    private static int parseLine(String service, URL u, BufferedReader r, int lc,
                          List<String> names)
            throws IOException, ServiceConfigurationError
    {
        String ln = r.readLine();
        if (ln == null) {
            return -1;
        }
        int ci = ln.indexOf('#');
        if (ci >= 0) ln = ln.substring(0, ci);
        ln = ln.trim();
        int n = ln.length();
        if (n != 0) {
            if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0))
                fail(service, u, lc, "Illegal configuration-file syntax");
            int cp = ln.codePointAt(0);
            if (!Character.isJavaIdentifierStart(cp))
                fail(service, u, lc, "Illegal provider-class name: " + ln);
            for (int i = Character.charCount(cp); i < n; i += Character.charCount(cp)) {
                cp = ln.codePointAt(i);
                if (!Character.isJavaIdentifierPart(cp) && (cp != '.'))
                    fail(service, u, lc, "Illegal provider-class name: " + ln);
            }
            if (/*!providers.containsKey(ln) &&*/ !names.contains(ln))
                names.add(ln);
        }
        return lc + 1;
    }

    private static void fail(String service, URL u, int line, String msg)
            throws ServiceConfigurationError
    {
        fail(service, u + ":" + line + ": " + msg);
    }

    private static void fail(String service, String msg)
            throws ServiceConfigurationError
    {
        throw new ServiceConfigurationError(service + ": " + msg);
    }

    private static void fail(String service, String msg, Throwable cause)
            throws ServiceConfigurationError
    {
        throw new ServiceConfigurationError(service + ": " + msg,
                cause);
    }

    private static Path writeTextFile(String pathName, String content) throws IOException {
        Path path = Paths.get(pathName);
        Files.createDirectories(path.getParent()); // Creating all necessary directories
        //Files.write(path, lzJson.getBytes("UTF-8")); // Writing the file
        BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"));
        writer.write(content);
        writer.flush();
        writer.close();
        return path;
    }
    
    private static void log(String message) {
        System.out.println(message);
    }

}
