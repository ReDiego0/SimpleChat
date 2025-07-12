package org.ReDiego0.simpleChat.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.ReDiego0.simpleChat.SimpleChat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleChatCommand implements CommandExecutor, TabCompleter {
    
    private final SimpleChat plugin;

    public SimpleChatCommand(SimpleChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("simplechat.admin")) {
            sender.sendMessage(Component.text("§cNo tienes permiso para usar este comando."));
            return true;
        }
        
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "reload":
                handleReload(sender);
                break;
            case "toggle":
                handleToggle(sender, args);
                break;
            case "info":
                handleInfo(sender);
                break;
            default:
                sendHelp(sender);
                break;
        }
        
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Component.text("§6=== SimpleChat Commands ==="));
        sender.sendMessage(Component.text("§e/simplechat reload §7- Recargar configuración"));
        sender.sendMessage(Component.text("§e/simplechat toggle <mundo> §7- Activar/desactivar chat en un mundo"));
        sender.sendMessage(Component.text("§e/simplechat info §7- Información del plugin"));
    }

    private void handleReload(CommandSender sender) {
        plugin.getConfigManager().reloadConfigs();
        sender.sendMessage(Component.text("§aConfiguración recargada correctamente."));
    }

    private void handleToggle(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Component.text("§cUso: /simplechat toggle <mundo>"));
            return;
        }
        
        String worldName = args[1];
        List<String> enabledWorlds = new ArrayList<>(plugin.getConfigManager().getEnabledWorlds());
        
        if (enabledWorlds.contains(worldName)) {
            enabledWorlds.remove(worldName);
            sender.sendMessage(Component.text("§cChat deshabilitado en el mundo: " + worldName));
        } else {
            enabledWorlds.add(worldName);
            sender.sendMessage(Component.text("§aChat habilitado en el mundo: " + worldName));
        }
        
        plugin.getConfigManager().getConfig().set("enabled-worlds", enabledWorlds);
        plugin.getConfigManager().saveConfig();
    }

    private void handleInfo(CommandSender sender) {
        sender.sendMessage(Component.text("§6=== SimpleChat Info ==="));
        sender.sendMessage(Component.text("§eVersión: §f" + Bukkit.getPluginManager().getPlugin("SimpleChat").getPluginMeta().getVersion()));
        sender.sendMessage(Component.text("§eMundos habilitados: §f" + plugin.getConfigManager().getEnabledWorlds().size()));
        sender.sendMessage(Component.text("§eMiniMessage: §f" + plugin.getConfigManager().isMinimessageEnabled()));
        sender.sendMessage(Component.text("§ePlaceholderAPI: §f" + plugin.getConfigManager().isPlaceholderAPIEnabled()));
        sender.sendMessage(Component.text("§eVault: §f" + (Bukkit.getPluginManager().getPlugin("Vault") != null)));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.addAll(Arrays.asList("reload", "toggle", "info"));
        }
        
        return completions;
    }
}