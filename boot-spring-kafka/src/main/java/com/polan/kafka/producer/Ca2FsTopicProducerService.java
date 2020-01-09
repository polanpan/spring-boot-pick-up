package com.unioncloud.callability.kafka.producer;

import com.unioncloud.callability.common.proto.message.Ca2FsTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p> 发送kafka消息汇总service</p>
 *
 * @author panliyong  2019-11-27 11:09
 */
@Service
@Slf4j
public class Ca2FsTopicProducerService {

    @Autowired
    private Ca2FsTopicProducer producer;

    /**
     * <p>发送签入消息</p>
     *
     * @param message 签入消息
     * @author panliyong  2019-11-27 11:14
     */
    public void sendCheckInRequestMessage(Ca2FsTopic.Ca2FsMessage message) {
        producer.sendMessage(message);
    }

    /**
     * <p>发送签出消息</p>
     *
     * @param message 签出消息
     * @author panliyong  2019-11-27 11:15
     */
    public void sendCheckOutRequestMessage(Ca2FsTopic.Ca2FsMessage message) {
        producer.sendMessage(message);
    }

    /**
     * <p>发送外呼消息</p>
     *
     * @param message 外呼消息
     * @author panliyong  2019-11-27 11:17
     */
    public void sendCalloutMessage(Ca2FsTopic.Ca2FsMessage message) {
        producer.sendMessage(message);
    }

    /**
     * <p>发送监听消息</p>
     *
     * @param message 监听消息
     * @author panliyong  2019-11-27 11:20
     */
    public void sendMonitorRequestMessage(Ca2FsTopic.Ca2FsMessage message) {
        producer.sendMessage(message);
    }

    /**
     * <p>发送取消监听的消息</p>
     *
     * @param message 取消监听的消息
     * @author panliyong  2019-11-27 11:21
     */
    public void sendUnMonitorRequestMessage(Ca2FsTopic.Ca2FsMessage message) {
        producer.sendMessage(message);
    }

    /**
     * <p>发送保持消息</p>
     *
     * @param message 保持消息
     * @author panliyong  2019-11-27 11:22
     */
    public void sendHoldRequestMessage(Ca2FsTopic.Ca2FsMessage message) {
        producer.sendMessage(message);
    }

    /**
     * <p>发送取消保持消息</p>
     *
     * @param message 取消保持消息
     * @author panliyong  2019-11-27 11:23
     */
    public void sendUnHoldRequestMessage(Ca2FsTopic.Ca2FsMessage message) {
        producer.sendMessage(message);
    }

    /**
     * <p>发送坐席挂断消息</p>
     *
     * @param message 坐席挂断消息
     * @author panliyong  2019-11-27 11:23
     */
    public void sendAgentHangupRequestMessage(Ca2FsTopic.Ca2FsMessage message) {
        producer.sendMessage(message);
    }

    /**
     * <p>发送机器人转人工消息</p>
     *
     * @param message 机器人转人工消息
     * @author panliyong  2019-11-27 11:24
     */
    public void sendRobot2AgentRequestMessage(Ca2FsTopic.Ca2FsMessage message) {
        producer.sendMessage(message);
    }

    /**
     * <p>发送坐席转坐席消息</p>
     *
     * @param message 坐席转坐席消息
     * @author panliyong  2019-11-27 11:28
     */
    public void sendAgent2AgentRequestMessage(Ca2FsTopic.Ca2FsMessage message) {
        producer.sendMessage(message);
    }

    /**
     * <p>发送坐席转机器人消息</p>
     *
     * @param message 坐席转机器人
     * @author panliyong  2019-11-27 11:29
     */
    public void sendAgent2RobotRequestMessage(Ca2FsTopic.Ca2FsMessage message) {
        producer.sendMessage(message);
    }

    /**
     * <p>发送机器人转外部消息</p>
     *
     * @param message 机器人转外部消息
     * @author panliyong  2019-11-27 11:33
     */
    public void sendRobot2OutRequestMessage(Ca2FsTopic.Ca2FsMessage message) {
        producer.sendMessage(message);
    }
}
