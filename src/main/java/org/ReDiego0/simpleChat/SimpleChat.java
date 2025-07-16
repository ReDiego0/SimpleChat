package org.ReDiego0.simpleChat;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.ReDiego0.simpleChat.commands.SimpleChatCommand;
import org.ReDiego0.simpleChat.config.ConfigManager;
import org.ReDiego0.simpleChat.listeners.CommandListener;
import org.ReDiego0.simpleChat.listeners.ChatListener;
import org.ReDiego0.simpleChat.managers.ChatManager;
import org.ReDiego0.simpleChat.placeholders.ChatPlaceholders;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SimpleChat extends JavaPlugin {

    private static SimpleChat instance;
    private ConfigManager configManager;
    private ChatManager chatManager;
    private MiniMessage miniMessage;
    private ChatPlaceholders chatPlaceholders;
    private ChatListener chatListener;

    private final Map<UUID, String> playerChannels = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        try {
            // Inicializar componentes básicos
            this.configManager = new ConfigManager(this);
            
            // Cargar configuración PRIMERO
            configManager.loadConfigs();
            configManager.loadFormatsConfig();
            
            // Inicializar MiniMessage (viene incluido en Paper)
            this.miniMessage = MiniMessage.miniMessage();
            
            // Inicializar managers DESPUÉS de cargar configuración
            this.chatManager = new ChatManager(this);

            // Registrar eventos
            chatListener = new ChatListener(this);
            getServer().getPluginManager().registerEvents(chatListener, this);
            getServer().getPluginManager().registerEvents(new CommandListener(this), this);


            // Registrar comandos
            if (getCommand("simplechat") != null) {
                getCommand("simplechat").setExecutor(new SimpleChatCommand(this));
            }
            if (getCommand("anuncio") != null) {
                getCommand("anuncio").setExecutor(new org.ReDiego0.simpleChat.commands.AnuncioCommand(this));
            }

            // Registrar PlaceholderAPI
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                chatPlaceholders = new ChatPlaceholders(this);
                chatPlaceholders.register();
                getLogger().info("PlaceholderAPI detectado y registrado!");
            } else {
                getLogger().warning("PlaceholderAPI no encontrado. Algunas funcionalidades pueden no funcionar correctamente.");
            }
            
            getLogger().info("SimpleChat habilitado correctamente!");
            
        } catch (Exception e) {
            getLogger().severe("Error al habilitar SimpleChat: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public Map<UUID, String> getPlayerChannels() {
        return playerChannels;
    }

    @Override
    public void onDisable() {
        try {
            if (chatPlaceholders != null) {
                chatPlaceholders.unregister();
            }
            
            if (chatListener != null) {
                chatListener.close();
            }
            
            getLogger().info("SimpleChat deshabilitado correctamente!");
        } catch (Exception e) {
            getLogger().warning("Error al deshabilitar SimpleChat: " + e.getMessage());
        }
    }

    public static SimpleChat getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public MiniMessage getMiniMessage() {
        return miniMessage;
    }
}