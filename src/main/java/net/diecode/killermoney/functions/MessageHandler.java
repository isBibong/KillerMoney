package net.diecode.killermoney.functions;

import net.diecode.killermoney.BukkitMain;
import net.diecode.killermoney.Logger;
import net.diecode.killermoney.configs.DefaultConfig;
import net.diecode.killermoney.enums.LanguageString;
import net.diecode.killermoney.enums.MessageMethod;
import net.diecode.killermoney.events.KMSendActionBarMessageEvent;
import net.diecode.killermoney.events.KMSendMessageEvent;
import net.diecode.killermoney.interfaces.IActionBar;
import net.diecode.killermoney.managers.KMPlayerManager;
import net.diecode.killermoney.managers.LanguageManager;
import net.diecode.killermoney.objects.KMPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class MessageHandler implements Listener {

    private static IActionBar actionBar;
    private static HashMap<UUID, BukkitTask> timers = new HashMap<UUID, BukkitTask>();

    @EventHandler
    public void onSendMessage(KMSendMessageEvent e) {
        e.getPlayer().sendMessage(LanguageManager.cGet(LanguageString.GENERAL_PREFIX) + e.getMessage());
    }

    @EventHandler
    public void onActionBarMessage(final KMSendActionBarMessageEvent e) {
        final UUID puuid = e.getPlayer().getUniqueId();

        if (timers.containsKey(puuid)) {
            BukkitTask task = timers.get(puuid);

            task.cancel();
        }

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(BukkitMain.getInstance(), new Runnable() {
            @Override
            public void run() {
                actionBar.sendActionBarMessage(e.getPlayer(), e.getMessage());
            }
        }, 0L, 20L);

        timers.put(puuid, task);

        Bukkit.getScheduler().runTaskLater(BukkitMain.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (timers.containsKey(puuid)) {
                    timers.get(puuid).cancel();
                }
            }
        }, e.getDuration() * 20L);
    }

    public static void process(Player player, String message) {
        KMPlayer kmp = KMPlayerManager.getKMPlayer(player);
        PluginManager pm = Bukkit.getPluginManager();

        if (kmp == null || !kmp.isEnableMessages()) {
            return;
        }

        switch (DefaultConfig.getMessageMethod()) {
            case DISABLED:              return;

            case ACTION_BAR:            pm.callEvent(new KMSendActionBarMessageEvent(player, message, 3));
                                        break;

            case CHAT:                  pm.callEvent(new KMSendMessageEvent(player, message));
                                        break;
        }
    }

    public static void initActionBar() {
        String version;

        try {
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];

            Class actionBarClass = Class.forName("net.diecode.killermoney.compatibility.actionbar.ActionBar_" + version);

            actionBar = (IActionBar)actionBarClass.newInstance();
        } catch (Exception e) {
            Logger.warning("Action bar is not compatibility with this server version. Using default \"CHAT\" value.");

            DefaultConfig.setMessageMethod(MessageMethod.CHAT);
        }
    }

}
