package org.ReDiego0.simpleChat.managers;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ReDiego0.simpleChat.SimpleChat;

public class ChatManager {

    private final SimpleChat plugin;
    private Permission vaultPerms;

    public ChatManager(SimpleChat plugin) {
        this.plugin = plugin;
        setupVault();
    }

    private void setupVault() {
        var rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp != null) vaultPerms = rsp.getProvider();
    }

    public String getPlayerGroup(Player player) {
        if (vaultPerms != null) {
            String[] groups = vaultPerms.getPlayerGroups(player);
            if (groups.length > 0) return groups[0];
        }
        return "default";
    }

    public Component formatMessage(Player player, String message) {
        String group = getPlayerGroup(player);
        String format = "<gray>[{group}]</gray> <white>{player}: {message}";

        // Aplica PlaceholderAPI al formato y al mensaje ANTES de MiniMessage
        if (plugin.getConfigManager().isPlaceholderAPIEnabled() &&
            Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            format = PlaceholderAPI.setPlaceholders(player, format);
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        format = format.replace("{group}", group)
                       .replace("{player}", player.getName())
                       .replace("{world}", player.getWorld().getName())
                       .replace("{message}", message);

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
        return plugin.getConfigManager().isChatEnabledInWorld(player.getWorld().getName());
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