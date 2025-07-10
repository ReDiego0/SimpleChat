package org.ReDiego0.simpleChat.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.ReDiego0.simpleChat.SimpleChat;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ConfigManager {
    
    private final SimpleChat plugin;
    private FileConfiguration config;
    private FileConfiguration groupsConfig;
    private File configFile;
    private File groupsFile;

    public ConfigManager(SimpleChat plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {
        createConfigFiles();
        loadDefaultConfigs();
    }

    private void createConfigFiles() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        groupsFile = new File(plugin.getDataFolder(), "groups.yml");
        
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        // Crear archivos si no existen
        if (!configFile.exists()) {
            try {
                plugin.saveResource("config.yml", false);
            } catch (Exception e) {
                plugin.getLogger().warning("No se pudo copiar config.yml desde resources, creando uno por defecto");
                try {
                    configFile.createNewFile();
                } catch (IOException ex) {
                    plugin.getLogger().severe("Error al crear config.yml: " + ex.getMessage());
                }
            }
        }
        
        if (!groupsFile.exists()) {
            try {
                plugin.saveResource("groups.yml", false);
            } catch (Exception e) {
                plugin.getLogger().warning("No se pudo copiar groups.yml desde resources, creando uno por defecto");
                try {
                    groupsFile.createNewFile();
                } catch (IOException ex) {
                    plugin.getLogger().severe("Error al crear groups.yml: " + ex.getMessage());
                }
            }
        }
        
        config = YamlConfiguration.loadConfiguration(configFile);
        groupsConfig = YamlConfiguration.loadConfiguration(groupsFile);
    }

    private void loadDefaultConfigs() {
        // Configuración principal por defecto
        if (!config.contains("enabled-worlds")) {
            config.set("enabled-worlds", Arrays.asList("world", "world_nether", "world_the_end"));
        }
        
        if (!config.contains("minimessage.enabled")) {
            config.set("minimessage.enabled", true);
        }
        
        if (!config.contains("placeholderapi.enabled")) {
            config.set("placeholderapi.enabled", true);
        }
        
        if (!config.contains("chat.cancel-default-chat")) {
            config.set("chat.cancel-default-chat", true);
        }
        
        if (!config.contains("chat.default-format")) {
            config.set("chat.default-format", "<gray>[<white>%world%<gray>] <white>%player_name%<gray>: <white>%message%");
        }
        
        // Configuración de grupos por defecto
        if (!groupsConfig.contains("groups")) {
            // Crear configuración de grupos por defecto
            groupsConfig.set("groups.default.priority", 0);
            groupsConfig.set("groups.default.format", "<gray>[<white>%world%<gray>] <white>%player_name%<gray>: <white>%message%");
            groupsConfig.set("groups.default.permission", "simplechat.use");
            
            // Grupo VIP de ejemplo
            groupsConfig.set("groups.vip.priority", 10);
            groupsConfig.set("groups.vip.format", "<gold>[VIP] <white>%player_name%<gray>: <white>%message%");
            groupsConfig.set("groups.vip.permission", "simplechat.vip");
            
            // Grupo Moderador de ejemplo
            groupsConfig.set("groups.moderator.priority", 20);
            groupsConfig.set("groups.moderator.format", "<green>[MOD] <white>%player_name%<gray>: <white>%message%");
            groupsConfig.set("groups.moderator.permission", "simplechat.moderator");
        }
        
        saveConfigs();
    }

    public void saveConfigs() {
        try {
            config.save(configFile);
            groupsConfig.save(groupsFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Error al guardar configuraciones: " + e.getMessage());
        }
    }

    public void reloadConfigs() {
        config = YamlConfiguration.loadConfiguration(configFile);
        groupsConfig = YamlConfiguration.loadConfiguration(groupsFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getGroupsConfig() {
        return groupsConfig;
    }

    public List<String> getEnabledWorlds() {
        return config.getStringList("enabled-worlds");
    }

    public boolean isChatEnabledInWorld(String worldName) {
        List<String> enabledWorlds = getEnabledWorlds();
        return enabledWorlds.isEmpty() || enabledWorlds.contains(worldName);
    }

    public boolean isMinimessageEnabled() {
        return config.getBoolean("minimessage.enabled", true);
    }

    public boolean isPlaceholderAPIEnabled() {
        return config.getBoolean("placeholderapi.enabled", true);
    }

}