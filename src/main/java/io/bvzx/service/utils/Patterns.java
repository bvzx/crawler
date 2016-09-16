package io.bvzx.service.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/9/13.
 */
public class Patterns {
    public static boolean isChineseChar(String str){
        boolean temp = false;
        Pattern p=Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m=p.matcher(str);
        if(m.find()){
            temp =  true;
        }
        return temp;
    }

    public static String getNumber(String str){
        return Pattern.compile("[^0-9]").matcher(str).replaceAll("");
    }
}
