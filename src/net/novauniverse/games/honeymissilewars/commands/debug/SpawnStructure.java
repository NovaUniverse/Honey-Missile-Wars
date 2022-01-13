package net.novauniverse.games.honeymissilewars.commands.debug;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLib;
import com.github.shynixn.structureblocklib.api.enumeration.StructureRotation;

import net.novauniverse.games.honeymissilewars.HoneyMissileWars;
import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;

public class SpawnStructure extends NovaSubCommand {
	public SpawnStructure() {
		super("spawnstructure");
		
		setPermission("honeymissilewars.commands.debug");
		setPermissionDefaultValue(PermissionDefault.OP);

		setDescription("Spawn nbt structure");

		setAllowedSenders(AllowedSenders.PLAYERS);

		setFilterAutocomplete(true);
		setEmptyTabMode(true);

		addHelpSubCommand();
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		List<String> result = new ArrayList<>();

		if (args.length == 0 || args.length == 1) {
			File folder = this.getStructureFolder();

			for (String s : folder.list()) {
				result.add(s);
			}
		}
		return result;
	}

	@Override
	public boolean execute(final CommandSender sender, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Please provide the file name. Tab works to auto complete");
			return false;
		}

		final File file = new File(this.getStructureFolder().getAbsolutePath() + File.separator + args[0]);

		if (file.exists()) {
			sender.sendMessage(ChatColor.GOLD + "Spawning...");
			StructureBlockLib.INSTANCE.loadStructure(HoneyMissileWars.getInstance()).at(((Player) sender).getLocation()).rotation(StructureRotation.NONE).includeEntities(true).loadFromFile(file).onException(ex -> {
				ex.printStackTrace();
				Log.error("Missile", "Failed to place structure. " + ex.getClass().getName() + " " + ex.getMessage());
				sender.sendMessage(ChatColor.DARK_RED + "Failed to place structure. " + ex.getClass().getName() + " " + ex.getMessage());

			}).onResult(a -> {
				Log.trace("Missile", "Spawned structure: " + file.getName() + " at " + ((Player) sender).getLocation().toString());
				sender.sendMessage(ChatColor.GREEN + "Spawned structure: " + ChatColor.AQUA + file.getName() + ChatColor.GREEN + " at " + ChatColor.LIGHT_PURPLE + ((Player) sender).getLocation().toString());
			});
		} else {
			sender.sendMessage(ChatColor.RED + "Could not find file " + file.getAbsolutePath());
		}

		return true;
	}

	private File getStructureFolder() {
		return new File(HoneyMissileWars.getInstance().getDataFolder().getAbsolutePath() + File.separator + "structures" + File.separator);
	}
}