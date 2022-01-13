package net.novauniverse.games.honeymissilewars.commands.debug;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import net.novauniverse.games.honeymissilewars.HoneyMissileWars;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;

public class ReloadMissiles extends NovaSubCommand {
	public ReloadMissiles() {
		super("reloadmissiles");

		setPermission("honeymissilewars.commands.debug");
		setPermissionDefaultValue(PermissionDefault.OP);

		setDescription("Reload missiles file");

		setAllowedSenders(AllowedSenders.ALL);

		setFilterAutocomplete(true);
		setEmptyTabMode(true);

		addHelpSubCommand();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.GOLD + "Reloading missiles...");
		if (HoneyMissileWars.getInstance().readMissiles()) {
			sender.sendMessage(ChatColor.GREEN + "Missiles reloaded");
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "Failed. Check console for more info");
		}
		return true;
	}
}