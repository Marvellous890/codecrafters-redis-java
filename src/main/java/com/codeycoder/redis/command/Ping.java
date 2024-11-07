package com.codeycoder.redis.command;


import com.codeycoder.redis.config.ObjectFactory;

public class Ping extends AbstractHandler {
    public Ping(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public String handle(String[] arguments) {
        return objectFactory.getProtocolSerializer().simpleString("PONG");
    }
}