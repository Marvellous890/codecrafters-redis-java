package com.codeycoder.redis.command;

import com.codeycoder.redis.config.ObjectFactory;

import java.util.ArrayList;
import java.util.List;

public class Multi extends AbstractHandler {
    private final List<String[]> commandsCache = new ArrayList<>();

    public Multi(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public byte[] handle(String[] arguments) {
        return protocolSerializer().simpleString("OK");
    }

    public byte[] enqueueCommand(String[] command) {
        commandsCache.add(command);
        return protocolSerializer().simpleString("QUEUED");
    }

    public byte[] executeCommands() {
        if (commandsCache.isEmpty()) {
            return protocolSerializer().array(List.of());
        } else {
            var responses = commandsCache.stream()
                    .map(command -> objectFactory.getCommandFactory()
                            .getCommandHandler(command[0])
                            .handle(command))
                    .toList();
            commandsCache.clear();
            return protocolSerializer().arrayOfSerialized(responses);
        }
    }

    public byte[] discardTransaction() {
        commandsCache.clear();
        return protocolSerializer().simpleString("OK");
    }
}
