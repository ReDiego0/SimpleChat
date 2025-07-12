package org.ReDiego0.simpleChat.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ReDiego0.simpleChat.SimpleChat;

public class ChatPlaceholders extends PlaceholderExpansion {
    
    private final SimpleChat plugin;

    public ChatPlaceholders(SimpleChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "simplechat";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", org.bukkit.Bukkit.getPluginManager().getPlugin("SimpleChat").getPluginMeta().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        switch (params.toLowerCase()) {
            case "group":
                return plugin.getChatManager().getPlayerGroup(player);
            case "chat_enabled":
                return String.valueOf(plugin.getConfigManager().isChatEnabledInWorld(player.getWorld().getName()));
            case "can_use_chat":
                return String.valueOf(plugin.getChatManager().canUseChat(player));
            default:
                return null;
        }
    }
}