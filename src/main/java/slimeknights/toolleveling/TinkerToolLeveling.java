package slimeknights.toolleveling;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.File;

import slimeknights.mantle.network.NetworkWrapper;
import slimeknights.toolleveling.capability.CapabilityDamageXp;
import slimeknights.toolleveling.config.Config;
import slimeknights.toolleveling.config.ConfigSync;
import slimeknights.toolleveling.config.ConfigSyncPacket;
import slimeknights.toolleveling.debug.CommandLevelTool;
import slimeknights.toolleveling.debug.CommandModifierDump;

@Mod(modid = TinkerToolLeveling.MODID,
    version = TinkerToolLeveling.VERSION,
    name = "TinkerToolLeveling",
    dependencies = "required-after:forge@[14.21.1.2410,);"
                   + "required-after:mantle@[1.12-1.3.1.21,);"
                   + "required-after:tconstruct@[1.12-2.8,)",
    acceptedMinecraftVersions = "[1.12,1.13)"
)
public class TinkerToolLeveling {

  public static final String MODID = "tinkertoolleveling";
  public static final String VERSION = "${version}";

  @SidedProxy(clientSide = "slimeknights.toolleveling.CommonProxy", serverSide = "slimeknights.toolleveling.CommonProxy")
  public static CommonProxy proxy;

  public static NetworkWrapper networkWrapper;

  public static ModToolLeveling modToolLeveling = new ModToolLeveling();
  private File modConfigurationDirectory;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    modConfigurationDirectory = event.getModConfigurationDirectory();
    networkWrapper = new NetworkWrapper("tinkerlevel" + ":sync");
    networkWrapper.registerPacketClient(ConfigSyncPacket.class);

    CapabilityDamageXp.register();

    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  protected void registerTools(RegistryEvent.Register<Item> event) {
    Config.INSTANCE.load(new File(modConfigurationDirectory, "TinkerToolLeveling.cfg"));
    Config.INSTANCE.save();
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(slimeknights.toolleveling.EventHandler.INSTANCE);
    MinecraftForge.EVENT_BUS.register(EntityXpHandler.INSTANCE);
    if(event.getSide().isServer()) {
      MinecraftForge.EVENT_BUS.register(new ConfigSync());
    }
  }

  @EventHandler
  public void serverStart(FMLServerStartingEvent event) {
    event.registerServerCommand(new CommandLevelTool());
    event.registerServerCommand(new CommandModifierDump());
  }

}
