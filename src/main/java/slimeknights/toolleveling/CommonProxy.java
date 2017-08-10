package slimeknights.toolleveling;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommonProxy {
  public void playLevelupDing(EntityPlayer player) {
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
}
