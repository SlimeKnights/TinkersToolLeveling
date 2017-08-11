package slimeknights.toolleveling.capability;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

public interface IDamageXp extends INBTSerializable<NBTTagList> {

  void addDamageFromTool(float damage, ItemStack tool, EntityPlayer player);

  @SuppressWarnings("unused")
  float getDamageDealtByTool(ItemStack tool, EntityPlayer player);

  void distributeXpToTools(EntityLivingBase deadEntity);
}
