package dev.hely.core.bungee.databases.redis.jedis.subscription.impl;


import dev.hely.core.bungee.databases.redis.jedis.subscription.JedisSubscriptionGenerator;

public class StringJedisSubscriptionGenerator implements JedisSubscriptionGenerator<String> {

    @Override
    public String generateSubscription(String message) {
        return message;
    }

}
