package com.unioncloud.callability.kafka.consumer;

import com.unioncloud.callability.common.proto.message.Fs2CaTopic;
import com.unioncloud.callability.kafka.handler.Fs2CaConsumerHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * <p> Fs2Ca对应的 消费者 </p>
 *
 * @author panliyong  2019-11-12 10:49
 */
@Component
@Slf4j
public class Fs2CaTopicConsumer {

    @Autowired
    private Fs2CaConsumerHandler consumerHandler;

    /**
     * <p>消费数据</p>
     *
     * @param record 消费的消息
     * @author panliyong  2019-11-12 11:00
     */
    @KafkaListener(topics = {"${spring.kafka.consumer.fs2ca_topic}"},containerFactory = "fs2CaKafkaConsumerFactory")
    public void listenConsumer(ConsumerRecord<String, Fs2CaTopic.Fs2CaMessage> record) {
        Fs2CaTopic.Fs2CaMessage message = record.value();
        log.info("【Fs2CaTopicConsumer】消费 FreeSwitch 消息:\n{}", message);
        consumerHandler.process(message);
    }
}
