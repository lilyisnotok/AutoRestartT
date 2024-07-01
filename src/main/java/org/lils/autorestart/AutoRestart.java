package org.lils.autorestart;

import co.crystaldev.alpinecore.AlpinePlugin;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;


public final class AutoRestart extends AlpinePlugin {

    @Override
    public void onStart() {
        // Plugin startup logic

    }

    @Override
    public void onStop() {
        // Plugin shutdown logic
    }

    @Override
    public void setupDefaultVariables(@NotNull VariableConsumer variableConsumer) {
        variableConsumer.addVariable("prefix", "<bracket>[<info>AutoRestart</info>]</bracket>");
    }
}
