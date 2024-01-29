package lc.deathmessages;

import lc.deathmessages.configuration.LCConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LCDeathMessagesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("lcdeathmessages.reload")){
            sender.sendMessage(LCDeathMessages.color(LCConfig.prefix+LCConfig.NO_PERMISSION));
            return true;
        }
        sender.sendMessage(LCDeathMessages.color(LCConfig.prefix+"&b&nRecargando mensajes..."));
        long start = System.currentTimeMillis();
        LCDeathMessages.config.reloadConfig();
        LCDeathMessages.config.loadMessages();
        long end = System.currentTimeMillis();
        long finalTIme = end - start;
        sender.sendMessage(LCDeathMessages.color(LCConfig.prefix+"&eMensajes recargados en "+finalTIme+"ms. &b&nRevisa la consola para mas información"));
        return true;
    }
}
