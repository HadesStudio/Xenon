package dev.hely.core.bukkit.tag.menu.data.button;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.core.bukkit.tag.Tag;
import dev.hely.core.bukkit.tag.menu.CustomCategoryMenu;
import dev.hely.core.bukkit.tag.menu.data.action.CustomButtonAction;
import dev.hely.core.bukkit.tag.menu.data.category.CustomCategory;
import dev.hely.core.bukkit.tag.packet.implement.TagUpdatePacket;
import dev.hely.lib.util.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
public class CustomButton {
    private ItemStack button;
    private Integer slot;
    private CustomButtonAction actions;
    private String subMenu;
    private int amount;
    private List<String> ownsLore;
    private List<String> noPermsLore;
    private String tagName;

    public void execute(Player player) {
        Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

        switch (this.actions) {
            case OPEN_CATEGORY: {
                CustomCategory category = OxygenBukkit.getInstance().getTagManager().getCategoryByName(this.subMenu);

                player.closeInventory();
                new CustomCategoryMenu(category).openMenu(player);
                break;
            }
            case TOGGLE_TAG: {
                if (OxygenBukkit.getInstance().getTagManager().getTag(tagName) == null) {
                    player.sendMessage(CC.translate("&6[Hely Development] &7The tag by the name of '&6" + tagName + "&7' does not exist. We are creating it for you. Please try again momentarily."));
                    Tag tag = new Tag(tagName);
                    tag.save();

                    OxygenBukkit.getInstance().getRedisModule().sendPacket(new TagUpdatePacket(tag.getName(), false));
                    break;
                }
                Tag tag = OxygenBukkit.getInstance().getTagManager().getTag(tagName);
                if (!player.hasPermission(tag.getPermission())) {
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.messages.no-permission")));
                    return;
                }
                if (profile.getTag() == null || profile.getTag().isEmpty()) {
                    profile.setTag(tag.getName());
                    profile.save();
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.messages.activated-tag")));
                    return;
                }

                if (profile.getTag().equals(tag.getName())) {
                    profile.setTag(null);
                    profile.save();
                    player.sendMessage(CC.translate(OxygenBukkit.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.messages.reset-tag")));
                    return;
                }

                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.messages.need-reset")));
            }
            case REMOVE_TAG: {
                if (profile.getTag() != null) {
                    profile.setTag(null);
                    profile.save();
                }

                player.sendMessage(CC.translate(OxygenBukkit.getInstance().getTagsMenuConfig().getConfiguration().getString("menu.messages.reset-tag")));
                player.updateInventory();
            }
        }
    }
}
