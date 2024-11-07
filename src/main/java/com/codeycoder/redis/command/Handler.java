package com.codeycoder.redis.command;

import java.util.List;

public interface Handler {
    List<String> handle(String[] arguments);
}
