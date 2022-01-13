package net.novauniverse.games.honeymissilewars.commands.debug;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import net.novauniverse.games.honeymissilewars.HoneyMissileWars;
import net.novauniverse.games.honeymissilewars.game.team.TeamColor;
import net.novauniverse.games.honeymissilewars.missile.Missile;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;

public class SpawnMissile extends NovaSubCommand {
	public SpawnMissile() {
		super("spawnmissile");

		setPermission("honeymissilewars.commands.debug");
		setPermissionDefaultValue(PermissionDefault.OP);

		setDescription("Spawn a missile");

		setAllowedSenders(AllowedSenders.PLAYERS);

		setFilterAutocomplete(true);
		setEmptyTabMode(true);

		addHelpSubCommand();
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		List<String> result = new ArrayList<>();

		if (args.length == 0 || args.length == 1) {
			HoneyMissileWars.getInstance().getMissiles().forEach(missile -> {
				result.add(missile.getName());
			});
		} else if (args.length == 2) {
			for (TeamColor color : TeamColor.values()) {
				result.add(color.name());
			}
		}

		return result;
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Please provide a missile. Use tab to autocomplete");
			return false;
		}

		Missile missile = null;

		for (Missile m : HoneyMissileWars.getInstance().getMissiles()) {
			if (m.getName().equalsIgnoreCase(args[0])) {
				missile = m;
				break;
			}
		}

		if (missile == null) {
			sender.sendMessage(ChatColor.RED + "Could not fine missile " + args[0]);
			return false;
		}

		if (args.length == 1) {
			sender.sendMessage(ChatColor.RED + "Please provide a team color. Use tab to autocomplete");
			return false;
		}

		TeamColor teamColor = null;

		try {
			teamColor = TeamColor.valueOf(args[1].toUpperCase());
		} catch (Exception e) {
			sender.sendMessage(ChatColor.RED + "Invalid team color. Use tab to auto complete valid ones");
			return false;
		}

		Location location = ((Player) sender).getLocation();

		sender.sendMessage(ChatColor.GREEN + "Spawning " + teamColor.name().toLowerCase() + " " + missile.getName() + "...");
		missile.spawn(location, teamColor);

		return true;
	}
}
