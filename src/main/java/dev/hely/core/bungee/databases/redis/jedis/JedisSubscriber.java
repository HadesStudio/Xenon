package dev.hely.core.bungee.databases.redis.jedis;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.databases.redis.jedis
//   Date: Sunday, August 13, 2023 - 2:33 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.google.gson.JsonObject;
import dev.hely.core.bungee.OxygenBungee;
import dev.hely.core.bungee.databases.redis.jedis.subscription.JedisSubscriptionGenerator;
import dev.hely.core.bungee.databases.redis.jedis.subscription.JedisSubscriptionHandler;
import dev.hely.core.bungee.databases.redis.jedis.subscription.impl.JsonJedisSubscriptionGenerator;
import dev.hely.core.bungee.databases.redis.jedis.subscription.impl.StringJedisSubscriptionGenerator;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@SuppressWarnings("rawtypes")
public class JedisSubscriber<K> {

    private static final Map<Class, JedisSubscriptionGenerator> GENERATORS = new HashMap<>();

    static {
        GENERATORS.put(String.class, new StringJedisSubscriptionGenerator());
        GENERATORS.put(JsonObject.class, new JsonJedisSubscriptionGenerator());
    }

    protected final String channel;
    private final Class<K> typeParameter;
    @Getter private final Jedis jedis;
    @Getter private final JedisPubSub pubSub;

    private final JedisSubscriptionHandler<K> jedisSubscriptionHandler;

    public JedisSubscriber(String channel, Class<K> typeParameter, JedisSubscriptionHandler<K> jedisSubscriptionHandler) {
        this.channel = channel;
        this.typeParameter = typeParameter;
        this.jedisSubscriptionHandler = jedisSubscriptionHandler;

        this.pubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                JedisSubscriptionGenerator<K> jedisSubscriptionGenerator = GENERATORS.get(typeParameter);

                if (jedisSubscriptionGenerator != null) {
                    K object = jedisSubscriptionGenerator.generateSubscription(message);
                    JedisSubscriber.this.jedisSubscriptionHandler.handleMessage(object);
                } else {
                    System.out.println("Generator type is null");
                }
            }
        };

        this.jedis = new Jedis(OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getString("database.redis.host"), OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getInt("database.redis.port"));
        this.authenticate();
        this.connect();
    }

    private void authenticate() {
        if (OxygenBungee.getInstance().getRedisBungeeModule().isAuth()) {
            this.jedis.auth(OxygenBungee.getInstance().getRedisBungeeModule().getPassword());
        }
    }

    private void connect() {
        Logger.getGlobal().info("Jedis is now reading on " + this.channel);

        new Thread(() -> {
            try {
                this.jedis.subscribe(this.pubSub, this.channel);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.getGlobal().info("For some odd reason our JedisSubscriber(" + this.channel + ") threw an exception");
                close();
                connect();
            }
        }).start();
    }

    public void close() {
        Logger.getGlobal().info("Jedis is no longer reading on " + this.channel);

        if (this.pubSub != null) {
            this.pubSub.unsubscribe();
        }

        if (this.jedis != null) {
            this.jedis.close();
        }
    }
}
