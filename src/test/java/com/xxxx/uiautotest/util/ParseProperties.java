package com.xxxx.uiautotest.util;

import java.io.File;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 说明：
 * 使用Properties加载配置文件，解析指定的key值返回
 * Created by quqing on 15/11/7.
 */
public class ParseProperties {
    private File file;
    private String fileName = "config.properties";

    /**
     * construct with parameter intialize
     *
     * @param filePath whole path and name of config file
     * @throws RuntimeException
     */
    public ParseProperties(String filePath) {
        if (filePath == null) {
            throw new RuntimeException("the parameter can not be null!");
        }
        this.fileName = filePath;
        this.file = new File(System.getProperty("user.dir") + File.separator + fileName);
    }

    /**
     * get specified key in config files
     *
     * @param key the key name to get value
     * @throws RuntimeException
     */
    public String get(String key) {
        String keyValue = null;
        Properties properties = new Properties();
        try {
            if (!file.exists()) {
                throw new FileNotFoundException("the config file [" + fileName + "] not exist!");
            }
            properties.load(new FileInputStream(file));
        } catch (Exception e) {
            properties.clear();
            throw new RuntimeException("load properties failed:" + e.getMessage());
        }
        if (properties.containsKey(key)) {
            keyValue = (String) properties.get(key);
        }
        return keyValue;
    }
}