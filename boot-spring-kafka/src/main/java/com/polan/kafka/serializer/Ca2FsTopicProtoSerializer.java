package com.unioncloud.callability.kafka.serializer;

import com.unioncloud.callability.common.proto.message.Ca2FsTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * <p> proto 序列化类</p>
 *
 * @author panliyong  2019-11-12 10:23
 */
@Slf4j
public class Ca2FsTopicProtoSerializer implements Serializer<Ca2FsTopic.Ca2FsMessage> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Ca2FsTopic.Ca2FsMessage data) {
        if (data == null) {
            log.error("【Ca2FsTopicProtoSerializer】data is null .....");
            return null;
        }
        return data.toByteArray();
    }

    @Override
    public void close() {
    }
}
