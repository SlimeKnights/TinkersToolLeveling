package slimeknights.toolleveling.capability;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.toolleveling.TinkerToolLeveling;

public class DamageXpHandler implements IDamageXp, ICapabilitySerializable<NBTTagList> {

  private static String TAG_PLAYER_UUID = "player_uuid";
  private static String TAG_DAMAGE_LIST = "damage_data";
  private static String TAG_ITEM = "item";
  private static String TAG_DAMAGE = "damage";

  private Map<UUID, Map<ItemStack, Float>> playerToDamageMap = new HashMap<>();

  @Override
  public void addDamageFromTool(float damage, ItemStack tool, EntityPlayer player) {
    Map<ItemStack, Float> damageMap = playerToDamageMap.getOrDefault(player.getUniqueID(), new HashMap<>());

    damage += getDamageDealtByTool(tool, player);

    damageMap.put(tool, damage);
    playerToDamageMap.put(player.getUniqueID(), damageMap);
  }

  @Override
  public float getDamageDealtByTool(ItemStack tool, EntityPlayer player) {
    Map<ItemStack, Float> damageMap = playerToDamageMap.getOrDefault(player.getUniqueID(), new HashMap<>());

    return damageMap.entrySet().stream()
                    .filter(itemStackFloatEntry -> ToolCore.isEqualTinkersItem(tool, itemStackFloatEntry.getKey()))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .orElse(0f);
  }

  @Override
  public void distributeXpToTools(EntityLivingBase deadEntity) {
    playerToDamageMap.forEach((uuid, itemStackFloatMap) -> distributeXpForPlayer(deadEntity.getEntityWorld(), uuid, itemStackFloatMap));
  }

  private void distributeXpForPlayer(World world, UUID playerUuid, Map<ItemStack, Float> damageMap) {
    Optional.ofNullable(world.getPlayerEntityByUUID(playerUuid))
            .ifPresent(
                player -> damageMap.forEach(
                    (itemStack, damage) -> distributeXpToPlayerForTool(player, itemStack, damage)
                )
            );
  }

  private void distributeXpToPlayerForTool(EntityPlayer player, ItemStack tool, float damage) {
    if(player.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
      IItemHandler itemHandler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

      // check for identity. should work in most cases because the entity was killed without loading/unloading
      for(int i = 0; i < itemHandler.getSlots(); i++) {
        if(itemHandler.getStackInSlot(i) == tool) {
          TinkerToolLeveling.modToolLeveling.addXp(tool, Math.round(damage), player);
          return;
        }
      }

      // check for equal stack in case instance equality didn't find it
      for(int i = 0; i < itemHandler.getSlots(); i++) {
        if(ToolCore.isEqualTinkersItem(itemHandler.getStackInSlot(i), tool)) {
          TinkerToolLeveling.modToolLeveling.addXp(itemHandler.getStackInSlot(i), Math.round(damage), player);
          return;
        }
      }
    }
  }

  @Override
  public NBTTagList serializeNBT() {
    NBTTagList playerList = new NBTTagList();

    playerToDamageMap.forEach((uuid, itemStackFloatMap) -> playerList.appendTag(convertPlayerDataToTag(uuid, itemStackFloatMap)));

    return playerList;
  }

  private NBTTagCompound convertPlayerDataToTag(UUID uuid, Map<ItemStack, Float> itemStackFloatMap) {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setUniqueId(TAG_PLAYER_UUID, uuid);

    NBTTagList damageTag = new NBTTagList();

    itemStackFloatMap.forEach((itemStack, damage) -> damageTag.appendTag(convertItemDamageDataToTag(itemStack, damage)));

    tag.setTag(TAG_DAMAGE_LIST, damageTag);
    return tag;
  }

  private NBTTagCompound convertItemDamageDataToTag(ItemStack stack, Float damage) {
    NBTTagCompound tag = new NBTTagCompound();

    NBTTagCompound itemTag = stack.writeToNBT(new NBTTagCompound());
    tag.setTag(TAG_ITEM, itemTag);
    tag.setFloat(TAG_DAMAGE, damage);

    return tag;
  }


  @Override
  public void deserializeNBT(NBTTagList nbt) {
    playerToDamageMap = new HashMap<>();
    for(int i = 0; i < nbt.tagCount(); i++) {
      NBTTagCompound tag = nbt.getCompoundTagAt(i);

      UUID playerUuid = tag.getUniqueId(TAG_PLAYER_UUID);
      NBTTagList data = tag.getTagList(TAG_DAMAGE_LIST, 10);

      Map<ItemStack, Float> damageMap = new HashMap<>();

      for(int j = 0; j < data.tagCount(); j++) {
        deserializeTagToMapEntry(damageMap, data.getCompoundTagAt(j));
      }

      playerToDamageMap.put(playerUuid, damageMap);
    }
  }

  private void deserializeTagToMapEntry(Map<ItemStack, Float> damageMap, NBTTagCompound tag) {
    ItemStack stack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag(TAG_ITEM));
    if(stack != null) {
      damageMap.put(stack, tag.getFloat(TAG_DAMAGE));
    }
  }

  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    return capability == CapabilityDamageXp.CAPABILITY;
  }

  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    if(capability == CapabilityDamageXp.CAPABILITY) {
      return (T) this;
    }
    return null;
  }
}
