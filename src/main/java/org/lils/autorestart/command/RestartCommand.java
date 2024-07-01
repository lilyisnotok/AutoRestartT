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
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.lils.autorestart.Config;

@Command(name = "restart", aliases = { "forcerestart", "reboot", "forcereboot" } )
@Permission("autorestart.admin.restart")
@Description("Force restart the server.")
public class RestartCommand extends AlpineCommand {
    RestartCommand(AlpinePlugin plugin) {
        super(plugin);
    }

    @Execute
    public void restart(
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
                    Messaging.broadcast(config.restartTimeBroadcast.build(RestartCommand.this.plugin, "seconds", cd));
                    cd--;
                } else if (cd > 5) {
                    cd--;
                } else if (cd > 0) {
                    broadcastRestartMessage(config, cd);
                    Messaging.broadcast(config.restartTimeBroadcast.build(RestartCommand.this.plugin, "seconds", cd));
                    cd--;
                } else {
                    Messaging.broadcast(config.restartBroadcast.build(RestartCommand.this.plugin));
                    broadcastRestartMessage(config, "now");
                    //Bukkit.getServer().shutdown();
                    cancel();
                }
            }
        }.runTaskTimer(this.plugin, 0L, 20L);
    }

    private void broadcastRestartMessage(Config config, Object countdown) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Messaging.title(player,
                    config.restartTitle.build(this.plugin),
                    config.restartSubTitle.build(this.plugin, "countdown", countdown));
        }
    }
}
