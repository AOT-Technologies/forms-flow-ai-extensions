package org.camunda.rpa.client.util;

import java.util.Map;

/**
 * author : Shibin Thomas
 */
public class ObjectUtil {

    /**
     *
     * @param entry
     * @param key
     * @return
     */
    public static String convertKeyToString(Map<String, Object> entry, String key){
        if(entry == null) return null;
        return entry.containsKey(key)?entry.get(key).toString():null;
    }

    /**
     *
     * @param entry
     * @param key
     * @return
     */
    public static Long convertKeyToLong(Map<String, Object> entry, String key){
        if(entry == null) return 0L;
        return entry.containsKey(key)?Long.parseLong(String.valueOf(entry.get(key))):0L;
    }
}
