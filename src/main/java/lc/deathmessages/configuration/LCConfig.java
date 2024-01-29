package lc.deathmessages.configuration;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.SqlRow;
import lc.deathmessages.LCDeathMessages;
import lc.deathmessages.LCSound;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LCConfig {

    private LCDeathMessages plugin;
    private final String fileName;
    private FileConfiguration fileConfiguration = null;
    private File file = null;
    private final String folderName;

    public static String prefix = "";
    public static String NO_PERMISSION = "&c¡No tienes acceso a esta función!";
    public static final Map<String, Set<String>> CUSTOM_CAUSES_MESSAGES = new HashMap<>();
    public static final Map<String, LCSound> SOUNDS = new HashMap<>();
    public static final Map<String, Set<String>> TAGGED_CUSTOM_CAUSES_MESSAGES = new HashMap<>();

    public static final Set<String> TAGGED_OTROS_MESSAGES = new HashSet<>();
    public static final Set<String> OTROS_MESSAGES = new HashSet<>();

    public static final Map<Integer, Set<String>> TAGGED_PROJECTILE_MESSAGES = new HashMap<>();
    public static final Map<Integer, Set<String>> TAGGED_ENTITY_EXPLOSION_MESSAGES = new HashMap<>();
    public static final Map<Integer, Set<String>> TAGGED_ENTITY_ATTACK_MESSAGES = new HashMap<>();
    public static final Set<String> TAGGED_PLAYER_ATTACK_MESSAGES = new HashSet<>();

    public static final Map<Integer, Set<String>> PROJECTILE_MESSAGES = new HashMap<>();
    public static final Map<Integer, Set<String>> ENTITY_EXPLOSION_MESSAGES = new HashMap<>();
    public static final Map<Integer, Set<String>> ENTITY_ATTACK_MESSAGES = new HashMap<>();
    public static final Set<String> PLAYER_ATTACK_MESSAGES = new HashSet<>();


    public LCConfig(String fileName, String folderName, LCDeathMessages plugin){
        this.fileName = fileName;
        this.folderName = folderName;
        this.plugin = plugin;
    }

    public String getPath(){
        return this.fileName;
    }

    public void registerConfig(){
        if(folderName != null){
            file = new File(plugin.getDataFolder() +File.separator + folderName,fileName);
        }else{
            file = new File(plugin.getDataFolder(), fileName);
        }

        if(!file.exists()){
            if(folderName != null){
                plugin.saveResource(folderName+File.separator+fileName, false);
            }else{
                plugin.saveResource(fileName, false);
            }
        }

        fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            Bukkit.getLogger().severe("[LCDeathMessages] ¡Error cargando \"deathmessages.yml\"!");
        }
    }
    public void saveConfig() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().severe("[LCDeathMessages] ¡Error guardando \"deathmessages.yml\"!");
        }
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            reloadConfig();
        }
        return fileConfiguration;
    }

    public void reloadConfig() {
        if (fileConfiguration == null) {
            if(folderName != null){
                file = new File(plugin.getDataFolder() +File.separator + folderName, fileName);
            }else{
                file = new File(plugin.getDataFolder(), fileName);
            }

        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);

        if(file != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(file);
            fileConfiguration.setDefaults(defConfig);
        }
    }

    public void loadMessages() {
        LCDeathMessages.msg("&bCargando Sonidos de muerte...");
        if(!fileConfiguration.getConfigurationSection("Opcional.sonidos").getKeys(false).isEmpty()){
            for(String cause : fileConfiguration.getConfigurationSection("Opcional.sonidos").getKeys(false)){
                ConfigurationSection s = fileConfiguration.getConfigurationSection("Opcional.sonidos."+cause);
                SOUNDS.put(cause.toUpperCase(), new LCSound(Sound.valueOf(s.getString("SOUND")), (float) s.getDouble("VOLUMEN"), (float) s.getDouble("PITCH")));
            }

        }

        LCDeathMessages.msg("&bCargando Mensajes Default... &7(NO TAG)");
        if(!fileConfiguration.getConfigurationSection("NO_TAG").getKeys(false).isEmpty()){
           OTROS_MESSAGES.addAll(new HashSet<>(fileConfiguration.getStringList("NO_TAG.Otra")));

        }
        LCDeathMessages.msg("&bCargando Mensajes Default... &7(TAGGED)");
        if(!fileConfiguration.getConfigurationSection("TAGGED").getKeys(false).isEmpty()){
            TAGGED_OTROS_MESSAGES.addAll(new HashSet<>(fileConfiguration.getStringList("TAGGED.Otra")));
        }
        LCDeathMessages.msg("&bCargando Mensajes Custom... &7(NO TAG)");
        prefix = fileConfiguration.getString("Opcional.prefix");
        NO_PERMISSION = fileConfiguration.getString("Opcional.no-permisos");
        if(!fileConfiguration.getConfigurationSection("NO_TAG").getKeys(false).isEmpty()){
            for(String key : fileConfiguration.getConfigurationSection("NO_TAG").getKeys(false)){
                if(fileConfiguration.isConfigurationSection("NO_TAG."+key) || key.equals("PLAYER_ATTACK")) continue;
                LCDeathMessages.msg("&eCargando mensaje &6"+key.toUpperCase()+"&7 - &b"+new HashSet<>(fileConfiguration.getStringList("NO_TAG."+key)).size());
                CUSTOM_CAUSES_MESSAGES.put(key.toUpperCase(), new HashSet<>(fileConfiguration.getStringList("NO_TAG."+key)));

            }
        }
        LCDeathMessages.msg("&bCargando Mensajes Custom... &7(TAGGED)");
        if(!fileConfiguration.getConfigurationSection("TAGGED").getKeys(false).isEmpty()){
            for(String key : fileConfiguration.getConfigurationSection("TAGGED").getKeys(false)){
                if(fileConfiguration.isConfigurationSection("TAGGED."+key) || key.equals("PLAYER_ATTACK")) continue;
                LCDeathMessages.msg("&eCargando mensaje &6"+key.toUpperCase()+"&7 - &b"+new HashSet<>(fileConfiguration.getStringList("TAGGED."+key)).size());
                TAGGED_CUSTOM_CAUSES_MESSAGES.put(key.toUpperCase(), new HashSet<>(fileConfiguration.getStringList("TAGGED."+key)));
            }
        }

        LCDeathMessages.msg("&bCargando Mensajes Obligatorios... &7(NO TAG)");
        if(!fileConfiguration.getConfigurationSection("NO_TAG").getKeys(false).isEmpty()){
            for(String key : fileConfiguration.getConfigurationSection("NO_TAG").getKeys(false)){
                if(key.equalsIgnoreCase("PLAYER_ATTACK")){
                    LCDeathMessages.msg("&eCargando mensaje &6PLAYER_ATTACK&7 - &b"+new HashSet<>(fileConfiguration.getStringList("NO_TAG."+key)).size());
                    PLAYER_ATTACK_MESSAGES.addAll(new HashSet<>(fileConfiguration.getStringList("NO_TAG."+key)));
                }
                if(!fileConfiguration.isConfigurationSection("NO_TAG."+key)) continue;
                ConfigurationSection s = fileConfiguration.getConfigurationSection("NO_TAG."+key);
                if(s.getName().equalsIgnoreCase("ENTITY_EXPLOSION")){
                    if(s.get("TNT") != null){
                        LCDeathMessages.msg("&eCargando mensaje &6ENTITY_EXPLOSION.TNT&7 - &b"+new HashSet<>(s.getStringList("TNT")).size());
                        ENTITY_EXPLOSION_MESSAGES.put(0, new HashSet<>(s.getStringList("TNT")));
                    }
                    if(s.get("END_CRYSTAL") != null){
                        LCDeathMessages.msg("&eCargando mensaje &6ENTITY_EXPLOSION.END_CRYSTAL&7 - &b"+new HashSet<>(s.getStringList("END_CRYSTAL")).size());
                        ENTITY_EXPLOSION_MESSAGES.put(1, new HashSet<>(s.getStringList("END_CRYSTAL")));
                    }
                    if(s.get("CREEPER") != null){
                        LCDeathMessages.msg("&eCargando mensaje &6ENTITY_EXPLOSION.CREEPER&7 - &b"+new HashSet<>(s.getStringList("CREEPER")).size());
                        ENTITY_EXPLOSION_MESSAGES.put(2, new HashSet<>(s.getStringList("CREEPER")));
                    }
                    if(s.get("Otro") != null){
                        LCDeathMessages.msg("&eCargando mensaje &6ENTITY_EXPLOSION.Otro&7 - &b"+new HashSet<>(s.getStringList("Otro")).size());
                        ENTITY_EXPLOSION_MESSAGES.put(3, new HashSet<>(s.getStringList("Otro")));
                    }
                }
                if(s.getName().equalsIgnoreCase("ENTITY_ATTACK")){
                    if(s.get("NO_NAME") != null ){
                        LCDeathMessages.msg("&eCargando mensaje &6ENTITY_ATTACK.NO_NAME&7 - &b"+new HashSet<>(s.getStringList("NO_NAME")).size());
                        ENTITY_ATTACK_MESSAGES.put(0, new HashSet<>(s.getStringList("NO_NAME")));
                    }
                    if(s.get("WITH_NAME") != null){
                        LCDeathMessages.msg("&eCargando mensaje &6ENTITY_ATTACK.WITH_NAME&7 - &b"+new HashSet<>(s.getStringList("WITH_NAME")).size());
                        ENTITY_ATTACK_MESSAGES.put(1, new HashSet<>(s.getStringList("WITH_NAME")));
                    }
                }
                if(s.getName().equalsIgnoreCase("PROJECTILE")){
                    if(s.get("ARROW") != null){
                        LCDeathMessages.msg("&eCargando mensaje &6PROJECTILE.ARROW&7 - &b"+new HashSet<>(s.getStringList("ARROW")).size());
                        PROJECTILE_MESSAGES.put(0, new HashSet<>(s.getStringList("ARROW")));
                    }
                    if(s.get("SNOWBALL") != null){
                        LCDeathMessages.msg("&eCargando mensaje &6PROJECTILE.SNOWBALL&7 - &b"+new HashSet<>(s.getStringList("SNOWBALL")).size());
                        PROJECTILE_MESSAGES.put(1, new HashSet<>(s.getStringList("SNOWBALL")));
                    }
                    if(s.get("Otros") != null){
                        LCDeathMessages.msg("&eCargando mensaje &6PROJECTILE.Otros&7 - &b"+new HashSet<>(s.getStringList("Otros")).size());
                        PROJECTILE_MESSAGES.put(2, new HashSet<>(s.getStringList("Otros")));
                    }
                }

            }
        }


        LCDeathMessages.msg("&bCargando Mensajes Obligatorios... &7(TAGGED)");
        if(!fileConfiguration.getConfigurationSection("TAGGED").getKeys(false).isEmpty()){
            for(String key : fileConfiguration.getConfigurationSection("TAGGED").getKeys(false)){
                if(key.equalsIgnoreCase("PLAYER_ATTACK")){
                    
                        LCDeathMessages.msg("&eCargando mensaje &6PLAYER_ATTACK&7 - &b"+new HashSet<>(fileConfiguration.getStringList("TAGGED."+key)).size());
                        TAGGED_PLAYER_ATTACK_MESSAGES.addAll(new HashSet<>(fileConfiguration.getStringList("TAGGED."+key)));

                }
                if(!fileConfiguration.isConfigurationSection("TAGGED."+key)) continue;
                ConfigurationSection s = fileConfiguration.getConfigurationSection("TAGGED."+key);
                if(s.getName().equalsIgnoreCase("ENTITY_EXPLOSION")){
                    if(s.get("TNT") != null){
                        LCDeathMessages.msg("&eCargando mensaje &6ENTITY_EXPLOSION.TNT&7 - &b"+new HashSet<>(s.getStringList("TNT")).size());
                        TAGGED_ENTITY_EXPLOSION_MESSAGES.put(0, new HashSet<>(s.getStringList("TNT")));
                    }
                    if(s.get("END_CRYSTAL") != null){
                        LCDeathMessages.msg("&eCargando mensaje &6ENTITY_EXPLOSION.END_CRYSTAL&7 - &b"+new HashSet<>(s.getStringList("END_CRYSTAL")).size());
                        TAGGED_ENTITY_EXPLOSION_MESSAGES.put(1, new HashSet<>(s.getStringList("END_CRYSTAL")));
                    }
                    if(s.get("CREEPER") != null){
                        LCDeathMessages.msg("&eCargando mensaje &6ENTITY_EXPLOSION.CREEPER&7 - &b"+new HashSet<>(s.getStringList("CREEPER")).size());
                        TAGGED_ENTITY_EXPLOSION_MESSAGES.put(2, new HashSet<>(s.getStringList("CREEPER")));
                    }
                    if(s.get("Otro") != null){
                        LCDeathMessages.msg("&eCargando mensaje &6ENTITY_EXPLOSION.Otro&7 - &b"+new HashSet<>(s.getStringList("Otro")).size());
                        TAGGED_ENTITY_EXPLOSION_MESSAGES.put(3, new HashSet<>(s.getStringList("Otro")));
                    }
                }
                if(s.getName().equalsIgnoreCase("ENTITY_ATTACK")){
                    if(s.get("NO_NAME") != null){
                        LCDeathMessages.msg("&eCargando mensaje &6ENTITY_ATTACK.NO_NAME&7 - &b"+new HashSet<>(s.getStringList("NO_NAME")).size());
                        TAGGED_ENTITY_ATTACK_MESSAGES.put(0, new HashSet<>(s.getStringList("NO_NAME")));
                    }
                    if(s.get("WITH_NAME") != null){
                        LCDeathMessages.msg("&eCargando mensaje &6ENTITY_ATTACK.WITH_NAME&7 - &b"+new HashSet<>(s.getStringList("WITH_NAME")).size());
                        TAGGED_ENTITY_ATTACK_MESSAGES.put(1, new HashSet<>(s.getStringList("WITH_NAME")));
                    }
                }
                if(s.getName().equalsIgnoreCase("PROJECTILE")){
                    if(s.get("ARROW") != null){
                        LCDeathMessages.msg("&eCargando mensaje &6PROJECTILE.ARROW&7 - &b"+new HashSet<>(s.getStringList("ARROW")).size());
                        TAGGED_PROJECTILE_MESSAGES.put(0, new HashSet<>(s.getStringList("ARROW")));
                    }
                    if(s.get("SNOWBALL") != null){
                        LCDeathMessages.msg("&eCargando mensaje &6PROJECTILE.SNOWBALL&7 - &b"+new HashSet<>(s.getStringList("SNOWBALL")).size());
                        TAGGED_PROJECTILE_MESSAGES.put(1, new HashSet<>(s.getStringList("SNOWBALL")));
                    }
                    if(s.get("Otros") != null){
                        LCDeathMessages.msg("&eCargando mensaje &6PROJECTILE.Otros&7 - &b"+new HashSet<>(s.getStringList("Otros")).size());
                        TAGGED_PROJECTILE_MESSAGES.put(2, new HashSet<>(s.getStringList("Otros")));
                    }
                }

            }
        }


    }

}
