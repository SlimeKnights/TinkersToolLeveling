package slimeknights.toolleveling.config;

import com.google.common.collect.Sets;

import net.minecraft.item.Item;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import slimeknights.mantle.config.AbstractConfigFile;
import slimeknights.mantle.configurate.objectmapping.Setting;
import slimeknights.mantle.configurate.objectmapping.serialize.ConfigSerializable;
import slimeknights.tconstruct.library.TinkerRegistry;

import static slimeknights.tconstruct.tools.harvest.TinkerHarvestTools.excavator;
import static slimeknights.tconstruct.tools.harvest.TinkerHarvestTools.hammer;
import static slimeknights.tconstruct.tools.harvest.TinkerHarvestTools.lumberAxe;
import static slimeknights.tconstruct.tools.harvest.TinkerHarvestTools.scythe;

@ConfigSerializable
public class ConfigFile extends AbstractConfigFile {

  private final static int CONFIG_VERSION = 1;

  @Setting
  General general = new General();
  @Setting
  ToolXP toolxp = new ToolXP();

  public ConfigFile() {
  }

  public ConfigFile(File file) {
    super(file);
  }

  @Override
  public int getConfigVersion() {
    return CONFIG_VERSION;
  }

  @Override
  public void insertDefaults() {
    clearNeedsSaving();
// fill in defaults for missing entries
    TinkerRegistry.getTools().stream()
                  .filter(tool -> !toolxp.baseXpForTool.containsKey(tool))
                  .forEach(tool -> {
                    toolxp.baseXpForTool.put(tool, getDefaultXp(tool));
                    setNeedsSaving();
                  });
  }

  private int getDefaultXp(Item item) {
    Set<Item> aoeTools = Sets.newHashSet(hammer, excavator, lumberAxe);
    if(scythe != null) {
      aoeTools.add(scythe);
    }

    if(aoeTools.contains(item)) {
      return 9 * toolxp.defaultBaseXP;
    }
    return toolxp.defaultBaseXP;
  }



  @ConfigSerializable
  static class General {

    @Setting(comment = "Reduces the amount of modifiers a newly build tool gets if the value is lower than the regular amount of modifiers the tool would have")
    public int newToolMinModifiers = 3;

    @Setting(comment = "Maximum achievable levels. If set to 0 or lower there is no upper limit")
    public int maximumLevels = -1;
  }

  @ConfigSerializable
  static class ToolXP {
    @Setting(comment = "Base XP used when no more specific entry is present for the tool")
    public int defaultBaseXP = 500;

    @Setting(comment = "Base XP for each of the listed tools")
    public Map<Item, Integer> baseXpForTool = new HashMap<>();

    @Setting(comment = "XP scaling rate for each consecutive level. 2f --> twice as much xp required for each new level.")
    public float levelMultiplier = 2f;
  }
}
