package com.unioncloud.callability.kafka.serializer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.unioncloud.callability.common.proto.message.Fs2CaTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * <p> proto 反序列化类</p>
 *
 * @author panliyong  2019-11-12 10:23
 */
@Slf4j
public class Fs2CaTopicProtoDeserializer implements Deserializer<Fs2CaTopic.Fs2CaMessage> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public Fs2CaTopic.Fs2CaMessage deserialize(String topic, byte[] data) {
        if (null == data) {
            return null;
        }
        Fs2CaTopic.Fs2CaMessage message;
        try {
            message = Fs2CaTopic.Fs2CaMessage.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            throw new SerializationException("Error when deserializing byte[] to Example due to unsupported encoding!");
        }
        return message;
    }

    @Override
    public void close() {
    }
}
