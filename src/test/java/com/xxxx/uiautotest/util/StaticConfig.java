package com.xxxx.uiautotest.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yuyilong on 15/8/26.
 */
public class StaticConfig {
    private static final ParseProperties CONFIG = new ParseProperties(
            "src/config.properties");

    public static String IdentifyID_Image = "8888";
    public static String IdentifyID_Message = "888888";
    public static int retrycount = Integer.parseInt(CONFIG.get("retrycount"));
    public static String miDevices = CONFIG.get("miDevices");

    public static String[] getMiDevices() {
        String miPhones = StaticConfig.miDevices;
        if (null != miPhones) {
            if (miPhones.indexOf(",") != -1) {
                return miPhones.split(",");
            } else {
                return new String[]{miPhones};
            }
        } else {
            return null;
        }
    }

    public static String[] getMiUdid() {
        String[] miUdid;
        String[] miDevices = StaticConfig.getMiDevices();
        if (null != miDevices) {
            miUdid = new String[miDevices.length];
            for (int i = 0; i < miDevices.length; i++) {
                miUdid[i] = miDevices[i].split("=")[0];
            }
            return miUdid;
        } else {
            return null;
        }
    }

    public static Map<String, String> getMiResolution() {
        Map<String, String> miResolution = new LinkedHashMap<String, String>();
        String[] miDevices = StaticConfig.getMiDevices();
        if (null != miDevices) {
            for (int i = 0; i < miDevices.length; i++) {
                miResolution.put(miDevices[i].split("=")[0], miDevices[i].split("=")[1]);
            }
            return miResolution;
        } else {
            return null;
        }
    }
}