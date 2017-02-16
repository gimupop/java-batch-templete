package jp.co.yanagawa.bachTemplete.common;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import static jp.co.yanagawa.bachTemplete.util.FileUtil.getResourcePath;

public class Config {
    private static Properties prop = new Properties();
    static {
    	load("config.xml");
    }
    
    private static void load(String file) {
        try {
            prop.loadFromXML(new FileInputStream(new File(getResourcePath(file))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String propertyName) {
        return prop.getProperty(propertyName);
    }

    public static int getInt(String propertyName, int defaultValue) {
        try {
            return Integer.parseInt(prop.getProperty(propertyName));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}