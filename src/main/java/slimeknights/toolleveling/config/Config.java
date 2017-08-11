package slimeknights.toolleveling.config;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import slimeknights.mantle.config.AbstractConfig;
import slimeknights.tconstruct.library.TinkerRegistry;

import static slimeknights.tconstruct.tools.harvest.TinkerHarvestTools.excavator;
import static slimeknights.tconstruct.tools.harvest.TinkerHarvestTools.hammer;
import static slimeknights.tconstruct.tools.harvest.TinkerHarvestTools.lumberAxe;
import static slimeknights.tconstruct.tools.harvest.TinkerHarvestTools.scythe;

public class Config extends AbstractConfig {

  public static final Config INSTANCE = new Config();

  // General
  private int                newToolMinModifiers =   3;
  private int                maximumLevels       =  -1;
  // ToolXP
  private int                defaultBaseXP       = 500;
  private float              levelMultiplier     =   2f;
  private final Map<Item, Integer> baseXpForTool = new HashMap<>();

  /* Config File */
  private static Configuration configFile;

  public static void load(FMLPreInitializationEvent event) {
    configFile = new Configuration(event.getSuggestedConfigurationFile(), "2", false);

    MinecraftForge.EVENT_BUS.register(INSTANCE);

    syncConfig();
  }

  public static void syncConfig() {
    Property prop;

    // General
    {
      String cat = "general";
      List<String> propOrder = Lists.newArrayList();
      ConfigCategory general = configFile.getCategory(cat);

      prop = configFile.get(cat, "newToolMinModifiers", INSTANCE.newToolMinModifiers);
      prop.setComment("Reduces the amount of modifiers a newly build tool gets if the value is lower than the regular amount of modifiers the tool would have.");
      INSTANCE.newToolMinModifiers = prop.getInt();
      propOrder.add(prop.getName());

      prop = configFile.get(cat, "maximumLevels", INSTANCE.maximumLevels);
      prop.setComment("Maximum achievable levels. If set to lower 0 there is no upper limit.");
      INSTANCE.maximumLevels = prop.getInt();
      propOrder.add(prop.getName());

      general.setPropertyOrder(propOrder);
    }

    // ToolXPbase
    {
      String cat = "toolxpbase";
      List<String> propOrder = Lists.newArrayList();
      ConfigCategory toolXPbase = configFile.getCategory(cat);

      prop = configFile.get(cat, "defaultBaseXP", INSTANCE.defaultBaseXP);
      prop.setComment("Needed base XP for level up.");
      INSTANCE.defaultBaseXP = prop.getInt();
      propOrder.add(prop.getName());

      prop = configFile.get(cat, "levelMultiplier", INSTANCE.levelMultiplier);
      prop.setComment("Base XP multiplier for each reached level.");
      INSTANCE.levelMultiplier = (float) prop.getDouble();
      propOrder.add(prop.getName());

      toolXPbase.setPropertyOrder(propOrder);
    }

    // ToolXPspecific
    {
      String cat = "toolxpspecific";
      List<String> propOrder = Lists.newArrayList();
      ConfigCategory toolXPspecific = configFile.getCategory(cat);
      toolXPspecific.setComment("Tool specific base XP.");

      TinkerRegistry.getTools().forEach(tool -> {
        Property property = configFile.get(cat, tool.getIdentifier(), getDefaultXp(tool));
        INSTANCE.baseXpForTool.put(tool, property.getInt());
        propOrder.add(property.getName());
      });

      toolXPspecific.setPropertyOrder(propOrder);
    }

    // save changes if any
    if (configFile.hasChanged()) {
      configFile.save();
    }
  }

  public static int getStartingModifiers() {
    return INSTANCE.newToolMinModifiers;
  }

  public static boolean canLevelUp(int currentLevel) {
    return INSTANCE.maximumLevels < 0 || INSTANCE.maximumLevels >= currentLevel;
  }

  public static int getBaseXpForTool(Item item) {
    return INSTANCE.baseXpForTool.getOrDefault(item, INSTANCE.defaultBaseXP);
  }

  public static float getLevelMultiplier() {
    return INSTANCE.levelMultiplier;
  }

  private static int getDefaultXp(Item item) {
    HashSet<Item> aoeTools = Sets.newHashSet(hammer, excavator, lumberAxe);
    if (scythe != null) {
      aoeTools.add(scythe);
    }

    if (aoeTools.contains(item)) {
      return 9 * INSTANCE.defaultBaseXP;
    }
    return INSTANCE.defaultBaseXP;
  }
}
