package lc.deathmessages;

import lc.deathmessages.configuration.LCConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class LCDeathMessages extends JavaPlugin {

    public static LCConfig config;

    @Override
    public void onLoad() {
        msg("&eCargando &narchivo de configuración&r&e.");
        config = new LCConfig("deathmessages.yml", null, this);
        config.registerConfig();
        msg("&eCargando mensajes...");
        long a = System.currentTimeMillis();
        config.loadMessages();
        long f = System.currentTimeMillis();
        int finalTime = (int) (f - a);
        msg("&eLos mensajes han sido cargados en "+finalTime+"ms.");
    }

    public static void msg(String msg){
        Bukkit.getConsoleSender().sendMessage(color("&8[&c&lMUERTES&8] &f"+msg));
    }

    public static String color(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    @Override
    public void onEnable() {
        msg("&eRegistrando eventos y comandos...");
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getCommand("lcdeathmessages").setExecutor(new LCDeathMessagesCommand());
        getCommand("deathmessages").setExecutor(new LCDeathMessagesCommand());
        getCommand("lcdm").setExecutor(new LCDeathMessagesCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
