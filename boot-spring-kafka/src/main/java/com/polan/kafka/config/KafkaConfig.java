package com.unioncloud.callability.kafka;

import com.unioncloud.callability.common.proto.message.Ca2FsTopic;
import com.unioncloud.callability.kafka.serializer.Ca2FsTopicProtoSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> kafka 配置</p>
 *
 * @author panliyong  2018/5/30 10:42
 */
@Configuration
@EnableKafka
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    /**
     * 构建 ca2Fs kafka 生产者
     */
    @Bean(name = "ca2FsKafkaProducer")
    public KafkaTemplate<String, Ca2FsTopic.Ca2FsMessage> ca2FsKafkaTemplate() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Ca2FsTopicProtoSerializer.class);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
    }

    /**
     * 构建 ca2Acd kafka 生产者
     */
    @Bean(name = "ca2AcdKafkaProducer")
    @Primary
    public KafkaTemplate<String, String> ca2AcdKafkaTemplate() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
    }

    /**
     * 构建 fs2Ca kafka 消费者工厂
     */
    @Bean
    public ConsumerFactory<String, Ca2FsTopic.Ca2FsMessage> fs2CaKafkaConsumerFactory() {
        Map<String, Object> configProps = FsKafkaProperties.buildProperties(kafkaProperties);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    /**
     * 构建 cad2Ca kafka 消费者工厂
     */
    @Bean
    public ConsumerFactory<String, String> cad2CaKafkaConsumerFactory() {
        Map<String, Object> configProps = AcdKafkaProperties.buildProperties(kafkaProperties);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerStringContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cad2CaKafkaConsumerFactory());

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Ca2FsTopic.Ca2FsMessage> kafkaListenerCa2FsMessageContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Ca2FsTopic.Ca2FsMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(fs2CaKafkaConsumerFactory());
        return factory;
    }

}