package net.novauniverse.games.honeymissilewars.missile;

import org.json.JSONObject;

import com.github.shynixn.structureblocklib.api.enumeration.StructureRotation;

public class MissileOffset {
	private int x;
	private int y;
	private int z;
	private StructureRotation rotation;

	private MissileOffset(int x, int y, int z, StructureRotation rotation) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.rotation = rotation;
	}

	public static MissileOffset parse(JSONObject json) {
		int x = json.getInt("x");
		int y = json.getInt("y");
		int z = json.getInt("z");
		StructureRotation rotation = StructureRotation.valueOf(json.getString("rotation"));

		return new MissileOffset(x, y, z, rotation);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public StructureRotation getRotation() {
		return rotation;
	}
}