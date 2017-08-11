package slimeknights.toolleveling;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ClientProxy extends CommonProxy {
  @Override
  public void sendLevelUpMessage(int level, ItemStack itemStack, EntityPlayer player) {
    player.sendMessage(new TextComponentString(
        TextFormatting.DARK_AQUA + I18n.format("message.levelup",
            itemStack.getDisplayName() + TextFormatting.DARK_AQUA, level)
    ));
  }
}
