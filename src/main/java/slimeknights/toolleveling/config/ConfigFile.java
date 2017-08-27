package slimeknights.toolleveling.config;

import static slimeknights.tconstruct.tools.harvest.TinkerHarvestTools.excavator;
import static slimeknights.tconstruct.tools.harvest.TinkerHarvestTools.hammer;
import static slimeknights.tconstruct.tools.harvest.TinkerHarvestTools.lumberAxe;
import static slimeknights.tconstruct.tools.harvest.TinkerHarvestTools.scythe;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.item.Item;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.toolleveling.TinkerToolLeveling;

@net.minecraftforge.common.config.Config(modid = TinkerToolLeveling.MODID)
public class ConfigFile {
	
	public static General general = new General();
	public static ToolXP toolxp = new ToolXP();
	
	static {
		TinkerRegistry.getTools().stream().filter(tool -> !toolxp.baseXpForTool.containsKey(tool)).forEach(tool -> {
			toolxp.baseXpForTool.put(tool, getDefaultXp(tool));
		});
	}

	public static int getDefaultXp(Item item) {
		Set<Item> aoeTools = Sets.newHashSet(hammer, excavator, lumberAxe);
		if (scythe != null) {
			aoeTools.add(scythe);
		}

		if (aoeTools.contains(item)) {
			return 9 * toolxp.baseXpForTool.getOrDefault(item, 500);
		}
		return toolxp.baseXpForTool.getOrDefault(item, 500);
	}

	static class General {
		@net.minecraftforge.common.config.Config.Comment("Reduces the amount of modifiers a newly build tool gets if the value is lower than the regular amount of modifiers the tool would have")
		public int newToolMinModifiers = 3;

		@net.minecraftforge.common.config.Config.Comment("Maximum achievable levels. If set to 0 or lower there is no upper limit")
		public int maximumLevels = -1;
	}
	
	static class ToolXP {
		@net.minecraftforge.common.config.Config.Comment("Base XP used when no more specific entry is present for the tool")
		public int defaultBaseXP = 500;

		@net.minecraftforge.common.config.Config.Comment("Base XP for each of the listed tools")
		public Map<Item, Integer> baseXpForTool = new HashMap<>();

		public float levelMultiplier = 2f;
	}
}
