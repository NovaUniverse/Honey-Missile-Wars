package net.novauniverse.games.honeymissilewars.map;

import org.bukkit.Material;
import org.bukkit.World;

import net.zeeraa.novacore.spigot.utils.VectorArea;

public class MapBlockSection {
	private VectorArea area;
	private Material material;

	public MapBlockSection(VectorArea area, Material material) {
		this.area = area;
		this.material = material;
	}

	public VectorArea getArea() {
		return area;
	}

	public Material getMaterial() {
		return material;
	}

	public void apply(World world) {
		for (int x = area.getPosition1().getBlockX(); x <= area.getPosition2().getBlockX(); x++) {
			for (int y = area.getPosition1().getBlockY(); y <= area.getPosition2().getBlockY(); y++) {
				for (int z = area.getPosition1().getBlockZ(); z <= area.getPosition2().getBlockZ(); z++) {
					world.getBlockAt(x, y, z).setType(material);
				}
			}
		}
	}
}