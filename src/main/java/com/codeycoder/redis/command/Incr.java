package com.codeycoder.redis.command;

import com.codeycoder.redis.config.ObjectFactory;
import com.codeycoder.redis.storage.Storage;

public class Incr extends AbstractHandler implements Write {
    public Incr(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public byte[] handle(String[] arguments) {
        var key = arguments[1];
        var value = Storage.contains(key) ? Long.parseLong((String) Storage.get(key).value()) : 0;
        value++;
        Storage.put(key, String.valueOf(value));
        return objectFactory.getProtocolSerializer().integer(value);
    }
}