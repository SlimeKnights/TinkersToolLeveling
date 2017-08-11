package slimeknights.toolleveling;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.List;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.client.CustomFontColor;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.toolleveling.config.Config;

// utility class for constructing tooltip
@SideOnly(Side.CLIENT)
final class Tooltips {

  private Tooltips() {
  } // non-instantiable

  public static void addTooltips(ItemStack itemStack, List<String> tooltips) {
    NBTTagCompound tag = TinkerUtil.getModifierTag(itemStack, TinkerToolLeveling.modToolLeveling.getModifierIdentifier());
    if (!tag.hasNoTags()) {
      ToolLevelNBT data = new ToolLevelNBT(tag);
      if (Config.canLevelUp(data.level)) {
        tooltips.add(1, getXpToolTip(data.xp, TinkerToolLeveling.modToolLeveling.getXpForLevelup(data.level, itemStack)));
      }
      tooltips.add(1, getLevelTooltip(data.level));
    }
  }

  private static String getXpToolTip(int xp, int xpNeeded) {
    return String.format("%s: %s", I18n.format("tooltip.xp"), getXpString(xp, xpNeeded));
  }

  private static String getXpString(int xp, int xpNeeded) {
    return TextFormatting.WHITE + String.format("%d / %d", xp, xpNeeded);
    //float xpPercentage = (float)xp / (float)xpNeeded * 100f;
    //return String.format("%.2f", xpPercentage) + "%"
  }

  private static String getLevelTooltip(int level) {
    return String.format("%s: %s", I18n.format("tooltip.level"), getLevelString(level));
  }

  private static String getLevelString(int level) {
    return getLevelColor(level) + level + TextFormatting.RESET;
  }

  private static String getLevelColor(int level) {
    float hue;

    if      (level <=   1) hue = 0.1f;
    else if (level ==   2) hue = 0.2f;
    else if (level <=   4) hue = 0.3f;
    else if (level <=   8) hue = 0.4f;
    else if (level <=  16) hue = 0.5f;
    else if (level <=  32) hue = 0.6f;
    else if (level <=  64) hue = 0.7f;
    else if (level <= 128) hue = 0.8f;
    else if (level <= 256) hue = 0.9f;
    else                   hue = 1.0f;

    return CustomFontColor.encodeColor(Color.HSBtoRGB(hue, 0.75f, 0.8f));

        /* Old colors
        switch (level%12)
        {
            case 0: return TextFormatting.GRAY.toString();
            case 1: return TextFormatting.DARK_RED.toString();
            case 2: return TextFormatting.GOLD.toString();
            case 3: return TextFormatting.YELLOW.toString();
            case 4: return TextFormatting.DARK_GREEN.toString();
            case 5: return TextFormatting.DARK_AQUA.toString();
            case 6: return TextFormatting.LIGHT_PURPLE.toString();
            case 7: return TextFormatting.WHITE.toString();
            case 8: return TextFormatting.RED.toString();
            case 9: return TextFormatting.DARK_PURPLE.toString();
            case 10:return TextFormatting.AQUA.toString();
            case 11:return TextFormatting.GREEN.toString();
            default: return "";
        }*/
  }
}
