package slimeknights.toolleveling;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.toolleveling.config.Config;

import java.util.List;

// utility class for constructing tooltip
@SideOnly(Side.CLIENT)
final class Tooltips {

  private Tooltips() {
  } // non-instantiable

  public static void addTooltips(ItemStack itemStack, List<String> tooltips) {
    NBTTagCompound tag = TinkerUtil.getModifierTag(itemStack, TinkerToolLeveling.modToolLeveling.getModifierIdentifier());
    if (!tag.hasNoTags()) {
      ToolLevelNBT data = new ToolLevelNBT(tag);
      tooltips.add(1, getTooltip(
          data.level,
          data.xp,
          TinkerToolLeveling.modToolLeveling.getXpForLevelup(data.level, itemStack),
          Config.canLevelUp(data.level)
      ));
    }
  }

  private static String getTooltip(int level, int xp, int xpNeeded, boolean canLevelUp) {
    if (canLevelUp) {
      return String.format("%s %s    %s %s",
          TextFormatting.GRAY                         + I18n.format("tooltip.level"),
          CommonProxy.getLevelColor(level).toString() + level,
          TextFormatting.GRAY                         + I18n.format("tooltip.xp"),
          TextFormatting.WHITE.toString()             + xp + "/" + xpNeeded + TextFormatting.RESET
      );
    } else {
      return String.format("%s %s",
          TextFormatting.GRAY                         + I18n.format("tooltip.level"),
          CommonProxy.getLevelColor(level).toString() + level + TextFormatting.RESET
      );
    }
  }
}
