package org.ReDiego0.simpleChat.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ReDiego0.simpleChat.SimpleChat;
import org.ReDiego0.simpleChat.models.ChatGroup;

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
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
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
        
        ChatGroup group = plugin.getGroupManager().getPlayerGroup(player);
        
        switch (params.toLowerCase()) {
            case "group":
                return group.getName();
            case "group_priority":
                return String.valueOf(group.getPriority());
            case "group_format":
                return group.getFormat();
            case "group_permission":
                return group.getPermission();
            case "chat_enabled":
                return String.valueOf(plugin.getConfigManager().isChatEnabledInWorld(player.getWorld().getName()));
            case "can_use_chat":
                return String.valueOf(plugin.getChatManager().canUseChat(player));
            default:
                return null;
        }
    }
}