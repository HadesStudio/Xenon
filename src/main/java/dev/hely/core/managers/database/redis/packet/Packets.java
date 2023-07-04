package dev.hely.core.managers.database.redis.packet;

import dev.hely.core.managers.database.redis.packet.implement.data.PlayerAttachmentUpdatePacket;
import dev.hely.core.managers.database.redis.packet.implement.data.PlayerPermissionUpdatePacket;
import dev.hely.core.managers.database.redis.packet.implement.data.PlayerPermissionsClearPacket;
import dev.hely.core.managers.database.redis.packet.implement.data.global.GlobalPlayerStatusUpdatePacket;
import dev.hely.core.managers.database.redis.packet.implement.data.global.GlobalPlayerUpdatePacket;
import dev.hely.core.managers.database.redis.packet.implement.data.server.ServerUpdatePacket;
import dev.hely.core.managers.database.redis.packet.implement.other.PlayerMessagePacket;
import dev.hely.core.managers.database.redis.packet.implement.rank.GrantsUpdatePacket;
import dev.hely.core.managers.database.redis.packet.implement.rank.RankDeletePacket;
import dev.hely.core.managers.database.redis.packet.implement.rank.RankUpdatePacket;

public class Packets {

    private GlobalPlayerUpdatePacket globalPlayerUpdatePacket;
    private ServerUpdatePacket serverUpdatePacket;
    private PlayerAttachmentUpdatePacket playerAttachmentUpdatePacket;
    private PlayerPermissionUpdatePacket playerPermissionUpdatePacket;
    private PlayerMessagePacket playerMessagePacket;
    private GrantsUpdatePacket grantsUpdatePacket;
    private RankDeletePacket rankDeletePacket;
    private RankUpdatePacket rankUpdatePacket;
    private GlobalPlayerStatusUpdatePacket globalPlayerStatusUpdatePacket;
    private PlayerPermissionsClearPacket playerPermissionsClearPacket;
}
