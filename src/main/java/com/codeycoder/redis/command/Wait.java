package com.codeycoder.redis.command;

import com.codeycoder.redis.config.ObjectFactory;
import org.apache.commons.lang3.math.NumberUtils;

public class Wait extends AbstractHandler {
    public Wait(ObjectFactory objectFactory) {
        super(objectFactory);
    }
    @Override
    public byte[] handle(String[] arguments) {
        if (!(NumberUtils.isDigits(arguments[1]) && NumberUtils.isDigits(arguments[2]))) {
            throw new IllegalArgumentException(String.format("Digits expected: %s, %s", arguments[1], arguments[2]));
        }
        return objectFactory.getProtocolSerializer().integer(0);
    }
}