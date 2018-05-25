package slimeknights.toolleveling.debug;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import slimeknights.tconstruct.library.TinkerRegistry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

/**
 * @author Florian Warzecha
 * @version 1.0
 * @date 25 of Mai 2018
 */
public class CommandModifierDump extends CommandBase {
    
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
    
    @Override
    public String getName() {
        return "dumpModifiers";
    }
    
    @Override
    public String getUsage(ICommandSender sender) {
        return "Writes all modifiers known to Tinker's Construct into logs/modifiers.txt";
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        File dataDir = server.getDataDirectory();
        File logFile = new File(dataDir.getAbsolutePath() + "/logs/modifiers.txt");
        System.out.println(logFile.getAbsolutePath());
        if (logFile.exists()) {
            logFile.delete();
        }
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        StringBuilder dump = new StringBuilder();
        TinkerRegistry.getAllModifiers().forEach(mod -> {
            dump.append(mod.getLocalizedName()).append(" (").append(mod.getIdentifier()).append("): ");
            dump.append(mod.getLocalizedDesc()).append("\n");
        });
    
        try {
            Files.write(logFile.toPath(), Collections.singleton(dump.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
