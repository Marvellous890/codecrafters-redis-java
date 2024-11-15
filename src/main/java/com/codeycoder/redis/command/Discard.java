package com.codeycoder.redis.command;

import com.codeycoder.redis.config.ObjectFactory;

public class Discard extends AbstractHandler {
    private static final String DISCARD_WITHOUT_MULTI_ERROR_MESSAGE = "ERR DISCARD without MULTI";
    public Discard(ObjectFactory objectFactory) {
        super(objectFactory);
    }
    @Override
    public byte[] handle(String[] arguments) {
        return protocolSerializer().simpleError(DISCARD_WITHOUT_MULTI_ERROR_MESSAGE);
    }
}
