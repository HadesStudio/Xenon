package dev.hely.core.bukkit.utils.prompt;

import java.util.Map;
import java.util.UUID;

import dev.hely.core.bukkit.OxygenBukkit;
import dev.hely.lib.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.google.common.collect.Maps;

import lombok.Getter;

@Getter
public class PromptManager {
	
	private final Map<UUID, PromptData> promptMap;

	public PromptManager(OxygenBukkit instance) {
		this.promptMap = Maps.newHashMap();

		instance.getServer().getScheduler().runTaskTimerAsynchronously(instance, new PromptRunnable(this), 20L, 20L);
	}
	
	public void startPrompt(Player player, Prompt<?> prompt) {
		if(this.promptMap.containsKey(player.getUniqueId())) {
			player.sendMessage(CC.translate("&cSorry, you already have a promt active.."));
			return;
		}
		
		this.promptMap.put(player.getUniqueId(), new PromptData(prompt, System.currentTimeMillis()));
		prompt.handleBegin(player);
	}
	
	public void endPrompt(Player player) {
		if(!this.promptMap.containsKey(player.getUniqueId())) {
			player.sendMessage(CC.translate("&cSorry, you are currently not in a promt."));
			return;
		}
		
		PromptData promptData = this.promptMap.remove(player.getUniqueId());
		
		promptData.getPrompt().handleCancel(player);
	}
	
	public PromptData getPromptByPlayer(Player player) {
		if(this.promptMap.containsKey(player.getUniqueId())) {
			return this.promptMap.get(player.getUniqueId());
		}
		
		return null;
	}
	
	public boolean onPrompt(Player player) {
		return getPromptByPlayer(player) != null;
	}
	
	protected void handleChat(AsyncPlayerChatEvent event, Prompt prompt) {
		Player player = event.getPlayer();
		String message = event.getMessage();
				
		if(message.equalsIgnoreCase("cancel") || message.equalsIgnoreCase("leave") || message.equalsIgnoreCase("quit")) {
			endPrompt(player);
			return;
		}
		
		Object value = prompt.handleInput(player, message);
		if(value == null) {
			prompt.handleFailed(player, message);
			return;
		}
		
		Prompt<?> nextPrompt = prompt.acceptInput(player, value);
		if(nextPrompt == null) {
			this.promptMap.remove(player.getUniqueId());
		} else {
			this.promptMap.remove(player.getUniqueId());
			this.promptMap.put(player.getUniqueId(), new PromptData(nextPrompt, System.currentTimeMillis()));
			nextPrompt.handleBegin(player);
		}
	}
}
