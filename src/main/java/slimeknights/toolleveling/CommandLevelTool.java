package slimeknights.toolleveling;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.utils.TinkerUtil;

import javax.annotation.Nonnull;

class CommandLevelTool extends CommandBase {
  @Override
  public int getRequiredPermissionLevel() {
    return 4;
  }

  @Nonnull
  @Override
  public String getName() {
    return "levelupTool";
  }

  @Nonnull
  @Override
  public String getUsage(@Nonnull ICommandSender sender) {
    return new TextComponentTranslation(
        "message.command.help"
    ).getFormattedText();
  }

  @Override
  public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
    EntityPlayer player = getCommandSenderAsPlayer(sender);
    ItemStack itemStack = player.getHeldItemMainhand();

    if (itemStack.getItem() instanceof ToolCore) {
      int xp;
      if (args.length > 0) {
        xp = parseInt(args[0]);
      } else {
        ToolLevelNBT data = new ToolLevelNBT(TinkerUtil.getModifierTag(itemStack, TinkerToolLeveling.modToolLeveling.getModifierIdentifier()));
        xp = TinkerToolLeveling.modToolLeveling.getXpForLevelup(data.level, itemStack);
      }
      TinkerToolLeveling.modToolLeveling.addXp(itemStack, xp, player);
    } else {
      throw new CommandException(new TextComponentTranslation(
          "message.command.exception"
      ).getFormattedText());
    }
  }
}
