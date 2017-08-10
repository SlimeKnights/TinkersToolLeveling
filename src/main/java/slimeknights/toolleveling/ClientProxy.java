package slimeknights.toolleveling;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.common.Sounds;

public class ClientProxy extends CommonProxy {
  private static final SoundEvent SOUND_LEVELUP = registerSound();

  @Override
  public void playLevelupDing(EntityPlayer player) {
    Sounds.PlaySoundForPlayer(player, SOUND_LEVELUP, 1f, 1f);
  }

  @Override
  public void sendLevelUpMessage(int level, ItemStack itemStack, EntityPlayer player) {
    player.sendMessage(new TextComponentString(
        TextFormatting.DARK_AQUA + I18n.format("message.levelup",
        itemStack.getDisplayName() + TextFormatting.DARK_AQUA, level)
    ));
  }

  private static SoundEvent registerSound() {
    ResourceLocation location = new ResourceLocation(TinkerToolLeveling.MODID, "levelup");
    SoundEvent event = new SoundEvent(location);
    event.setRegistryName(location);
    return event;
  }
}
