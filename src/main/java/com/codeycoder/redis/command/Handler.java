package com.codeycoder.redis.command;

public interface Handler {
    String handle(String[] arguments);
}
