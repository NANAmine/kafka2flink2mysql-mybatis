package com.flink.unit;

/**
 * @Author LT-0024
 * @Date 2020/7/13 16:44
 * @Version 1.0
 */
import java.io.File;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 用ConcurrentMap来缓存属性文件的key-value
 */
public class PropertiesUnit {

    private static ResourceLoad loader = ResourceLoad.getInstance();
    private static ConcurrentMap<String, String> configMap = new ConcurrentHashMap<String, String>();
    static String path = System.getProperty("user.dir") + File.separator + "res" + File.separator + "config.properties";
    private static final String DEFAULT_CONFIG_FILE = "/resources/config.properties";

    private static Properties prop = null;

    public static String getStringByKey(String key, String propName) {
        try {
            prop = loader.getPropFromProperties(propName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        key = key.trim();
        if (!configMap.containsKey(key)) {
            if (prop.getProperty(key) != null) {
                configMap.put(key, prop.getProperty(key));
            }
        }
        return configMap.get(key);
    }

    public static String getStringByKey(String key) {
        return getStringByKey(key, DEFAULT_CONFIG_FILE);
    }

    public static Properties getProperties() {
        try {
            return loader.getPropFromProperties(DEFAULT_CONFIG_FILE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
