package webfx.tools.emultranslator;

import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonArray;
import webfx.platform.shared.services.json.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Bruno Salmon
 */
public final class EmulTranslator {

    public static void main(String[] args) {
        processJsonFile("/javaemul.json");
    }

    private static void processJsonFile(String file) {
        JsonObject json = parseJson(file);
        JsonObject variables = json.getObject("variables");
        JsonObject modules = json.getObject("modules");
        JsonArray moduleNames = modules.keys();
        for (int i = 0; i < moduleNames.size(); i++) {
            JsonObject module = modules.getObject(moduleNames.getString(i));
            processModule(module, variables);
        }
    }

    private static JsonObject parseJson(String file) {
        return Json.parseObject(readTextFile(file));
    }

    private static String readTextFile(String file) {
        InputStream is = EmulTranslator.class.getResourceAsStream(file);
        return is == null ? null : new Scanner(is, StandardCharsets.UTF_8).useDelimiter("\\A").next();
    }

    private static void processModule(JsonObject module, JsonObject variables) {
        String jdkSrc = resolveVariables(module, "jdk-src", variables, module);
        String jdkModule = resolveVariables(module, "jdk-module", variables, module);
        String webfxSrc = resolveVariables(module, "webfx-src", variables, module);
        JsonObject relocations = module.getObject("relocations");
        JsonArray relocationKeys = relocations == null ? null : relocations.keys();
        JsonArray includes = module.getArray("includes");
        JsonArray excludes = module.getArray("excludes");
        JsonObject replacements = module.getObject("replacements");
        /* Define ZIP File System Properties in HashMap */
        Map<String, String> zip_properties = new HashMap<>();
        /* We want to read an existing ZIP File, so we set this to False */
        zip_properties.put("create", "false");
        /* Specify the encoding as UTF -8 */
        zip_properties.put("encoding", "UTF-8");
        /* Specify the path to the ZIP File that you want to read as a File System */
        URI zip_disk = URI.create("jar:file:/" + jdkSrc.replaceAll(" ", "%20"));
        /* Create ZIP javaFile System */
        try (FileSystem zipfs = FileSystems.newFileSystem(zip_disk, zip_properties)) {
            for (int i = 0; i < includes.size(); i++)
                processInclude(includes.getString(i), zipfs, jdkModule, webfxSrc, relocations, relocationKeys, excludes, replacements);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processInclude(String includePattern, FileSystem zipfs, String jdkModule, String webfxSrc, JsonObject relocations, JsonArray relocationKeys, JsonArray excludes, JsonObject replacements) throws IOException {
        /* Path inside ZIP File */
        int p = includePattern.indexOf('*');
        if (p == -1) {
            Path javaFilePath = zipfs.getPath(/*"/" + jdkModule +*/ "/" + includePattern);
            processJavaFilePath(javaFilePath, includePattern, webfxSrc, relocations, relocationKeys, excludes, replacements);
        } else {
            String searchRoot = includePattern.substring(0, p++);
            boolean recurse = p < includePattern.length() && includePattern.charAt(p) == '*';
            p = searchRoot.lastIndexOf('/');
            if (p != -1)
                searchRoot = searchRoot.substring(0, p);
            Path searchRootPath = zipfs.getPath(/*"/" + jdkModule + */ "/" + searchRoot);
            int relativePathStartIndex = 1; //jdkModule.length() + 2;
            Files.walkFileTree(searchRootPath, new FileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    return dir == searchRootPath || recurse ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    processJavaFilePath(file, file.toString().substring(relativePathStartIndex), webfxSrc, relocations, relocationKeys, excludes, replacements);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.TERMINATE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    private static void processJavaFilePath(Path javaFilePath, String relativePath, String webfxSrc, JsonObject relocations, JsonArray relocationKeys, JsonArray excludes, JsonObject replacements) throws IOException {
        boolean skip = contains(excludes, relativePath);
        System.out.print(javaFilePath);
        if (skip) {
            System.out.println(" (skipped)");
            return;
        }
        String relocatedPath = relativePath;
        boolean hasRelocation = relocations != null && relocationKeys != null;
        if (hasRelocation) {
            for (int j = 0; j < relocationKeys.size(); j++) {
                String relocateSrc = relocationKeys.getString(j);
                String relocateSrcPath = packageToPath(relocateSrc);
                if (relativePath.startsWith(relocateSrcPath)) {
                    String relocateDst = relocations.getString(relocateSrc);
                    relocatedPath = relocatedPath.replace(relocateSrcPath, packageToPath(relocateDst));
                    break;
                }
            }
        }
        Path webfxFilePath = Paths.get(webfxSrc, relocatedPath);
        Files.createDirectories(webfxFilePath.getParent());
        String content = new String(Files.readAllBytes(javaFilePath));
        if (hasRelocation)
            for (int j = 0; j < relocationKeys.size(); j++) {
                String relocateSrc = relocationKeys.getString(j);
                String relocateDst = relocations.getString(relocateSrc);
                content = content.replace(" " + relocateSrc, " " + relocateDst);
                content = content.replace("(" + relocateSrc, "(" + relocateDst);
            }
        int globalReplacementCount = 0;
        if (replacements != null) {
            JsonArray keys = replacements.keys();
            for (int j = 0; j < keys.size(); j++) {
                String pattern = keys.getString(j);
                String replacement = replacements.getString(pattern);
                String before = content;
                if (!pattern.startsWith("regex:"))
                    content = content.replace(pattern, replacement);
                else
                    content = content.replaceAll(pattern.substring(6), replacement);
                if (before != content)
                    globalReplacementCount++;
            }
        }
        int specificReplacementCount = 0;
        String replaceFileContent = readTextFile("/" + relativePath.replace('/', '.') + ".txt");
        if (replaceFileContent != null) {
            replaceFileContent = replaceFileContent.replace("\r", "");
            int i0 = 0;
            String textToken = ">>>TEXT<<<\n";
            String replaceToken = ">>>REPLACE<<<\n";
            while (true) {
                i0 = replaceFileContent.indexOf(textToken, i0);
                if (i0 == -1)
                    break;
                int i1 = replaceFileContent.indexOf(replaceToken, i0);
                if (i1 == -1)
                    break;
                int i2 = replaceFileContent.indexOf(textToken, i1);
                if (i2 == -1)
                    i2 = replaceFileContent.length();
                String text = replaceFileContent.substring(i0 + textToken.length(), i1).trim();
                String replace = replaceFileContent.substring(i1 + replaceToken.length(), i2).trim();
                String newContent = content.replace(text, replace);
                if (newContent.equals(content))
                    System.out.print("\nPattern not found: " + text);
                else
                    specificReplacementCount++;
                content = newContent;
                i0 = i2;
            }
        }
        int replacementCount = globalReplacementCount + specificReplacementCount;
        if (replacementCount > 1 || specificReplacementCount > 0)
            System.out.print(" -> " + replacementCount + " replacements (" + globalReplacementCount + " global + " + specificReplacementCount + " specific)");
        System.out.println();
        Files.write(webfxFilePath, content.getBytes());
    }

    private static String resolveVariables(JsonObject json, String key, JsonObject... variables) {
        return resolveVariables(json.getString(key), variables);
    }

    private static final Pattern variablePattern = Pattern.compile("\\$\\{([^}]+)}");

    private static String resolveVariables(String text, JsonObject... variables) {
        Matcher matcher = variablePattern.matcher(text);
        StringBuffer sb = null;
        int p = 0;
        while (matcher.find()) {
            String group = matcher.group(1);
            String replacement = null;
            for (JsonObject variablesObject : variables) {
                replacement = variablesObject.getString(group);
                if (replacement != null)
                    break;
            }
            if (replacement != null) {
                if (sb == null)
                    sb = new StringBuffer();
                sb.append(text, p, matcher.start()).append(replacement);
                p = matcher.end();
            }
        }
        if (sb != null)
            return sb.append(text.substring(p)).toString();
        return text;
    }

    private static String packageToPath(String p) {
        return p.replace(".", "/");
    }

    private static boolean contains(JsonArray array, String object) {
        if (array != null) {
            for (int i = 0; i < array.size(); i++)
                if (Objects.equals(array.getString(i), object))
                    return true;
        }
        return false;
    }
}
