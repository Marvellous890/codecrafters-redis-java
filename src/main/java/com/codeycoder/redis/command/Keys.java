package com.codeycoder.redis.command;

import com.codeycoder.redis.config.ObjectFactory;

import java.io.IOException;
import java.util.List;

public class Keys extends AbstractHandler {
    public Keys(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public byte[] handle(String[] arguments) {
        List<String> keys;
        try {
            keys = objectFactory.getRdbProcessor().readAllKeys();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return protocolSerializer().array(keys);
    }
}