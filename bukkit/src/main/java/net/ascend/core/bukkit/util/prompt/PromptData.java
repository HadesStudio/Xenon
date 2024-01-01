package net.ascend.core.bukkit.util.prompt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PromptData {

	private final Prompt prompt;
	private final long startMillis;
}
