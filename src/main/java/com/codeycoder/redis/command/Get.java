package com.codeycoder.redis.command;

import com.codeycoder.redis.config.Logger;
import com.codeycoder.redis.config.ObjectFactory;
import com.codeycoder.redis.storage.Storage;
import com.codeycoder.redis.storage.StorageRecord;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

public class Get extends AbstractHandler {
    private static final Logger LOGGER = new Logger(Get.class);

    public Get(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public byte[] handle(String[] arguments) {
        String value;
        if (objectFactory.getApplicationProperties().getDir() == null) {
            StorageRecord storageRecord = Storage.get(arguments[1]);
            value = Optional.ofNullable(storageRecord)
                    .map(StorageRecord::value)
                    .orElse(null);
        } else {
            try {
                Map<String, StorageRecord> pairs = objectFactory.getRdbProcessor().readAllPairs();
                StorageRecord recordValue = pairs.get(arguments[1]);
                if (Instant.now().isAfter(recordValue.expiration())) {
                    value = null;
                } else {
                    value = recordValue.value();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return objectFactory.getProtocolSerializer().bulkString(value);
    }
}