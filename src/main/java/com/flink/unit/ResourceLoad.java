package com.flink.unit;

/**
 * @Author LT-0024
 * @Date 2020/7/13 16:42
 * @Version 1.0
 */
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class ResourceLoad {

    private static ResourceLoad loader = new ResourceLoad();
    private static Map<String, Properties> loaderMap = new HashMap<String, Properties>();

    private ResourceLoad() {
    }

    public static ResourceLoad getInstance() {
        return loader;
    }

    public Properties getPropFromProperties(String fileName) throws Exception {

        Properties prop = loaderMap.get(fileName);
        if (prop != null) {
            return prop;
        }
        String filePath = null;
        String configPath = System.getProperty("configurePath");

        if (configPath == null) {
            String path = System.getProperty("user.dir") + File.separator + "conf" + File.separator + "config.properties";
            filePath = path;
        } else {
            filePath = configPath + "/" + fileName;
        }
        prop = new Properties();
        prop.load(new FileInputStream(new File(filePath)));

        loaderMap.put(fileName, prop);
        return prop;
    }
}
