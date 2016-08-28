package slimeknights.toolleveling;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.library.modifiers.TinkerGuiException;
import slimeknights.tconstruct.library.tinkering.TinkersItem;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.library.utils.ToolBuilder;
import slimeknights.toolleveling.capability.CapabilityDamageXp;

public class ModToolLeveling extends ModifierTrait {

  public ModToolLeveling() {
    super("toolleveling", 0xffffff);

    aspects.clear();
    addAspects(new ModifierAspect.DataAspect(this),
               new ModifierAspect.SingleAspect(this));
  }

  @Override
  public boolean isHidden() {
    return false;
  }

  @Override
  public String getTooltip(NBTTagCompound modifierTag, boolean detailed) {
    ToolLevelNBT data = getLevelData(modifierTag);
    return "XP: " + data.xp + " / " + getXpForLevelup(data.level);
  }

  @Override
  public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
    super.applyEffect(rootCompound, modifierTag);

    ToolLevelNBT data = getLevelData(modifierTag);

    // apply bonus modifiers
    NBTTagCompound toolTag = TagUtil.getToolTag(rootCompound);
    int modifiers = toolTag.getInteger(Tags.FREE_MODIFIERS) + data.bonusModifiers;
    toolTag.setInteger(Tags.FREE_MODIFIERS, Math.max(0, modifiers));
    TagUtil.setToolTag(rootCompound, toolTag);
  }

  /* Actions that award XP */

  @Override
  public void afterBlockBreak(ItemStack tool, World world, IBlockState state, BlockPos pos, EntityLivingBase player, boolean wasEffective) {
    if(wasEffective) {
      addXp(tool, 1);
    }
  }

  @Override
  public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
    if(!target.getEntityWorld().isRemote && wasHit && player instanceof EntityPlayer) {
      // if we killed it the event for distributing xp was already fired and we just do it manually here
      if(!target.isEntityAlive()) {
        addXp(tool, Math.round(damageDealt));
      }
      else if(target.hasCapability(CapabilityDamageXp.CAPABILITY, null)) {
        target.getCapability(CapabilityDamageXp.CAPABILITY, null).addDamageFromTool(damageDealt, tool, (EntityPlayer) player);
      }
    }
  }

  /* XP Handling */

  public void addXp(ItemStack tool, int amount) {
    NBTTagList tagList = TagUtil.getModifiersTagList(tool);
    int index = TinkerUtil.getIndexInCompoundList(tagList, identifier);
    NBTTagCompound modifierTag = tagList.getCompoundTagAt(index);

    ToolLevelNBT data = getLevelData(modifierTag);
    data.xp += amount;

    int xpForLevelup = getXpForLevelup(data.level);

    boolean leveledUp = false;
    // check for levelup
    if(data.xp >= xpForLevelup) {
      data.xp -= xpForLevelup;
      data.level++;
      data.bonusModifiers++;
      leveledUp = true;
    }

    data.write(modifierTag);
    //tagList.set(index, modifierTag);
    TagUtil.setModifiersTagList(tool, tagList);

    if(leveledUp) {
      this.apply(tool);
      TinkerToolLeveling.proxy.playLevelupDing();
      try {
        NBTTagCompound rootTag = TagUtil.getTagSafe(tool);
        ToolBuilder.rebuildTool(rootTag, (TinkersItem) tool.getItem());
        tool.setTagCompound(rootTag);
      } catch(TinkerGuiException e) {
        // this should never happen
        e.printStackTrace();
      }
    }
  }

  public int getXpForLevelup(int level) {
    if(level <= 1) {
      return Config.getBaseXpForTool();
    }
    return (int)((float)getXpForLevelup(level-1)*Config.getLevelMultiplier());
  }

  private ToolLevelNBT getLevelData(ItemStack itemStack) {
    return getLevelData(TinkerUtil.getModifierTag(itemStack, getModifierIdentifier()));
  }

  private ToolLevelNBT getLevelData(NBTTagCompound modifierNBT) {
    return new ToolLevelNBT(modifierNBT);
  }

}
