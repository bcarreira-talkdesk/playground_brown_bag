package com.talkdesk.playground.controller

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/test")
class Test(
    val redisTemplate: RedisTemplate<String, String>
) {

    @PostMapping("/{key}/{value}")
    fun createKey(@PathVariable key: String, @PathVariable value: String) {
        redisTemplate.opsForValue().set(key, value)
    }

    @GetMapping("/{key}")
    fun getKey(@PathVariable key: String): String? {
        return redisTemplate.opsForValue().get(key)
    }
}