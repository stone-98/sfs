package github.stone.sfs.utils;

import github.stone.sfs.common.Constants;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

/**
 * @author stone
 * @date 2021/10/22
 */
@Slf4j
public class PropertyUtil {

    private static Properties properties;

    private static Object lock = new Object();

    private static void readPropertiesFile() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        String rpcConfigPath = null;
        if (url != null) {
            rpcConfigPath = url.getPath() + Constants.FILE_NAME;
        }
        try (InputStreamReader inputStreamReader = new InputStreamReader(
                new FileInputStream(rpcConfigPath), StandardCharsets.UTF_8)) {
            properties = new Properties();
            properties.load(inputStreamReader);
        } catch (IOException e) {
            log.error("Occur exception when read properties file [{}]", Constants.FILE_NAME, e);
        }
    }

    public static String getProperty(String key) {
        if (properties == null) {
            synchronized (lock) {
                if (properties == null) {
                    readPropertiesFile();
                }
            }
        }
        return properties.get(key) == null ? null : properties.get(key).toString();
    }
}
