package com.codeycoder.redis.config;

import org.apache.commons.codec.digest.DigestUtils;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApplicationProperties {
    private int port = 6379;
    private ReplicaProperties replicaProperties;

    // Master properties
    private String replicationId;
    private Long replicationOffset;
    private List<Socket> replicas;

    public ApplicationProperties(String[] args) {
        parseArgs(args);
        if (isMaster()) {
            setMasterProperties();
        }
    }

    public int getPort() {
        return port;
    }

    public boolean isReplica() {
        return replicaProperties != null;
    }

    public boolean isMaster() {
        return replicaProperties == null;
    }

    public ReplicaProperties getReplica() {
        return replicaProperties;
    }

    public String getReplicationId() {
        return replicationId;
    }

    public Long getReplicationOffset() {
        return replicationOffset;
    }

    public synchronized List<Socket> getReplicas() {
        if (replicas == null) {
            replicas = new ArrayList<>();
        }
        return replicas;
    }

    public synchronized void addReplica(Socket socket) {
        if (replicas == null) {
            replicas = new ArrayList<>();
        }
        replicas.add(socket);
    }

    private void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String parameter = args[i].toLowerCase().substring(2);
            switch (parameter) {
                case "port":
                    port = Integer.parseInt(args[i + 1]);
                    i++;
                    break;
                case "replicaof":
                    String input = args[i + 1];
                    String[] parts = input.split(" ");
                    String host = parts[0];
                    int port = Integer.parseInt(parts[1]);

                    replicaProperties = new ReplicaProperties(host, port);
                    i += 2;
                    break;
                default:
                    throw new RuntimeException("Unknown parameter: " + parameter);
            }
        }
    }

    private void setMasterProperties() {
        replicationId = DigestUtils.sha1Hex(UUID.randomUUID().toString());
        replicationOffset = 0L;
    }
}