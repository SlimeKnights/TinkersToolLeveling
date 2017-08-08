package slimeknights.toolleveling.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public final class CapabilityDamageXp implements Capability.IStorage<IDamageXp>{

    @CapabilityInject(IDamageXp.class)
    public static Capability<IDamageXp> CAPABILITY = null;

    private static final CapabilityDamageXp INSTANCE = new CapabilityDamageXp();

    public static void register(){
        CapabilityManager.INSTANCE.register(IDamageXp.class, INSTANCE, DamageXpHandler::new);
    }

    @Override
    public NBTBase writeNBT(Capability<IDamageXp> capability, IDamageXp instance, EnumFacing side){
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<IDamageXp> capability, IDamageXp instance, EnumFacing side, NBTBase nbt){
        instance.deserializeNBT((NBTTagList)nbt);
    }

    private CapabilityDamageXp(){
    }
}
