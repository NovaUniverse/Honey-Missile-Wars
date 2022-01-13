package net.novauniverse.games.honeymissilewars.missile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.json.JSONObject;

import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLib;
import com.github.shynixn.structureblocklib.api.enumeration.StructureRotation;

import net.novauniverse.games.honeymissilewars.HoneyMissileWars;
import net.novauniverse.games.honeymissilewars.game.team.TeamColor;
import net.zeeraa.novacore.commons.log.Log;

public class Missile {
	private String name;
	private String displayName;
	private Map<TeamColor, MissileOffset> teamOffset;
	private Material icon;

	public Missile(String name, String displayName, Map<TeamColor, MissileOffset> teamOffset, Material icon) {
		this.name = name;
		this.displayName = displayName;
		this.teamOffset = teamOffset;
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Map<TeamColor, MissileOffset> getTeamOffset() {
		return teamOffset;
	}

	public Material getIcon() {
		return icon;
	}

	public File getFile(TeamColor team) {
		return new File(HoneyMissileWars.getInstance().getDataFolder().getAbsoluteFile() + File.separator + "structures" + File.separator + team.name().toLowerCase() + "_" + name + ".nbt");
	}

	public void spawn(Location location, TeamColor color) {
		MissileOffset offset = teamOffset.get(color);
		Location spawnLocation = location.clone();

		StructureRotation rotation = StructureRotation.NONE;

		if (offset == null) {
			Log.warn("Missile", "No offset defined for " + name + " team " + color.name() + ". Using defaults. Please add offset in json file");
		} else {
			spawnLocation = spawnLocation.add(offset.getX(), offset.getY(), offset.getZ());
			rotation = offset.getRotation();
		}

		File file = this.getFile(color);

		if (!file.exists()) {
			Log.error("Missile", "Missing file " + file.getAbsolutePath());
			return;
		}

		// used for logging
		final String spawnLocationString = spawnLocation.toString();

		StructureBlockLib.INSTANCE.loadStructure(HoneyMissileWars.getInstance()).at(spawnLocation).rotation(rotation).includeEntities(true).loadFromFile(file).onException(ex -> {
			ex.printStackTrace();
			Log.error("Missile", "Failed to place structure. " + ex.getClass().getName() + " " + ex.getMessage());
		}).onResult(a -> {
			Log.trace("Missile", "Spawned missile: " + name + " at " + spawnLocationString);
		});
	}

	public static Missile parse(JSONObject json) {
		String name = json.getString("name");
		String displayName = json.getString("display_name");

		Material icon = Material.valueOf(json.getString("icon"));

		Map<TeamColor, MissileOffset> teamOffset = new HashMap<>();
		JSONObject offsets = json.getJSONObject("offset");

		for (TeamColor color : TeamColor.values()) {
			if (offsets.has(color.name())) {
				JSONObject offsetJson = offsets.getJSONObject(color.name());

				MissileOffset offset = MissileOffset.parse(offsetJson);

				teamOffset.put(color, offset);
			}
		}

		return new Missile(name, displayName, teamOffset, icon);
	}
}