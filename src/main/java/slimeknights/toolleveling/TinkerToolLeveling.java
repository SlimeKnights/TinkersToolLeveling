package slimeknights.toolleveling;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;

import slimeknights.mantle.network.NetworkWrapper;
import slimeknights.toolleveling.capability.CapabilityDamageXp;
import slimeknights.toolleveling.config.Config;
import slimeknights.toolleveling.config.ConfigSync;
import slimeknights.toolleveling.config.ConfigSyncPacket;
import slimeknights.toolleveling.debug.CommandLevelTool;

@Mod(modid = TinkerToolLeveling.MODID,
    version = TinkerToolLeveling.VERSION,
    name = "TinkerToolLeveling",
    dependencies = "required-after:forge@[14.21.1.2443,);"
                   + "required-after:mantle@[1.12-1.3.1,);"
                   + "required-after:tconstruct@[1.12-2.7.0,)",
    acceptedMinecraftVersions = MinecraftForge.MC_VERSION
)
public class TinkerToolLeveling {

  public static final String MODID = "tinkertoolleveling";
  public static final String VERSION = "${version}";

  @SidedProxy(clientSide = "slimeknights.toolleveling.CommonProxy", serverSide = "slimeknights.toolleveling.CommonProxy")
  public static CommonProxy proxy;

  public static NetworkWrapper networkWrapper;

  public static ModToolLeveling modToolLeveling = new ModToolLeveling();

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    Config.INSTANCE.load(new File(event.getModConfigurationDirectory(), "TinkerToolLeveling.cfg"));

    networkWrapper = new NetworkWrapper("tinkerlevel" + ":sync");
    networkWrapper.registerPacketClient(ConfigSyncPacket.class);

    CapabilityDamageXp.register();
    MinecraftForge.EVENT_BUS.register(slimeknights.toolleveling.EventHandler.INSTANCE);
    MinecraftForge.EVENT_BUS.register(EntityXpHandler.INSTANCE);

    Config.INSTANCE.save();
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    if(event.getSide().isServer()) {
      MinecraftForge.EVENT_BUS.register(new ConfigSync());
    }
  }

  @EventHandler
  public void serverStart(FMLServerStartingEvent event) {
    event.registerServerCommand(new CommandLevelTool());
  }

}
