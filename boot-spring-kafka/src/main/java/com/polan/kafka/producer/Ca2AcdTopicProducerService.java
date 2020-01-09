package com.unioncloud.callability.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p> Ca2Acd 发送kafka消息汇总service</p>
 *
 * @author panliyong  2019-11-27 11:09
 */
@Service
@Slf4j
public class Ca2AcdTopicProducerService {

    @Autowired
    private Ca2AcdTopicProducer producer;

    /**
     * <p>客户入队消息</p>
     *
     * @param message 客户入队消息
     * @author panliyong  2019-11-27 11:14
     */
    public void sendCustomerPutQueenRequestMessage(String message) {
        producer.sendMessage(message);
    }

}
