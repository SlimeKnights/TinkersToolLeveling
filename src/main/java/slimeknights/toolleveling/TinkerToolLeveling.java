package slimeknights.toolleveling;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import slimeknights.toolleveling.capability.CapabilityDamageXp;

@Mod(modid = TinkerToolLeveling.MODID,
    version = TinkerToolLeveling.VERSION,
    dependencies = "required-after:Forge@[12.18.1.2073,);"
                   + "required-after:tconstruct@[1.10.2-2.4.0,)",
    acceptedMinecraftVersions = "[1.10.2, 1.11)"
)
public class TinkerToolLeveling {

  public static final String MODID = "tinkertoolleveling";
  public static final String VERSION = "${version}";

  @SidedProxy(clientSide = "slimeknights.toolleveling.ClientProxy", serverSide = "slimeknights.toolleveling.CommonProxy")
  public static CommonProxy proxy;

  public static ModToolLeveling modToolLeveling = new ModToolLeveling();

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    CapabilityDamageXp.register();
    MinecraftForge.EVENT_BUS.register(slimeknights.toolleveling.EventHandler.INSTANCE);
    MinecraftForge.EVENT_BUS.register(EntityXpHandler.INSTANCE);
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {

  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {

  }

}
