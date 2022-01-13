package net.novauniverse.games.honeymissilewars.map.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.json.JSONArray;
import org.json.JSONObject;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.utils.VectorArea;

public class MissileWarsMainMap {
	public static final String[] REQUIRED_JSON_PARAMETERS = { "join_areas", "leave_areas", "arena_select_sign", "missile_select_sign", "play_area" };

	private List<JoinArea> joinAreas;
	private List<VectorArea> leaveAreas;

	private Location mapSelectorSign;
	private Location missileSelectorSign;

	private VectorArea playArea;

	private World world;

	public static MissileWarsMainMap parse(JSONObject json, World world) {
		boolean ok = true;
		for (String p : REQUIRED_JSON_PARAMETERS) {
			if (!json.has(p)) {
				ok = false;
				Log.warn("MissileWarsMainMap:parse()", "Missing parameter: " + p);
			}
		}
		if (!ok) {
			Log.fatal("MissileWarsMainMap:parse()", "Failed to load map. Missing JSON parameters. Check warnings above for more info");
			return null;
		}

		List<JoinArea> joinAreas = new ArrayList<JoinArea>();

		JSONArray joinAreasJson = json.getJSONArray("join_areas");
		for (int i = 0; i < joinAreasJson.length(); i++) {
			JSONObject jo = joinAreasJson.getJSONObject(i);

			JoinArea joinArea = JoinArea.parse(jo);

			if (joinArea != null) {
				joinAreas.add(joinArea);
			}
		}

		List<VectorArea> leaveAreas = new ArrayList<VectorArea>();

		JSONArray leaveAreasJson = json.getJSONArray("leave_areas");
		for (int i = 0; i < leaveAreasJson.length(); i++) {
			JSONObject lj = leaveAreasJson.getJSONObject(i);

			VectorArea vectorArea = VectorArea.fromJSON(lj);

			if (vectorArea != null) {
				leaveAreas.add(vectorArea);
			}
		}

		JSONObject arenaSelectSignJson = json.getJSONObject("arena_select_sign");
		JSONObject missileSelectSignJson = json.getJSONObject("missile_select_sign");

		Location arenaSelectSign = new Location(world, arenaSelectSignJson.getInt("x"), arenaSelectSignJson.getInt("y"), arenaSelectSignJson.getInt("z"));
		Location missileSelectSign = new Location(world, missileSelectSignJson.getInt("x"), missileSelectSignJson.getInt("y"), missileSelectSignJson.getInt("z"));

		VectorArea playArea = VectorArea.fromJSON(json.getJSONObject("play_area"));

		return new MissileWarsMainMap(joinAreas, leaveAreas, arenaSelectSign, missileSelectSign, playArea, world);
	}

	private MissileWarsMainMap(List<JoinArea> joinAreas, List<VectorArea> leaveAreas, Location mapSelectorSign, Location missileSelectorSign, VectorArea playArea, World world) {
		this.joinAreas = joinAreas;
		this.leaveAreas = leaveAreas;
		this.mapSelectorSign = mapSelectorSign;
		this.missileSelectorSign = missileSelectorSign;
		this.playArea = playArea;
		this.world = world;
	}

	public List<JoinArea> getJoinAreas() {
		return joinAreas;
	}

	public List<VectorArea> getLeaveAreas() {
		return leaveAreas;
	}

	public Location getMapSelectorSign() {
		return mapSelectorSign;
	}

	public Location getMissileSelectorSign() {
		return missileSelectorSign;
	}

	public VectorArea getPlayArea() {
		return playArea;
	}

	public void clearPlayArea() {
		for (int x = playArea.getPosition1().getBlockX(); x <= playArea.getPosition2().getBlockX(); x++) {
			for (int y = playArea.getPosition1().getBlockY(); y <= playArea.getPosition2().getBlockY(); y++) {
				for (int z = playArea.getPosition1().getBlockZ(); z <= playArea.getPosition2().getBlockZ(); z++) {
					world.getBlockAt(x, y, z).setType(Material.AIR);
				}
			}
		}
	}
}