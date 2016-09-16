package io.bvzx.service.bean;

import io.bvzx.service.utils.Patterns;

import java.lang.reflect.Field;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/9/13.
 */
public abstract class CommonVo {

    public abstract Class getClazz();

    public String buildEncodeUrl(boolean isUrlEncode) {
        StringBuilder stringBuilder=new StringBuilder();
        Field[] field=getClazz().getDeclaredFields();
        boolean firstFlag=true;
        for (Field urlParam:field){
            urlParam.setAccessible(true);
            String fieldName=urlParam.getName();
            String fieldValue= null;
            try {
                fieldValue = (String) urlParam.get(this);
                Patterns.isChineseChar(fieldValue);
                if (isUrlEncode){
                    fieldValue=URLEncoder.encode(fieldValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            String uniteParam=fieldName+"="+fieldValue;
            if (firstFlag){
                uniteParam="&"+uniteParam;
                firstFlag=true;
            }
            stringBuilder.append(uniteParam);
        }
        return stringBuilder.toString();
    }


}
