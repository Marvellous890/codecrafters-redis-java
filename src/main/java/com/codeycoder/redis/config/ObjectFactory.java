package com.codeycoder.redis.config;

import com.codeycoder.redis.command.CommandFactory;
import com.codeycoder.redis.protocol.ProtocolDeserializer;
import com.codeycoder.redis.protocol.ProtocolSerializer;
import com.codeycoder.redis.protocol.RdbProcessor;
import com.codeycoder.redis.replica.CommandReplicator;

import java.lang.reflect.InvocationTargetException;

public class ObjectFactory {
    private final ApplicationProperties applicationProperties;
    private ProtocolDeserializer protocolDeserializer;
    private ProtocolSerializer protocolSerializer;
    private CommandFactory commandFactory;
    private CommandReplicator commandReplicator;
    private RdbProcessor rdbProcessor;

    public ObjectFactory(ApplicationProperties applicationProperties) throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
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

    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

    public CommandReplicator getCommandReplicator() {
        return commandReplicator;
    }

    public RdbProcessor getRdbProcessor() {
        return rdbProcessor;
    }

    private void init() throws InvocationTargetException, NoSuchMethodException, InstantiationException,
            IllegalAccessException {
        protocolDeserializer = new ProtocolDeserializer();
        protocolSerializer = new ProtocolSerializer();
        commandFactory = new CommandFactory(this);
        commandReplicator = new CommandReplicator(this);
        rdbProcessor = new RdbProcessor(applicationProperties);
    }
}