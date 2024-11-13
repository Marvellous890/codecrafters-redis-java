package com.codeycoder.redis.command;

import com.codeycoder.redis.config.Logger;
import com.codeycoder.redis.config.ObjectFactory;
import com.codeycoder.redis.protocol.ValueType;
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
                    .filter(r -> r.valueType() == ValueType.STRING)
                    .map(StorageRecord::value)
                    .map(Object::toString)
                    .orElse(null);
        } else {
            try {
                Map<String, StorageRecord> pairs = objectFactory.getRdbProcessor().readAllPairs();
                StorageRecord recordValue = pairs.get(arguments[1]);
                if (recordValue == null || Instant.now().isAfter(recordValue.expiration())) {
                    value = null;
                } else {
                    value = Optional.ofNullable(recordValue.value())
                            .map(Object::toString)
                            .orElse(null);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return protocolSerializer().bulkString(value);
    }
}