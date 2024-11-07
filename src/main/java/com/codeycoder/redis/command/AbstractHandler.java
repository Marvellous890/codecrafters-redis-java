package com.codeycoder.redis.command;

import com.codeycoder.redis.config.ObjectFactory;

public abstract class AbstractHandler implements Handler {
    protected ObjectFactory objectFactory;

    protected AbstractHandler(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }
}