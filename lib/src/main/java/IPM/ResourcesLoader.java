package IPM;

import java.io.InputStream;
import java.net.URL;

/**
 * Utility class which manages the access to this project's assets. Helps
 * keeping the assets files structure organized.
 */
public class ResourcesLoader {

    // Private constructor to prevent instantiation
    private ResourcesLoader() {
    }

    /**
     * Loads a resource from the classpath and returns its URL.
     *
     * @param path the path of the resource to load
     * @return the URL of the loaded resource
     */
    public static URL loadURL(String path) {
        return ResourcesLoader.class.getResource(path);
    }

    /**
     * Loads a resource from the classpath and returns its string representation.
     *
     * @param path the path of the resource to load
     * @return the string representation of the loaded resource
     */
    public static String load(String path) {
        return loadURL(path).toString();
    }

    /**
     * Loads a resource from the classpath and returns its input stream.
     *
     * @param name the name of the resource to load
     * @return the input stream of the loaded resource
     */
    public static InputStream loadStream(String name) {
        return ResourcesLoader.class.getResourceAsStream(name);
    }
}
