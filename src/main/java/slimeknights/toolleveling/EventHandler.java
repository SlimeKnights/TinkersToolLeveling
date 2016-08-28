package slimeknights.toolleveling;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import slimeknights.tconstruct.library.events.TinkerToolEvent;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.library.utils.TinkerUtil;

public final class EventHandler {

  public static EventHandler INSTANCE = new EventHandler();


  @SubscribeEvent
  public void onToolBuild(TinkerToolEvent.OnItemBuilding event) {
    // set free modifiers
    NBTTagCompound toolTag = TagUtil.getToolTag(event.tag);
    int modifiers = toolTag.getInteger(Tags.FREE_MODIFIERS);
    modifiers = Math.min(Config.GENERAL.newToolMinModifiers, modifiers);
    modifiers = Math.max(0, modifiers);
    toolTag.setInteger(Tags.FREE_MODIFIERS, modifiers);
    TagUtil.setToolTag(event.tag, toolTag);

    TinkerToolLeveling.modToolLeveling.apply(event.tag);
  }

  @SubscribeEvent
  public void onTooltip(ItemTooltipEvent event) {
    Tooltips.addTooltips(event.getItemStack(), event.getToolTip());
  }

  private EventHandler() {
  }
}
