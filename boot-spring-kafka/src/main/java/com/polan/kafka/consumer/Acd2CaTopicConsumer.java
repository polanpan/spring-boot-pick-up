package com.unioncloud.callability.kafka.consumer;

import com.unioncloud.callability.kafka.handler.Acd2CaConsumerHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * <p> acd2Ca对应的 消费者 </p>
 *
 * @author panliyong  2019-11-12 10:49
 */
@Component
@Slf4j
public class Acd2CaTopicConsumer {

    @Autowired
    private Acd2CaConsumerHandler consumerHandler;

    /**
     * <p>消费数据</p>
     *
     * @param record 消费的消息
     * @author panliyong  2019-11-12 11:00
     */
    @KafkaListener(topics = {"${spring.kafka.consumer.acd2ca_topic}"}, containerFactory = "cad2CaKafkaConsumerFactory")
    public void listenConsumer(ConsumerRecord<String, String> record) {
        String message = record.value();
        log.info("【Acd2CaTopicConsumer】消费 FreeSwitch 消息:\n{}", message);
        consumerHandler.process(message);
    }
}
