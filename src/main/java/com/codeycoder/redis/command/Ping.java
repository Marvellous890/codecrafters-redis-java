package com.codeycoder.redis.command;


import com.codeycoder.redis.config.ObjectFactory;

import java.util.List;

public class Ping extends AbstractHandler {
    public Ping(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public byte[] handle(String[] arguments) {
        return objectFactory.getProtocolSerializer().simpleString("PONG");
    }
}