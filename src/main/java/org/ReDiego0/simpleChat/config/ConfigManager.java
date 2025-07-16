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
    private File configFile;
    private FileConfiguration formatsConfig;
    private File formatsFile;

    public ConfigManager(SimpleChat plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {
        createConfigFile();
        loadDefaultConfig();
    }

    private void createConfigFile() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        // Crear archivo si no existe
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
        
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private void loadDefaultConfig() {
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
        
        saveConfig();
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Error al guardar configuración: " + e.getMessage());
        }
    }

    public void reloadConfigs() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void loadFormatsConfig() {
        formatsFile = new File(plugin.getDataFolder(), "formats.yml");
        if (!formatsFile.exists()) {
            plugin.saveResource("formats.yml", false);
        }
        formatsConfig = YamlConfiguration.loadConfiguration(formatsFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getFormatsConfig() {
        return formatsConfig;
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