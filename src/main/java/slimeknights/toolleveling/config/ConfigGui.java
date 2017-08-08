package slimeknights.toolleveling.config;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import slimeknights.toolleveling.TinkerToolLeveling;

import java.util.List;

/**
 * Created by gudenau on 8/7/2017.
 * <p>
 * TinkersToolLeveling
 */
class ConfigGui extends GuiConfig{
    ConfigGui(GuiScreen parentScreen){
        super(parentScreen,
            getConfigElements(),
            TinkerToolLeveling.MODID,
            false,
            false,
            //TODO Allow translations
            "Tinker's Tool Leveling"
        );
    }

    private static List<IConfigElement> getConfigElements(){
        List<IConfigElement> list = Lists.newArrayList();

        list.add(new ConfigElement(Config.General));
        list.add(new ConfigElement(Config.ToolXP));

        return list;
    }
}
