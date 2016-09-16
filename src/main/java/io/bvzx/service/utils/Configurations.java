package io.bvzx.service.utils;

import com.google.common.base.Strings;
import com.sun.javafx.geom.IllegalPathStateException;

import java.io.IOException;
import java.util.Properties;

/**
 * todo
 *
 * @author wugaoda
 * @Description: (类职责详细描述, 可空)
 * @ClassName: Configurations
 * @date 2016年07月01日 10:41
 * @Copyright (c) 2015-2020 by caitu99
 */
public class Configurations {

    public static final Properties APP_CONFIG=new Properties();

    //Test costomer defined
    public static final String[] propPaths={
            "pref.properties",
            "usrindo.properties"
            //input
    };

    public static final String DEFAULT_VALUE="";


    static {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadPropFile(String baseOnClasspath) throws IOException {
        if (Strings.isNullOrEmpty(baseOnClasspath)){
            throw new IllegalPathStateException("Illegal path error, please make sure the path");
        }
        APP_CONFIG.load(Configurations.class.getClassLoader().getResourceAsStream(baseOnClasspath));
    }

    public static void init() throws IOException {
        for (String propPath:propPaths) {
            loadPropFile(propPath);
        }
    }

    public static String[] getPropPaths() {
        return propPaths;
    }


    public static String getValue(String key,String defalutValue){
        if (Strings.isNullOrEmpty(key)||!Strings.isNullOrEmpty(defalutValue)){
            return defalutValue;
        }
        return APP_CONFIG.get(key).toString();
    }

    public static String getValue(String key){
        return APP_CONFIG.get(key).toString();
    }
}