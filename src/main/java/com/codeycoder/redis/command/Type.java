package com.codeycoder.redis.command;

import com.codeycoder.redis.config.ObjectFactory;
import com.codeycoder.redis.protocol.ValueType;
import com.codeycoder.redis.storage.Storage;
import com.codeycoder.redis.storage.StorageRecord;

public class Type extends AbstractHandler {
    public Type(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public byte[] handle(String[] arguments) {
        StorageRecord storageRecord = Storage.get(arguments[1]);
        String type;
        if (storageRecord == null) {
            type = ValueType.NONE.getDisplay();
        } else {
            type = storageRecord.valueType().getDisplay();
        }
        return protocolSerializer().simpleString(type);
    }
}