package com.codeycoder.redis.storage;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Storage {
    private static final Map<String, StorageRecord> storage = new ConcurrentHashMap<>();

    private Storage() {
    }

    public static void put(String key, String value) {
        storage.put(key, new StorageRecord(value, Instant.MAX));
    }

    public static void put(String key, String value, Long expiration) {
        storage.put(key, new StorageRecord(value, Instant.now().plusMillis(expiration)));
    }

    public static String get(String key) {
        StorageRecord storageRecord = storage.get(key);
        if (storageRecord == null) {
            return null;
        }
        if (Instant.now().isAfter(storageRecord.expiration())) {
            remove(key);
            return null;
        }
        return storageRecord.value();
    }

    public static void remove(String key) {
        storage.remove(key);
    }
}