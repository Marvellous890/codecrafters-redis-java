package com.codeycoder.redis.command;

import com.codeycoder.redis.config.ObjectFactory;

public class Echo extends AbstractHandler {
    public Echo(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public byte[] handle(String[] arguments) {
        return protocolSerializer().bulkString(arguments[1]);
    }
}