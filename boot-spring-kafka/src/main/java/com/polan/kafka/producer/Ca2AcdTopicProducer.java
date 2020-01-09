package com.unioncloud.callability.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;

/**
 * <p> Ca2Acd 呼叫消息 生产</p>
 *
 * @author panliyong  2019/3/6 15:07
 */
@Slf4j
@Component
public class Ca2AcdTopicProducer {

    @Value("${spring.kafka.producer.ca2acd_topic}")
    private String ca2AcdTopic;

    @Resource(name = "ca2AcdKafkaProducer")
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发送数据
     */
    void sendMessage(String message) {

        ListenableFuture sendResult = kafkaTemplate.send(ca2AcdTopic, message);
        // 添加回调
        sendResult.addCallback(o -> log.info("【Ca2AcdTopicProducer】send to FreeSwitch 成功,msg：\n{}", message)
                , throwable -> log.error("【Ca2AcdTopicProducer】send to FreeSwitch 出现异常,msg：\n{}", message)
        );
    }
}