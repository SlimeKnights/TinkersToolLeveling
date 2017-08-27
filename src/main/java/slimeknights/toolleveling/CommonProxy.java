package slimeknights.toolleveling;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.RegistryEvent;
import slimeknights.tconstruct.common.Sounds;

public class CommonProxy {

  SoundEvent SOUND_LEVELUP;
  
  public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
	  SOUND_LEVELUP = registerSound("levelup");
	  
	  event.getRegistry().registerAll(SOUND_LEVELUP);
  }

  public void playLevelupDing(EntityPlayer player) {
    Sounds.PlaySoundForPlayer(player, SOUND_LEVELUP, 1f, 1f);
  }

  public void sendLevelUpMessage(int level, ItemStack itemStack, EntityPlayer player) {
    ITextComponent textComponent;
    // special message
    if(I18n.canTranslate("message.levelup." + level)) {
      textComponent = new TextComponentString(TextFormatting.DARK_AQUA + I18n.translateToLocalFormatted("message.levelup." + level, itemStack.getDisplayName() + TextFormatting.DARK_AQUA));
    }
    // generic message
    else {
      textComponent = new TextComponentString(TextFormatting.DARK_AQUA + I18n.translateToLocalFormatted("message.levelup.generic", itemStack.getDisplayName() + TextFormatting.DARK_AQUA, Tooltips.getLevelString(level)));
    }
    player.sendStatusMessage(textComponent, false);
    player.sendMessage(textComponent);
  }

  private static SoundEvent registerSound(String name) {
    ResourceLocation location = new ResourceLocation(TinkerToolLeveling.MODID, name);
    SoundEvent event = new SoundEvent(location);
    event.setRegistryName(location);
    return event;
  }
}
