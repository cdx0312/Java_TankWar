package com.tank;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by cdxu0 on 2017/8/21.
 */
public class PropertyMgr {
    static Properties properties = new Properties();
    static {
        try {
            properties.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config/tank.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
