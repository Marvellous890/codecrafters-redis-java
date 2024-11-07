package com.codeycoder.redis.command;

import com.codeycoder.redis.config.ObjectFactory;
import com.codeycoder.redis.storage.Storage;

public class Get extends AbstractHandler {
    public Get(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public byte[] handle(String[] arguments) {
        String value = Storage.get(arguments[1]);
        return objectFactory.getProtocolSerializer().bulkString(value);
    }
}