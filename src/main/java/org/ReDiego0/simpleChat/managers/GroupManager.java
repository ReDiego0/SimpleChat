package org.ReDiego0.simpleChat.managers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.ReDiego0.simpleChat.SimpleChat;
import org.ReDiego0.simpleChat.models.ChatGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GroupManager {
    
    private final SimpleChat plugin;
    private final Map<String, ChatGroup> groups;

    public GroupManager(SimpleChat plugin) {
        this.plugin = plugin;
        this.groups = new HashMap<>();
    }

    public void loadGroups() {
        groups.clear();
        
        // Verificar que groupsConfig no sea null
        if (plugin.getConfigManager().getGroupsConfig() == null) {
            plugin.getLogger().severe("groups.yml no pudo ser cargado!");
            createDefaultGroup();
            return;
        }
        
        ConfigurationSection groupsSection = plugin.getConfigManager().getGroupsConfig().getConfigurationSection("groups");
        if (groupsSection == null) {
            plugin.getLogger().warning("No se encontró la sección 'groups' en groups.yml, creando configuración por defecto");
            createDefaultGroup();
            return;
        }
        
        Set<String> groupNames = groupsSection.getKeys(false);
        
        for (String groupName : groupNames) {
            String path = "groups." + groupName;
            
            int priority = plugin.getConfigManager().getGroupsConfig().getInt(path + ".priority", 0);
            String format = plugin.getConfigManager().getGroupsConfig().getString(path + ".format", "<white>%player_name%<gray>: <white>%message%");
            String permission = plugin.getConfigManager().getGroupsConfig().getString(path + ".permission", "simplechat.use");
            
            ChatGroup group = new ChatGroup(groupName, priority, format, permission);
            groups.put(groupName, group);
        }
        
        // Asegurar que existe un grupo default
        if (!groups.containsKey("default")) {
            createDefaultGroup();
        }
        
        plugin.getLogger().info("Cargados " + groups.size() + " grupos de chat.");
    }

    private void createDefaultGroup() {
        ChatGroup defaultGroup = new ChatGroup("default", 0, "<white>%player_name%<gray>: <white>%message%", "simplechat.use");
        groups.put("default", defaultGroup);
        plugin.getLogger().info("Grupo 'default' creado automáticamente");
    }

    public ChatGroup getPlayerGroup(Player player) {
        ChatGroup highestGroup = groups.get("default");
        int highestPriority = -1;
        
        for (ChatGroup group : groups.values()) {
            if (player.hasPermission(group.getPermission()) && group.getPriority() > highestPriority) {
                highestGroup = group;
                highestPriority = group.getPriority();
            }
        }
        
        return highestGroup != null ? highestGroup : groups.get("default");
    }

    public ChatGroup getGroup(String name) {
        return groups.get(name);
    }

    public Map<String, ChatGroup> getAllGroups() {
        return new HashMap<>(groups);
    }

    public void addGroup(String name, int priority, String format, String permission) {
        ChatGroup group = new ChatGroup(name, priority, format, permission);
        groups.put(name, group);
        
        // Guardar en configuración
        String path = "groups." + name;
        plugin.getConfigManager().getGroupsConfig().set(path + ".priority", priority);
        plugin.getConfigManager().getGroupsConfig().set(path + ".format", format);
        plugin.getConfigManager().getGroupsConfig().set(path + ".permission", permission);
        plugin.getConfigManager().saveConfigs();
    }

    public void removeGroup(String name) {
        if (name.equals("default")) {
            return; // No permitir eliminar el grupo default
        }
        
        groups.remove(name);
        plugin.getConfigManager().getGroupsConfig().set("groups." + name, null);
        plugin.getConfigManager().saveConfigs();
    }
}