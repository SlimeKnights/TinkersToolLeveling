package slimeknights.toolleveling;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.tconstruct.common.Sounds;

import static slimeknights.toolleveling.TinkerToolLeveling.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class CommonProxy{
    static SoundEvent SOUND_LEVELUP;

    public void playLevelupDing(EntityPlayer player){
        Sounds.PlaySoundForPlayer(player, SOUND_LEVELUP, 1f, 1f);
    }

    public void sendLevelUpMessage(int level, ItemStack itemStack, EntityPlayer player){
        ITextComponent textComponent;
        // special message
        if(I18n.canTranslate("message.levelup." + level)){
            textComponent = new TextComponentString(TextFormatting.DARK_AQUA + I18n.translateToLocalFormatted("message.levelup." + level, itemStack.getDisplayName() + TextFormatting.DARK_AQUA));
        }
        // generic message
        else{
            textComponent = new TextComponentString(TextFormatting.DARK_AQUA + I18n.translateToLocalFormatted("message.levelup.generic", itemStack.getDisplayName() + TextFormatting.DARK_AQUA, Tooltips.getLevelString(level)));
        }
        player.sendStatusMessage(textComponent, false);
    }

    @SubscribeEvent
    public static void onSoundRegisteryEvent(RegistryEvent.Register<SoundEvent> event){
        SOUND_LEVELUP = registerSound("levelup", event.getRegistry());
    }

    private static SoundEvent registerSound(String name, IForgeRegistry<SoundEvent> registry){
        ResourceLocation location = new ResourceLocation(MODID, name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(location);
        registry.register(event);
        return event;
    }
}
