package lc.deathmessages;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class Tagged {
    private static HashMap<Player, Long> taggedtime = new HashMap<>();

    private static HashMap<Player, Player> taggedplayer = new HashMap<>();

    public static void addTagged(Player player, Player killer, Long time) {
        taggedplayer.put(player, killer);
        taggedtime.put(player, time);
    }

    public static Long getTime(Player j) {
        if (taggedtime.containsKey(j))
            return taggedtime.get(j);
        return Long.valueOf(0L);
    }

    public static Player getKiller(Player j) {
        Player ret = null;
        if (taggedplayer.containsKey(j)) {
            ret = taggedplayer.get(j);
            taggedplayer.remove(j);
            taggedtime.remove(j);
        }
        return ret;
    }

    public static void removeTagged(Player jug) {
        taggedplayer.remove(jug);
        taggedtime.remove(jug);
    }
}
