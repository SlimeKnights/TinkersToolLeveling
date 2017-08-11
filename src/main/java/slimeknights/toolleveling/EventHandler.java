package slimeknights.toolleveling;

import com.google.common.collect.Lists;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.events.TinkerToolEvent;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.toolleveling.config.Config;

public final class EventHandler {

  public static final EventHandler INSTANCE = new EventHandler();


  @SuppressWarnings("unused")
  @SubscribeEvent
  public void onToolBuild(TinkerToolEvent.OnItemBuilding event) {
    // we build a dummy tool tag to get the base modifier amount, unchanged by traits
    List<Material> materials = Lists.newArrayList();
    for(int i = 0; i < event.tool.getRequiredComponents().size(); i++) {
      materials.add(Material.UNKNOWN);
    }
    NBTTagCompound baseTag = event.tool.buildTag(materials);

    int modifiers = baseTag.getInteger(Tags.FREE_MODIFIERS);
    int modifierDelta = Config.getStartingModifiers() - modifiers;

    // set free modifiers
    NBTTagCompound toolTag = TagUtil.getToolTag(event.tag);
    modifiers = toolTag.getInteger(Tags.FREE_MODIFIERS);
    modifiers += modifierDelta;
    modifiers = Math.max(0, modifiers);
    toolTag.setInteger(Tags.FREE_MODIFIERS, modifiers);
    TagUtil.setToolTag(event.tag, toolTag);

    if(TinkerUtil.getModifierTag(event.tag, TinkerToolLeveling.modToolLeveling.getModifierIdentifier()).hasNoTags()) {
      TinkerToolLeveling.modToolLeveling.apply(event.tag);
    }

    if(!TinkerUtil.hasModifier(event.tag, TinkerToolLeveling.modToolLeveling.getModifierIdentifier())) {
      TinkerToolLeveling.modToolLeveling.apply(event.tag);
    }
  }

  @SuppressWarnings("unused")
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onTooltip(ItemTooltipEvent event) {
    Tooltips.addTooltips(event.getItemStack(), event.getToolTip());
  }

  private EventHandler() {
  }
}
