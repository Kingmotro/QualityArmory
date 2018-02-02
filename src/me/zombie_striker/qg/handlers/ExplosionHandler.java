package me.zombie_striker.qg.handlers;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

public class ExplosionHandler {

	private static List<Material> indestruct = Arrays.asList(Material.OBSIDIAN, Material.BEDROCK, Material.OBSERVER,
			Material.FURNACE, Material.WATER, Material.STATIONARY_LAVA, Material.LAVA, Material.STATIONARY_WATER,
			Material.COMMAND, Material.COMMAND_CHAIN, Material.COMMAND_MINECART, Material.COMMAND_REPEATING);

	public static void handleExplosion(Location origin, int radius, int power) {
		for (int x = origin.getBlockX() - radius; x < origin.getBlockX(); x++) {
			for (int y = origin.getBlockY() - radius; y < origin.getBlockY(); y++) {
				for (int z = origin.getBlockZ() - radius; z < origin.getBlockZ(); z++) {
					Location temp = new Location(origin.getWorld(), x, y, z);
					if (temp.distance(origin) <= radius)
						if (!indestruct.contains(temp.getBlock().getType()))
							origin.getBlock().breakNaturally();
				}
			}
		}
	}
}