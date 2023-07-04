package dev.hely.core.managers.database.redis;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.*;
import dev.hely.core.Oxygen;
import dev.hely.core.managers.database.redis.packet.Packets;
import dev.hely.core.managers.database.redis.packet.RedisPacket;
import dev.hely.core.managers.database.redis.utils.RedisCommand;
import dev.hely.core.managers.modules.ModuleLoader;
import dev.hely.lib.CC;
import dev.hely.lib.adapter.BlockVectorAdapter;
import dev.hely.lib.adapter.LocationAdapter;
import dev.hely.lib.adapter.PotionEffectAdapter;
import dev.hely.lib.adapter.VectorAdapter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisManager implements ModuleLoader {
    public static final String MODULE_NAME = "Redis Manager";
    private boolean enabled;

    public ExecutorService REDIS_EXECUTOR = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("Redis Thread - %d").build());
    @Getter private String channel;
    @Getter private JedisPool subscriberPool, publisherPool;
    @Getter
    private long lastConnect = -1;
    @Getter private JedisPubSub pubsub;
    private JsonParser PARSER = new JsonParser();
    private boolean auth;
    private String password;
    private Packets packets = new Packets();
    public Map<String, RedisPacket> packetData = new HashMap<>();

    @Override
    public void enable() {
        ConsoleCommandSender bukkitCommandSender = Bukkit.getConsoleSender();

        String host = Oxygen.getInstance().getDatabase_config().getConfiguration().getString("database.redis.host");
        int port = Oxygen.getInstance().getDatabase_config().getConfiguration().getInt("database.redis.port");
        this.password = Oxygen.getInstance().getDatabase_config().getConfiguration().getString("database.redis.auth.password");
        this.auth = this.password != null && this.password.length() > 0;
        this.channel = Oxygen.getInstance().getDatabase_config().getConfiguration().getString("database.redis.channel");

        try {
            this.subscriberPool = this.publisherPool = new JedisPool(new JedisPoolConfig(), host, port,
                    30_000, this.password.isEmpty() ? null : this.password, 0, null);

            this.subscriberPool.getResource();
            this.setupPubSub();

            try {
                Thread.sleep(1500L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            this.subscriberPool = null;
            this.publisherPool = null;
            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
            bukkitCommandSender.sendMessage(CC.translate("&d&lRedis - Local Pool"));
            bukkitCommandSender.sendMessage(CC.translate("&cFailed to initialize redis."));
            bukkitCommandSender.sendMessage(CC.translate("&c" + e.getMessage()));
            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
        }

        if (this.isRedisConnected()) {
            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
            bukkitCommandSender.sendMessage(CC.translate("&dSuccessfuly enabled &d&lRedis Database&d."));
            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
        } else {
            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
            bukkitCommandSender.sendMessage(CC.translate("&d&lRedis - Local Pool"));
            bukkitCommandSender.sendMessage(CC.translate("&cThe server could not connect to redis local pool."));
            bukkitCommandSender.sendMessage(CC.translate("&7&m=================================================================================="));
        }
        this.lastConnect = System.currentTimeMillis();
        this.enabled = true;
    }

    @Override
    public void disable() {

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
        return MODULE_NAME;
    }


    public void sendRequest(RedisPacket packet, boolean here) {
        if (!isRedisConnected() || here) {
            try {
                packet.onReceive(packet.getData().get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        JsonObject object = new JsonObject();
        object.addProperty("identifier", packet.getIdentifier());
        object.add("packetData", packet.getData().get());

        REDIS_EXECUTOR.execute(() -> {
            this.sendRequest(this.channel, object);
        });
    }

    public void sendProxyRequest(JsonObject object) {
        if (Oxygen.getInstance().getConfig().getConfig().getString("plugin-settings.server-type").equalsIgnoreCase("velocity")) {
            this.sendRequest(ChannelType.VELOCITY.getName(), object);
        } else {
            this.sendRequest(ChannelType.BUNGEE.getName(), object);
        }
    }

    public void sendRequest(String channel, JsonObject object) {
        try {
            if (object == null) {
                throw new IllegalStateException("Object that was being sent was null!");
            }

            try (Jedis jedis = this.publisherPool.getResource()) {
                try {
                    if (this.auth) {
                        jedis.auth(this.password);
                    }

                    jedis.publish(channel, object.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupPubSub() {
        this.pubsub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (channel.equalsIgnoreCase(Oxygen.getInstance().getRedisManager().getChannel())) {
                    JsonObject object;

                    try {
                        object = PARSER.parse(message).getAsJsonObject();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        return;
                    }

                    String identifier = object.get("identifier").getAsString();
                    JsonObject data = object.get("packetData").getAsJsonObject();

                    RedisPacket packet = packetData.get(identifier);

                    if (isRedisConnected()) {
                        if (packet != null) {
                            try {
                                packet.onReceive(data);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Bukkit.getConsoleSender().sendMessage(CC.translate("&eError parsing redis packet &7(&b" + packet.getIdentifier() + "&7) - &c" + e.getMessage()));
                            }
                        }
                    }
                }
            }
        };

        REDIS_EXECUTOR.execute(() -> {
            try (Jedis jedis = this.subscriberPool.getResource()) {
                String[] channels = new String[2];

                channels[0] = this.channel;

                if (Oxygen.getInstance().getConfig().getConfig().getString("plugin-settings.server-type").equalsIgnoreCase("velocity")) {
                    channels[1] = ChannelType.VELOCITY.getName();
                } else {
                    channels[1] = ChannelType.BUNGEE.getName();
                }


                jedis.subscribe(this.pubsub, channels);
            } catch (Exception e) {
                subscriberPool = null;
                publisherPool = null;
            }
        });
    }

    public boolean isRedisConnected() {
        return this.subscriberPool != null && !this.subscriberPool.isClosed() && this.publisherPool != null && !this.publisherPool.isClosed();
    }

    public void close() {
        try {
            if (this.pubsub != null && this.pubsub.isSubscribed()) {
                this.pubsub.unsubscribe();
            }
            if (this.subscriberPool != null && !this.subscriberPool.isClosed()) {
                this.subscriberPool.close();
            }
            if (this.publisherPool != null && !this.publisherPool.isClosed()) {
                this.publisherPool.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerPacket(RedisPacket packet) {
        this.packetData.put(packet.getIdentifier(), packet);
    }

    public void setupPackets() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        int i = 0;
        for (Field field : this.packets.getClass().getDeclaredFields()) {
            if (RedisPacket.class.isAssignableFrom(field.getType()) && field.getType().getSuperclass() == RedisPacket.class) {
                boolean accessible = field.isAccessible();

                field.setAccessible(true);
                field.set(this.packets, field.getType().getDeclaredConstructor().newInstance());

                RedisPacket packet = (RedisPacket) field.get(this.packets);

                field.setAccessible(accessible);

                this.packetData.put(packet.getIdentifier(), packet);
                i++;
            }
        }
    }
}