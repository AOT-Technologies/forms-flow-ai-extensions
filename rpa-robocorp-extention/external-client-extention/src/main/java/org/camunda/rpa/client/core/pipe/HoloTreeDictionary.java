package org.camunda.rpa.client.core.pipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HoloTreeDictionary {

    private static Map<String, String> holoTreeInfo = new HashMap<>();

    public static Map<String, String> getHoloTreeInfo() {
        return holoTreeInfo;
    }

    public static void setHoloTreeInfo(List<String> robotList) {
        String space = "space-";
        for(int i = 0; i < robotList.size(); i++) {
            HoloTreeDictionary.holoTreeInfo.put( robotList.get(i), (space+i) );
        }
    }
}
