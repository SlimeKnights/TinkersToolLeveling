package slimeknights.toolleveling;

import net.minecraft.util.SoundEvent;
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
import slimeknights.tconstruct.common.config.ConfigSync;
import slimeknights.toolleveling.capability.CapabilityDamageXp;
import slimeknights.toolleveling.debug.CommandLevelTool;

@Mod(modid = TinkerToolLeveling.MODID,
    version = TinkerToolLeveling.VERSION,
    name = "TinkerToolLeveling",
    dependencies = "required-after:forge@[14.21.1.2420,);"
                   + "required-after:mantle@[1.12-1.3.0.5,);"
                   + "required-after:tconstruct@[1.12-2.7.1,)",
    acceptedMinecraftVersions = "[1.12,1.13)"
)
public class TinkerToolLeveling {

  public static final String MODID = "tinkertoolleveling";
  public static final String VERSION = "${version}";

  @SidedProxy(clientSide = "slimeknights.toolleveling.CommonProxy", serverSide = "slimeknights.toolleveling.CommonProxy")
  public static CommonProxy proxy;

  public static ModToolLeveling modToolLeveling = new ModToolLeveling();

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    CapabilityDamageXp.register();
    MinecraftForge.EVENT_BUS.register(slimeknights.toolleveling.EventHandler.INSTANCE);
    MinecraftForge.EVENT_BUS.register(EntityXpHandler.INSTANCE);
    MinecraftForge.EVENT_BUS.register(this);
  }
  
  @SubscribeEvent
  public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
	  proxy.registerSounds(event);
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
