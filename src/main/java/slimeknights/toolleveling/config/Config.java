package slimeknights.toolleveling.config;

import net.minecraft.item.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import slimeknights.mantle.config.AbstractConfig;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifier;

import javax.tools.Tool;

public class Config extends AbstractConfig {

  public static Config INSTANCE = new Config();

  public ConfigFile configFile;

  public void load(File file) {
    ConfigFile.init();

    configFile = this.load(new ConfigFile(file), ConfigFile.class);
  }

  public static int getBaseXpForTool(Item item) {
    ConfigFile.ToolXP toolXP = INSTANCE.configFile.toolxp;
    return toolXP.baseXpForTool.getOrDefault(item, toolXP.defaultBaseXP);
  }

  public static float getLevelMultiplier() {
    return INSTANCE.configFile.toolxp.levelMultiplier;
  }

  public static int getStartingModifiers() {
    return INSTANCE.configFile.general.newToolMinModifiers;
  }

  public static boolean canLevelUp(int currentLevel) {
    return INSTANCE.configFile.general.maximumLevels < 0 || INSTANCE.configFile.general.maximumLevels >= currentLevel;
  }
  
  public static boolean randomModifiersEnabled() {
    return INSTANCE.configFile.modifier.randomModifiers;
  }
  
  public static List<IModifier> getModifiers(Item item) {
    ConfigFile.Modifier modifier = INSTANCE.configFile.modifier;
    List<IModifier> modifiers = new ArrayList<>();
    modifier.modifiersForTool.getOrDefault(item, modifier.modifiers).stream().forEach(mod -> modifiers.add(TinkerRegistry.getModifier(mod)));
    return modifiers;
  }
  
  public static boolean modifierAndFree() {
    return INSTANCE.configFile.modifier.both;
  }
}
