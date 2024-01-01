package net.ascend.core.bungee.database.redis;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.ascend.core.bukkit.modules.database.redis.packet.Packet;
import net.ascend.core.bukkit.modules.database.redis.packet.PacketListener;
import net.ascend.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import net.ascend.core.bungee.XenonBungee;
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
 * Created on 05/08/2023
 * Protect Name Oxygen
 */
@Getter
public class RedisBungeeModule {

    private boolean enabled;

    private boolean auth;
    private String password;
    private String channel;
    private JedisPool jedisPool;

    private Multimap<Object, Method> listeners;
    private Set<String> channels;

    public RedisBungeeModule() {
        String host = XenonBungee.getInstance().getDatabaseConfig().getConfiguration().getString("database.redis.host");
        int port = XenonBungee.getInstance().getDatabaseConfig().getConfiguration().getInt("database.redis.port");
        this.password = XenonBungee.getInstance().getDatabaseConfig().getConfiguration().getString("database.redis.auth.password");
        this.auth = this.password != null && this.password.length() > 0;
        this.channel = XenonBungee.getInstance().getDatabaseConfig().getConfiguration().getString("database.redis.channel");

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

    public void disable() {
        if (jedisPool != null) {
            jedisPool.close();
        }

        enabled = false;
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

            jedis.publish(channel, XenonBungee.PLAIN_GSON.toJson(packet));
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
                            entry.getValue().invoke(entry.getKey(), XenonBungee.PLAIN_GSON.fromJson(message, entry.getValue().getParameterTypes()[0]));
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

