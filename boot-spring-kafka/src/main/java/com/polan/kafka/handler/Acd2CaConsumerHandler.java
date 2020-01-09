package com.unioncloud.callability.kafka.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p> ACD  消费数据处理</p>
 *
 * @author panliyong  2019/3/6 14:37
 */
@Slf4j
@Component
public class Acd2CaConsumerHandler {

    /**
     * <p>kafka 消息消费逻辑处理方法</p>
     *
     * @param message 消费到的消息
     * @author panliyong  2019-11-16 16:38
     */
    public void process(String message) {
        try {
            //    TODO
        } catch (Exception e) {
            log.error("【Acd2CaConsumerHandler】-> Acd2CaConsumerHandler 消费消息产生异常", e);
        }
    }

}
