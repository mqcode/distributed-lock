package com.github.io.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    private static final String SINGLE_REDIS_ADDRESS = "redis://127.0.0.1:6379";

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer()
                .setAddress(SINGLE_REDIS_ADDRESS);

        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;

    }
}
