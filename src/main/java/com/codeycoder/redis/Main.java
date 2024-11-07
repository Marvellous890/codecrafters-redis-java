package com.codeycoder.redis;

import com.codeycoder.redis.config.ApplicationProperties;
import com.codeycoder.redis.config.ObjectFactory;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) throws IOException {
        ApplicationProperties properties = new ApplicationProperties(args);
        ObjectFactory objectFactory = new ObjectFactory(properties);

        try (ServerSocket serverSocket = new ServerSocket(properties.getPort())) {
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);

            System.out.println("Server has started on port: " + properties.getPort());

            while (true) {
                // TODO: connection handler pool
                new com.codeycoder.redis.ConnectionHandler(
                        serverSocket.accept(),
                        objectFactory.getProtocolDeserializer(),
                        objectFactory.getCommandHandler()
                ).start();
            }
        }
    }
}