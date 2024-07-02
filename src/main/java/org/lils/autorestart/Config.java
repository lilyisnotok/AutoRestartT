package org.lils.autorestart;

import co.crystaldev.alpinecore.framework.config.AlpineConfig;
import co.crystaldev.alpinecore.framework.config.object.ConfigMessage;
import de.exlll.configlib.Comment;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class Config extends AlpineConfig {

    @Getter
    private static Config instance;
    { instance = this; }

    @Comment({
            "Force Restart Message Configuration"
    })
    public ConfigMessage forceRestartSuccess = ConfigMessage.of(
            "%prefix% You have <highlight>successfully</highlight> initiated a force reboot in <highlight>%seconds%</highlight> seconds.");

    @Comment({
            "",
            "Title / SubTitle Configuration"
    })
    public ConfigMessage restartTitle = ConfigMessage.of(
            "<info>Restarting</info>");

    public ConfigMessage restartSubTitle = ConfigMessage.of(
            "in <highlight>%countdown%</highlight>.");

    @Comment({
            "",
            "General Restart Broadcast Configuration"
    })
    public ConfigMessage restartTimeBroadcastMin = ConfigMessage.of(
            "%prefix% The server is restarting in <highlight>%min%</highlight> minutes.");

    public ConfigMessage restartTimeBroadcastSec = ConfigMessage.of(
            "%prefix% The server is restarting in <highlight>%sec%</highlight> seconds.");

    public ConfigMessage restartBroadcast = ConfigMessage.of(
            "%prefix% The server is now restarting...");

    @Comment({
            "",
            "Restart Times Configuration"
    })
    public List<String> restartTimes = Arrays.asList("00:00", "12:00");

    @Comment({
            ""
    })
    public ConfigMessage timeTillNextRestart = ConfigMessage.of(
            "%prefix% The server will restart in <highlight>%hour%h %min%m %sec%s</highlight>.");

}
