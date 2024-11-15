package com.codeycoder.redis.command;

import com.codeycoder.redis.config.ObjectFactory;
import com.codeycoder.redis.config.Logger;
import com.codeycoder.redis.protocol.ValueType;
import com.codeycoder.redis.storage.Storage;
import com.codeycoder.redis.storage.StreamRecord;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Xread extends AbstractHandler {
    private static final Logger LOGGER = new Logger(Xread.class);

    public Xread(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public byte[] handle(String[] arguments) {
        // TODO: validate arguments

        int blockIndex = getBlockIndex(arguments);
        boolean isBlocking = blockIndex > 0;

        int streamsIndex = getStreamsIndex(arguments);
        var keysIdsNumber = (arguments.length - streamsIndex - 1) / 2;
        List<Pair<String, String>> keyIdPairs = parseKeyIdPairs(arguments, streamsIndex, keysIdsNumber);
        List result;

        if (!isBlocking) {
            result = getResult(keyIdPairs);
        } else {
            long blockTimeout = Long.parseLong(arguments[blockIndex + 1]);
            long start = System.currentTimeMillis();
            long end = blockTimeout == 0 ? Long.MAX_VALUE : start + blockTimeout;
            boolean timedOut = true;

            result = getResult(keyIdPairs);

            while (System.currentTimeMillis() < end) {
                if (!allSubListsAreEmpty(result)) {
                    timedOut = false;
                    break;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                result = getResult(keyIdPairs);
            }

            if (timedOut) {
                return protocolSerializer().bulkString(null);
            }
        }

        return protocolSerializer().array(result);
    }

    private int getBlockIndex(String[] arguments) {
        for (int i = 1; i < arguments.length - 2; i++) {
            if (arguments[i].equalsIgnoreCase("block")) {
                LOGGER.log("Found 'block' argument");
                return i;
            }
        }
        return -1;
    }

    private int getStreamsIndex(String[] arguments) {
        for (int i = 1; i < arguments.length - 2; i++) {
            if (arguments[i].equalsIgnoreCase("streams")) {
                return i;
            }
        }

        throw new IllegalArgumentException("Expected 'streams' argument");
    }

    private List<Pair<String, String>> parseKeyIdPairs(String[] arguments, int streamsIndex, int keysIdsNumber) {
        List<Pair<String, String>> keyIdPairs = new ArrayList<>();
        int firstKeyIndex = streamsIndex + 1;

        for (int i = firstKeyIndex; i < firstKeyIndex + keysIdsNumber; i++) {
            String streamKey = arguments[i];
            String streamId = checkSpecialId(streamKey, arguments[i + keysIdsNumber]);
            keyIdPairs.add(Pair.of(streamKey, streamId));
        }
        return keyIdPairs;
    }

    private String checkSpecialId(String streamKey, String streamId) {
        if (streamId.equals("$")) {
            return Optional.ofNullable(Storage.get(streamKey))
                    .filter(r -> r.valueType() == ValueType.STREAM)
                    .flatMap(r -> ((List<StreamRecord>) r.value()).reversed().stream().findFirst())
                    .map(StreamRecord::id)
                    .orElse("0-0");
        } else {
            return streamId;
        }
    }

    private List getResult(List<Pair<String, String>> keyIdPairs) {
        return keyIdPairs.stream()
                .map(this::toSerializableStreams)
                .toList();
    }

    private boolean allSubListsAreEmpty(List list) {
        // getResult() returns a list of lists of streamKey and records, so we check if all the records are empty
        return list.stream().allMatch(l -> ((List) ((List) l).get(1)).isEmpty());
    }

    private List toSerializableStreams(Pair<String, String> keyIdPair) {
        String streamKey = keyIdPair.getKey();
        String startId = keyIdPair.getValue();

        List foundStream =
                Optional.ofNullable(Storage.get(streamKey))
                        .filter(r -> r.valueType() == ValueType.STREAM)
                        .map(r -> (List<StreamRecord>) r.value())
                        .orElseGet(ArrayList::new)
                        .stream()
                        .filter(r -> r.id().compareTo(startId) > 0)
                        .map(StreamRecord::toSerializable)
                        .toList();

        return List.of(streamKey, foundStream);
    }
}