package com.unioncloud.callability.kafka.producer;

import com.unioncloud.callability.common.proto.message.Ca2FsTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;

/**
 * <p>  呼叫消息 生产</p>
 *
 * @author panliyong  2019/3/6 15:07
 */
@Slf4j
@Component
public class Ca2FsTopicProducer {

    @Value("${spring.kafka.producer.topic}")
    private String ca2FsTopic;

    @Resource(name = "ca2FsKafkaProducer")
    private KafkaTemplate<String, Ca2FsTopic.Ca2FsMessage> kafkaTemplate;

    /**
     * 发送数据
     */
    void sendMessage(Ca2FsTopic.Ca2FsMessage message) {

        ListenableFuture sendResult = kafkaTemplate.send(ca2FsTopic, message);
        // 添加回调
        sendResult.addCallback(o -> log.info("【Ca2FsTopicProducer】send to FreeSwitch 成功,msg：\n{}", message)
                , throwable -> log.error("【Ca2FsTopicProducer】send to FreeSwitch 出现异常,msg：\n{}", message)
        );
    }
}