package com.codeycoder.redis.command;

import com.codeycoder.redis.config.ObjectFactory;
import com.codeycoder.redis.protocol.ProtocolSerializer;

public abstract class AbstractHandler implements Handler {
    protected ObjectFactory objectFactory;

    protected AbstractHandler(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    protected ProtocolSerializer protocolSerializer() {
        return objectFactory.getProtocolSerializer();
    }
}