package com.codeycoder.redis.config;


import com.codeycoder.redis.CommandHandler;
import com.codeycoder.redis.protocol.ProtocolDeserializer;
import com.codeycoder.redis.protocol.ProtocolSerializer;

public class ObjectFactory {
    private final ApplicationProperties applicationProperties;

    private ProtocolDeserializer protocolDeserializer;
    private ProtocolSerializer protocolSerializer;
    private CommandHandler commandHandler;

    public ObjectFactory(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        init();
    }

    public ApplicationProperties getApplicationProperties() {
        return applicationProperties;
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

    private void init() {
        protocolDeserializer = new ProtocolDeserializer();
        protocolSerializer = new ProtocolSerializer();
        commandHandler = new CommandHandler(protocolSerializer, applicationProperties);
    }
}