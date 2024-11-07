package com.codeycoder.redis.command;

import com.codeycoder.redis.config.ObjectFactory;

import java.util.List;

public class Echo extends AbstractHandler {
    public Echo(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public List<String> handle(String[] arguments) {
        return List.of(objectFactory.getProtocolSerializer().bulkString(arguments[1]));
    }
}