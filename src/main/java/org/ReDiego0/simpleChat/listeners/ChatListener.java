package org.ReDiego0.simpleChat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.ReDiego0.simpleChat.SimpleChat;
// pq si
import java.util.List;

public class ChatListener implements Listener {
    
    private final SimpleChat plugin;

    public ChatListener(SimpleChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncChat(AsyncChatEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        String channel = plugin.getPlayerChannels().getOrDefault(player.getUniqueId(), "GLOBAL");

        if (!"GLOBAL".equals(channel)) {
            // Está en canal Towny, dejar que TownyChat maneje
            return;
        }

        event.setCancelled(true);
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        plugin.getChatManager().sendFormattedMessage(player, message);
    }

    public void close() {
        // Método para limpiar recursos si es necesario
    }
}