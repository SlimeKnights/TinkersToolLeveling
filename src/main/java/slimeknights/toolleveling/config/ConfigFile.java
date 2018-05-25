package slimeknights.toolleveling.config;

import com.google.common.collect.Sets;

import net.minecraft.item.Item;

import java.io.File;
import java.util.*;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.mantle.config.AbstractConfigFile;
import slimeknights.mantle.configurate.objectmapping.Setting;
import slimeknights.mantle.configurate.objectmapping.serialize.ConfigSerializable;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.TinkerGuiException;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;

import static slimeknights.tconstruct.tools.harvest.TinkerHarvestTools.excavator;
import static slimeknights.tconstruct.tools.harvest.TinkerHarvestTools.hammer;
import static slimeknights.tconstruct.tools.harvest.TinkerHarvestTools.lumberAxe;
import static slimeknights.tconstruct.tools.harvest.TinkerHarvestTools.scythe;

@ConfigSerializable
public class ConfigFile extends AbstractConfigFile {

  private final static int CONFIG_VERSION = 2;
  
  private String[] defaultModifiers = new String[]{"haste", "luck", "diamond", "reinforced", "soulbound", "mending_moss", "glowing"};
  private String[] allModifiers = new String[]{"aquadynamic", "hovering", "harvestwidth", "endspeed", "momentum", "superheat", "baconlicious", "soulbound", "reinforced", "sharp", "crumbling", "splintering", "crude2", "crude1",
                "stiff", "poisonous", "webbed", "harvestheight", "flammable", "coldblooded", "holy", "established", "luck", "unnatural", "smite", "glowing", "mending_moss", "haste", "jagged", "dense", "diamond", "shocking",
                "fiery", "heavy", "fractured", "enderfence", "hellish", "sharpness", "lightweight", "fins", "bane_of_arthopods", "splitting", "necrotic", "shulking", "insatiable", "prickly", "spiky", "petramor"};

  @Setting
  General general = new General();
  @Setting
  ToolXP toolxp = new ToolXP();
  @Setting
  Modifier modifier = new Modifier();

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
    TinkerRegistry.getAllModifiers().stream()
            .filter(mod -> Arrays.stream(defaultModifiers).anyMatch(it -> it.equals(mod.getIdentifier())))
            .forEach(mod -> modifier.modifiers.add(mod.getIdentifier()));
    
    TinkerRegistry.getTools().stream()
                  .filter(tool -> !toolxp.baseXpForTool.containsKey(tool))
                  .forEach(tool -> {
                    toolxp.baseXpForTool.put(tool, getDefaultXp(tool));
                    
                    List<String> modifiers = new ArrayList<>();
  
                    Arrays.stream(allModifiers)
                            .filter(mod -> TinkerRegistry.getModifier(mod) != null)
                            .filter(mod -> {
                              try {
                                ItemStack toolInstance = tool.getDefaultInstance();
                                NBTTagCompound tag = TagUtil.getToolTag(toolInstance);
                                tag.setInteger(Tags.FREE_MODIFIERS, 100);
                                TagUtil.setToolTag(toolInstance, tag);
                                System.out.print(mod);
                                return TinkerRegistry.getModifier(mod).canApply(toolInstance, toolInstance);
                              } catch (TinkerGuiException e) {
                                  e.printStackTrace();
                                  return false;
                              }
                            })
                            .forEach(modifiers::add);
                    
                    modifier.modifiersForTool.put(tool, modifiers);
                    
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

    public float levelMultiplier = 2f;
  }
  
  @ConfigSerializable
  static class Modifier {
    @Setting(comment = "If set to true, on top of the random modifier granted, you get another free modifier on level up.")
    public boolean both = false;
    
    @Setting(comment = "Apply random modifiers on level up, instead of giving another free modifier.")
    public boolean randomModifiers = true;
    
    @Setting(comment = "Modifiers used when no more specific entry is present for the tool.")
    public List<String> modifiers = new ArrayList<>();
    
    @Setting(comment = "Modifiers for each of the listed tools")
    public Map<Item, List<String>> modifiersForTool = new HashMap<>();
  }
}
