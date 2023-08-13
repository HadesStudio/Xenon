package dev.hely.core.bungee.databases.redis.jedis.subscription;

public interface JedisSubscriptionGenerator<K> {

    K generateSubscription(String message);

}
