package slimeknights.toolleveling;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import slimeknights.mantle.network.NetworkWrapper;
import slimeknights.toolleveling.capability.CapabilityDamageXp;
import slimeknights.toolleveling.config.Config;
import slimeknights.toolleveling.config.ConfigSync;
import slimeknights.toolleveling.config.ConfigSyncPacket;
import slimeknights.toolleveling.debug.CommandLevelTool;

@SuppressWarnings("WeakerAccess")
@Mod(modid = TinkerToolLeveling.MODID,
    version = TinkerToolLeveling.VERSION
)
public class TinkerToolLeveling {
  public static final String MODID = "tinkertoolleveling";
  public static final String VERSION = "${version}";

  @SuppressWarnings("unused")
  @SidedProxy(clientSide = "slimeknights.toolleveling.CommonProxy", serverSide = "slimeknights.toolleveling.CommonProxy")
  public static CommonProxy proxy;

  public static NetworkWrapper networkWrapper;

  public static final ModToolLeveling modToolLeveling = new ModToolLeveling();

  @SuppressWarnings("unused")
  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    Config.load(event);

    networkWrapper = new NetworkWrapper("tinkerlevel" + ":sync");
    networkWrapper.registerPacketClient(ConfigSyncPacket.class);

    CapabilityDamageXp.register();
    MinecraftForge.EVENT_BUS.register(slimeknights.toolleveling.EventHandler.INSTANCE);
    MinecraftForge.EVENT_BUS.register(EntityXpHandler.INSTANCE);

    Config.INSTANCE.save();
  }

  @SuppressWarnings("unused")
  @EventHandler
  public void init(FMLInitializationEvent event) {
    /* Need this here, because preInit doesn't guarantee that TTL will
     * be called after TC. If not, the list of tools is empty.         */
    Config.syncConfig();
  }

  @SuppressWarnings("unused")
  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    if (event.getSide().isServer()) {
      MinecraftForge.EVENT_BUS.register(new ConfigSync());
    }
  }

  @SuppressWarnings("unused")
  @EventHandler
  public void serverStart(FMLServerStartingEvent event) {
    event.registerServerCommand(new CommandLevelTool());
  }

}
