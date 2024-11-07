package com.codeycoder.redis.config;


import com.codeycoder.redis.CommandHandler;
import com.codeycoder.redis.command.CommandFactory;
import com.codeycoder.redis.protocol.ProtocolDeserializer;
import com.codeycoder.redis.protocol.ProtocolSerializer;

import java.lang.reflect.InvocationTargetException;

public class ObjectFactory {
    private final ApplicationProperties applicationProperties;
    private CommandFactory commandFactory;
    private ProtocolDeserializer protocolDeserializer;
    private ProtocolSerializer protocolSerializer;
    private CommandHandler commandHandler;

    public ObjectFactory(ApplicationProperties applicationProperties) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.applicationProperties = applicationProperties;
        init();
    }

    public ApplicationProperties getApplicationProperties() {
        return applicationProperties;
    }

    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

    public ProtocolDeserializer getProtocolDeserializer() {
        return protocolDeserializer;
    }

    public ProtocolSerializer getProtocolSerializer() {
        return protocolSerializer;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    private void init() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        protocolDeserializer = new ProtocolDeserializer();
        protocolSerializer = new ProtocolSerializer();
        commandFactory = new CommandFactory(this);
        commandHandler = new CommandHandler(commandFactory);
    }
}