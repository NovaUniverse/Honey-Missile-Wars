package net.novauniverse.games.honeymissilewars.map;

import java.util.List;

import org.bukkit.World;

public class MissileWarsMap {
	private String name;
	private String displayName;
	private List<MapBlockSection> blockSelections;

	public MissileWarsMap(String name, String displayName, List<MapBlockSection> blockSelections) {
		this.name = name;
		this.displayName = displayName;
		this.blockSelections = blockSelections;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public List<MapBlockSection> getBlockSelections() {
		return blockSelections;
	}

	public void apply(World world) {
		blockSelections.forEach(i -> {
			i.apply(world);
		});
	}
}