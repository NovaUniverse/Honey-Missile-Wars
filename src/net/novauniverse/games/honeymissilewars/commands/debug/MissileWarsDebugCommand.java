package net.novauniverse.games.honeymissilewars.commands.debug;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import net.novauniverse.games.honeymissilewars.HoneyMissileWars;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaCommand;

public class MissileWarsDebugCommand extends NovaCommand {
	public MissileWarsDebugCommand() {
		super("missilewarsdebug", HoneyMissileWars.getInstance());

		setAliases(NovaCommand.generateAliasList("hmd", "hmwd"));

		setPermission("honeymissilewars.commands.debug");
		setPermissionDefaultValue(PermissionDefault.OP);

		setDescription("Debug command for honey missilewars");

		setAllowedSenders(AllowedSenders.ALL);

		setFilterAutocomplete(true);
		setEmptyTabMode(true);

		addSubCommand(new ReloadMissiles());
		addSubCommand(new SpawnMissile());
		addSubCommand(new SpawnStructure());

		addHelpSubCommand();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.GOLD + "Use " + ChatColor.AQUA + "/missilewarsdebug help " + ChatColor.GOLD + "for help");
		return true;
	}

}