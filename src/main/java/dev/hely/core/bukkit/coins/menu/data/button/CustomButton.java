package dev.hely.core.bukkit.coins.menu.data.button;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.core.bukkit.coins.item.type.ItemTypes;
import dev.hely.core.bukkit.coins.menu.data.action.CustomButtonAction;
import dev.hely.core.bukkit.coins.menu.data.category.CustomCategory;
import dev.hely.core.bukkit.coins.menu.CustomCategoryMenu;
import dev.hely.core.bukkit.profile.Profile;
import dev.hely.lib.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created By LeandroSSJ
 * Created on 13/03/2022
 */
@Getter
@Setter
public class CustomButton {

    private ItemStack button;
    private Integer slot;
    private CustomButtonAction actions;
    private String subMenu;
    private int price;
    private int amount;
    private List<String> commands;
    private List<String> lore;
    private ItemTypes itemType;
    private String name;

    public void execute(Player player) {
        switch (this.actions) {
            case BUY: {
                if (this.commands == null || this.commands.isEmpty()) {
                    break;
                }
                if (OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId()) == null) {
                    player.sendMessage(CC.translate("&cputa your profile is null"));
                    return;
                }
                Profile profile = OxygenBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());

                if (profile.getCurrency() < this.price) {
                    player.sendMessage(CC.translate("&cputa not enough money"));

                    return;
                }

                if (player.getInventory().firstEmpty() == -1) {
                    player.sendMessage(CC.translate("&cputa inventory full"));
                    return;
                }

                profile.setCurrency(profile.getCurrency() - this.price);
                profile.save();

                player.sendMessage(CC.translate("&aputa purchase successfull"));

                getCommands().forEach(item -> Bukkit.dispatchCommand(Bukkit
                        .getConsoleSender(), item
                        .replace("%player%", player.getName())));

                break;
            }
            case OPEN_CATEGORY: {
                OxygenBukkit.getInstance().debug_message("&b" + player.getName() + " &chas just clicked inside the &4Coins Menu&c.");
                OxygenBukkit.getInstance().debug_message("&bDebugging Information:");
                if (this.subMenu == null) {
                    OxygenBukkit.getInstance().debug_message("&c- Sub-Menu: &4Sub-Menu IS NULL (URGENT ERROR)");
                    break;
                } else {
                    OxygenBukkit.getInstance().debug_message("&c- Sub-Menu: &4" + subMenu);
                }

                CustomCategory category = OxygenBukkit.getInstance().getStoreManager().getCategoryByName(this.subMenu);

                if (category == null) {
                    OxygenBukkit.getInstance().debug_message("&c- Category: &4Category IS NULL (URGENT ERROR)");
                    break;
                } else {
                    OxygenBukkit.getInstance().debug_message("&c- Category: &4" + category);
                }

                player.closeInventory();
                new CustomCategoryMenu(category).openMenu(player);
                break;
            }
        }
    }
}
