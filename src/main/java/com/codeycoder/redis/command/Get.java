package com.codeycoder.redis.command;

import com.codeycoder.redis.config.ObjectFactory;
import com.codeycoder.redis.storage.Storage;

import java.util.List;

public class Get extends AbstractHandler {
    public Get(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public List<String> handle(String[] arguments) {
        String value = Storage.get(arguments[1]);
        return List.of(objectFactory.getProtocolSerializer().bulkString(value));
    }
}