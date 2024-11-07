package com.codeycoder.redis.command;


import com.codeycoder.redis.config.ObjectFactory;

import java.util.List;

public class Ping extends AbstractHandler {
    public Ping(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public List<String> handle(String[] arguments) {
        return List.of(objectFactory.getProtocolSerializer().simpleString("PONG"));
    }
}