package dev.hely.core.bukkit.utils.prompt;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import dev.hely.lib.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PromptRunnable implements Runnable {

	private final PromptManager promptHandler;
	
	@Override
	public void run() {
		Iterator<Entry<UUID, PromptData>> iterator = promptHandler.getPromptMap().entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<UUID, PromptData> entry = iterator.next();
			PromptData promptData = entry.getValue();
			long startMillis = promptData.getStartMillis();
			
			if((System.currentTimeMillis() - startMillis) > 15_000L) {
				Player player = Bukkit.getPlayer(entry.getKey());
				if(player != null) {
					player.sendMessage(CC.translate("&cYou're time limit for the prompt has expired, please try again."));
				}
				
				iterator.remove();
			}
		}
	}
}
