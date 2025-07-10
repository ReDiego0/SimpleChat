package org.ReDiego0.simpleChat.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.ReDiego0.simpleChat.SimpleChat;
import org.ReDiego0.simpleChat.models.ChatGroup;

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
            case "format":
                handleFormat(sender, args);
                break;
            case "group":
                handleGroup(sender, args);
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
        sender.sendMessage(Component.text("§e/simplechat format <grupo> <formato> §7- Cambiar formato de un grupo"));
        sender.sendMessage(Component.text("§e/simplechat group list §7- Listar todos los grupos"));
        sender.sendMessage(Component.text("§e/simplechat group create <nombre> <prioridad> <permiso> §7- Crear grupo"));
        sender.sendMessage(Component.text("§e/simplechat info §7- Información del plugin"));
    }

    private void handleReload(CommandSender sender) {
        plugin.getConfigManager().reloadConfigs();
        plugin.getGroupManager().loadGroups();
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
        plugin.getConfigManager().saveConfigs();
    }

    private void handleFormat(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(Component.text("§cUso: /simplechat format <grupo> <formato>"));
            return;
        }
        
        String groupName = args[1];
        String format = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        
        ChatGroup group = plugin.getGroupManager().getGroup(groupName);
        if (group == null) {
            sender.sendMessage(Component.text("§cEl grupo '" + groupName + "' no existe."));
            return;
        }
        
        plugin.getConfigManager().getGroupsConfig().set("groups." + groupName + ".format", format);
        plugin.getConfigManager().saveConfigs();
        plugin.getGroupManager().loadGroups();
        
        sender.sendMessage(Component.text("§aFormato del grupo '" + groupName + "' actualizado."));
    }

    private void handleGroup(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Component.text("§cUso: /simplechat group <list|create|remove>"));
            return;
        }
        
        switch (args[1].toLowerCase()) {
            case "list":
                sender.sendMessage(Component.text("§6=== Grupos de Chat ==="));
                for (ChatGroup group : plugin.getGroupManager().getAllGroups().values()) {
                    sender.sendMessage(Component.text("§e" + group.getName() + " §7(Prioridad: " + group.getPriority() + ")"));
                    sender.sendMessage(Component.text("§7  Formato: " + group.getFormat()));
                    sender.sendMessage(Component.text("§7  Permiso: " + group.getPermission()));
                }
                break;
            case "create":
                if (args.length < 5) {
                    sender.sendMessage(Component.text("§cUso: /simplechat group create <nombre> <prioridad> <permiso>"));
                    return;
                }
                
                String name = args[2];
                int priority;
                try {
                    priority = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Component.text("§cLa prioridad debe ser un número."));
                    return;
                }
                
                String permission = args[4];
                String defaultFormat = "<white>%player_name%<gray>: <white>%message%";
                
                plugin.getGroupManager().addGroup(name, priority, defaultFormat, permission);
                sender.sendMessage(Component.text("§aGrupo '" + name + "' creado correctamente."));
                break;
            case "remove":
                if (args.length < 3) {
                    sender.sendMessage(Component.text("§cUso: /simplechat group remove <nombre>"));
                    return;
                }
                
                String groupToRemove = args[2];
                plugin.getGroupManager().removeGroup(groupToRemove);
                sender.sendMessage(Component.text("§aGrupo '" + groupToRemove + "' eliminado."));
                break;
        }
    }

    private void handleInfo(CommandSender sender) {
        sender.sendMessage(Component.text("§6=== SimpleChat Info ==="));
        sender.sendMessage(Component.text("§eVersión: §f" + plugin.getDescription().getVersion()));
        sender.sendMessage(Component.text("§eMundos habilitados: §f" + plugin.getConfigManager().getEnabledWorlds().size()));
        sender.sendMessage(Component.text("§eGrupos cargados: §f" + plugin.getGroupManager().getAllGroups().size()));
        sender.sendMessage(Component.text("§eMiniMessage: §f" + plugin.getConfigManager().isMinimessageEnabled()));
        sender.sendMessage(Component.text("§ePlaceholderAPI: §f" + plugin.getConfigManager().isPlaceholderAPIEnabled()));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.addAll(Arrays.asList("reload", "toggle", "format", "group", "info"));
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "group":
                    completions.addAll(Arrays.asList("list", "create", "remove"));
                    break;
                case "format":
                    completions.addAll(plugin.getGroupManager().getAllGroups().keySet());
                    break;
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("remove")) {
            completions.addAll(plugin.getGroupManager().getAllGroups().keySet());
        }
        
        return completions;
    }
}