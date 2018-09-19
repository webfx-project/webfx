package webfx.platform.gwt.tools;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Bruno Salmon
 */
public class ServiceLoaderGenerator {

    private final static String TEMPLATE = "package java.util;\n" +
            "\n" +
            "import java.util.Iterator;\n" +
            "import webfx.platforms.core.util.function.Factory;\n" +
            "\n" +
            "public class ServiceLoader<S> implements Iterable<S> {\n" +
            "\n" +
            "    public static <S> ServiceLoader<S> load(Class<S> serviceClass) {\n" +
            "${generatedCode}" +
            "        return new ServiceLoader<S>();\n" +
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

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        generateCode(sb, "exports.spi.single", true);
        generateCode(sb, "exports.spi.multiple", false);
        String serviceLoaderJavaCode = TEMPLATE.replace("${generatedCode}", sb);
        String serviceLoaderPath = System.getProperty("user.dir") + "/src/main/resources/super/java/util/ServiceLoader.java";
        System.out.println("Writing " + serviceLoaderPath);
        try {
            writeTextFile(serviceLoaderPath, serviceLoaderJavaCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateCode(StringBuilder sb, String spiPath, boolean single) {
        for (String spiClassName: loadProviderClassNames(spiPath, true)) {
            List<String> providerClassNames = loadProviderClassNames(spiClassName, false);
            if (providerClassNames.isEmpty())
                System.out.println("WARNING: No provider found for " + spiClassName);
            else {
                sb.append("        if (serviceClass.equals(").append(spiClassName).append(".class)) return new ServiceLoader<S>(");
                for (String providerClassName : providerClassNames) {
                    String firstProvider = providerClassNames.get(0);
                    if (providerClassName != firstProvider)
                        if (single) {
                            System.out.println("INFO: Keeping single provider " + firstProvider + ", skipping provider " + spiClassName);
                            continue;
                        } else
                            sb.append(", ");
                    sb.append(providerClassName.replace('$', '.')).append(providerClassName.equals("webfx.platform.gwt.services.json.GwtJsonObject") ? "::create" : "::new");
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
            r = new BufferedReader(new InputStreamReader(in, "utf-8"));
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

}
