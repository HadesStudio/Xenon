package dev.hely.core.leandro.redis;

import dev.hely.core.Oxygen;
import dev.hely.core.managers.modules.ModuleLoader;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.function.Consumer;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Oxygen
 */
@Getter
public class RedisManager implements ModuleLoader {

    private boolean enabled;

    private boolean auth;
    private String password;
    private String channel;
    private JedisPool jedisPool;

    @Override
    public void enable() {
        String host = Oxygen.getInstance().getDatabaseConfig().getConfiguration().getString("database.redis.host");
        int port = Oxygen.getInstance().getDatabaseConfig().getConfiguration().getInt("database.redis.port");
        this.password = Oxygen.getInstance().getDatabaseConfig().getConfiguration().getString("database.redis.auth.password");
        this.auth = this.password != null && this.password.length() > 0;
        this.channel = Oxygen.getInstance().getDatabaseConfig().getConfiguration().getString("database.redis.channel");

        this.jedisPool = new JedisPool(new JedisPoolConfig(), host, port, 30_000, this.password.isEmpty() ? null : this.password, 0, null);
        this.jedisPool.getResource();

        this.connect();

        enabled = true;
    }

    @Override
    public void disable() {
        // redis close
        enabled = false;
    }

    @Override
    public void reload() {

    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getName() {
        return "Redis Manager";
    }

    public void connect() {
        new Thread(() -> this.runCommand(jedis -> {
            jedis.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    if (channel.equalsIgnoreCase(Oxygen.getInstance().getRedisManager().getChannel())) {
                        String[] args = message.split(";");
                        String packet = args[0];

                        if (packet.equalsIgnoreCase("staffchat")) { // EXAMPLE
                            String server = args[1];
                            String staff = args[2];

                            // STAFF JOINED SERVER
                        }

                    }
                }
            }, this.channel);
        })).start();
    }

    public void write(String message) {
        Jedis jedis = this.jedisPool.getResource();

        if (jedis != null) {
            if (!this.password.isEmpty()) {
                jedis.auth(password);
            }

            jedis.publish(channel, message);
        }
    }

    public void runCommand(Consumer<Jedis> consumer) {
        Jedis jedis = this.jedisPool.getResource();

        if (jedis != null) {
            if (!this.password.isEmpty()) {
                jedis.auth(password);
            }

            consumer.accept(jedis);
            this.jedisPool.returnResource(jedis);
        }
    }
}
