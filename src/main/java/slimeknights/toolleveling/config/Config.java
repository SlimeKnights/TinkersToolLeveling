package slimeknights.toolleveling.config;

import net.minecraft.item.Item;
import slimeknights.mantle.config.AbstractConfig;

public class Config extends AbstractConfig {
	
  public static int getBaseXpForTool(Item item) {
    ConfigFile.ToolXP toolXP = ConfigFile.toolxp;
    return toolXP.baseXpForTool.getOrDefault(item, toolXP.defaultBaseXP);
  }

  public static float getLevelMultiplier() {
    return ConfigFile.toolxp.levelMultiplier;
  }

  public static int getStartingModifiers() {
    return ConfigFile.general.newToolMinModifiers;
  }

  public static boolean canLevelUp(int currentLevel) {
    return ConfigFile.general.maximumLevels < 0 || ConfigFile.general.maximumLevels >= currentLevel;
  }
}
