package dev.hely.core.managers.database.redis.cache;

import dev.hely.core.Oxygen;
import dev.hely.lib.CC;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

import java.util.UUID;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.managers.database.redis.cache
//   Date: Tuesday, July 04, 2023 - 6:07 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

@RequiredArgsConstructor
@Getter
@Setter
public class CachedData {

    private final Oxygen plugin;
    private final UUID uuid;
    private final String name;

    public Document getData() {
        if (!this.plugin.getRedisManager().isRedisConnected()) return null;

        long start = System.currentTimeMillis();

        if (this.uuid != null) {
            try (Jedis jedis = this.plugin.getRedisManager().getSubscriberPool().getResource()) {
                String json = jedis.hget("player-data::uuid", this.uuid.toString());

                if (json == null) {
                    return null;
                }

                return Document.parse(json);
            } catch (Exception e) {
                return null;
            }
        } else {
            try (Jedis jedis = this.plugin.getRedisManager().getSubscriberPool().getResource()) {
                String json = jedis.hget("player-data::name", this.name.toLowerCase());

                if (json == null) {
                    return null;
                }

                return Document.parse(json);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public void update(Document document) {
        if (!this.plugin.getRedisManager().isRedisConnected()) return;

        try (Jedis jedis = plugin.getRedisManager().getSubscriberPool().getResource()) {
            jedis.hset("player-data::uuid", this.uuid.toString(), document.toJson());
            jedis.hset("player-data::name", this.name.toLowerCase(), document.toJson());

            jedis.expire("player-data::uuid", 60 * 60);
            jedis.expire("player-data::name", 60 * 60);
        } catch (Exception e) {
            if (this.plugin.getRedisManager().isRedisConnected() && System.currentTimeMillis() - this.plugin.getRedisManager().getLastConnect() >= 3000L) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(CC.translate("&eFailed to cache data to redis. Please make sure your redis allows saving data."));
            }
        }
    }
}
