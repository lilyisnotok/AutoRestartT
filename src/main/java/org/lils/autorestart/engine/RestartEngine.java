package org.lils.autorestart.engine;

import co.crystaldev.alpinecore.AlpinePlugin;
import co.crystaldev.alpinecore.framework.engine.AlpineEngine;
import co.crystaldev.alpinecore.util.Messaging;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.lils.autorestart.Config;
import org.lils.autorestart.command.RestartCommand;

import java.text.SimpleDateFormat;
import java.util.*;

public class RestartEngine extends AlpineEngine {
    RestartEngine(AlpinePlugin plugin) {
        super(plugin);
        startAutoRestart();
    }

    public void startAutoRestart() {
        Config config = Config.getInstance();
        final long ONE_MINUTE = 60000L;
        final long ONE_SECOND = 1000L;

        List<Long> reminderIntervals = Arrays.asList(
                60 * ONE_MINUTE,
                30 * ONE_MINUTE,
                15 * ONE_MINUTE,
                5 * ONE_MINUTE,
                1 * ONE_MINUTE,
                30 * ONE_SECOND,
                15 * ONE_SECOND,
                5 * ONE_SECOND,
                4 * ONE_SECOND,
                3 * ONE_SECOND,
                2 * ONE_SECOND,
                1 * ONE_SECOND
        );

         new BukkitRunnable() {
            List<String> restartTimeConfig = config.restartTimes;
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                String currentTimeOfDay = new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());

                for (String restartTime : restartTimeConfig) {
                    try {
                        Date restartDate = new SimpleDateFormat("HH:mm").parse(restartTime);
                        Calendar restartCalendar = Calendar.getInstance();
                        restartCalendar.setTime(restartDate);

                        for (long interval : reminderIntervals) {
                            Calendar reminderCalendar = (Calendar) restartCalendar.clone();
                            reminderCalendar.add(Calendar.MILLISECOND, -(int) interval);
                            String reminderTime = new SimpleDateFormat("HH:mm:ss").format(reminderCalendar.getTime());

                            if (currentTimeOfDay.equals(reminderTime)) {
                                if (interval < ONE_MINUTE) {
                                    Messaging.broadcast(config.restartTimeBroadcastSec.build(RestartEngine.this.plugin, "sec", interval / ONE_SECOND));
                                    broadcastRestartMessage(config, interval / ONE_SECOND);
                                } else {
                                    Messaging.broadcast(config.restartTimeBroadcastMin.build(RestartEngine.this.plugin, "min", interval / ONE_MINUTE));
                                }
                            }
                        }

                        if (currentTimeOfDay.equals(restartTime + ":00")) {
                            broadcastRestartMessage(config, "now");
                            Messaging.broadcast(config.restartBroadcast.build(RestartEngine.this.plugin));
                            Bukkit.getServer().shutdown();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
         }.runTaskTimer(this.plugin, 0L, 20L);
    }

    private void broadcastRestartMessage(Config config, Object countdown) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Messaging.title(player,
                    config.restartTitle.build(this.plugin),
                    config.restartSubTitle.build(this.plugin,
                            "countdown", countdown));
        }
    }
}
