package net.novauniverse.games.honeymissilewars.map.main;

import org.json.JSONObject;

import net.novauniverse.games.honeymissilewars.game.team.TeamColor;
import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.utils.VectorArea;

public class JoinArea {
	public static final String[] REQUIRED_JSON_PARAMETERS = { "x1", "x2", "y1", "y2", "z1", "z2", "team" };

	private VectorArea area;
	private TeamColor team;

	private JoinArea(VectorArea area, TeamColor team) {
		this.area = area;
		this.team = team;
	}

	public static JoinArea parse(JSONObject json) {
		boolean ok = true;
		for (String p : REQUIRED_JSON_PARAMETERS) {
			if (!json.has(p)) {
				ok = false;
				Log.warn("JoinArea:parse()", "Missing parameter: " + p);
			}
		}
		if (!ok) {
			Log.error("JoinArea:parse()", "Failed to load area. Missing JSON parameters. Check warnings above for more info");
			return null;
		}

		VectorArea area = VectorArea.fromJSON(json);
		TeamColor team;
		try {
			team = TeamColor.valueOf(json.getString("team"));
		} catch (Exception e) {
			Log.error("JoinArea:parse()", "Failed to load area. Invalid team value. Please use either RED or GREEN");
			return null;
		}

		return new JoinArea(area, team);
	}

	public VectorArea getArea() {
		return area;
	}

	public TeamColor getTeam() {
		return team;
	}
}