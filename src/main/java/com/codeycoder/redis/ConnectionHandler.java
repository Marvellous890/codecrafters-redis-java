package com.codeycoder.redis;

import com.codeycoder.redis.command.CommandFactory;
import com.codeycoder.redis.command.Handler;
import com.codeycoder.redis.command.Psync;
import com.codeycoder.redis.command.Write;
import com.codeycoder.redis.config.ApplicationProperties;
import com.codeycoder.redis.config.ObjectFactory;
import com.codeycoder.redis.exception.EndOfStreamException;
import com.codeycoder.redis.protocol.ProtocolDeserializer;
import com.codeycoder.redis.replica.CommandReplicator;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionHandler extends Thread {
    private final Socket socket;
    private final ProtocolDeserializer protocolDeserializer;
    private final CommandFactory commandFactory;
    private final CommandReplicator commandReplicator;
    private final ApplicationProperties applicationProperties;

    private boolean isReplicaSocket;

    public ConnectionHandler(Socket socket, ObjectFactory objectFactory) {
        this.socket = socket;
        protocolDeserializer = objectFactory.getProtocolDeserializer();
        commandFactory = objectFactory.getCommandFactory();
        commandReplicator = objectFactory.getCommandReplicator();
        applicationProperties = objectFactory.getApplicationProperties();
    }

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            OutputStream outputStream = socket.getOutputStream();

            while (true) {
                String commandString = protocolDeserializer.parseInput(inputStream);
                String[] arguments = commandString.split(" ");
                String command = arguments[0].toUpperCase();
                Handler handler = commandFactory.getCommandHandler(command);
                byte[] response = handler.handle(arguments);

                if (handler instanceof Psync) {
                    isReplicaSocket = true;
                    applicationProperties.addReplica(socket);
                    System.out.printf("Replica with port %s has been added%n", socket.getPort());
                }

                if (handler instanceof Write) {
                    commandReplicator.replicateWriteCommand(commandString);
                }

                outputStream.write(response);
                outputStream.flush();
            }

        } catch (EndOfStreamException e) {
            System.out.println("End of input stream reached");
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            if (!isReplicaSocket) {
                try {
                    socket.close();
                    System.out.printf("%s socket closed%n", getName());
                } catch (IOException e) {
                    System.out.println("IOException: " + e.getMessage());
                }
            }
        }
    }
}