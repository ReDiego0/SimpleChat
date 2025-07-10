package org.ReDiego0.simpleChat.listeners;

import org.ReDiego0.simpleChat.SimpleChat;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.EventHandler;

public class CommandListener implements Listener {

    private final SimpleChat plugin;

    public CommandListener(SimpleChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage().toLowerCase();

        if (msg.equals("/tc")) {
            plugin.getPlayerChannels().put(event.getPlayer().getUniqueId(), "TOWN");
        } else if (msg.equals("/nc")) {
            plugin.getPlayerChannels().put(event.getPlayer().getUniqueId(), "NATION");
        } else if (msg.equals("/ac")) {
            plugin.getPlayerChannels().put(event.getPlayer().getUniqueId(), "ALLIANCE");
        } else if (msg.equals("/g")) {
            plugin.getPlayerChannels().put(event.getPlayer().getUniqueId(), "GLOBAL");
        }
    }
}
