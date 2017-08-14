package slimeknights.toolleveling.config;

import net.minecraft.item.Item;

import java.io.File;

import slimeknights.mantle.config.AbstractConfig;

public class Config extends AbstractConfig {

  public static Config INSTANCE = new Config();

  public ConfigFile configFile;

  public void load(File file) {
    ConfigFile.init();

    //configFile = this.load(new ConfigFile(file), ConfigFile.class);
  }

  public static int getBaseXpForTool(Item item) {
    //ConfigFile.ToolXP toolXP = INSTANCE.configFile.toolxp;
    return 500;//toolXP.baseXpForTool.getOrDefault(item, toolXP.defaultBaseXP);
  }

  public static float getLevelMultiplier() {
    return 2f;//INSTANCE.configFile.toolxp.levelMultiplier;
  }

  public static int getStartingModifiers() {
    return 3;//INSTANCE.configFile.general.newToolMinModifiers;
  }

  public static boolean canLevelUp(int currentLevel) {
    return true;//INSTANCE.configFile.general.maximumLevels < 0 || INSTANCE.configFile.general.maximumLevels >= currentLevel;
  }
}
