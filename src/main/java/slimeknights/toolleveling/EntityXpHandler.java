package slimeknights.toolleveling;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.toolleveling.capability.CapabilityDamageXp;
import slimeknights.toolleveling.capability.DamageXpHandler;

public class EntityXpHandler {

  public static final EntityXpHandler INSTANCE = new EntityXpHandler();

  private static final ResourceLocation CAPABILITY_KEY = new ResourceLocation(TinkerToolLeveling.MODID, "entityxp");

  @SubscribeEvent
  public void onCapabilityAttach(AttachCapabilitiesEvent<Entity> event) {
    if(event.getObject() instanceof EntityLivingBase && event.getObject().isEntityAlive()) {
      event.addCapability(CAPABILITY_KEY, new DamageXpHandler());
    }
  }

  @SubscribeEvent
  public void onDeath(LivingDeathEvent event) {
    if(!event.getEntity().getEntityWorld().isRemote && event.getEntity().hasCapability(CapabilityDamageXp.CAPABILITY, null)) {
      event.getEntity().getCapability(CapabilityDamageXp.CAPABILITY, null).distributeXpToTools(event.getEntityLiving());
    }
  }

  private EntityXpHandler() {
  }
}
