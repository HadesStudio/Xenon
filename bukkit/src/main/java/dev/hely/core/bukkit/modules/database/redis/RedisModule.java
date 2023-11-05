package dev.hely.core.bukkit.modules.database.redis;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.modules.ModuleLoader;
import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import dev.hely.core.bukkit.modules.database.redis.packet.PacketListener;
import dev.hely.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name OxygenGlobal
 */
@Getter
public class RedisModule implements ModuleLoader {
    private boolean enabled;

    private Gson gson = new GsonBuilder().create();
    private int maxPools;
    private int timeout;
    private boolean auth;
    private String password;
    private String channel;
    private JedisPool jedisPool;

    private Multimap<Object, Method> listeners;
    private Set<String> channels;

    @Override
    public void enable() {
        this.maxPools = 20;
        this.timeout = 2000;
        this.listeners = ArrayListMultimap.create();
        this.channels = new HashSet<>();

        String host = OxygenBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.redis.host");
        int port = OxygenBukkit.getInstance().getDatabaseConfig().getConfiguration().getInt("database.redis.port");
        this.password = OxygenBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.redis.auth.password");
        this.auth = this.password != null && this.password.length() > 0;
        this.channel = OxygenBukkit.getInstance().getDatabaseConfig().getConfiguration().getString("database.redis.channel");

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxPools);
        jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
        if (!this.password.isEmpty()) {
            try (Jedis jedis = jedisPool.getResource()){
                jedis.auth(getPassword());
            } catch (JedisException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void disable() {
        enabled = false;

        if (isRedisConnected()) {
            jedisPool.close();
            jedisPool = null;
            listeners.clear();
            channels.clear();
        }
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
        return "Redis";
    }


    public boolean isRedisConnected() {
        return this.jedisPool != null && !this.jedisPool.isClosed();
    }

    public void registerListener(PacketListener listener) {
        List<Method> declaredMethods = Arrays.asList(listener.getClass().getDeclaredMethods());
        if (declaredMethods.stream().noneMatch(method -> method.isAnnotationPresent(IncomingPacketHandler.class))) return;

        Set<Method> methods = declaredMethods.stream()
                .filter(method -> method.isAnnotationPresent(IncomingPacketHandler.class))
                .filter(method -> method.getParameters().length == 1)
                .collect(Collectors.toSet());

        methods.forEach(method -> channels.addAll(Arrays.asList(getChannel() + "@" + method.getParameterTypes()[0].getSimpleName())));

        listeners.putAll(listener, methods);
    }

    public void sendPacket(Packet packet) {
        if (isRedisConnected()) {
            try (Jedis jedis = jedisPool.getResource()) {
                String channel = getChannel() + '@' + packet.getClass().getSimpleName();

                jedis.publish(channel, OxygenBukkit.PLAIN_GSON.toJson(packet));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private JedisPubSub getJedisPubSub() {
        JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (isRedisConnected()) {
                    for (Map.Entry<Object, Method> entry : listeners.entries()) {
                        if (Objects.equals(getChannel() + '@' + entry.getValue().getParameterTypes()[0].getSimpleName(), channel)) {
                            try {
                                entry.getValue().invoke(entry.getKey(), OxygenBukkit.PLAIN_GSON.fromJson(message, entry.getValue().getParameterTypes()[0]));
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        };

        return jedisPubSub;
    }


    public void runCommand(Consumer<Jedis> consumer) {
        Jedis jedis = jedisPool.getResource();
        if (jedis != null) {
            consumer.accept(jedis);
            jedisPool.returnResource(jedis);
        }
    }

    public void subscribeJedis() {
        CompletableFuture.runAsync(() -> {
            runCommand(command -> command.subscribe(getJedisPubSub(), channels.toArray(new String[0])));
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    public <T> T executeCommand(RedisCommand<T> command) {
        return this.executeCommand(command, jedisPool);
    }

    public <T> T executeBackendCommand(RedisCommand<T> command) {
        return this.executeCommand(command, jedisPool);
    }

    private <T> T executeCommand(RedisCommand<T> command, JedisPool pool) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return command.execute(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
