package slimeknights.toolleveling;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import slimeknights.tconstruct.common.Sounds;

public class CommonProxy {
  private static final SoundEvent SOUND_LEVELUP = registerSound();

  public void playLevelupDing(EntityPlayer player) {
    Sounds.PlaySoundForPlayer(player, SOUND_LEVELUP, 1f, 1f);
  }

  public void sendLevelUpMessage(int level, ItemStack itemStack, EntityPlayer player) {
    TextComponentTranslation componentText = new TextComponentTranslation(
        "message.levelup",
        itemStack.getDisplayName(),
        level
    );

    componentText.getStyle().setColor(TextFormatting.DARK_AQUA);

    player.sendMessage(componentText);
  }

  private static SoundEvent registerSound() {
    ResourceLocation location = new ResourceLocation(TinkerToolLeveling.MODID, "levelup");
    SoundEvent event = new SoundEvent(location);
    event.setRegistryName(location);
    GameRegistry.findRegistry(SoundEvent.class).register(event);
    return event;
  }
}
