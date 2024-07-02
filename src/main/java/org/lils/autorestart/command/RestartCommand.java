package org.lils.autorestart.command;

import co.crystaldev.alpinecore.AlpinePlugin;
import co.crystaldev.alpinecore.framework.command.AlpineCommand;
import co.crystaldev.alpinecore.util.Messaging;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.lils.autorestart.Config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Command(name = "restart", aliases = { "autore", "autorestart", "autoreboot", "reboot"} )
@Description("The base command for AutoRestart")
@Permission("autorestart.command.base")
public class RestartCommand extends AlpineCommand {
    RestartCommand(AlpinePlugin plugin) {
        super(plugin);
    }

    @Execute(name = "now", aliases = "force")
    public void forceRestart(
            @Context CommandSender sender,
            @OptionalArg("time") Integer countdown
    ) {
        Config config = Config.getInstance();

        if (countdown == null) { countdown = 5; }
        Integer finalCountdown = countdown;

        Messaging.send(sender, config.forceRestartSuccess.build(this.plugin, "seconds", countdown));

        new BukkitRunnable() {
            int cd = finalCountdown;

            @Override
            public void run() {
                if (cd == 30 || cd == 10) {
                    Messaging.broadcast(config.restartTimeBroadcastSec.build(RestartCommand.this.plugin, "sec", cd));
                    cd--;
                } else if (cd > 5) {
                    cd--;
                } else if (cd > 0) {
                    broadcastRestartMessage(config, cd);
                    Messaging.broadcast(config.restartTimeBroadcastSec.build(RestartCommand.this.plugin, "sec", cd));
                    cd--;
                } else {
                    Messaging.broadcast(config.restartBroadcast.build(RestartCommand.this.plugin));
                    broadcastRestartMessage(config, "now");
                    Bukkit.getServer().shutdown();
                    cancel();
                }
            }
        }.runTaskTimer(this.plugin, 0L, 20L);
    }

    @Execute(name = "time")
    public void time(
            @Context CommandSender sender
    ) {
        Config config = Config.getInstance();
        List<String> restartTimeConfig = config.restartTimes;

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        long minDifference = Long.MAX_VALUE;

        for (String restartTime : restartTimeConfig) {
            try {
                SimpleDateFormat dateFormat;
                if (restartTime.length() == 5) {
                    dateFormat = new SimpleDateFormat("HH:mm");
                } else {
                    dateFormat = new SimpleDateFormat("HH:mm:ss");
                }

                Date restartDate = dateFormat.parse(restartTime);
                Calendar restartCalendar = Calendar.getInstance();
                restartCalendar.setTime(restartDate);
                restartCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                if (restartCalendar.before(calendar)) {
                    restartCalendar.add(Calendar.DAY_OF_MONTH, 1);
                }

                long diff = restartCalendar.getTimeInMillis() - currentDate.getTime();
                if (diff < minDifference) {
                    minDifference = diff;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        long hours = (minDifference / (1000 * 60 * 60)) % 24;
        long minutes = (minDifference / (1000 * 60)) % 60;
        long seconds = (minDifference / 1000) % 60;

        Messaging.send(sender, config.timeTillNextRestart.build(this.plugin,
                "hour", hours,
                "min", minutes,
                "sec", seconds));
    }

    private void broadcastRestartMessage(Config config, Object countdown) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Messaging.title(player,
                    config.restartTitle.build(this.plugin),
                    config.restartSubTitle.build(this.plugin, "countdown", countdown));
        }
    }
}
