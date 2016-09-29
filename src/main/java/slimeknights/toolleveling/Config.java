package slimeknights.toolleveling;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import slimeknights.mantle.configurate.ConfigurationNode;
import slimeknights.mantle.configurate.ConfigurationOptions;
import slimeknights.mantle.configurate.commented.CommentedConfigurationNode;
import slimeknights.mantle.configurate.hocon.HoconConfigurationLoader;
import slimeknights.mantle.configurate.loader.ConfigurationLoader;
import slimeknights.mantle.configurate.objectmapping.ObjectMappingException;
import slimeknights.mantle.configurate.objectmapping.Setting;
import slimeknights.mantle.configurate.objectmapping.serialize.ConfigSerializable;
import slimeknights.mantle.configurate.objectmapping.serialize.TypeSerializer;
import slimeknights.mantle.configurate.objectmapping.serialize.TypeSerializers;
import slimeknights.tconstruct.library.TinkerRegistry;

import static slimeknights.tconstruct.tools.harvest.TinkerHarvestTools.*;

// todo: sync with server
public class Config {

  @ConfigSerializable
  public static class General {

    @Setting(comment = "Reduces the amount of modifiers a newly build tool gets if the value is lower than the regular amount of modifiers the tool would have")
    public int newToolMinModifiers = 3;
  }

  @ConfigSerializable
  private static class ToolXP {
    @Setting(comment = "Base XP used when no more specific entry is present for the tool")
    public int defaultBaseXP = 500;

    @Setting(comment = "Base XP for each of the listed tools")
    public Map<Item, Integer> baseXpForTool = new HashMap<>();

    public float levelMultiplier = 2f;
  }

  public static General GENERAL = new General();
  private static ToolXP TOOLXP = new ToolXP();


  public static int getBaseXpForTool(Item item) {
    return TOOLXP.baseXpForTool.getOrDefault(item, TOOLXP.defaultBaseXP);
  }

  public static float getLevelMultiplier() {
    return TOOLXP.levelMultiplier;
  }

  public static void load(FMLPreInitializationEvent event) {
    TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Item.class), new TypeSerializer<Item>() {
      @Override
      public Item deserialize(TypeToken<?> typeToken, ConfigurationNode configurationNode)
          throws ObjectMappingException {
        return Item.getByNameOrId(configurationNode.getString());
      }

      @Override
      public void serialize(TypeToken<?> typeToken, Item item, ConfigurationNode configurationNode)
          throws ObjectMappingException {
        configurationNode.setValue(item.getRegistryName());
      }
    });

    File file = new File(event.getModConfigurationDirectory(), "TinkerToolLeveling.cfg");
    ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(file).build();

    try {
      CommentedConfigurationNode node = loader.load(ConfigurationOptions.defaults().setShouldCopyDefaults(true));

      loadGeneralOptions(node);
      loadToolXp(node);

      loader.save(node);
    } catch(IOException | ObjectMappingException e) {
      e.printStackTrace();
    }
  }

  private static void loadGeneralOptions(CommentedConfigurationNode node) throws ObjectMappingException {
    GENERAL = node.getNode("general").getValue(TypeToken.of(General.class), GENERAL);
  }

  private static void loadToolXp(CommentedConfigurationNode node) throws ObjectMappingException {
    ConfigurationNode xpNode = node.getNode("toolxp");
    TypeToken<ToolXP> xpTypeToken = TypeToken.of(ToolXP.class);
    TOOLXP = xpNode.getValue(xpTypeToken, TOOLXP);

    // fill in defaults for missing big AOE tools
    Set<Item> aoeTools = Sets.newHashSet(hammer, excavator, lumberAxe);
    if(scythe != null) {
      aoeTools.add(scythe);
    }
    TinkerRegistry.getTools().stream()
                  .filter(aoeTools::contains)
                  .forEach(tool -> TOOLXP.baseXpForTool.put(tool, TOOLXP.defaultBaseXP * 9));

    // fill in defaults for missing entries
    TinkerRegistry.getTools().stream()
                  .filter(tool -> !TOOLXP.baseXpForTool.containsKey(tool))
                  .forEach(tool -> TOOLXP.baseXpForTool.put(tool, TOOLXP.defaultBaseXP));

    xpNode.setValue(xpTypeToken, TOOLXP);
  }
}
