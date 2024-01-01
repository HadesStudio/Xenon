package net.ascend.core.bukkit.modules.database.redis;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.modules.database.redis
//   Date: Tuesday, September 12, 2023 - 6:24 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import redis.clients.jedis.Jedis;

public interface RedisCommand<T> {
    public T execute(Jedis redis);
}
