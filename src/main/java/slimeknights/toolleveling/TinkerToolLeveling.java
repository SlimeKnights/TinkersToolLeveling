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
import slimeknights.mantle.pulsar.control.PulseManager;
import slimeknights.toolleveling.capability.CapabilityDamageXp;
import slimeknights.toolleveling.config.Config;
import slimeknights.toolleveling.debug.CommandLevelTool;

@Mod(modid = TinkerToolLeveling.MODID,
    version = TinkerToolLeveling.VERSION,
    name = "TinkerToolLeveling",
    dependencies = "required-after:forge@[13.20.0.2296,);"
        + "required-after:mantle@[1.11.2-1.2.0,);"
        + "required-after:tconstruct@[1.11.2-2.7.0,)",
    acceptedMinecraftVersions = "1.11.2",
    guiFactory = "slimeknights.toolleveling.config.ConfigGuiFactory"
)
public class TinkerToolLeveling{

    public static final String MODID = "tinkertoolleveling";
    public static final String VERSION = "${version}";

    @SidedProxy(clientSide = "slimeknights.toolleveling.CommonProxy", serverSide = "slimeknights.toolleveling.CommonProxy")
    public static CommonProxy proxy;

    public static NetworkWrapper networkWrapper;

    public static ModToolLeveling modToolLeveling = new ModToolLeveling();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        Config.load(event);

        networkWrapper = new NetworkWrapper("tinkerlevel" + ":sync");

        CapabilityDamageXp.register();
        MinecraftForge.EVENT_BUS.register(slimeknights.toolleveling.EventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(EntityXpHandler.INSTANCE);
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event){
        event.registerServerCommand(new CommandLevelTool());
    }

}
