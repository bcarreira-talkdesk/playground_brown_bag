package com.talkdesk.playground.health_check

import org.slf4j.LoggerFactory
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisConnectionHealthIndicator(
    private val redisConnectionFactory: LettuceConnectionFactory
) : HealthIndicator {
    private var connection: RedisConnection? = null
    private val logger = LoggerFactory.getLogger(this::class.java)

    private fun getConnection(): RedisConnection {
        if (connection == null) {
            connection = redisConnectionFactory.connection
        }
        return connection!!
    }

    override fun health(): Health {
        return try {
            val connection = getConnection()
            if (connection.ping() != null) {
                Health.up().build()
            } else {
                val message = "Restarting application due to Redis connection timeout"
                logger.warn(message)
                Health.outOfService().build()
            }
        } catch (e: Exception) {
            logger.warn("Restarting application due to an error ${e.message}", e)
            Health.down(e).build()
        }
    }
}
