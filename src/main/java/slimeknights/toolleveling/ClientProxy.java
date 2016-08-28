package slimeknights.toolleveling;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class ClientProxy extends CommonProxy {

  SoundEvent SOUND_LEVELUP = registerSound("levelup");

  @Override
  public void playLevelupDing() {
    Minecraft.getMinecraft().thePlayer.playSound(SOUND_LEVELUP, 1f, 1f);
  }

  private static SoundEvent registerSound(String name) {
    ResourceLocation location = new ResourceLocation(TinkerToolLeveling.MODID, name);
    SoundEvent event = new SoundEvent(location);
    SoundEvent.REGISTRY.register(-1, location, event);
    return event;
  }
}
