package org.lils.autorestart.engine;

import co.crystaldev.alpinecore.AlpinePlugin;
import co.crystaldev.alpinecore.framework.engine.AlpineEngine;
import co.crystaldev.alpinecore.util.Messaging;
import net.kyori.adventure.text.Component;
import org.bukkit.scheduler.BukkitRunnable;
import org.lils.autorestart.Config;

import java.text.SimpleDateFormat;
import java.util.*;

public class RestartEngine extends AlpineEngine {
    RestartEngine(AlpinePlugin plugin) {
        super(plugin);
        startAutoRestart();
    }

    public void startAutoRestart() {
        Config config = Config.getInstance();

         new BukkitRunnable() {
            List<String> restartTimeConfig = config.restartTimes;
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                String currentTimeOfDay = new SimpleDateFormat("HH:mm").format(calendar.getTime());

                for (String restartTime : restartTimeConfig) {
                    if (currentTimeOfDay.equals(restartTime)) {
                        Messaging.broadcast(Component.text("ITS RESTART TIME: " + restartTime));
                        //Bukkit.getServer().shutdown();
                    }
                }
            }
        }.runTaskTimer(this.plugin, 0L, 1200L);
    }
}
