package slimeknights.toolleveling.config;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;
import slimeknights.tconstruct.library.Util;
import slimeknights.mantle.pulsar.config.ForgeCFG;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static slimeknights.toolleveling.TinkerToolLeveling.MODID;

public class Config{
    //public static ForgeCFG pulseConfig = new ForgeCFG("TinkersToolLeveling", "Tinkers' Tool Leveling Config");
    public static Config instance = new Config();
    public static Logger log = Util.getLogger("Config");

    private Config(){}

    // General
    public static int newToolMinModifiers = 3;
    public static int maximumLevels = -1;

    // ToolXP
    public static int defaultBaseXP = 500;
    public static Map<Item, Integer> baseXpForTool = new HashMap<>();
    public static double levelMultiplier = 2f;

    static Configuration configFile;

    static ConfigCategory General;
    static ConfigCategory ToolXP;

    public static void load(FMLPreInitializationEvent event){
        configFile = new Configuration(event.getSuggestedConfigurationFile(), "0.1", false);

        MinecraftForge.EVENT_BUS.register(instance);

        syncConfig();
    }

    @SubscribeEvent
    public void update(ConfigChangedEvent.OnConfigChangedEvent event){
        if(event.getModID().equals(MODID)){
            syncConfig();
        }
    }

    public static boolean syncConfig(){
        Property property;

        // General
        {
            String category = "general";
            General = configFile.getCategory(category);
            List<String> propOrder = Lists.newArrayList();

            property = configFile.get(category, "newToolMinModifiers", newToolMinModifiers);
            property.setComment("Reduces the amount of modifiers a newly build tool gets if the value is lower than the regular amount of modifiers the tool would have");
            newToolMinModifiers = property.getInt();
            propOrder.add(property.getName());

            property = configFile.get(category, "maximumLevels", maximumLevels);
            property.setComment("Maximum achievable levels. If set to 0 or lower there is no upper limit");
            maximumLevels = property.getInt();
            propOrder.add(property.getName());

            General.setPropertyOrder(propOrder);
        }

        // ToolXP
        {
            String category = "toolxp";
            ToolXP = configFile.getCategory(category);
            List<String> propOrder = Lists.newArrayList();

            property = configFile.get(category, "defaultBaseXP", defaultBaseXP);
            property.setComment("Base XP used when no more specific entry is present for the tool");
            defaultBaseXP = property.getInt();
            propOrder.add(property.getName());

            /*
            public static Map<Item, Integer> baseXpForTool = new HashMap<>();
             */

            //property = configFile.get(category, "baseXpForTool", baseXpForTool);

            property = configFile.get(category, "levelMultiplier", levelMultiplier);
            property.setComment("");
            levelMultiplier = property.getDouble();
            propOrder.add(property.getName());

            ToolXP.setPropertyOrder(propOrder);
        }

        boolean changed = false;
        if(configFile.hasChanged()){
            configFile.save();
            changed = true;
        }

        /*if(pulseConfig.getConfig().hasChanged()){
            pulseConfig.flush();
            changed = true;
        }*/

        return changed;
    }

    // TODO
    public static int getBaseXpForTool(Item item) {
        //ConfigFile.ToolXP toolXP = INSTANCE.configFile.toolxp;
        //return toolXP.baseXpForTool.getOrDefault(item, toolXP.defaultBaseXP);
        return defaultBaseXP;
    }

    public static boolean canLevelUp(int currentLevel) {
        return maximumLevels < 0 || maximumLevels >= currentLevel;
    }
}
