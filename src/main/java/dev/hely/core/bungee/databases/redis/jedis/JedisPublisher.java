package dev.hely.core.bungee.databases.redis.jedis;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bungee.databases.redis.jedis
//   Date: Sunday, August 13, 2023 - 2:01 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import dev.hely.core.bungee.OxygenBungee;
import dev.hely.core.bungee.databases.redis.RedisBungeeModule;
import lombok.RequiredArgsConstructor;
import redis.clients.jedis.Jedis;

@RequiredArgsConstructor
public class JedisPublisher<K> {
    public void write(K message) {
        Jedis jedis = null;

        try {
            jedis = OxygenBungee.getInstance().getRedisBungeeModule().getJedisPool().getResource();

            if (OxygenBungee.getInstance().getRedisBungeeModule().isAuth()) {
                jedis.auth(OxygenBungee.getInstance().getRedisBungeeModule().getPassword());
            }

            jedis.publish(OxygenBungee.getInstance().getRedisBungeeModule().getChannel(), message.toString());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void writeDirectly(String message) {
        Jedis jedis = null;

        try {
            jedis = OxygenBungee.getInstance().getRedisBungeeModule().getJedisPool().getResource();

            if (OxygenBungee.getInstance().getRedisBungeeModule().isAuth()) {
                jedis.auth(OxygenBungee.getInstance().getRedisBungeeModule().getPassword());
            }

            jedis.publish(OxygenBungee.getInstance().getRedisBungeeModule().getChannel(), message);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}

