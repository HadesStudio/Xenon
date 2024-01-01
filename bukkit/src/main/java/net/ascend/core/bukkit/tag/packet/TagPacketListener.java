package net.ascend.core.bukkit.tag.packet;

import net.ascend.core.bukkit.modules.database.redis.packet.PacketListener;
import net.ascend.core.bukkit.modules.database.redis.packet.annotation.IncomingPacketHandler;
import net.ascend.core.bukkit.tag.Tag;
import net.ascend.core.bukkit.tag.TagManager;
import net.ascend.core.bukkit.tag.packet.implement.TagImportPacket;
import net.ascend.core.bukkit.tag.packet.implement.TagUpdatePacket;
import lombok.AllArgsConstructor;

/**
 * Created By LeandroSSJ
 * Created on 07/07/2023
 * Protect Name Xenon
 */
@AllArgsConstructor
public class TagPacketListener implements PacketListener {

    private final TagManager tagManager;

    @IncomingPacketHandler
    public void onTagUpdate(TagUpdatePacket packet) {
        Tag tag = tagManager.getTag(packet.getName());
        if (packet.isRemove()) {
            if (tag != null) {
                tag.remove();
            }
            return;
        }
        if (tag != null) {
            tag.load();
        } else {
            Tag newTag = new Tag(packet.getName());
            newTag.load();

            tagManager.getTags().add(newTag);
        }
    }

    @IncomingPacketHandler
    public void onTagImport(TagImportPacket packet) {
        tagManager.setupTags();
    }
}
