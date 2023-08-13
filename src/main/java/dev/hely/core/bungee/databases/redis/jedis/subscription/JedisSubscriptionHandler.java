package dev.hely.core.bungee.databases.redis.jedis.subscription;

public interface JedisSubscriptionHandler<K> {

    void handleMessage(K object);

}
