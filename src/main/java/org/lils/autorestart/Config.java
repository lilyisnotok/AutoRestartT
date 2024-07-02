package org.lils.autorestart;

import co.crystaldev.alpinecore.framework.config.AlpineConfig;
import co.crystaldev.alpinecore.framework.config.object.ConfigMessage;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class Config extends AlpineConfig {

    @Getter
    private static Config instance;
    { instance = this; }

    public ConfigMessage forceRestartSuccess = ConfigMessage.of(
            "%prefix% You have <highlight>successfully</highlight> initiated a force reboot in <highlight>%seconds%</highlight> seconds.");

    public ConfigMessage forceRestartBroadcast = ConfigMessage.of(
            "%prefix% A <highlight>force-restart</highlight> has been queued in <highlight>%seconds%</highlight> seconds.");

    public ConfigMessage restartTitle = ConfigMessage.of(
            "<info>Restarting</info>");

    public ConfigMessage restartSubTitle = ConfigMessage.of(
            "in <highlight>%countdown%</highlight>");

    public ConfigMessage restartTimeBroadcast = ConfigMessage.of(
            "%prefix% The server is restarting in <highlight>%seconds%</highlight> seconds.");

    public ConfigMessage restartBroadcast = ConfigMessage.of(
            "%prefix% The server is now restarting...");

    public List<String> restartTimes = Arrays.asList("12:00", "00:00");

}
