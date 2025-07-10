package org.ReDiego0.simpleChat.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ReDiego0.simpleChat.SimpleChat;

public class AnuncioCommand implements CommandExecutor {

    private final SimpleChat plugin;

    public AnuncioCommand(SimpleChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("simplechat.anuncio")) {
            sender.sendMessage(Component.text("§cNo tienes permiso para usar este comando."));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(Component.text("§eUso: /anuncio <mensaje>"));
            return true;
        }

        String mensaje = String.join(" ", args);

        // Formato llamativo con MiniMessage
        String formato = "<gradient:#FFD700:#FF4500><bold>[ANUNCIO]</bold></gradient> <white>" + mensaje;
        Component anuncio = MiniMessage.miniMessage().deserialize(formato);

        // Enviar mensaje a todos
        Bukkit.getServer().sendMessage(anuncio);

        // BossBar temporal
        BossBar bossBar = Bukkit.createBossBar("§l§6[ANUNCIO] §f" + mensaje, BarColor.YELLOW, BarStyle.SOLID);
        bossBar.setProgress(1.0);
        for (Player p : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(p);
            // Sonido llamativo
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.2f);
        }
        // Remover bossbar después de 5 segundos
        Bukkit.getScheduler().runTaskLater(plugin, bossBar::removeAll, 100L);

        return true;
    }
}
