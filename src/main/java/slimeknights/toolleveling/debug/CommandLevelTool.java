package slimeknights.toolleveling.debug;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.toolleveling.TinkerToolLeveling;
import slimeknights.toolleveling.ToolLevelNBT;

public class CommandLevelTool extends CommandBase {

  @Override
  public int getRequiredPermissionLevel() {
    return 4;
  }

  @Override
  public String getName() {
    return "levelupTool";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/levelupTool while holding a tinker tool in your hand";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    EntityPlayer player = getCommandSenderAsPlayer(sender);
    ItemStack itemStack = player.getHeldItemMainhand();

    if(itemStack != null && itemStack.getItem() instanceof ToolCore) {
      int xp;
      if(args.length > 0) {
        xp = parseInt(args[0]);
      }
      else {
        ToolLevelNBT data = new ToolLevelNBT(TinkerUtil.getModifierTag(itemStack, TinkerToolLeveling.modToolLeveling.getModifierIdentifier()));
        xp = TinkerToolLeveling.modToolLeveling.getXpForLevelup(data.level, itemStack);
      }
      TinkerToolLeveling.modToolLeveling.addXp(itemStack, xp, player);
    }
    else {
      throw new CommandException("No tinker tool in hand");
    }
  }
}
