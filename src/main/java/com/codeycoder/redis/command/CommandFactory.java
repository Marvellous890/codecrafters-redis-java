package com.codeycoder.redis.command;

import com.codeycoder.redis.config.ObjectFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.Map;

public class CommandFactory {
    private final Map<Command, Handler> commandHandlers = new EnumMap<>(Command.class);

    public CommandFactory(ObjectFactory objectFactory) throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        initHandlers(objectFactory);
    }

    public Handler getCommandHandler(String command) {
        Handler handler = commandHandlers.get(Command.valueOf(command));
        if (handler == null) {
            throw new RuntimeException("Unknown command: " + command);
        }
        return handler;
    }

    private void initHandlers(ObjectFactory objectFactory) throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        for (Command command : Command.values()) {
            Constructor<? extends Handler> handlerConstructor = command.handler.getConstructor(ObjectFactory.class);
            Handler handler = handlerConstructor.newInstance(objectFactory);
            commandHandlers.put(command, handler);
        }
    }

    private enum Command {
        CONFIG(Config.class),
        DISCARD(Discard.class),
        ECHO(Echo.class),
        EXEC(Exec.class),
        GET(Get.class),
        INCR(Incr.class),
        INFO(Info.class),
        KEYS(Keys.class),
        MULTI(Multi.class),
        PING(Ping.class),
        PSYNC(Psync.class),
        REPLCONF(ReplConf.class),
        SET(Set.class),
        TYPE(Type.class),
        WAIT(Wait.class),
        XADD(Xadd.class),
        XRANGE(Xrange.class),
        XREAD(Xread.class);

        private final Class<? extends Handler> handler;

        Command(Class<? extends Handler> handler) {
            this.handler = handler;
        }
    }
}