package com.codeycoder.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Storage {
    private static final Map<String, String> storage = new ConcurrentHashMap<>();
    private Storage() {}

    public static void put(String key, String value) {
        storage.put(key, value);
    }

    public static String get(String key) {
        return storage.get(key);
    }
}
