package lc.deathmessages;

import com.avaje.ebean.validation.NotNull;
import lc.deathmessages.configuration.LCConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class DeathListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        EntityDamageEvent damageEvent = p.getLastDamageCause();
        if(p.getKiller() != null){
            p.getKiller().playSound(p.getKiller().getLocation(), Sound.NOTE_PLING, 1.0F, 1.3F);
        }
        if(Bukkit.getServer().getName().toLowerCase().contains("kitpvp")) {
            if (p.hasPermission("mine.ruby")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give LCoins " + p.getName() + " 5");
                p.sendMessage(ChatColor.GOLD + "+5 LCoins");
            } else if (p.hasPermission("mine.elite")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give LCoins " + p.getName() + " 4");
                p.sendMessage(ChatColor.GOLD + "+4 LCoins");
            } else if (p.hasPermission("mine.svip")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give LCoins " + p.getName() + " 3");
                p.sendMessage(ChatColor.GOLD + "+3 LCoins");
            } else if (p.hasPermission("mine.vip")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give LCoins " + p.getName() + " 2");
                p.sendMessage(ChatColor.GOLD + "+2 LCoins");
            } else {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give LCoins " + p.getName() + " 1");
                p.sendMessage(ChatColor.GOLD + "+1 LCoins");
            }
        }
        if(!LCConfig.SOUNDS.isEmpty()){
            boolean a = false;
            for(Map.Entry<String, LCSound> sounds : LCConfig.SOUNDS.entrySet()) {
                if (damageEvent.getCause().name().equalsIgnoreCase(sounds.getKey())) {
                    a = true;
                    p.playSound(p.getLocation(), sounds.getValue().getSonido(), sounds.getValue().getVolumen(), sounds.getValue().getPitch());

                }
            }
            if(!a) {
                LCSound defaultSound = LCConfig.SOUNDS.get("DEFAULT");
                p.playSound(p.getLocation(), defaultSound.getSonido(), defaultSound.getVolumen(), defaultSound.getPitch());

            }
        }
        if (System.currentTimeMillis() - Tagged.getTime(p).longValue() < 10000L) {
            if(p.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && damageEvent instanceof EntityDamageByEntityEvent){
                EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) damageEvent;

                if(damageByEntityEvent.getDamager() instanceof Player){
                    if(LCConfig.TAGGED_PLAYER_ATTACK_MESSAGES.isEmpty()) return;
                    e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.TAGGED_PLAYER_ATTACK_MESSAGES).replaceAll("%player%", p.getName()).replaceAll("%killer%", Tagged.getKiller(p).getName())));
                    return;
                }


                if(LCConfig.TAGGED_ENTITY_ATTACK_MESSAGES.isEmpty()) return;
                if(!(damageByEntityEvent.getDamager() instanceof Player) || damageByEntityEvent.getDamager().getCustomName() == null || !damageByEntityEvent.getDamager().isCustomNameVisible()){
                    if(LCConfig.TAGGED_ENTITY_ATTACK_MESSAGES.get(0).isEmpty() || LCConfig.TAGGED_ENTITY_ATTACK_MESSAGES.get(0) == null) return;
                    e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.TAGGED_ENTITY_ATTACK_MESSAGES.get(0)).replaceAll("%player%", p.getName()).replaceAll("%killer%", Tagged.getKiller(p).getName())));
                    return;
                }else{
                    if(LCConfig.TAGGED_ENTITY_ATTACK_MESSAGES.get(1).isEmpty() || LCConfig.TAGGED_ENTITY_ATTACK_MESSAGES.get(1) == null) return;
                    e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.TAGGED_ENTITY_ATTACK_MESSAGES.get(1))
                            .replaceAll("%player%", p.getName()).replaceAll("%entity%", damageByEntityEvent.getDamager().getCustomName())));
                    return;
                }
            }

            if(p.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && damageEvent instanceof EntityDamageByEntityEvent){
                EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) damageEvent;
                if(LCConfig.TAGGED_ENTITY_EXPLOSION_MESSAGES.isEmpty()) return;
                switch (damageByEntityEvent.getDamager().getType()){
                    case PRIMED_TNT: {
                        if(LCConfig.TAGGED_ENTITY_EXPLOSION_MESSAGES.get(0).isEmpty() || LCConfig.TAGGED_ENTITY_EXPLOSION_MESSAGES.get(0) == null) return;
                        e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.TAGGED_ENTITY_EXPLOSION_MESSAGES.get(0)).replaceAll("%player%", p.getName()).replaceAll("%killer%", Tagged.getKiller(p).getName())));
                        return;
                    }
                    case ENDER_CRYSTAL:{
                        if(LCConfig.TAGGED_ENTITY_EXPLOSION_MESSAGES.get(1).isEmpty() || LCConfig.TAGGED_ENTITY_EXPLOSION_MESSAGES.get(1) == null) return;
                        e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.TAGGED_ENTITY_EXPLOSION_MESSAGES.get(1)).replaceAll("%player%", p.getName()).replaceAll("%killer%", Tagged.getKiller(p).getName())));
                        return;
                    }
                    case CREEPER:{
                        if(LCConfig.TAGGED_ENTITY_EXPLOSION_MESSAGES.get(2).isEmpty() || LCConfig.TAGGED_ENTITY_EXPLOSION_MESSAGES.get(2) == null) return;
                        e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.TAGGED_ENTITY_EXPLOSION_MESSAGES.get(2)).replaceAll("%player%", p.getName()).replaceAll("%killer%", Tagged.getKiller(p).getName())));
                        return;
                    }
                    default:{
                        if(LCConfig.TAGGED_ENTITY_EXPLOSION_MESSAGES.get(3).isEmpty() || LCConfig.TAGGED_ENTITY_EXPLOSION_MESSAGES.get(3) == null) return;
                        e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.TAGGED_ENTITY_EXPLOSION_MESSAGES.get(3)).replaceAll("%player%", p.getName()).replaceAll("%killer%", Tagged.getKiller(p).getName())));
                        return;
                    }
                }

            }

            if(p.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.PROJECTILE && damageEvent instanceof EntityDamageByEntityEvent){
                EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) damageEvent;
                if(LCConfig.TAGGED_PROJECTILE_MESSAGES.isEmpty()) return;
                switch (damageByEntityEvent.getDamager().getType()){
                    case ARROW: {
                        if(LCConfig.TAGGED_PROJECTILE_MESSAGES.get(0).isEmpty() || LCConfig.TAGGED_PROJECTILE_MESSAGES.get(0) == null) return;
                        e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.TAGGED_PROJECTILE_MESSAGES.get(0)).replaceAll("%player%", p.getName()).replaceAll("%killer%", Tagged.getKiller(p).getName())));
                        return;
                    }
                    case SNOWBALL:{
                        if(LCConfig.TAGGED_PROJECTILE_MESSAGES.get(1).isEmpty() || LCConfig.TAGGED_PROJECTILE_MESSAGES.get(1) == null) return;
                        e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.TAGGED_PROJECTILE_MESSAGES.get(1)).replaceAll("%player%", p.getName()).replaceAll("%killer%", Tagged.getKiller(p).getName())));
                        return;
                    }
                    default:{
                        if(LCConfig.TAGGED_PROJECTILE_MESSAGES.get(3).isEmpty() || LCConfig.TAGGED_PROJECTILE_MESSAGES.get(3) == null) return;
                        e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.TAGGED_PROJECTILE_MESSAGES.get(3)).replaceAll("%player%", p.getName()).replaceAll("%killer%", Tagged.getKiller(p).getName())));
                        return;
                    }
                }
            }

            //CUSTOM
            if(LCConfig.TAGGED_CUSTOM_CAUSES_MESSAGES.isEmpty()) e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.TAGGED_OTROS_MESSAGES).replaceAll("%player%", p.getName()).replaceAll("%killer%", Tagged.getKiller(p).getName())));
            for(Map.Entry<String, Set<String>> cause : LCConfig.TAGGED_CUSTOM_CAUSES_MESSAGES.entrySet()){
                if(cause.getKey().equalsIgnoreCase(p.getLastDamageCause().getCause().name())){
                    if(cause.getValue().isEmpty()) return;
                    String finalMsg = obtenerElementoAleatorio(cause.getValue()).replaceAll("%player%", p.getName()).replaceAll("%killer%", Tagged.getKiller(p).getName());
                    e.setDeathMessage(LCDeathMessages.color(finalMsg));
                    return;
                }
            }
            e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.TAGGED_OTROS_MESSAGES).replaceAll("%player%", p.getName()).replaceAll("%killer%", Tagged.getKiller(p).getName())));


            return;
        }
        if(p.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && damageEvent instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) damageEvent;

            if(damageByEntityEvent.getDamager() instanceof Player){
                if(LCConfig.PLAYER_ATTACK_MESSAGES.isEmpty()) return;
                e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.PLAYER_ATTACK_MESSAGES).replaceAll("%player%", p.getName()).replaceAll("%killer%", damageByEntityEvent.getDamager().getName())));
                return;
            }

            if(LCConfig.ENTITY_ATTACK_MESSAGES.isEmpty()) return;
            if(!(damageByEntityEvent.getDamager() instanceof Player) || damageByEntityEvent.getDamager().getCustomName() == null || !damageByEntityEvent.getDamager().isCustomNameVisible()){
                if(LCConfig.ENTITY_ATTACK_MESSAGES.get(0).isEmpty() || LCConfig.ENTITY_ATTACK_MESSAGES.get(0) == null) return;
                e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.ENTITY_ATTACK_MESSAGES.get(0)).replaceAll("%player%", p.getName()).replaceAll("%entity%", damageByEntityEvent.getDamager().getName())));
                return;
            }else{
                if(LCConfig.ENTITY_ATTACK_MESSAGES.get(1).isEmpty() || LCConfig.ENTITY_ATTACK_MESSAGES.get(1) == null) return;
                e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.ENTITY_ATTACK_MESSAGES.get(1))
                        .replaceAll("%player%", p.getName()).replaceAll("%entity%", damageByEntityEvent.getDamager().getCustomName())));
                return;
            }
        }

        if(p.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && damageEvent instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) damageEvent;
            if(LCConfig.ENTITY_EXPLOSION_MESSAGES.isEmpty()) return;
            switch (damageByEntityEvent.getDamager().getType()){
                case PRIMED_TNT: {
                    if(LCConfig.ENTITY_EXPLOSION_MESSAGES.get(0).isEmpty() || LCConfig.ENTITY_EXPLOSION_MESSAGES.get(0) == null) return;
                    e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.ENTITY_EXPLOSION_MESSAGES.get(0)).replaceAll("%player%", p.getName())));
                    return;
                }
                case ENDER_CRYSTAL:{
                    if(LCConfig.ENTITY_EXPLOSION_MESSAGES.get(1).isEmpty() || LCConfig.ENTITY_EXPLOSION_MESSAGES.get(1) == null) return;
                    e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.ENTITY_EXPLOSION_MESSAGES.get(1)).replaceAll("%player%", p.getName())));
                    return;
                }
                case CREEPER:{
                    if(LCConfig.ENTITY_EXPLOSION_MESSAGES.get(2).isEmpty() || LCConfig.ENTITY_EXPLOSION_MESSAGES.get(2) == null) return;
                    e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.ENTITY_EXPLOSION_MESSAGES.get(2)).replaceAll("%player%", p.getName())));
                    return;
                }
                default:{
                    if(LCConfig.ENTITY_EXPLOSION_MESSAGES.get(3).isEmpty() || LCConfig.ENTITY_EXPLOSION_MESSAGES.get(3) == null) return;
                    e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.ENTITY_EXPLOSION_MESSAGES.get(3)).replaceAll("%player%", p.getName())));
                    return;
                }
            }

        }

        if(p.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.PROJECTILE && damageEvent instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) damageEvent;
            if(LCConfig.PROJECTILE_MESSAGES.isEmpty()) return;
            switch (damageByEntityEvent.getDamager().getType()){
                case ARROW: {
                    if(LCConfig.PROJECTILE_MESSAGES.get(0).isEmpty() || LCConfig.PROJECTILE_MESSAGES.get(0) == null) return;
                    e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.PROJECTILE_MESSAGES.get(0)).replaceAll("%player%", p.getName())));
                    return;
                }
                case SNOWBALL:{
                    if(LCConfig.PROJECTILE_MESSAGES.get(1).isEmpty() || LCConfig.PROJECTILE_MESSAGES.get(1) == null) return;
                    e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.PROJECTILE_MESSAGES.get(1)).replaceAll("%player%", p.getName())));
                    return;
                }
                default:{
                    if(LCConfig.PROJECTILE_MESSAGES.get(3).isEmpty() || LCConfig.PROJECTILE_MESSAGES.get(3) == null) return;
                    e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.PROJECTILE_MESSAGES.get(3)).replaceAll("%player%", p.getName())));
                    return;
                }
            }
        }

        //CUSTOM
        if(LCConfig.CUSTOM_CAUSES_MESSAGES.isEmpty()) return;
        for(Map.Entry<String, Set<String>> cause : LCConfig.CUSTOM_CAUSES_MESSAGES.entrySet()){
            if(cause.getKey().equalsIgnoreCase(p.getLastDamageCause().getCause().name())){
                if(cause.getValue().isEmpty()) return;
                e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(cause.getValue()).replaceAll("%player%", p.getName())));
                return;
            }
        }
        e.setDeathMessage(LCDeathMessages.color(obtenerElementoAleatorio(LCConfig.OTROS_MESSAGES).replaceAll("%player%", p.getName())));


    }

    private static @NotNull <T> T obtenerElementoAleatorio(Set<T> set) {
        if (set.isEmpty()) {
            return null; // El conjunto está vacío, no hay elementos para elegir
        }

        int indiceAleatorio = new SplittableRandom().nextInt(set.size());
        Iterator<T> iterator = set.iterator();

        // Iterar hasta llegar al índice aleatorio
        for (int i = 0; i < indiceAleatorio; i++) {
            iterator.next();
        }

        return iterator.next();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Tagged.removeTagged(e.getPlayer());
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        Entity ent = e.getEntity();
        if (ent instanceof Player) {
            Player target = (Player)ent;
            Entity damager = e.getDamager();
            if (e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                if (damager instanceof Snowball) {
                    Snowball snowball = (Snowball)damager;
                    if (snowball.getShooter() instanceof Player) {
                        Player killer = (Player)snowball.getShooter();
                        Tagged.addTagged(target, killer, Long.valueOf(System.currentTimeMillis()));
                        return;
                    }
                } else if (damager instanceof Egg) {
                    Egg egg = (Egg)damager;
                    if (egg.getShooter() instanceof Player) {
                        Player killer = (Player)egg.getShooter();
                        Tagged.addTagged(target, killer, Long.valueOf(System.currentTimeMillis()));
                        return;
                    }
                } else if (damager instanceof Arrow) {
                    Arrow arrow = (Arrow)damager;
                    if (arrow.getShooter() instanceof Player) {
                        Player killer = (Player)arrow.getShooter();
                        Tagged.addTagged(target, killer, Long.valueOf(System.currentTimeMillis()));
                        return;
                    }
                } else if (damager instanceof EnderPearl) {
                    EnderPearl ePearl = (EnderPearl)damager;
                    if (ePearl.getShooter() instanceof Player) {
                        Player killer = (Player)ePearl.getShooter();
                        Tagged.addTagged(target, killer, Long.valueOf(System.currentTimeMillis()));
                        return;
                    }
                } else if (damager instanceof ThrownPotion) {
                    ThrownPotion potion = (ThrownPotion)damager;
                    if (potion.getShooter() instanceof Player) {
                        Player killer = (Player)potion.getShooter();
                        Tagged.addTagged(target, killer, Long.valueOf(System.currentTimeMillis()));
                        return;
                    }
                }
            } else if (damager instanceof Player) {
                Player killer = (Player)damager;
                Tagged.addTagged(target, killer, Long.valueOf(System.currentTimeMillis()));
                return;
            }
        }
    }
}
