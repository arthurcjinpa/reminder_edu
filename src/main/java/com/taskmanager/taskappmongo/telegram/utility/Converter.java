package com.taskmanager.taskappmongo.telegram.utility;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.HashMap;
import java.util.Map;

public class Converter {
    public static String convertMapToString(String key, String taskId) {
        Map<String, String> callBackDataMap = new HashMap<>();
        callBackDataMap.put(key, taskId);
        return Joiner.on(",").withKeyValueSeparator("=").join(callBackDataMap);
    }

    public static Map<String, String> convertStringToMap(String mapAsString) {
        return Splitter.on(',').withKeyValueSeparator('=').split(mapAsString);
    }
}
