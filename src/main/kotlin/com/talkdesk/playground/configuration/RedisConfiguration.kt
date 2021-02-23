package com.talkdesk.playground.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisClusterConfiguration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory


@Configuration
class RedisConfiguration() {

    @Bean
    @Profile("!dev")
    fun redisStandaloneConnectionFactory(): LettuceConnectionFactory {
        val redisStandaloneConfiguration = RedisStandaloneConfiguration()
        return LettuceConnectionFactory(
                redisStandaloneConfiguration
        )
    }

    @Bean
    @Primary
    fun redisClusterConnectionFactory(): LettuceConnectionFactory {
        val redisStandaloneConfiguration = RedisClusterConfiguration("redis-node1:6379,redis-node2:6379,redis-node3:6379".split(","))
        return LettuceConnectionFactory(
                redisStandaloneConfiguration
        )
    }

}
