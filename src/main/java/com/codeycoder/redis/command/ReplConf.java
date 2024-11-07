package com.codeycoder.redis.command;


import com.codeycoder.redis.config.ObjectFactory;

import java.util.List;

public class ReplConf extends AbstractHandler {
    public ReplConf(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public List<String> handle(String[] arguments) {
        String parameter = arguments[1].toLowerCase();
        switch (parameter) {
            case "listening-port":
                objectFactory.getApplicationProperties().addReplica(Integer.parseInt(arguments[2]));
                break;
            case "capa":
                if (!"psync2".equalsIgnoreCase(arguments[2])) {
                    throw new RuntimeException("Unknown parameter: " + arguments[2]);
                }
                break;
            default:
                throw new RuntimeException("Unknown parameter: " + parameter);
        }
        return List.of(objectFactory.getProtocolSerializer().simpleString("OK"));
    }
}