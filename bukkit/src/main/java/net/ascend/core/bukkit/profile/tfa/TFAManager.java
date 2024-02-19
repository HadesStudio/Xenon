package net.ascend.core.bukkit.profile.tfa;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.bukkit.profile.tfa
//   Date: Thursday, August 10, 2023 - 5:55 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import net.ascend.core.bukkit.XenonBukkit;
import net.ascend.core.bukkit.modules.ModuleLoader;
import net.ascend.core.bukkit.profile.Profile;
import net.ascend.core.bukkit.profile.tfa.data.AuthData;
import net.ascend.core.bukkit.profile.tfa.utils.TFAQRGenerator;
import net.ascend.core.bukkit.profile.tfa.utils.TFAUtil;
import net.ascend.core.bukkit.lib.CC;
import net.ascend.core.bukkit.lib.maker.ItemMaker;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class TFAManager implements ModuleLoader {

    public boolean status;

    private Map<UUID, AuthData> authMode;

    @Override
    public void onEnable() {
        this.authMode = new HashMap<>();
    }

    @Override
    public void onDisable() {
        if (!this.authMode.isEmpty()) {
            XenonBukkit.getInstance().getServer().getOnlinePlayers().stream().filter(player -> this.authMode.containsKey(player.getUniqueId())).forEach(this::giveItems);
        }
    }

    @Override
    public void onReload() {

    }


    public void clearItems(Player player) {
        AuthData authData = new AuthData();
        authData.setInvItems(player.getInventory().getContents());
        authData.setArmor(player.getInventory().getArmorContents());
        authData.setGameMode(player.getGameMode());
        authData.setLocation(player.getLocation());
        authMode.put(player.getUniqueId(), authData);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.setGameMode(GameMode.CREATIVE);

        player.getInventory().setItem(0, this.placeholder());
        player.getInventory().setItem(1, this.placeholder());
        player.getInventory().setItem(2, this.placeholder());
        player.getInventory().setItem(3, this.placeholder());
        player.getInventory().setItem(5, this.placeholder());
        player.getInventory().setItem(6, this.placeholder());
        player.getInventory().setItem(7, this.placeholder());
        player.getInventory().setItem(8, this.placeholder());
    }

    public void giveItems(Player player) {
        AuthData authData = authMode.get(player.getUniqueId());
        player.getInventory().setContents(authData.getInvItems());
        player.getInventory().setArmorContents(authData.getArmor());
        player.setGameMode(authData.getGameMode());
        player.teleport(authData.getLocation());

        player.updateInventory();

        authMode.remove(player.getUniqueId());
    }

    public ItemStack placeholder() {
        ItemMaker itemStack = ItemMaker.of("STAINED_GLASS_PANE");
        itemStack.setDisplayName(CC.translate("&7"));
        itemStack.setData((short)7);
        return itemStack.build();
    }

    public void giveQRMap(Player player) {
        Profile profile = XenonBukkit.getInstance().getProfileManager().getProfile(player.getUniqueId());
        GoogleAuthenticator authenticator = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = authenticator.createCredentials();
        try {
            XenonBukkit.getInstance().getTfaManager().clearItems(player);

            URL url = new URL(TFAUtil.getQrImageURL(key.getKey(), "Xenon"));
            BufferedImage image = ImageIO.read(url);

            ItemMaker item = ItemMaker.of("MAP");
            item.setDisplayName(XenonBukkit.getInstance().getConfiguration().getConfiguration().getString("two-factor.qr-code-map.name"));
            for (String lore_line : XenonBukkit.getInstance().getConfiguration().getConfiguration().getStringList("two-factor.qr-code-map.lore")) {
                item.addLore(CC.translate(lore_line));
            }

            MapView view = Bukkit.createMap(player.getWorld());
            view.getRenderers().clear();
            view.addRenderer(new TFAQRGenerator(image));

            item.setData((short)view.getId());

            player.getInventory().setItem(4, item.build());

            profile.setNeedsVerify(true);
            profile.setTfaKey(key.getKey());
            profile.save();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
