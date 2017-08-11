package slimeknights.toolleveling;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
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
    // colored numeric level value
    TextComponentString componentLvl = new TextComponentString("" + level);
    componentLvl.getStyle().setColor(getLevelColor(level));

    // on client localized level up string
    TextComponentTranslation componentText = new TextComponentTranslation(
        "message.levelup",
        itemStack.getDisplayName(),
        componentLvl
    );
    componentText.getStyle().setColor(TextFormatting.DARK_AQUA);

    player.sendMessage(componentText);
  }

  public static TextFormatting getLevelColor(int level) {
    if      (level <=   1) return TextFormatting.GREEN;
    else if (level ==   2) return TextFormatting.DARK_GREEN;
    else if (level <=   4) return TextFormatting.AQUA;
    else if (level <=   8) return TextFormatting.BLUE;
    else if (level <=  16) return TextFormatting.LIGHT_PURPLE;
    else if (level <=  32) return TextFormatting.DARK_PURPLE;
    else if (level <=  64) return TextFormatting.YELLOW;
    else if (level <= 128) return TextFormatting.GOLD;
    else if (level <= 256) return TextFormatting.RED;
    else                   return TextFormatting.DARK_RED;
  }

  private static SoundEvent registerSound() {
    ResourceLocation location = new ResourceLocation(TinkerToolLeveling.MODID, "levelup");
    SoundEvent event = new SoundEvent(location);
    event.setRegistryName(location);
    GameRegistry.findRegistry(SoundEvent.class).register(event);
    return event;
  }
}
