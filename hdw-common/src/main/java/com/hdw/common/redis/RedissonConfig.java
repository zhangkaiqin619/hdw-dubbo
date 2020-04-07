package com.hdw.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @Description Redsson配置
 * @Author TuMingLong
 * @Date 2020/4/1 12:26
 */
@Slf4j
@Configuration
public class RedissonConfig {

    public RedissonConfig() {
        log.debug("-----Redisson init-----");
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient RedissonClient() throws IOException {
        Config config = Config.fromYAML(new ClassPathResource("redisson-single.yml").getInputStream());
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
