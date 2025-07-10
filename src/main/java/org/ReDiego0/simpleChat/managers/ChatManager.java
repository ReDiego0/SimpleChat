package org.ReDiego0.simpleChat.managers;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ReDiego0.simpleChat.SimpleChat;
import org.ReDiego0.simpleChat.models.ChatGroup;

public class ChatManager {
    
    private final SimpleChat plugin;

    public ChatManager(SimpleChat plugin) {
        this.plugin = plugin;
    }

    public Component formatMessage(Player player, String message) {
        ChatGroup group = plugin.getGroupManager().getPlayerGroup(player);
        String format = group.getFormat();

        // Aplica PlaceholderAPI al formato y al mensaje ANTES de MiniMessage
        if (plugin.getConfigManager().isPlaceholderAPIEnabled() &&
            Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            format = PlaceholderAPI.setPlaceholders(player, format);
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        format = format.replace("%player_name%", player.getName());
        format = format.replace("%world%", player.getWorld().getName());
        format = format.replace("%message%", message);

        Component finalComponent;
        if (plugin.getConfigManager().isMinimessageEnabled()) {
            finalComponent = plugin.getMiniMessage().deserialize(format);
        } else {
            finalComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(format);
        }

        return finalComponent;
    }

    public boolean canUseChat(Player player) {
        // Verificar si el chat está habilitado en el mundo
        if (!plugin.getConfigManager().isChatEnabledInWorld(player.getWorld().getName())) {
            return false;
        }
        
        // Verificar permisos
        ChatGroup group = plugin.getGroupManager().getPlayerGroup(player);
        return player.hasPermission(group.getPermission());
    }

    public void sendFormattedMessage(Player sender, String message) {
        if (!canUseChat(sender)) {
            return;
        }
        
        Component formattedMessage = formatMessage(sender, message);
        
        // Enviar mensaje a todos los jugadores en el mismo mundo
        for (Player player : sender.getWorld().getPlayers()) {
            player.sendMessage(formattedMessage);
        }
        
        // Enviar al console
        Bukkit.getConsoleSender().sendMessage(formattedMessage);
    }
}