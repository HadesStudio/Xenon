package dev.hely.core.modules.database.redis;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.hely.core.Oxygen;
import dev.hely.core.modules.ModuleLoader;
import dev.hely.core.modules.database.redis.packet.Packet;
import dev.hely.core.modules.database.redis.packet.PacketListener;
import dev.hely.core.modules.database.redis.packet.annotation.IncomingPacketHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created By LeandroSSJ
 * Created on 04/07/2023
 * Protect Name Oxygen
 */
@Getter
public class RedisModule implements ModuleLoader {

    private boolean enabled;

    private boolean auth;
    private String password;
    private String channel;
    private JedisPool jedisPool;

    private Multimap<Object, Method> listeners;
    private Set<String> channels;

    @Override
    public void enable() {
        String host = Oxygen.getInstance().getDatabaseConfig().getConfiguration().getString("database.redis.host");
        int port = Oxygen.getInstance().getDatabaseConfig().getConfiguration().getInt("database.redis.port");
        this.password = Oxygen.getInstance().getDatabaseConfig().getConfiguration().getString("database.redis.auth.password");
        this.auth = this.password != null && this.password.length() > 0;
        this.channel = Oxygen.getInstance().getDatabaseConfig().getConfiguration().getString("database.redis.channel");

        this.jedisPool = new JedisPool(new JedisPoolConfig(), host, port, 30_000);
        if (!this.password.isEmpty()) {
            try (Jedis jedis = this.jedisPool.getResource()){
                jedis.auth(this.password);
            } catch (JedisException ex) {
                ex.printStackTrace();
            }
        }

        this.listeners = ArrayListMultimap.create();
        this.channels = new HashSet<>();

        enabled = true;
    }

    @Override
    public void disable() {
        if (jedisPool != null) {
            jedisPool.close();
        }

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
        return "Redis";
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
        try (Jedis jedis = jedisPool.getResource()) {
            String channel = getChannel() + '@' + packet.getClass().getSimpleName();

            jedis.publish(channel, Oxygen.PLAIN_GSON.toJson(packet));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JedisPubSub getJedisPubSub() {
        JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                for (Map.Entry<Object, Method> entry : listeners.entries()) {
                    if (Objects.equals(getChannel() + '@' + entry.getValue().getParameterTypes()[0].getSimpleName(), channel)) {
                        try {
                            entry.getValue().invoke(entry.getKey(), Oxygen.PLAIN_GSON.fromJson(message, entry.getValue().getParameterTypes()[0]));
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
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
}
