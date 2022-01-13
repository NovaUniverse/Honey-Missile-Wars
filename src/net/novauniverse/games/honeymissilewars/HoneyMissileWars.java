package net.novauniverse.games.honeymissilewars;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;

import net.novauniverse.games.honeymissilewars.commands.debug.MissileWarsDebugCommand;
import net.novauniverse.games.honeymissilewars.game.HoneyMissileWarsGame;
import net.novauniverse.games.honeymissilewars.game.team.MissileWarsTeamManager;
import net.novauniverse.games.honeymissilewars.map.main.MissileWarsMainMap;
import net.novauniverse.games.honeymissilewars.missile.Missile;
import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.commons.utils.JSONFileUtils;
import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.command.CommandRegistry;
import net.zeeraa.novacore.spigot.module.ModuleManager;
import net.zeeraa.novacore.spigot.module.modules.customitems.CustomItemManager;
import net.zeeraa.novacore.spigot.module.modules.game.GameManager;
import net.zeeraa.novacore.spigot.module.modules.multiverse.MultiverseManager;
import net.zeeraa.novacore.spigot.module.modules.multiverse.MultiverseWorld;
import net.zeeraa.novacore.spigot.module.modules.multiverse.WorldUnloadOption;

public class HoneyMissileWars extends JavaPlugin implements Listener {
	private static HoneyMissileWars instance;

	private MissileWarsTeamManager teamManager;
	private HoneyMissileWarsGame game;
	private MultiverseWorld gameWorld;

	private MissileWarsMainMap mainMap;
	private List<MissileWarsMainMap> availableMaps;
	private MissileWarsMainMap activeMap;

	private List<Missile> missiles;

	public MissileWarsMainMap getMainMap() {
		return mainMap;
	}

	public MissileWarsMainMap getActiveMap() {
		return activeMap;
	}

	public List<MissileWarsMainMap> getAvailableMaps() {
		return availableMaps;
	}

	public MissileWarsTeamManager getTeamManager() {
		return teamManager;
	}

	public HoneyMissileWarsGame getGame() {
		return game;
	}

	public MultiverseWorld getGameWorld() {
		return gameWorld;
	}

	public List<Missile> getMissiles() {
		return missiles;
	}
	
	public static HoneyMissileWars getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		HoneyMissileWars.instance = this;

		getDataFolder().mkdir();

		// Load required modules
		ModuleManager.require(MultiverseManager.class);
		ModuleManager.require(GameManager.class);
		ModuleManager.require(CustomItemManager.class);

		CommandRegistry.registerCommand(new MissileWarsDebugCommand());

		activeMap = null;
		missiles = new ArrayList<>();

		File worldFile = new File(getDataFolder().getAbsoluteFile() + File.separator + "map");
		if (!worldFile.exists()) {
			Log.fatal(getName(), "Could not find map file at " + worldFile.getAbsolutePath() + ". Please add the map to that location and restart the server");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		try {
			gameWorld = MultiverseManager.getInstance().createFromFile(worldFile, "missilewars", WorldUnloadOption.DELETE);
		} catch (IOException e) {
			e.printStackTrace();
			Log.fatal(getName(), "Could not load map " + worldFile.getAbsolutePath() + ". " + e.getClass().getName() + " " + e.getMessage());
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		if (!readMainMap()) {
			Bukkit.getPluginManager().disablePlugin(this);
		}

		if (!readMissiles()) {
			Bukkit.getPluginManager().disablePlugin(this);
		}

		teamManager = new MissileWarsTeamManager();
		game = new HoneyMissileWarsGame();

		NovaCore.getInstance().setTeamManager(teamManager);

		GameManager.getInstance().setUseTeams(true);
		GameManager.getInstance().setShowDeathMessaage(true);
		GameManager.getInstance().loadGame(game);
	}

	@Override
	public void onDisable() {
		HandlerList.unregisterAll((Plugin) this);

	}

	private boolean readMainMap() {
		File mainMapFile = new File(getDataFolder().getAbsoluteFile() + File.separator + "lobby.json");
		if (!mainMapFile.exists()) {
			Log.fatal(getName(), "Could not find map data file at " + mainMapFile.getAbsolutePath() + "");
			return false;
		}

		try {
			JSONObject mainMapJson = JSONFileUtils.readJSONObjectFromFile(mainMapFile);
			mainMap = MissileWarsMainMap.parse(mainMapJson, gameWorld.getWorld());

			if (mainMap == null) {
				Log.fatal(getName(), "Failed to read map, check the logs");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.fatal(getName(), "Could not load map data file at " + mainMapFile.getAbsolutePath() + ". " + e.getClass().getName() + " " + e.getMessage());
			return false;
		}

		return true;
	}

	public boolean readMissiles() {
		missiles.clear();
		File missilesFile = new File(getDataFolder().getAbsoluteFile() + File.separator + "missiles.json");
		if (!missilesFile.exists()) {
			Log.fatal(getName(), "Could not find missiles data file at " + missilesFile.getAbsolutePath() + "");
			return false;
		}

		try {
			JSONArray missilesJson = JSONFileUtils.readJSONArrayFromFile(missilesFile);

			for (int i = 0; i < missilesJson.length(); i++) {
				JSONObject json = missilesJson.getJSONObject(i);

				try {
					Missile missile = Missile.parse(json);

					if (missile != null) {
						Log.info("MissileLoader", "Loaded missile " + missile.getName());
						missiles.add(missile);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					Log.error("MissileLoader", "Caught exception while parsing missile. " + e1.getClass().getName() + " " + e1.getMessage());
				}
			}

			Log.info("MissileLoader", "Loaded " + missiles.size() + " missiles");
		} catch (Exception e) {
			e.printStackTrace();
			Log.fatal(getName(), "Could not load missile data file at " + missilesFile.getAbsolutePath() + ". " + e.getClass().getName() + " " + e.getMessage());
			return false;
		}

		return true;
	}
}