package com.codeycoder.redis.command;

import com.codeycoder.redis.config.ObjectFactory;

public class Multi extends AbstractHandler {
    public Multi(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public byte[] handle(String[] arguments) {
        return protocolSerializer().simpleString("OK");
    }
}
