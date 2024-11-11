package com.codeycoder.redis.replica;

import com.codeycoder.redis.config.Logger;
import com.codeycoder.redis.config.ObjectFactory;

import java.util.List;

public class CommandReplicator {
    private static final Logger LOGGER = new Logger(CommandReplicator.class);

    private final ObjectFactory objectFactory;

    public CommandReplicator(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public void replicateWriteCommand(String commandString) {
        objectFactory.getApplicationProperties()
                .getReplicas()
                .forEach(replica -> replicate(replica, commandString));
    }

    private void replicate(ReplicaClient replica, String commandString) {
        byte[] request = objectFactory.getProtocolSerializer().array(List.of(commandString.split(" ")));
        replica.send(request);
    }
}