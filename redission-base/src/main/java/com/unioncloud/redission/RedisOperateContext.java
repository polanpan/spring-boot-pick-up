package com.unioncloud.redission;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> 业务逻辑中使用的Redis 操作集中使用模块 上下文 </p>
 * @author panliyong  2018/6/4 16:41
 */
@Configuration
public class RedisOperateContext {
    @Bean
    public RedissonClient redisson(@Autowired Config config) {
        return Redisson.create(config);
    }
}
