package com.codeycoder.redis.command;

public interface Handler {
    byte[] handle(String[] arguments);
}
