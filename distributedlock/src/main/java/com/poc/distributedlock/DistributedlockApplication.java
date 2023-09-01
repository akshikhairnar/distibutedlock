package com.poc.distributedlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

@SpringBootApplication
public class DistributedlockApplication {

	private static final String LOCK_NAME = "lock";

	public static void main(String[] args) {
		SpringApplication.run(DistributedlockApplication.class, args);
	}

	@Bean(destroyMethod = "destroy")
	public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
		return new RedisLockRegistry(redisConnectionFactory, LOCK_NAME);
	}

}
