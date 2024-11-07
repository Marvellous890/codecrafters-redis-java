package com.codeycoder.redis.protocol;

import java.util.List;

public class ProtocolSerializer {
    private static final String CRLF_TERMINATOR = "\r\n";

    public String simpleString(String value) {
        return "+" + value + CRLF_TERMINATOR;
    }

    public String bulkString(String value) {
        if (value == null) {
            return "$-1" + CRLF_TERMINATOR;
        }
        return "$" + value.length() + CRLF_TERMINATOR + value + CRLF_TERMINATOR;
    }

    public String array(List<String> values) {
        StringBuilder stringBuilder = new StringBuilder("*")
                .append(values.size())
                .append(CRLF_TERMINATOR);
        values.stream()
                .map(this::bulkString)
                .forEach(stringBuilder::append);
        return stringBuilder.toString();
    }
}