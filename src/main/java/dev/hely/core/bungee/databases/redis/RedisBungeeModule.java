package dev.hely.core.bungee.databases.redis;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import dev.hely.core.bukkit.modules.database.redis.packet.Packet;
import dev.hely.core.bukkit.modules.database.redis.packet.PacketListener;
import dev.hely.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import dev.hely.core.bungee.OxygenBungee;
import dev.hely.core.bungee.databases.redis.jedis.JedisPublisher;
import dev.hely.core.bungee.server.data.ServerData;
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
    private JedisPublisher<JsonObject> serverManager;

    public RedisBungeeModule() {
        String host = OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getString("database.redis.host");
        int port = OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getInt("database.redis.port");
        this.password = OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getString("database.redis.auth.password");
        this.auth = this.password != null && this.password.length() > 0;
        this.channel = OxygenBungee.getInstance().getDatabaseConfig().getConfiguration().getString("database.redis.channel");

        this.jedisPool = new JedisPool(new JedisPoolConfig(), host, port, 30_000);
        if (!this.password.isEmpty()) {
            try (Jedis jedis = this.jedisPool.getResource()){
                jedis.auth(this.password);
            } catch (JedisException ex) {
                ex.printStackTrace();
            }
        }

        this.serverManager = new JedisPublisher<>();
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

            jedis.publish(channel, OxygenBungee.PLAIN_GSON.toJson(packet));
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
                            entry.getValue().invoke(entry.getKey(), OxygenBungee.PLAIN_GSON.fromJson(message, entry.getValue().getParameterTypes()[0]));
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

    public void writeServerCreate(String serverName, String serverAddress, int serverPort) {
        JsonObject data = new JsonObject();
        data.addProperty("serverName", serverName);
        data.addProperty("serverAddress", serverAddress);
        data.addProperty("serverPort", serverPort);

        writeMessage("CREATE_SERVER", data);
    }

    public void writeServerDelete(ServerData serverData) {
        JsonObject data = new JsonObject();
        data.addProperty("serverName", serverData.getServerName());
        data.addProperty("serverAddress", serverData.getInetSocketAddress().getAddress().getHostAddress());
        data.addProperty("serverPort", serverData.getInetSocketAddress().getPort());

        writeMessage("DELETE_SERVER", data);
    }

    private void writeMessage(String channel, JsonObject messageData) {

        JsonObject object = new JsonObject();
        object.addProperty("type", channel);
        object.add("data", messageData);

        serverManager.write(object);
    }
}

