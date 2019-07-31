package com.unioncloud.redission.config;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Data;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Redisson 哨兵模式 的配置文件</p>
 * @author panliyong  2018/5/2 15:56
 */

@ConditionalOnProperty(name = "spring.redisson.type", havingValue = "sentinel", matchIfMissing = true)
@ConfigurationProperties(prefix = "spring.redisson.sentinel")
@Configuration
@Data
public class RedissonSentinelConfig {

    private List<String> sentinelAddresses = new ArrayList<>();

    private String masterName;

    private String passWord;


    @Bean
    Config config() {

        final int size = sentinelAddresses.size();
        if (sentinelAddresses.isEmpty()) {
            throw new RuntimeException("redisson sentinel node address is empty!");
        }
        String[] nodeArray = new String[size];
        String[] nodes = sentinelAddresses.toArray(nodeArray);
        Config config = new Config();
        config.useSentinelServers()
                .addSentinelAddress(nodes)
                .setMasterName(masterName)
                .setPassword(passWord)
                .setConnectTimeout(1000 * 60 * 3);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return config;
    }
}